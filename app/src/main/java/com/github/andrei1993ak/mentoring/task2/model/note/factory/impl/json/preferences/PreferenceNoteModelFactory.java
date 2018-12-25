package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import android.content.Context;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.ResultWrapper;

import java.util.List;

import io.reactivex.Completable;

public class PreferenceNoteModelFactory implements INotesModelFactory {

    static final String PREFERENCE_KEY = PreferenceNoteModelFactory.class.getSimpleName();

    @Override
    public Loader<ResultWrapper<List<INote>>> getAllNotesLoader(final Context pContext) {
        return new PreferencesLoader(pContext, false);
    }

    @Override
    public Loader<ResultWrapper<List<INote>>> getFavouriteNotesLoader(final Context pContext) {
        return new PreferencesLoader(pContext, true);
    }

    @Override
    public Completable getDeleteNoteCompletable(final long pNoteId) {
        return Completable.create(new DeletePreferenceNoteCompletableOnSubscriber(pNoteId));
    }

    @Override
    public Completable getUpdateNoteCompletable(final long pNoteId, final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return Completable.create(new UpdatePreferenceNoteCompletableOnSubscriber(pNoteId, pTitle, pDescription, pIsFavourite));
    }

    @Override
    public Completable getCreateNoteCompletable(final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return Completable.create(new CreatePreferenceNoteCompletableOnSubscribe(pTitle, pDescription, pIsFavourite));
    }
}
