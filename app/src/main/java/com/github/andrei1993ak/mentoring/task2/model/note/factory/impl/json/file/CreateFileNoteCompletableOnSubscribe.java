package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class CreateFileNoteCompletableOnSubscribe implements CompletableOnSubscribe {

    private final Note mNote;
    private final File mFile;

    CreateFileNoteCompletableOnSubscribe(final String pTitle, final String pDescription, final boolean pIsFavourite, final File pFile) {
        mNote = new Note(System.currentTimeMillis(), pTitle, pDescription, pIsFavourite);
        mFile = pFile;
    }

    @Override
    public void subscribe(final CompletableEmitter emitter) throws Exception {
        final List<INote> notes = new ArrayList<>(new GetAllFileNoteCallable(mFile).call());
        notes.add(mNote);

        new SaveAllFileNoteCallable(notes, mFile).call();

        emitter.onComplete();
    }
}
