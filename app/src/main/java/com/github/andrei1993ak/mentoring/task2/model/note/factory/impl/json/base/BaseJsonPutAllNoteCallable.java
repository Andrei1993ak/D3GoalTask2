package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.base;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseJsonPutAllNoteCallable implements ICallable<Boolean> {

    private final List<INote> mNotes;

    public BaseJsonPutAllNoteCallable(final List<INote> pNotes) {
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

        saveJsonString(json);

        return true;
    }

    protected abstract void saveJsonString(final String pJson);
}
