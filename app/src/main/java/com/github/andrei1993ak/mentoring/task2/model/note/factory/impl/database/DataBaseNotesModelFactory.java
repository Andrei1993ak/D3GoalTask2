package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.database;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class DataBaseNotesModelFactory implements INotesModelFactory {

    @Override
    public Single<List<INote>> getAllNotes() {
        return new Single<List<INote>>() {
            @Override
            protected void subscribeActual(final SingleObserver<? super List<INote>> observer) {
                final List<INote> resultList = new ArrayList<>();
                resultList.addAll(NoteRecord.listAll(NoteRecord.class));

                observer.onSuccess(resultList);
            }
        };
    }

    @Override
    public Single<List<INote>> getFavouriteNotes() {
        return new Single<List<INote>>() {
            @Override
            protected void subscribeActual(final SingleObserver<? super List<INote>> observer) {
                final List<INote> resultList = new ArrayList<>();
                resultList.addAll(NoteRecord.find(NoteRecord.class, "is_favourite = ?", String.valueOf(1)));

                observer.onSuccess(resultList);
            }
        };
    }

    @Override
    public Completable getDeleteNoteCompletable(final long pNoteId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(final CompletableEmitter emitter) {
                final NoteRecord noteRecord = NoteRecord.findById(NoteRecord.class, pNoteId);

                if (noteRecord != null) {
                    noteRecord.delete();

                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable("File not Found!"));
                }
            }
        });
    }

    @Override
    public Completable getUpdateNoteCompletable(final long pNoteId, final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(final CompletableEmitter emitter) {
                final NoteRecord noteRecord = NoteRecord.findById(NoteRecord.class, pNoteId);

                noteRecord.title = pTitle;
                noteRecord.description = pDescription;
                noteRecord.isFavourite = pIsFavourite;

                noteRecord.save();

                emitter.onComplete();
            }
        });
    }

    @Override
    public Completable getCreateNoteCompletable(final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(final CompletableEmitter emitter) {
                final NoteRecord noteRecord = new NoteRecord();

                noteRecord.title = pTitle;
                noteRecord.description = pDescription;
                noteRecord.isFavourite = pIsFavourite;

                noteRecord.save();

                emitter.onComplete();
            }
        });
    }
}
