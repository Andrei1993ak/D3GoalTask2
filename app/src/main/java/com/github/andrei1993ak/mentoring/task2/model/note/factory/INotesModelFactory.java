package com.github.andrei1993ak.mentoring.task2.model.note.factory;

import android.content.Context;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences.PreferenceNoteModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.memory.MemoryNotesModelFactory;

import java.util.List;

public interface INotesModelFactory {

    Loader<List<INote>> getAllNotesLoader(final Context pContext);

    Loader<List<INote>> getFavouriteNotesLoader(final Context pContext);

    ICallable<Integer> getDeleteNoteCallable(final long pNoteId);

    ICallable<Boolean> getUpdateNoteCallable(final long pNoteId, final String pTitle, final String pDescription,
                                             final boolean pIsFavourite);

    ICallable<Boolean> getCreateNoteCallable(final String pTitle, final String pDescription,
                                             final boolean pIsFavourite);

    class Impl {
        public static INotesModelFactory get(final Context pContext) {
            final @ICurrentStorageTypeHolder.StorageType int selectedStorage = ICurrentStorageTypeHolder.Impl.get(pContext).getCurrentItem();

            if (selectedStorage == ICurrentStorageTypeHolder.StorageType.PREFERENCES) {
                return new PreferenceNoteModelFactory();
            } else {
                return new MemoryNotesModelFactory();
            }
        }
    }
}
