package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.memory;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.Note;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class MemoryNotesModelFactory implements INotesModelFactory {

    private static final Map<Long, INote> sNoteMap = new ConcurrentHashMap<>();

    @Override
    public Single<List<INote>> getAllNotes() {
        return new Single<List<INote>>() {
            @Override
            protected void subscribeActual(final SingleObserver<? super List<INote>> observer) {
                final List<INote> noteList = new ArrayList<>();
                noteList.addAll(sNoteMap.values());

                observer.onSuccess(noteList);
            }
        };
    }

    @Override
    public Single<List<INote>> getFavouriteNotes() {
        return new Single<List<INote>>() {
            @Override
            protected void subscribeActual(final SingleObserver<? super List<INote>> observer) {
                final List<INote> noteList = new ArrayList<>();

                final Collection<INote> values = sNoteMap.values();

                for (final INote note : values) {
                    if (note.isFavourite()) {
                        noteList.add(note);
                    }
                }

                observer.onSuccess(noteList);
            }
        };
    }

    @Override
    public Completable getDeleteNoteCompletable(final long pNoteId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(final CompletableEmitter emitter) {
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
    public Completable getUpdateNoteCompletable(final long pNoteId, final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(final CompletableEmitter emitter) {
                final Note note = new Note(pNoteId, pTitle, pDescription, pIsFavourite);

                if (sNoteMap.put(pNoteId, note) != null) {
                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable("Item Not Found!"));
                }
            }
        });
    }

    @Override
    public Completable getCreateNoteCompletable(final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(final CompletableEmitter emitter) {
                final Note note = new Note(System.currentTimeMillis(), pTitle, pDescription, pIsFavourite);

                if (sNoteMap.put(note.getId(), note) != null) {
                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable("Item Not Found!"));
                }
            }
        });
    }
}
