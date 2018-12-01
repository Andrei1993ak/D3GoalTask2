package com.github.andrei1993ak.mentoring.task2.model.note.factory;

import android.content.Context;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static com.github.andrei1993ak.mentoring.task2.model.note.factory.ICurrentStorageTypeHolder.StorageType.DATABASE;
import static com.github.andrei1993ak.mentoring.task2.model.note.factory.ICurrentStorageTypeHolder.StorageType.EXTERNAL;
import static com.github.andrei1993ak.mentoring.task2.model.note.factory.ICurrentStorageTypeHolder.StorageType.LOCAL;
import static com.github.andrei1993ak.mentoring.task2.model.note.factory.ICurrentStorageTypeHolder.StorageType.MEMORY;
import static com.github.andrei1993ak.mentoring.task2.model.note.factory.ICurrentStorageTypeHolder.StorageType.PREFERENCES;
import static com.github.andrei1993ak.mentoring.task2.model.note.factory.ICurrentStorageTypeHolder.StorageType.UNKNOWN;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface ICurrentStorageTypeHolder {

    void setCurrentItem(final @StorageType int pCurrentItem);

    @StorageType
    int getCurrentItem();

    class Impl {
        private static CurrentStorageTypeHolder sInstance;

        public static ICurrentStorageTypeHolder get(final Context pContext) {
            if (sInstance == null) {
                sInstance = new CurrentStorageTypeHolder(pContext);
            }

            return sInstance;
        }
    }

    @Retention(SOURCE)
    @IntDef({LOCAL, DATABASE, UNKNOWN, EXTERNAL, PREFERENCES, MEMORY})
    @interface StorageType {
        int UNKNOWN = -1;
        int LOCAL = 0;
        int DATABASE = 1;
        int EXTERNAL = 2;
        int PREFERENCES = 3;
        int MEMORY = 4;
    }
}
