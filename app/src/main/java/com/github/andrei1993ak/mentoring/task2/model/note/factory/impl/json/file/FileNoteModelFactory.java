package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file;

import android.content.Context;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.ResultWrapper;

import java.io.File;
import java.util.List;

import io.reactivex.Completable;

public class FileNoteModelFactory implements INotesModelFactory {

    private static final String FILE_NAME = "myData.txt";
    private final File mFile;

    public FileNoteModelFactory(final File pDataDir) {
        mFile = new File(pDataDir, FILE_NAME);
    }

    @Override
    public Loader<ResultWrapper<List<INote>>> getAllNotesLoader(final Context pContext) {
        return new FileLoader(pContext, mFile, false);
    }

    @Override
    public Loader<ResultWrapper<List<INote>>> getFavouriteNotesLoader(final Context pContext) {
        return new FileLoader(pContext, mFile, true);
    }

    @Override
    public Completable getDeleteNoteCompletable(final long pNoteId) {
        return Completable.create(new DeleteFileNoteCompletableOnSubscribe(pNoteId, mFile));
    }

    @Override
    public Completable getUpdateNoteCompletable(final long pNoteId, final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return Completable.create(new UpdateFileNoteCompletableOnSubscriber(pNoteId, pTitle, pDescription, pIsFavourite, mFile));
    }

    @Override
    public Completable getCreateNoteCompletable(final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return Completable.create(new CreateFileNoteCompletableOnSubscribe(pTitle, pDescription, pIsFavourite, mFile));
    }
}
