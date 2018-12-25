package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.network;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotesApi {

    @GET("/api/notes")
    Single<List<NoteEntity>> getAllNotes(@Query("favouriteOnly") String isFavouriteOnly);

    @POST("/api/notes")
    Completable createNote(@Body NoteEntity pNote);

    @PUT("/api/notes/{id}")
    Completable replaceNote(@Path("id") long pId, @Body NoteEntity pNote);

    @DELETE("api/notes/{id}")
    Completable deleteNote(@Path("id") long pId);
}
