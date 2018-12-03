package com.github.andrei1993ak.mentoring.task2.activities;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;

public interface IAppNavigator {
    void goToCreationNote(boolean pEquals);

    void goToEditNote(INote pNote);

    void goToDisplayingNotes();
}
