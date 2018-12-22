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
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.activities.EditNotificationDialog;
import com.github.andrei1993ak.mentoring.task2.activities.IAppNavigator;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.adapters.IOnNoteActionsClickListener;
import com.github.andrei1993ak.mentoring.task2.model.note.adapters.NotesAdapter;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.ResultWrapper;
import com.github.andrei1993ak.mentoring.task2.utils.UiUtils;
import com.github.andrei1993ak.mentoring.task2.utils.views.VerticalSpaceItemDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class NotesTabFragment extends Fragment implements NotesPagerAdapter.IOnPageSelectedListener {
    private final static int GET_NOTES_LOADER_ID = 0;
    private static final String IS_FAVOURITE_KEY = "IS_FAVOURITE_KEY";
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    static NotesTabFragment newInstance(final boolean pIsFavourite) {
        final NotesTabFragment notesTabFragment = new NotesTabFragment();

        final Bundle args = new Bundle();
        args.putBoolean(IS_FAVOURITE_KEY, pIsFavourite);

        notesTabFragment.setArguments(args);

        return notesTabFragment;
    }

    @BindView(R.id.recycler_notes)
    RecyclerView mRecyclerView;

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
        mAdapter = new NotesAdapter(context, new OnNoteActionsClickListener());

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

    private void onItemEditClick(final long pId) {
        final INote note = mAdapter.getItem(pId);

        final FragmentActivity activity = getActivity();

        if (activity instanceof IAppNavigator) {
            ((IAppNavigator) activity).goToEditNote(note);
        }
    }

    private void onItemDeleteClick(final long pId) {
        final Completable deleteNoteCompletable = INotesModelFactory.Impl.get(getContext()).getDeleteNoteCallable(pId);

        mCompositeDisposable.add(
                deleteNoteCompletable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                mAdapter.deleteNote(pId);
                            }

                            @Override
                            public void onError(final Throwable e) {
                                showError();
                            }
                        }));
    }

    @Override
    public void onSelected() {
        if (isResumed()) {
            getLoaderManager().restartLoader(GET_NOTES_LOADER_ID, new Bundle(), new GetNotesLoaderCallbacks()).forceLoad();
        }
    }

    @Override
    public void onUnselected() {
    }

    private class OnNoteActionsClickListener implements IOnNoteActionsClickListener {
        @Override
        public void onActionClick(final long pNoteId) {

            final EditNotificationDialog editNotificationDialog = EditNotificationDialog.newInstance(pNoteId);
            editNotificationDialog.setOnClickListener(new DialogOnEditDeleteClickListener());
            editNotificationDialog.show(getChildFragmentManager(), EditNotificationDialog.class.getSimpleName());
        }
    }

    private class DialogOnEditDeleteClickListener implements EditNotificationDialog.OnClickListener {

        @Override
        public void onDeleteClicked(final long pNoteId) {
            onItemDeleteClick(pNoteId);
        }

        @Override
        public void onEditClicked(final long pNoteId) {
            onItemEditClick(pNoteId);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        final Fragment dialogFragment = getChildFragmentManager().findFragmentByTag(EditNotificationDialog.class.getSimpleName());

        if (dialogFragment instanceof EditNotificationDialog) {
            ((EditNotificationDialog) dialogFragment).setOnClickListener(new DialogOnEditDeleteClickListener());
        }
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

    private void showError() {
        final Context context = getContext();

        if (UiUtils.isContextAlive(context)) {
            Toast.makeText(context, R.string.error_message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCompositeDisposable.dispose();
    }
}
