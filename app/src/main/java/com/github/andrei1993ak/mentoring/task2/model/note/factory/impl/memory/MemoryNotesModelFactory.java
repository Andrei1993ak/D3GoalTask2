package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.memory;

import android.content.Context;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.ResultWrapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class MemoryNotesModelFactory implements INotesModelFactory {

    private static final Map<Long, INote> sNoteMap = new ConcurrentHashMap<>();

    @Override
    public Loader<ResultWrapper<List<INote>>> getAllNotesLoader(final Context pContext) {
        return new MemoryLoader(pContext, sNoteMap.values(), false);
    }

    @Override
    public Loader<ResultWrapper<List<INote>>> getFavouriteNotesLoader(final Context pContext) {
        return new MemoryLoader(pContext, sNoteMap.values(), true);
    }

    @Override
    public Completable getDeleteNoteCallable(final long pNoteId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(final CompletableEmitter emitter) throws Exception {
                final INote removedNote = sNoteMap.remove(pNoteId);

                if (removedNote != null) {
                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable("Item Not Found!"));
                }
            }
        });
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
