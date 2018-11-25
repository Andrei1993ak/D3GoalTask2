package com.github.andrei1993ak.mentoring.task2.notes.loaders.impl;

import android.content.Context;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.mentoring.task2.notes.INote;
import com.github.andrei1993ak.mentoring.task2.notes.loaders.INotesLoaderFactory;

import java.util.List;

public class StubNotesLoader implements INotesLoaderFactory {

    @Override
    public Loader<List<INote>> getAllNotesLoader(final Context pContext) {
        return new StubAllNotesLoader(pContext);
    }

    @Override
    public Loader<List<INote>> getFavouriteNotesLoader(final Context pContext) {
        return new StubFavouritesNotesLoader(pContext);
    }
}
