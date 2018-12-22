package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.network;

import android.content.Context;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.holders.ApiHolder;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.ResultWrapper;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

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
    public Completable getDeleteNoteCallable(final long pNoteId) {
        return ApiHolder.getInstance().getApi().deleteNote(pNoteId);
    }

    @Override
    public ICallable<Boolean> getUpdateNoteCallable(final long pNoteId, final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return new ICallable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final NoteEntity entity = new NoteEntity();

                entity.setId(pNoteId);
                entity.setTitle(pTitle);
                entity.setDescription(pDescription);
                entity.setFavourite(pIsFavourite);

                final Call<ResponseBody> responseBodyCall = ApiHolder.getInstance().getApi().replaceNote(pNoteId, entity);
                final Response<ResponseBody> response = responseBodyCall.execute();

                return response.isSuccessful();
            }
        };
    }

    @Override
    public ICallable<Boolean> getCreateNoteCallable(final String pTitle, final String pDescription, final boolean pIsFavourite) {
        return new ICallable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final NoteEntity entity = new NoteEntity();

                entity.setTitle(pTitle);
                entity.setDescription(pDescription);
                entity.setFavourite(pIsFavourite);

                final Call<ResponseBody> responseBodyCall = ApiHolder.getInstance().getApi().createNote(entity);
                final Response<ResponseBody> response = responseBodyCall.execute();

                return response.isSuccessful();
            }
        };
    }
}
