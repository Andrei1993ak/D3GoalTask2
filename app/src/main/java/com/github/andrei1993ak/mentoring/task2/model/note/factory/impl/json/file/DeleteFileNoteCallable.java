package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeleteFileNoteCallable implements ICallable<Integer> {

    private final long mId;
    private final File mFile;

    DeleteFileNoteCallable(final long pId, final File pFile) {
        mId = pId;
        mFile = pFile;
    }

    @Override
    public Integer call() {
        int result = 0;

        final List<INote> notes = new ArrayList<>(new GetAllFileNoteCallable(mFile).call());

        for (final Iterator<INote> iter = notes.listIterator(); iter.hasNext(); ) {
            final INote note = iter.next();

            if (note.getId() == mId) {
                iter.remove();
                result++;
                break;
            }
        }

        new SaveAllFileNoteCallable(notes, mFile).call();

        return result;
    }
}
