package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class PreferencesNotesSingle extends Single<List<INote>> {

    private final boolean mIsFavouriteOnly;

    PreferencesNotesSingle(final boolean pIsFavouriteOnly) {
        super();

        mIsFavouriteOnly = pIsFavouriteOnly;
    }

    @Override
    protected void subscribeActual(final SingleObserver<? super List<INote>> observer) {
        final List<INote> noteList = new GetAllPreferenceNoteCallable().call();

        if (mIsFavouriteOnly) {
            final List<INote> favouriteNotes = new ArrayList<>();

            for (final INote note : noteList) {
                if (note.isFavourite()) {
                    favouriteNotes.add(note);
                }
            }

            observer.onSuccess(favouriteNotes);
        } else {
            observer.onSuccess(noteList);
        }
    }
}
