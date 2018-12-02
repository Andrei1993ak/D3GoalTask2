package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.database;

import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.orm.SugarRecord;

public class NoteRecord extends SugarRecord<NoteRecord> implements INote {

    String title;
    String description;
    boolean isFavourite;

    public NoteRecord() {
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isFavourite() {
        return isFavourite;
    }
}
