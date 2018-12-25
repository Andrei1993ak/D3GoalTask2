package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;

import java.io.File;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class FileNoteModelFactory implements INotesModelFactory {

    private static final String FILE_NAME = "myData.txt";
    private final File mFile;

    public FileNoteModelFactory(final File pDataDir) {
        mFile = new File(pDataDir, FILE_NAME);
    }

    @Override
    public Single<List<INote>> getAllNotes() {
        return new FileNoteSingle(mFile, false);
    }

    @Override
    public Single<List<INote>> getFavouriteNotes() {
        return new FileNoteSingle(mFile, true);
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
