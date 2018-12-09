package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.network;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.google.gson.annotations.SerializedName;

class NoteEntity implements INote {

    @SerializedName("id")
    private Long mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("favourite")
    private boolean mIsFavourite;

    @Override
    public Long getId() {
        return mId;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public boolean isFavourite() {
        return mIsFavourite;
    }

    void setId(final long pId) {
        mId = pId;
    }

    void setTitle(final String pTitle) {
        mTitle = pTitle;
    }

    void setDescription(final String pDescription) {
        mDescription = pDescription;
    }

    void setFavourite(final boolean pFavourite) {
        mIsFavourite = pFavourite;
    }
}
