package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UpdateFileNoteCallable implements ICallable<Boolean> {

    private final Note mNote;
    private final File mFile;

    UpdateFileNoteCallable(final long pId, final String pTitle, final String pDescription, final boolean pIsFavourite, final File pFile) {
        mNote = new Note(pId, pTitle, pDescription, pIsFavourite);
        mFile = pFile;
    }

    @Override
    public Boolean call() {
        int index = 0;
        final long noteId = mNote.getId();

        final List<INote> notes = new ArrayList<>(new GetAllFileNoteCallable(mFile).call());

        for (final Iterator<INote> iter = notes.listIterator(); iter.hasNext(); ) {
            final INote note = iter.next();

            if (note.getId() == noteId) {
                iter.remove();
                break;
            }

            index++;
        }

        notes.add(index, mNote);

        new SaveAllFileNoteCallable(notes, mFile).call();

        return true;
    }
}
