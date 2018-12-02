package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.andrei1993ak.mentoring.task2.holders.ContextHolder;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.base.BaseJsonPutAllNoteCallable;

import java.util.List;

public class PutAllPreferenceNoteCallable extends BaseJsonPutAllNoteCallable {

    PutAllPreferenceNoteCallable(final List<INote> pNotes) {
        super(pNotes);
    }

    @Override
    protected void saveJsonString(final String pJson) {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextHolder.getInstance().getContext());
        final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putString(PreferenceNoteModelFactory.PREFERENCE_KEY, pJson);
        edit.commit();
    }
}
