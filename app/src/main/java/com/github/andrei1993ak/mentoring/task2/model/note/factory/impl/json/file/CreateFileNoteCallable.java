package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreateFileNoteCallable implements ICallable<Boolean> {

    private final Note mNote;
    private final File mFile;

    CreateFileNoteCallable(final String pTitle, final String pDescription, final boolean pIsFavourite, final File pFile) {
        mNote = new Note(System.currentTimeMillis(), pTitle, pDescription, pIsFavourite);
        mFile = pFile;
    }

    @Override
    public Boolean call() {
        final List<INote> notes = new ArrayList<>(new GetAllFileNoteCallable(mFile).call());
        notes.add(mNote);

        new SaveAllFileNoteCallable(notes, mFile).call();

        return true;
    }
}
