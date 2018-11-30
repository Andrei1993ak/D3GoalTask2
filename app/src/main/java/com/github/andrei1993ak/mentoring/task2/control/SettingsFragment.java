package com.github.andrei1993ak.mentoring.task2.control;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.model.loaders.ICurrentStorageTypeHolder;
import com.github.andrei1993ak.mentoring.task2.model.loaders.StorageTypeResolver;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (key.equals(getString(R.string.storage_pref_key))) {
            final String selectedStorageType = sharedPreferences.getString(key, getString(R.string.storageoptions_stub));
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
}
