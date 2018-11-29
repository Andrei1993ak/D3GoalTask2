package com.github.andrei1993ak.mentoring.task2.notes.loaders.impl;

import android.content.Context;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.notes.INote;
import com.github.andrei1993ak.mentoring.task2.notes.Note;
import com.github.andrei1993ak.mentoring.task2.notes.loaders.INotesLoaderFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryNotesLoaderFactory implements INotesLoaderFactory {

    private static final Map<Long, INote> sNoteMap = new ConcurrentHashMap<>();
    private static int idCounter = 0;

    static {
        Note note = new Note(idCounter++, "Title1", "Description", true);
        Note note2 = new Note(idCounter++, "Title2", "Description", false);
        Note note3 = new Note(idCounter++, "Title3", "Description", true);
        sNoteMap.put(note.getId(), note);
        sNoteMap.put(note2.getId(), note2);
        sNoteMap.put(note3.getId(), note3);
    }

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
    public ICallable<Boolean> getUpdateNoteCallable(final long pNoteId, final INote pNote) {
        return new ICallable<Boolean>() {

            @Override
            public Boolean call() {
                return sNoteMap.put(pNoteId, pNote) != null;
            }
        };
    }

    @Override
    public ICallable<Boolean> getCreateNoteCallable(final String pTitle, final String pDescription,
                                                    final boolean pIsFavourite) {
        return new ICallable<Boolean>() {

            @Override
            public Boolean call() {
                final Note note = new Note(idCounter++, pTitle, pDescription, pIsFavourite);

                return sNoteMap.put(note.getId(), note) != null;
            }
        };
    }
}
