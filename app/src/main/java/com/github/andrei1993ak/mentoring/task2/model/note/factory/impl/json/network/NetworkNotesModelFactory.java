package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.network;

import com.github.andrei1993ak.mentoring.task2.holders.ApiHolder;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class NetworkNotesModelFactory implements INotesModelFactory {

    @Override
    public Single<List<INote>> getAllNotes() {
        return getConvertedSingle(false);
    }

    @Override
    public Single<List<INote>> getFavouriteNotes() {
        return getConvertedSingle(true);
    }

    @Override
    public Completable getDeleteNoteCompletable(final long pNoteId) {
        return ApiHolder.getInstance().getApi().deleteNote(pNoteId);
    }

    @Override
    public Completable getUpdateNoteCompletable(final long pNoteId, final String pTitle, final String pDescription, final boolean pIsFavourite) {
        final NoteEntity entity = new NoteEntity();

        entity.setId(pNoteId);
        entity.setTitle(pTitle);
        entity.setDescription(pDescription);
        entity.setFavourite(pIsFavourite);

        return ApiHolder.getInstance().getApi().replaceNote(pNoteId, entity);
    }

    @Override
    public Completable getCreateNoteCompletable(final String pTitle, final String pDescription, final boolean pIsFavourite) {
        final NoteEntity entity = new NoteEntity();

        entity.setTitle(pTitle);
        entity.setDescription(pDescription);
        entity.setFavourite(pIsFavourite);

        return ApiHolder.getInstance().getApi().createNote(entity);
    }

    private Single<List<INote>> getConvertedSingle(final boolean pIsFavouriteOnly) {
        return ApiHolder.getInstance().getApi()
                .getAllNotes(String.valueOf(pIsFavouriteOnly))
                .flatMap(new Function<List<NoteEntity>, SingleSource<List<INote>>>() {
                    @Override
                    public SingleSource<List<INote>> apply(final List<NoteEntity> pNoteEntities) throws Exception {
                        final List<INote> noteList = new ArrayList<>();
                        noteList.addAll(pNoteEntities);

                        return Single.just(noteList);
                    }
                });
    }
}
