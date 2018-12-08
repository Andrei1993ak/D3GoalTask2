package com.github.andrei1993ak.mentoring.task2.activities;

import android.support.annotation.Nullable;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;

public interface IAppNavigator {
    void goToCreationNote(final boolean pIsFavourite, @Nullable final String pTile, @Nullable final String pDescription);

    void goToEditNote(final INote pNote);

    void goToDisplayingNotes();
}
