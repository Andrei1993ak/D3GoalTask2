package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.memory;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.BaseExceptionWrapperLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryLoader extends BaseExceptionWrapperLoader<List<INote>> {

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

    @Override
    public List<INote> loadResultDataInBackground() throws Exception {
        return mNotes;
    }
}
