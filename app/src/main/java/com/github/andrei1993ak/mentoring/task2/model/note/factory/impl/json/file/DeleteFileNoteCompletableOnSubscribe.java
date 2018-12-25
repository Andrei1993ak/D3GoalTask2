package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class DeleteFileNoteCompletableOnSubscribe implements CompletableOnSubscribe {

    private final long mId;
    private final File mFile;

    DeleteFileNoteCompletableOnSubscribe(final long pId, final File pFile) {
        mId = pId;
        mFile = pFile;
    }

    @Override
    public void subscribe(final CompletableEmitter emitter) throws Exception {
        boolean isItemRemoved = false;

        final List<INote> notes = new ArrayList<>(new GetAllFileNoteCallable(mFile).call());

        for (final Iterator<INote> iter = notes.listIterator(); iter.hasNext(); ) {
            final INote note = iter.next();

            if (note.getId() == mId) {
                iter.remove();
                isItemRemoved = true;
                break;
            }
        }

        new SaveAllFileNoteCallable(notes, mFile).call();

        if (isItemRemoved) {
            emitter.onComplete();
        } else {
            emitter.onError(new Exception("Item Not Found"));
        }
    }
}
