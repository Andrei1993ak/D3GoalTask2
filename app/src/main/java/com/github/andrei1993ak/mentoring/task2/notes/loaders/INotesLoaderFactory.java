package com.github.andrei1993ak.mentoring.task2.notes.loaders;

import android.content.Context;
import android.support.v4.content.Loader;
import android.widget.Toast;

import com.github.andrei1993ak.mentoring.task2.notes.INote;
import com.github.andrei1993ak.mentoring.task2.notes.loaders.impl.StubNotesLoader;

import java.util.List;

public interface INotesLoaderFactory {

    Loader<List<INote>> getAllNotesLoader(final Context pContext);

    Loader<List<INote>> getFavouriteNotesLoader(final Context pContext);

    class Impl {
        public static INotesLoaderFactory get(final Context pContext) {
            final int currentItem = ICurrentStorageTypeHolder.Impl.get(pContext).getCurrentItem();

            Toast.makeText(pContext, String.valueOf(currentItem), Toast.LENGTH_LONG).show();

            return new StubNotesLoader();
        }
    }
}
