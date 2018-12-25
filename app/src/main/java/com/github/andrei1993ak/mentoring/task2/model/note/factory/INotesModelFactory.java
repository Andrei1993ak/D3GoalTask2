package com.github.andrei1993ak.mentoring.task2.model.note.factory;

import android.content.Context;
import android.os.Environment;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.database.DataBaseNotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file.FileNoteModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.network.NetworkNotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences.PreferenceNoteModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.memory.MemoryNotesModelFactory;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface INotesModelFactory {

    Single<List<INote>> getAllNotes();

    Single<List<INote>> getFavouriteNotes();

    Completable getDeleteNoteCompletable(final long pNoteId);

    Completable getUpdateNoteCompletable(final long pNoteId, final String pTitle, final String pDescription, final boolean pIsFavourite);

    Completable getCreateNoteCompletable(final String pTitle, final String pDescription, final boolean pIsFavourite);

    class Impl {
        public static INotesModelFactory get(final Context pContext) {
            final @ICurrentStorageTypeHolder.StorageType int selectedStorage = ICurrentStorageTypeHolder.Impl.get(pContext).getCurrentItem();

            switch (selectedStorage) {
                case ICurrentStorageTypeHolder.StorageType.PREFERENCES:
                    return new PreferenceNoteModelFactory();
                case ICurrentStorageTypeHolder.StorageType.MEMORY:
                    return new MemoryNotesModelFactory();
                case ICurrentStorageTypeHolder.StorageType.LOCAL:
                    return new FileNoteModelFactory(pContext.getFilesDir());
                case ICurrentStorageTypeHolder.StorageType.EXTERNAL:
                    return new FileNoteModelFactory(Environment.getExternalStorageDirectory());
                case ICurrentStorageTypeHolder.StorageType.DATABASE:
                    return new DataBaseNotesModelFactory();
                case ICurrentStorageTypeHolder.StorageType.NETWORK:
                    return new NetworkNotesModelFactory();
                default:
                    return new DataBaseNotesModelFactory();
            }
        }
    }
}
