package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.memory;

import android.content.Context;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryNotesModelFactory implements INotesModelFactory {

    private static final Map<Long, INote> sNoteMap = new ConcurrentHashMap<>();

    @Override
    public Loader<List<INote>> getAllNotesLoader(final Context pContext) {
        return new MemoryLoader(pContext, sNoteMap.values(), false);
    }

    @Override
    public Loader<List<INote>> getFavouriteNotesLoader(final Context pContext) {
        return new MemoryLoader(pContext, sNoteMap.values(), true);
    }

    @Override
    public ICallable<Integer> getDeleteNoteCallable(final long pNoteId) {
        return new ICallable<Integer>() {

            @Override
            public Integer call() {
                return sNoteMap.remove(pNoteId) != null ? 1 : 0;
            }
        };
    }

    @Override
    public ICallable<Boolean> getUpdateNoteCallable(final long pNoteId, final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return new ICallable<Boolean>() {

            @Override
            public Boolean call() {
                final Note note = new Note(pNoteId, pTitle, pDescription, pIsFavourite);

                return sNoteMap.put(pNoteId, note) != null;
            }
        };
    }

    @Override
    public ICallable<Boolean> getCreateNoteCallable(final String pTitle, final String pDescription,
                                                    final boolean pIsFavourite) {
        return new ICallable<Boolean>() {

            @Override
            public Boolean call() {
                final Note note = new Note(System.currentTimeMillis(), pTitle, pDescription, pIsFavourite);

                return sNoteMap.put(note.getId(), note) != null;
            }
        };
    }
}
