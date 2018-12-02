package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;

import java.util.ArrayList;
import java.util.List;

public class DatabaseNotesLoader extends AsyncTaskLoader<List<INote>> {

    private final boolean mIsFavouriteOnly;

    DatabaseNotesLoader(@NonNull final Context context, final boolean pIsFavouriteOnly) {
        super(context);

        mIsFavouriteOnly = pIsFavouriteOnly;
    }

    @Override
    public List<INote> loadInBackground() {
        final List<NoteRecord> noteList = NoteRecord.listAll(NoteRecord.class);

        final List<INote> resultList = new ArrayList<>();

        if (mIsFavouriteOnly) {
            for (final INote note : noteList) {
                if (note.isFavourite()) {
                    resultList.add(note);
                }
            }
        } else {
            resultList.addAll(noteList);
        }

        return resultList;
    }
}
