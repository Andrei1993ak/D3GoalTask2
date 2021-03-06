package com.github.andrei1993ak.mentoring.task2.model.note.factory;

import android.content.Context;

import com.github.andrei1993ak.mentoring.task2.R;

public class StorageTypeResolver {

    public static @ICurrentStorageTypeHolder.StorageType
    int resolveType(final Context pContext, final String pTypeString) {
        final String localFileStorageString = pContext.getString(R.string.storageoptions_local_file);
        final String externalFileStorageString = pContext.getString(R.string.storageoptions_sd_card_file);
        final String memoryStorageString = pContext.getString(R.string.storageoptions_memory);
        final String preferencesFileStorageString = pContext.getString(R.string.storageoptions_preferences);

        if (localFileStorageString.equals(pTypeString)) {
            return ICurrentStorageTypeHolder.StorageType.LOCAL;
        } else if (memoryStorageString.equals(pTypeString)) {
            return ICurrentStorageTypeHolder.StorageType.MEMORY;
        } else if (externalFileStorageString.equals(pTypeString)) {
            return ICurrentStorageTypeHolder.StorageType.EXTERNAL;
        } else if (preferencesFileStorageString.equals(pTypeString)) {
            return ICurrentStorageTypeHolder.StorageType.PREFERENCES;
        } else {
            return ICurrentStorageTypeHolder.StorageType.DATABASE;
        }

    }
}
