package com.github.andrei1993ak.mentoring.task2.notes.loaders;

import android.content.Context;
import android.support.annotation.IntDef;

import com.github.andrei1993ak.mentoring.task2.notes.loaders.impl.CurrentStorageTypeHolder;

import java.lang.annotation.Retention;

import static com.github.andrei1993ak.mentoring.task2.notes.loaders.ICurrentStorageTypeHolder.StorageType.DATABASE;
import static com.github.andrei1993ak.mentoring.task2.notes.loaders.ICurrentStorageTypeHolder.StorageType.EXTERNAL;
import static com.github.andrei1993ak.mentoring.task2.notes.loaders.ICurrentStorageTypeHolder.StorageType.LOCAL;
import static com.github.andrei1993ak.mentoring.task2.notes.loaders.ICurrentStorageTypeHolder.StorageType.MEMORY;
import static com.github.andrei1993ak.mentoring.task2.notes.loaders.ICurrentStorageTypeHolder.StorageType.PREFERENCES;
import static com.github.andrei1993ak.mentoring.task2.notes.loaders.ICurrentStorageTypeHolder.StorageType.STUB;
import static com.github.andrei1993ak.mentoring.task2.notes.loaders.ICurrentStorageTypeHolder.StorageType.UNNKNOWN;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface ICurrentStorageTypeHolder {

    void setCurrentItem(final @StorageType int pCurrentItem);

    @StorageType
    int getCurrentItem();

    class Impl {
        private static CurrentStorageTypeHolder sInstanse;

        public static ICurrentStorageTypeHolder get(final Context pContext) {
            if (sInstanse == null) {
                sInstanse = new CurrentStorageTypeHolder(pContext);
            }

            return sInstanse;
        }
    }

    @Retention(SOURCE)
    @IntDef({LOCAL, STUB, DATABASE, UNNKNOWN, EXTERNAL, PREFERENCES, MEMORY})
    @interface StorageType {
        int UNNKNOWN = -1;
        int LOCAL = 0;
        int STUB = 1;
        int DATABASE = 2;
        int EXTERNAL = 3;
        int PREFERENCES = 4;
        int MEMORY = 5;
    }
}
