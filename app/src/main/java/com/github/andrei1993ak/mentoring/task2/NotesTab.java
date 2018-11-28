package com.github.andrei1993ak.mentoring.task2;

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

import com.github.andrei1993ak.mentoring.task2.notes.INote;
import com.github.andrei1993ak.mentoring.task2.notes.NotesAdapter;
import com.github.andrei1993ak.mentoring.task2.notes.loaders.INotesLoaderFactory;
import com.github.andrei1993ak.mentoring.task2.uiutils.ContextMenuRecyclerView;
import com.github.andrei1993ak.mentoring.task2.uiutils.VerticalSpaceItemDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesTab extends Fragment {
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
        Toast.makeText(getContext(), String.valueOf(info.id), Toast.LENGTH_LONG).show();

        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().initLoader(GET_NOTES_LOADER_ID, new Bundle(), new GetNotesLoaderCallbacks()).forceLoad();
    }

    private class GetNotesLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<INote>> {
        @NonNull
        @Override
        public Loader<List<INote>> onCreateLoader(final int pI, @Nullable final Bundle pBundle) {
            final Loader<List<INote>> loader;

            final Context context = getContext();

            final INotesLoaderFactory notesLoaderFactory = INotesLoaderFactory.Impl.get(context);

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
}