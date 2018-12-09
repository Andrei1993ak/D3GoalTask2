package com.github.andrei1993ak.mentoring.task2.holders;

import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.network.NotesApi;

public class ApiHolder {

    private static ApiHolder instance;

    private NotesApi mNotesApi;

    private ApiHolder() {
    }

    public static ApiHolder getInstance() {
        if (instance == null) {
            instance = new ApiHolder();
        }
        return instance;
    }

    public NotesApi getApi() {
        return mNotesApi;
    }

    public void setApi(final NotesApi pNotesApi) {
        mNotesApi = pNotesApi;
    }
}
