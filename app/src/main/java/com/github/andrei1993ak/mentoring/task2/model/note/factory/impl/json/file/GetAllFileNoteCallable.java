package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file;

import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.base.BaseJsonGetAllNoteCallable;
import com.github.andrei1993ak.mentoring.task2.utils.FileUtils;

import java.io.File;

public class GetAllFileNoteCallable extends BaseJsonGetAllNoteCallable {

    private final String mReadFromFile;

    public GetAllFileNoteCallable(final File pFile) {
        mReadFromFile = FileUtils.readFromFile(pFile);
    }

    @Override
    protected String getJsonString() {
        return mReadFromFile;
    }
}
