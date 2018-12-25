package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class CreatePreferenceNoteCompletableOnSubscribe implements CompletableOnSubscribe {

    private final Note mNote;

    CreatePreferenceNoteCompletableOnSubscribe(final String pTitle, final String pDescription, final boolean pIsFavourite) {
        mNote = new Note(System.currentTimeMillis(), pTitle, pDescription, pIsFavourite);
    }

    @Override
    public void subscribe(final CompletableEmitter emitter) throws Exception {
        final List<INote> notes = new ArrayList<>(new GetAllPreferenceNoteCallable().call());
        notes.add(mNote);

        new PutAllPreferenceNoteCallable(notes).call();

        emitter.onComplete();
    }
}
