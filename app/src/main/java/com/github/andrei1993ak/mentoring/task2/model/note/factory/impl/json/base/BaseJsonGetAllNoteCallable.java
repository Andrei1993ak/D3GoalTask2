package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.base;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseJsonGetAllNoteCallable implements ICallable<List<INote>> {

    @Override
    public List<INote> call() {
        final String jsonString = getJsonString();

        final List<INote> notes = new Gson().fromJson(jsonString, new TypeToken<ArrayList<Note>>() {
        }.getType());

        return notes == null ? Collections.<INote>emptyList() : notes;
    }

    protected abstract String getJsonString();
}
