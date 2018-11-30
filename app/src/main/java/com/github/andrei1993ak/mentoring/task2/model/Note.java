package com.github.andrei1993ak.mentoring.task2.model;

public class Note implements INote {

    private final long mId;
    private final String mTitle;
    private final String mDescription;
    private final boolean mIsFavourite;

    public Note(final long pId, final String pTitle, final String pDescription, final boolean pIsFavourite) {
        mId = pId;
        mTitle = pTitle;
        mDescription = pDescription;
        mIsFavourite = pIsFavourite;
    }

    @Override
    public long getId() {
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
}
