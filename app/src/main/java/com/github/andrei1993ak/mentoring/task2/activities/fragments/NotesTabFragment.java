package com.github.andrei1993ak.mentoring.task2.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.activities.IAppNavigator;
import com.github.andrei1993ak.mentoring.task2.core.ICallExecutor;
import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.core.ISuccess;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.adapters.NotesAdapter;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.ResultWrapper;
import com.github.andrei1993ak.mentoring.task2.utils.UiUtils;
import com.github.andrei1993ak.mentoring.task2.utils.views.ContextMenuRecyclerView;
import com.github.andrei1993ak.mentoring.task2.utils.views.VerticalSpaceItemDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesTabFragment extends Fragment {
    private final static int GET_NOTES_LOADER_ID = 0;
    private static final String IS_FAVOURITE_KEY = "IS_FAVOURITE_KEY";

    static NotesTabFragment newInstance(final boolean pIsFavourite) {
        final NotesTabFragment notesTabFragment = new NotesTabFragment();

        final Bundle args = new Bundle();
        args.putBoolean(IS_FAVOURITE_KEY, pIsFavourite);

        notesTabFragment.setArguments(args);

        return notesTabFragment;
    }

    @BindView(R.id.recycler_notes)
    ContextMenuRecyclerView mRecyclerView;

    private NotesAdapter mAdapter;

    private boolean mIsFavourite;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

        final Bundle arguments = getArguments();

        if (arguments != null && arguments.getBoolean(IS_FAVOURITE_KEY)) {
            mIsFavourite = true;
        }

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Context context = view.getContext();
        mAdapter = new NotesAdapter(context);

        registerForContextMenu(mRecyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(20));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().restartLoader(GET_NOTES_LOADER_ID, new Bundle(), new GetNotesLoaderCallbacks()).forceLoad();
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        final FragmentActivity activity = getActivity();

        if (activity != null) {
            final MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.my_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final ContextMenuRecyclerView.RecyclerViewContextMenuInfo info = (ContextMenuRecyclerView.RecyclerViewContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.action_delete_note) {
            final ICallable<Integer> deleteNoteCallable = INotesModelFactory.Impl.get(getContext()).getDeleteNoteCallable(info.id);
            ICallExecutor.Impl.newInstance(deleteNoteCallable).enqueue(new OnOperationUpdateSuccess());

            return true;
        } else if (item.getItemId() == R.id.action_edit_note) {
            final INote note = mAdapter.getItem(info.position);

            final FragmentActivity activity = getActivity();

            if (activity instanceof IAppNavigator) {
                ((IAppNavigator) activity).goToEditNote(note);
            }

            return true;
        }

        return super.onContextItemSelected(item);
    }

    private class GetNotesLoaderCallbacks implements LoaderManager.LoaderCallbacks<ResultWrapper<List<INote>>> {
        @NonNull
        @Override
        public Loader<ResultWrapper<List<INote>>> onCreateLoader(final int pI, @Nullable final Bundle pBundle) {
            final Loader<ResultWrapper<List<INote>>> loader;

            final Context context = getContext();

            final INotesModelFactory notesLoaderFactory = INotesModelFactory.Impl.get(context);

            if (mIsFavourite) {
                loader = notesLoaderFactory.getFavouriteNotesLoader(context);
            } else {
                loader = notesLoaderFactory.getAllNotesLoader(context);
            }

            return loader;
        }

        @Override
        public void onLoadFinished(@NonNull final Loader<ResultWrapper<List<INote>>> pLoader, final ResultWrapper<List<INote>> pResultWrapper) {
            if (pResultWrapper == null || pResultWrapper.getException() != null) {
                showError();
            } else {
                mAdapter.updateNotes(pResultWrapper.getResult());
            }
        }

        @Override
        public void onLoaderReset(@NonNull final Loader<ResultWrapper<List<INote>>> pLoader) {
            mAdapter.updateNotes(Collections.<INote>emptyList());
        }
    }

    private class OnOperationUpdateSuccess implements ISuccess<Integer> {
        @Override
        public void onResult(final Integer pResult) {
            onOperationFinished();
        }

        @Override
        public void onError(final Throwable pThrowable) {
            showError();

            onOperationFinished();
        }

        @Override
        public boolean isAlive() {
            return UiUtils.isContextAlive(getContext());
        }
    }

    private void showError() {
        final Context context = getContext();

        if (UiUtils.isContextAlive(context)) {
            Toast.makeText(context, R.string.error_message, Toast.LENGTH_LONG).show();
        }
    }

    private void onOperationFinished() {
        final FragmentActivity activity = getActivity();

        if (UiUtils.isContextAlive(activity)) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getLoaderManager().restartLoader(GET_NOTES_LOADER_ID, new Bundle(), new GetNotesLoaderCallbacks()).forceLoad();
                }
            });
        }
    }
}
