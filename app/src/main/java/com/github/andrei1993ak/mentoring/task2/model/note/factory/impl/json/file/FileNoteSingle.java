package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class FileNoteSingle extends Single<List<INote>> {

    private final File mFile;
    private final boolean mIsFavouriteOnly;

    FileNoteSingle(final File pFile, final boolean pIsFavouriteOnly) {
        super();

        mFile = pFile;
        mIsFavouriteOnly = pIsFavouriteOnly;
    }

    @Override
    protected void subscribeActual(final SingleObserver<? super List<INote>> observer) {
        final List<INote> noteList = new GetAllFileNoteCallable(mFile).call();

        if (mIsFavouriteOnly) {
            final List<INote> favouriteNotes = new ArrayList<>();

            for (final INote note : noteList) {
                if (note.isFavourite()) {
                    favouriteNotes.add(note);
                }
            }

            observer.onSuccess(favouriteNotes);
        } else {
            observer.onSuccess(noteList);
        }
    }
}
