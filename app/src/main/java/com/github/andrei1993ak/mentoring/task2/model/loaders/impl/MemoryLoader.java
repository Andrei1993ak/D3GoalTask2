package com.github.andrei1993ak.mentoring.task2.model.loaders.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.github.andrei1993ak.mentoring.task2.model.INote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryLoader extends AsyncTaskLoader<List<INote>> {

    private final List<INote> mNotes;

    MemoryLoader(@NonNull final Context context, final Collection<INote> pNoteList, final boolean pIsFavouriteOnly) {
        super(context);

        if (!pIsFavouriteOnly) {
            mNotes = new ArrayList<>(pNoteList);
        } else {
            mNotes = new ArrayList<>();

            for (final INote note : pNoteList) {
                if (note.isFavourite()) {
                    mNotes.add(note);
                }
            }
        }
    }

    @Nullable
    @Override
    public List<INote> loadInBackground() {
        return mNotes;
    }
}
