package com.github.andrei1993ak.mentoring.task2.activities.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.activities.IAppNavigator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesFragment extends Fragment implements ITitled {

    @BindView(R.id.note_view_pager)
    ViewPager mNotesPager;

    @BindView(R.id.fab)
    FloatingActionButton mCreateButton;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    private NotesPagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mAdapter = new NotesPagerAdapter(getChildFragmentManager(), view.getContext().getResources());
        mNotesPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mNotesPager);

        mCreateButton.setOnClickListener(new OnCreateButtonClickListener());
    }

    @Override
    public int getTitleResId() {
        return R.string.notes;
    }

    private class OnCreateButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(final View view) {
            final FragmentActivity activity = getActivity();

            if (activity instanceof IAppNavigator) {
                ((IAppNavigator) activity).goToCreationNote(mAdapter.isFavouriteTabActive(mNotesPager.getCurrentItem()), null, null);
            }
        }
    }
}
