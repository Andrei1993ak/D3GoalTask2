package com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.file;

import com.github.andrei1993ak.mentoring.task2.holders.ContextHolder;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.impl.json.base.BaseJsonPutAllNoteCallable;
import com.github.andrei1993ak.mentoring.task2.utils.FileUtils;

import java.io.File;
import java.util.List;

public class SaveAllFileNoteCallable extends BaseJsonPutAllNoteCallable {

    private final File mFile;

    public SaveAllFileNoteCallable(final List<INote> pNotes, final File pFile) {
        super(pNotes);

        mFile = pFile;
    }

    @Override
    protected void saveJsonString(final String pJson) {
        FileUtils.writeToFile(mFile, pJson);
    }
}
