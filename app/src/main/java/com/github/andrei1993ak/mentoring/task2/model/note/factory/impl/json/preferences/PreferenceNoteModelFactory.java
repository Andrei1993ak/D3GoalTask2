package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class PreferenceNoteModelFactory implements INotesModelFactory {

    static final String PREFERENCE_KEY = PreferenceNoteModelFactory.class.getSimpleName();

    @Override
    public Single<List<INote>> getAllNotes() {
        return new PreferencesNotesSingle(false);
    }

    @Override
    public Single<List<INote>> getFavouriteNotes() {
        return new PreferencesNotesSingle(true);
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
