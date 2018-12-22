package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.database;

import android.content.Context;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.ResultWrapper;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class DataBaseNotesModelFactory implements INotesModelFactory {

    @Override
    public Loader<ResultWrapper<List<INote>>> getAllNotesLoader(final Context pContext) {
        return new DatabaseNotesLoader(pContext, false);
    }

    @Override
    public Loader<ResultWrapper<List<INote>>> getFavouriteNotesLoader(final Context pContext) {
        return new DatabaseNotesLoader(pContext, true);
    }

    @Override
    public Completable getDeleteNoteCallable(final long pNoteId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(final CompletableEmitter emitter) throws Exception {
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
    public ICallable<Boolean> getUpdateNoteCallable(final long pNoteId, final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return new ICallable<Boolean>() {
            @Override
            public Boolean call() {
                final NoteRecord noteRecord = NoteRecord.findById(NoteRecord.class, pNoteId);

                noteRecord.title = pTitle;
                noteRecord.description = pDescription;
                noteRecord.isFavourite = pIsFavourite;

                noteRecord.save();

                return true;
            }
        };
    }

    @Override
    public ICallable<Boolean> getCreateNoteCallable(final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return new ICallable<Boolean>() {

            @Override
            public Boolean call() {
                final NoteRecord noteRecord = new NoteRecord();

                noteRecord.title = pTitle;
                noteRecord.description = pDescription;
                noteRecord.isFavourite = pIsFavourite;

                noteRecord.save();

                return true;
            }
        };
    }
}
