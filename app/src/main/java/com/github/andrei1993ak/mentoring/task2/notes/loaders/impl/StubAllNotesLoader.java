package com.github.andrei1993ak.mentoring.task2.notes.loaders.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.github.andrei1993ak.mentoring.task2.notes.INote;
import com.github.andrei1993ak.mentoring.task2.notes.Note;

import java.util.Arrays;
import java.util.List;

public class StubAllNotesLoader extends AsyncTaskLoader<List<INote>> {

    StubAllNotesLoader(@NonNull final Context context) {
        super(context);
    }

    @Nullable
    @Override
    public List<INote> loadInBackground() {
        final INote note1 = new Note(0, "Title1", "Long Description1", true);
        final INote note2 = new Note(1, "Title2", "Long Description2", false);
        final INote note3 = new Note(2, "Title2", "Long Description3", true);

        return Arrays.asList(note1, note2, note3);
    }
}
