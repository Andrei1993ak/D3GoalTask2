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
import com.github.andrei1993ak.mentoring.task2.activities.fragments.CreateEditNoteFragment;
import com.github.andrei1993ak.mentoring.task2.activities.fragments.NotesFragment;
import com.github.andrei1993ak.mentoring.task2.activities.fragments.SettingsFragment;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;

public class MainActivity extends AppCompatActivity implements IAppNavigator {

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
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        showFragment(SettingsFragment.getInstance(false));
    }

    @Override
    public void goToCreationNote(final boolean pIsFavouritePreselected) {
        showFragment(CreateEditNoteFragment.getCreationInstance(pIsFavouritePreselected));
    }

    @Override
    public void goToEditNote(final INote pNote) {
        showFragment(CreateEditNoteFragment.getEditInstance(pNote));
    }

    @Override
    public void goToDisplayingNotes() {
        showFragment(new NotesFragment());
    }

    private void showFragment(final Fragment pFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, pFragment).commit();
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
                showFragment(SettingsFragment.getInstance(true));
            } else if (id == R.id.nav_notes) {
                goToDisplayingNotes();
            }

            mDrawer.closeDrawer(GravityCompat.START);

            return true;
        }
    }
}
