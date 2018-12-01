package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.preferences;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeletePreferenceNoteCallable implements ICallable<Integer> {

    private final long mId;

    DeletePreferenceNoteCallable(final long pId) {
        mId = pId;
    }

    @Override
    public Integer call() {
        int result = 0;

        final List<INote> notes = new ArrayList<>(new GetAllPreferenceNoteCallable().call());

        for (final Iterator<INote> iter = notes.listIterator(); iter.hasNext(); ) {
            final INote note = iter.next();

            if (note.getId() == mId) {
                iter.remove();
                result++;
                break;
            }
        }

        new PutAllPreferenceNoteCallable(notes).call();

        return result;
    }
}
