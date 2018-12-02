package com.github.andrei1993ak.mentoring.task2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.activities.settings.SettingsActivity;

public class NotesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTabHost mTabHost;
    private String mAllNotesString;
    private String mFavouriteNotesString;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String currentTabTag = mTabHost.getCurrentTabTag();
                goToCreateNote(mFavouriteNotesString.equals(currentTabTag));
            }
        });

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mAllNotesString = getResources().getString(R.string.all_notes);
        mFavouriteNotesString = getResources().getString(R.string.favourite_notes);

        mTabHost.addTab(
                mTabHost.newTabSpec(mAllNotesString).setIndicator(mAllNotesString, null),
                NotesTabFragment.class, NotesTabFragment.getAllNotesArguments());
        mTabHost.addTab(
                mTabHost.newTabSpec(mFavouriteNotesString).setIndicator(mFavouriteNotesString, null),
                NotesTabFragment.class, NotesTabFragment.getFavouritesNotesArguments());
    }

    private void goToCreateNote(final boolean isFavouriteTabSelected) {
        startActivity(CreateEditNoteActivity.getCreateNoteIntent(this, isFavouriteTabSelected));
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.nav_manage) {
            startActivityForResult(new Intent(this, SettingsActivity.class), 222);
        }

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
