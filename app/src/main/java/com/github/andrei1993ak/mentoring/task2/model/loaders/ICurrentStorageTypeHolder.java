package com.github.andrei1993ak.mentoring.task2.model.loaders;

import android.content.Context;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static com.github.andrei1993ak.mentoring.task2.model.loaders.ICurrentStorageTypeHolder.StorageType.DATABASE;
import static com.github.andrei1993ak.mentoring.task2.model.loaders.ICurrentStorageTypeHolder.StorageType.EXTERNAL;
import static com.github.andrei1993ak.mentoring.task2.model.loaders.ICurrentStorageTypeHolder.StorageType.LOCAL;
import static com.github.andrei1993ak.mentoring.task2.model.loaders.ICurrentStorageTypeHolder.StorageType.MEMORY;
import static com.github.andrei1993ak.mentoring.task2.model.loaders.ICurrentStorageTypeHolder.StorageType.PREFERENCES;
import static com.github.andrei1993ak.mentoring.task2.model.loaders.ICurrentStorageTypeHolder.StorageType.STUB;
import static com.github.andrei1993ak.mentoring.task2.model.loaders.ICurrentStorageTypeHolder.StorageType.UNKNOWN;
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
    @IntDef({LOCAL, STUB, DATABASE, UNKNOWN, EXTERNAL, PREFERENCES, MEMORY})
    @interface StorageType {
        int UNKNOWN = -1;
        int LOCAL = 0;
        int STUB = 1;
        int DATABASE = 2;
        int EXTERNAL = 3;
        int PREFERENCES = 4;
        int MEMORY = 5;
    }
}
