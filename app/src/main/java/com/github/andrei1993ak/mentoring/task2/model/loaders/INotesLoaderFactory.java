package com.github.andrei1993ak.mentoring.task2.model.loaders;

import android.content.Context;
import android.support.v4.content.Loader;
import android.widget.Toast;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.INote;
import com.github.andrei1993ak.mentoring.task2.model.loaders.impl.MemoryNotesLoaderFactory;

import java.util.List;

public interface INotesLoaderFactory {

    Loader<List<INote>> getAllNotesLoader(final Context pContext);

    Loader<List<INote>> getFavouriteNotesLoader(final Context pContext);

    ICallable<Integer> getDeleteNoteCallable(final long pNoteId);

    ICallable<Boolean> getUpdateNoteCallable(final long pNoteId, final String pTitle, final String pDescription,
                                             final boolean pIsFavourite);

    ICallable<Boolean> getCreateNoteCallable(final String pTitle, final String pDescription,
                                             final boolean pIsFavourite);

    class Impl {
        public static INotesLoaderFactory get(final Context pContext) {
            final int currentItem = ICurrentStorageTypeHolder.Impl.get(pContext).getCurrentItem();

            Toast.makeText(pContext, String.valueOf(currentItem), Toast.LENGTH_LONG).show();

            return new MemoryNotesLoaderFactory();
        }
    }
}
