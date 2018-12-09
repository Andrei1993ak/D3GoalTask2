package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.network;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotesApi {

    @GET("/api/notes")
    Call<List<NoteEntity>> getAllNotes(@Query("favouriteOnly") String isFavouriteOnly);

    @POST("/api/notes")
    Call<ResponseBody> createNote(@Body NoteEntity pNote);

    @PUT("/api/notes/{id}")
    Call<ResponseBody> replaceNote(@Path("id") long pId, @Body NoteEntity pNote);

    @DELETE("api/notes/{id}")
    Call<ResponseBody> deleteNote(@Path("id") long pId);
}
