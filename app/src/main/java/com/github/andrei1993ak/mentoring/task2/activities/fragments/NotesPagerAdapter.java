package com.github.andrei1993ak.mentoring.task2.activities.fragments;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.github.andrei1993ak.mentoring.task2.R;

import java.util.Arrays;
import java.util.List;

class NotesPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private static final int ALL_NOTES_POSITION = 0;

    private int mCurrentPosition;

    private final String mAllNotesString;
    private final String mFavouriteNotesString;
    private final List<NotesTabFragment> mFragments;

    NotesPagerAdapter(final FragmentManager fm, final Resources pResources) {
        super(fm);

        mFragments = Arrays.asList(NotesTabFragment.newInstance(false), NotesTabFragment.newInstance(true));
        mFragments.get(0).onSelected();
        mAllNotesString = pResources.getString(R.string.all_notes);
        mFavouriteNotesString = pResources.getString(R.string.favourite_notes);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(final int position) {
        if (position == ALL_NOTES_POSITION) {
            return mAllNotesString;
        } else {
            return mFavouriteNotesString;
        }
    }

    @Override
    public Fragment getItem(final int position) {
        return mFragments.get(position);
    }

    boolean isFavouriteTabActive(final int pCurrentItem) {
        return pCurrentItem != ALL_NOTES_POSITION;
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(final int position) {
        try {
            mFragments.get(mCurrentPosition).onUnselected();
            mFragments.get(position).onSelected();
        } catch (final Exception p) {
            Log.e(NotesPagerAdapter.class.getSimpleName(), "onPageSelected: ", p);
        }

        mCurrentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(final int state) {

    }

    public interface IOnPageSelectedListener {
        void onSelected();

        void onUnselected();
    }
}
