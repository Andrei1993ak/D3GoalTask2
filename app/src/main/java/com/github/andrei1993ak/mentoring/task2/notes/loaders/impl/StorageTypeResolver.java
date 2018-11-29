package com.github.andrei1993ak.mentoring.task2.notes.loaders.impl;

import android.content.Context;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.notes.loaders.ICurrentStorageTypeHolder;

public class StorageTypeResolver {

    public static @ICurrentStorageTypeHolder.StorageType
    int resolveType(final Context pContext, final String pTypeString) {
        final String stubStorageString = pContext.getString(R.string.storageoptions_stub);
        final String databaseStorageString = pContext.getString(R.string.storageoptions_database);
        final String localFileStorageString = pContext.getString(R.string.storageoptions_local_file);
        final String externalFileStorageString = pContext.getString(R.string.storageoptions_sd_card_file);
        final String memoryStorageString = pContext.getString(R.string.storageoptions_memory);
        final String preferencesFileStorageString = pContext.getString(R.string.storageoptions_preferences);

        if (stubStorageString.equals(pTypeString)) {
            return ICurrentStorageTypeHolder.StorageType.STUB;
        } else if (databaseStorageString.equals(pTypeString)) {
            return ICurrentStorageTypeHolder.StorageType.DATABASE;
        } else if (localFileStorageString.equals(pTypeString)) {
            return ICurrentStorageTypeHolder.StorageType.LOCAL;
        } else if (memoryStorageString.equals(pTypeString)) {
            return ICurrentStorageTypeHolder.StorageType.MEMORY;
        } else if (externalFileStorageString.equals(pTypeString)) {
            return ICurrentStorageTypeHolder.StorageType.EXTERNAL;
        } else if (preferencesFileStorageString.equals(pTypeString)) {
            return ICurrentStorageTypeHolder.StorageType.PREFERENCES;
        } else {
            return ICurrentStorageTypeHolder.StorageType.UNKNOWN;
        }
    }
}
