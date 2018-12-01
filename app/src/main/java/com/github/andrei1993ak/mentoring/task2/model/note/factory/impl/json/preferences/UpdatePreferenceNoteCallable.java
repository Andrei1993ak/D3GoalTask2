package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UpdatePreferenceNoteCallable implements ICallable<Boolean> {

    private final Note mNote;

    UpdatePreferenceNoteCallable(final long pId, final String pTitle, final String pDescription, final boolean pIsFavourite) {
        mNote = new Note(pId, pTitle, pDescription, pIsFavourite);
    }

    @Override
    public Boolean call() {
        int index = 0;
        final long noteId = mNote.getId();

        final List<INote> notes = new ArrayList<>(new GetAllPreferenceNoteCallable().call());

        for (final Iterator<INote> iter = notes.listIterator(); iter.hasNext(); ) {
            final INote note = iter.next();

            if (note.getId() == noteId) {
                iter.remove();
                break;
            }

            index++;
        }

        notes.add(index, mNote);

        new PutAllPreferenceNoteCallable(notes).call();

        return true;
    }
}
