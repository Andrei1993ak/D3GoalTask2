package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileLoader extends AsyncTaskLoader<List<INote>> {

    private final File mFile;
    private final boolean mIsFavouriteOnly;

    FileLoader(final Context context, final File pFile, final boolean pIsFavouriteOnly) {
        super(context);

        mFile = pFile;
        mIsFavouriteOnly = pIsFavouriteOnly;
    }

    @Override
    public List<INote> loadInBackground() {
        final List<INote> noteList = new GetAllFileNoteCallable(mFile).call();

        if (mIsFavouriteOnly) {
            final List<INote> favouriteNotes = new ArrayList<>();

            for (final INote note : noteList) {
                if (note.isFavourite()) {
                    favouriteNotes.add(note);
                }
            }

            return favouriteNotes;
        } else {
            return noteList;
        }
    }
}
