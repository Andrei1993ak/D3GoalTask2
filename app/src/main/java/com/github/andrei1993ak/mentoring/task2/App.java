package com.github.andrei1993ak.mentoring.task2;

import com.github.andrei1993ak.mentoring.task2.holders.ApiHolder;
import com.github.andrei1993ak.mentoring.task2.holders.ContextHolder;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.network.NotesApi;
import com.orm.SugarApp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends SugarApp {

    public static final String BASE_URL = "http://notes.com";

    @Override
    public void onCreate() {
        super.onCreate();

        ContextHolder.getInstance().setContext(this);
        ApiHolder.getInstance().setApi(getApi());
    }

    private NotesApi getApi() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NotesApi.class);
    }
}
