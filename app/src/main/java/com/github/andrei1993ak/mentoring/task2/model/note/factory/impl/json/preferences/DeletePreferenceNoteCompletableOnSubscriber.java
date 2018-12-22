package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class DeletePreferenceNoteCompletableOnSubscriber implements CompletableOnSubscribe {

    private final long mId;

    DeletePreferenceNoteCompletableOnSubscriber(final long pId) {
        mId = pId;
    }

    @Override
    public void subscribe(final CompletableEmitter emitter) throws Exception {
        boolean isItemRemoved = false;

        final List<INote> notes = new ArrayList<>(new GetAllPreferenceNoteCallable().call());

        for (final Iterator<INote> iter = notes.listIterator(); iter.hasNext(); ) {
            final INote note = iter.next();

            if (note.getId() == mId) {
                iter.remove();
                isItemRemoved = true;
                break;
            }
        }

        new PutAllPreferenceNoteCallable(notes).call();

        if (isItemRemoved) {
            emitter.onComplete();
        } else {
            emitter.onError(new Exception("Item not Found"));
        }
    }
}
