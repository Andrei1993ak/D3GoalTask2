package com.github.andrei1993ak.mentoring.task2.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.andrei1993ak.mentoring.task2.R;

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
}
