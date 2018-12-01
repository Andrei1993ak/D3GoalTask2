package com.github.andrei1993ak.mentoring.task2.model.note.factory;

import android.content.Context;
import android.preference.PreferenceManager;

import com.github.andrei1993ak.mentoring.task2.R;

public class CurrentStorageTypeHolder implements ICurrentStorageTypeHolder {

    private int mCurrentItem;

    public CurrentStorageTypeHolder(final Context pContext) {
        mCurrentItem = StorageTypeResolver.resolveType(pContext, PreferenceManager.getDefaultSharedPreferences(pContext).getString(pContext.getString(R.string.storage_pref_key), pContext.getString(R.string.storageoptions_preferences)));
    }

    @Override
    public void setCurrentItem(final int pCurrentItem) {
        mCurrentItem = pCurrentItem;
    }

    @Override
    public int getCurrentItem() {
        return mCurrentItem;
    }
}
