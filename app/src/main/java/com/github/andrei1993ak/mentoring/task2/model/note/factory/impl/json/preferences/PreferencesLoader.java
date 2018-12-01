package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;

import java.util.ArrayList;
import java.util.List;

public class PreferencesLoader extends AsyncTaskLoader<List<INote>> {

    private final boolean mIsFavouriteOnly;

    PreferencesLoader(@NonNull final Context context, final boolean pIsFavouriteOnly) {
        super(context);

        mIsFavouriteOnly = pIsFavouriteOnly;
    }

    @Override
    public List<INote> loadInBackground() {
        final List<INote> noteList = new GetAllPreferenceNoteCallable().call();

        if (mIsFavouriteOnly) {
            final List<INote> favouriteNotes = new ArrayList<>();

            for (final INote note : noteList) {
                if (note.isFavourite()) {
                    favouriteNotes.add(note);
                }
            }

            return favouriteNotes;
        } else {
            return noteList;
        }
    }
}
