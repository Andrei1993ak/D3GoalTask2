package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.network;

import android.content.Context;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.mentoring.task2.holders.ApiHolder;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.ResultWrapper;

import java.util.List;

import io.reactivex.Completable;

public class NetworkNotesModelFactory implements INotesModelFactory {

    @Override
    public Loader<ResultWrapper<List<INote>>> getAllNotesLoader(final Context pContext) {
        return new NetworkNotesLoader(pContext, false);
    }

    @Override
    public Loader<ResultWrapper<List<INote>>> getFavouriteNotesLoader(final Context pContext) {
        return new NetworkNotesLoader(pContext, true);
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
}
