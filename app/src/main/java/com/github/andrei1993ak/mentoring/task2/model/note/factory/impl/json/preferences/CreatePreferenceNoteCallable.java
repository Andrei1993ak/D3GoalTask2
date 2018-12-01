package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;

import java.util.ArrayList;
import java.util.List;

public class CreatePreferenceNoteCallable implements ICallable<Boolean> {

    private final Note mNote;

    CreatePreferenceNoteCallable(final String pTitle, final String pDescription, final boolean pIsFavourite) {
        mNote = new Note(System.currentTimeMillis(), pTitle, pDescription, pIsFavourite);
    }

    @Override
    public Boolean call() {
        final List<INote> notes = new ArrayList<>(new GetAllPreferenceNoteCallable().call());
        notes.add(mNote);

        new PutAllPreferenceNoteCallable(notes).call();

        return true;
    }
}
