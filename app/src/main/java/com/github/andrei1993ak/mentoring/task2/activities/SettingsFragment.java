package com.github.andrei1993ak.mentoring.task2.activities;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.ICurrentStorageTypeHolder;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.StorageTypeResolver;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        final Bundle arguments = getArguments();

        if (arguments == null || !arguments.getBoolean(NotesActivity.SKIP_ASKING_PERMISSIONS, true)) {
            checkPermissionReadStorage(getActivity());
        }

        if (permissionNeeded(getActivity())) {
            final ListPreference preference = (ListPreference) findPreference(getString(R.string.storage_pref_key));
            final String[] limitedOptions = getResources().getStringArray(R.array.storageLimitedOptions);
            preference.setEntries(limitedOptions);
            preference.setEntryValues(limitedOptions);
        }
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (key.equals(getString(R.string.storage_pref_key))) {
            final String selectedStorageType = sharedPreferences.getString(key, getString(R.string.storageoptions_preferences));
            final int selected = StorageTypeResolver.resolveType(getActivity(), selectedStorageType);
            ICurrentStorageTypeHolder.Impl.get(getActivity()).setCurrentItem(selected);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

    }

    public void checkPermissionReadStorage(final Activity activity) {
        if (permissionNeeded(activity)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_STORAGE);
        }
    }

    private boolean permissionNeeded(final Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }
}
