package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import android.content.Context;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;

import java.util.List;

public class PreferenceNoteModelFactory implements INotesModelFactory {

    static final String PREFERENCE_KEY = PreferenceNoteModelFactory.class.getSimpleName();

    @Override
    public Loader<List<INote>> getAllNotesLoader(final Context pContext) {
        return new PreferencesLoader(pContext, false);
    }

    @Override
    public Loader<List<INote>> getFavouriteNotesLoader(final Context pContext) {
        return new PreferencesLoader(pContext, true);
    }

    @Override
    public ICallable<Integer> getDeleteNoteCallable(final long pNoteId) {
        return new DeletePreferenceNoteCallable(pNoteId);
    }

    @Override
    public ICallable<Boolean> getUpdateNoteCallable(final long pNoteId, final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return new UpdatePreferenceNoteCallable(pNoteId, pTitle, pDescription, pIsFavourite);
    }

    @Override
    public ICallable<Boolean> getCreateNoteCallable(final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return new CreatePreferenceNoteCallable(pTitle, pDescription, pIsFavourite);
    }
}
