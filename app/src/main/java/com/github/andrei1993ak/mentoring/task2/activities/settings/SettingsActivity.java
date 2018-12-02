package com.github.andrei1993ak.mentoring.task2.activities.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    static final String SKIP_ASKING_PERMISSIONS = "skipAskingPermissions";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showSettings(null);
    }

    private void showSettings(final Bundle pBundle) {
        final SettingsFragment settingsFragment = new SettingsFragment();

        if (pBundle != null) {
            settingsFragment.setArguments(pBundle);
        }

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, settingsFragment)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final String[] permissions, final int[] grantResults) {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(SKIP_ASKING_PERMISSIONS, true);
        showSettings(bundle);
    }
}
