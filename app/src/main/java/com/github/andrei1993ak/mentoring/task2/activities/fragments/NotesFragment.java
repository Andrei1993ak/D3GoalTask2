package com.github.andrei1993ak.mentoring.task2.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.activities.IAppNavigator;
import com.github.andrei1993ak.mentoring.task2.core.ICallExecutor;
import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.core.ISuccess;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.adapters.NotesAdapter;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.utils.UiUtils;
import com.github.andrei1993ak.mentoring.task2.utils.views.ContextMenuRecyclerView;
import com.github.andrei1993ak.mentoring.task2.utils.views.VerticalSpaceItemDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesFragment extends Fragment {

    private FragmentTabHost mTabHost;
    private String mAllNotesString;
    private String mFavouriteNotesString;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentActivity activity = getActivity();

        mTabHost = view.findViewById(android.R.id.tabhost);
        mTabHost.setup(activity, getFragmentManager(), android.R.id.tabcontent);

        mAllNotesString = getResources().getString(R.string.all_notes);
        mFavouriteNotesString = getResources().getString(R.string.favourite_notes);

        mTabHost.addTab(
                mTabHost.newTabSpec(mAllNotesString).setIndicator(mAllNotesString, null),
                NotesTabFragment.class, NotesTabFragment.getAllNotesArguments());
        mTabHost.addTab(
                mTabHost.newTabSpec(mFavouriteNotesString).setIndicator(mFavouriteNotesString, null),
                NotesTabFragment.class, NotesTabFragment.getFavouritesNotesArguments());

        final FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String currentTabTag = mTabHost.getCurrentTabTag();

                if (activity instanceof IAppNavigator) {
                    ((IAppNavigator) activity).goToCreationNote(mFavouriteNotesString.equals(currentTabTag));
                }
            }
        });
    }

    public static class NotesTabFragment extends Fragment {
        private final static int GET_NOTES_LOADER_ID = 0;

        private static final String IS_FAVOURITE_KEY = "IS_FAVOURITE_KEY";

        @BindView(R.id.recycler_notes)
        ContextMenuRecyclerView mRecyclerView;

        private NotesAdapter mAdapter;

        private boolean mIsFavourite;

        public static Bundle getFavouritesNotesArguments() {
            final Bundle bundle = new Bundle();
            bundle.putBoolean(IS_FAVOURITE_KEY, true);

            return bundle;
        }

        public static Bundle getAllNotesArguments() {
            final Bundle bundle = new Bundle();
            bundle.putBoolean(IS_FAVOURITE_KEY, false);

            return bundle;
        }

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

                if(activity instanceof IAppNavigator){
                    ((IAppNavigator) activity).goToEditNote(note);
                }

                return true;
            }

            return super.onContextItemSelected(item);
        }

        private class GetNotesLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<INote>> {
            @NonNull
            @Override
            public Loader<List<INote>> onCreateLoader(final int pI, @Nullable final Bundle pBundle) {
                final Loader<List<INote>> loader;

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
            public void onLoadFinished(@NonNull final Loader<List<INote>> pLoader, final List<INote> pINotes) {
                mAdapter.updateNotes(pINotes);
            }

            @Override
            public void onLoaderReset(@NonNull final Loader<List<INote>> pLoader) {
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
                onOperationFinished();
            }

            @Override
            public boolean isAlive() {
                return UiUtils.isContextAlive(getContext());
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
}
