package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;
import com.github.andrei1993ak.mentoring.task2.holders.ContextHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PutAllPreferenceNoteCallable implements ICallable<Boolean> {

    private final List<INote> mNotes;

    PutAllPreferenceNoteCallable(final List<INote> pNotes) {
        if (pNotes == null) {
            mNotes = Collections.emptyList();
        } else {
            mNotes = new ArrayList<>(pNotes);
        }
    }

    @Override
    public Boolean call() {
        final String json = new Gson().toJson(mNotes, new TypeToken<ArrayList<Note>>() {
        }.getType());

        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextHolder.getInstance().getContext());
        final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putString(PreferenceNoteModelFactory.PREFERENCE_KEY, json);

        return edit.commit();
    }
}
