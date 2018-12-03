package com.github.andrei1993ak.mentoring.task2.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;

public class NotesActivity extends AppCompatActivity implements IAppNavigator {

    static final String SKIP_ASKING_PERMISSIONS = "skipAskingPermissions";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notes);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener());

        goToDisplayingNotes();
    }

    private void showFragment(final Fragment pFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, pFragment).commit();
    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final String[] permissions, final int[] grantResults) {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(SKIP_ASKING_PERMISSIONS, true);
        showSettings(bundle);
    }

    private void showSettings(final Bundle pBundle) {
        final SettingsFragment settingsFragment = new SettingsFragment();

        if (pBundle != null) {
            settingsFragment.setArguments(pBundle);
        }

        showFragment(settingsFragment);
    }

    @Override
    public void goToCreationNote(final boolean pIsFavouritePreselected) {
        startActivity(CreateEditNoteActivity.getCreateNoteIntent(this, pIsFavouritePreselected));
    }

    @Override
    public void goToEditNote(final INote pNote) {
        startActivity(CreateEditNoteActivity.getEditNoteIntent(this, pNote));
    }

    @Override
    public void goToDisplayingNotes() {
        showFragment(new NotesFragment());
    }

    private class OnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

        private final DrawerLayout mDrawer;

        private OnNavigationItemSelectedListener() {
            mDrawer = findViewById(R.id.drawer_layout);
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
            final int id = item.getItemId();

            if (id == R.id.nav_manage) {
                showSettings(null);
            } else if (id == R.id.nav_notes) {
                goToDisplayingNotes();
            }

            mDrawer.closeDrawer(GravityCompat.START);

            return true;
        }
    }
}
