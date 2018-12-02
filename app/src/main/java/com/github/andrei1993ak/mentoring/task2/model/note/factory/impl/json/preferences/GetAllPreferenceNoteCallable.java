package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.andrei1993ak.mentoring.task2.holders.ContextHolder;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.base.BaseJsonGetAllNoteCallable;
import com.github.andrei1993ak.mentoring.task2.utils.TextUtils;

public class GetAllPreferenceNoteCallable extends BaseJsonGetAllNoteCallable {

    @Override
    protected String getJsonString() {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextHolder.getInstance().getContext());

        return defaultSharedPreferences.getString(PreferenceNoteModelFactory.PREFERENCE_KEY, TextUtils.Constants.EMPTY);
    }
}
