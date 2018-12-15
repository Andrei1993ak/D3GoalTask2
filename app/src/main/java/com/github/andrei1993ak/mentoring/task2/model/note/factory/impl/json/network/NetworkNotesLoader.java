package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.andrei1993ak.mentoring.task2.holders.ApiHolder;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.BaseExceptionWrapperLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NetworkNotesLoader extends BaseExceptionWrapperLoader<List<INote>> {

    private final boolean mIsFavouriteOnly;

    NetworkNotesLoader(@NonNull final Context context, final boolean pIsFavouriteOnly) {
        super(context);

        mIsFavouriteOnly = pIsFavouriteOnly;
    }

    @Override
    public List<INote> loadResultDataInBackground() throws Exception {
        final Call<List<NoteEntity>> allNotesCall = ApiHolder.getInstance().getApi().getAllNotes(mIsFavouriteOnly ? "true" : null);
        final Response response = allNotesCall.execute();

        if (response != null) {
            final Object responseBody = response.body();

            if (responseBody != null) {
                final List<NoteEntity> noteEntities = (List<NoteEntity>) responseBody;
                final List<INote> result = new ArrayList<>();

                result.addAll(noteEntities);

                return result;
            }
        }

        return Collections.emptyList();

    }
}
