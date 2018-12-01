package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;
import com.github.andrei1993ak.mentoring.task2.holders.ContextHolder;
import com.github.andrei1993ak.mentoring.task2.utils.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetAllPreferenceNoteCallable implements ICallable<List<INote>> {

    @Override
    public List<INote> call() {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextHolder.getInstance().getContext());

        final String jsonString = defaultSharedPreferences.getString(PreferenceNoteModelFactory.PREFERENCE_KEY, TextUtils.Constants.EMPTY);

        final List<INote> notes = new Gson().fromJson(jsonString, new TypeToken<ArrayList<Note>>() {
        }.getType());

        return notes == null ? Collections.<INote>emptyList() : notes;
    }
}
