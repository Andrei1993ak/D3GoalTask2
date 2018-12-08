package com.github.andrei1993ak.mentoring.task2.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.activities.fragments.CreateEditNoteFragment;
import com.github.andrei1993ak.mentoring.task2.activities.fragments.ITitled;
import com.github.andrei1993ak.mentoring.task2.activities.fragments.NotesFragment;
import com.github.andrei1993ak.mentoring.task2.activities.fragments.SettingsFragment;
import com.github.andrei1993ak.mentoring.task2.fcm.NotesFirebaseMessagingService;
import com.github.andrei1993ak.mentoring.task2.fcm.PushNotificationDataKeys;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IAppNavigator {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private FragmentManager mSupportFragmentManager;
    private Handler mHandler;
    private PushNotificationBroadcastReceiver mPushNotificationBroadcastReceiver;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notes);
        ButterKnife.bind(this);

        mPushNotificationBroadcastReceiver = new PushNotificationBroadcastReceiver();
        mHandler = new Handler(Looper.getMainLooper());
        mSupportFragmentManager = getSupportFragmentManager();
        mSupportFragmentManager.addOnBackStackChangedListener(new OnBackStackChangedListener());
        LocalBroadcastManager.getInstance(this).registerReceiver(mPushNotificationBroadcastReceiver, new IntentFilter(NotesFirebaseMessagingService.PUSH_MESSAGE_BROADCAST));

        initViews();

        if (savedInstanceState == null) {
            goToDisplayingNotes();
            checkPushNotification();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPushNotificationBroadcastReceiver);
    }

    private void checkPushNotification() {
        final Intent intent = getIntent();

        if (intent != null) {
            final Bundle extras = intent.getExtras();

            if (extras != null) {
                final String title = extras.getString(PushNotificationDataKeys.TITLE);
                final String description = extras.getString(PushNotificationDataKeys.DESCRIPTION);

                goToCreationNote(false, title, description);
            }
        }
    }

    private void initViews() {
        setSupportActionBar(mToolbar);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener());
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        replaceFragment(SettingsFragment.getInstance(false), true);
    }

    @Override
    public void goToCreationNote(final boolean pIsFavourite, @Nullable final String pTile, @Nullable final String pDescription) {
        replaceFragment(CreateEditNoteFragment.getCreationInstance(pIsFavourite, pTile, pDescription), true);
    }

    @Override
    public void goToEditNote(final INote pNote) {
        replaceFragment(CreateEditNoteFragment.getEditInstance(pNote), true);
    }

    @Override
    public void goToDisplayingNotes() {
        replaceFragment(new NotesFragment(), false);
    }

    private void replaceFragment(final Fragment pFragment, final boolean pAddToBackStack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final FragmentTransaction fragmentTransaction = mSupportFragmentManager.beginTransaction();

                if (pAddToBackStack) {
                    fragmentTransaction.addToBackStack(pFragment.getClass().getSimpleName());
                }

                fragmentTransaction.replace(R.id.content, pFragment).commit();

                updateToolbar(pFragment);
            }
        });
    }

    private void updateToolbar(final Fragment pFragment) {
        if (pFragment instanceof ITitled) {
            mToolbar.setTitle(((ITitled) pFragment).getTitleResId());
        } else {
            mToolbar.setTitle(R.string.app_name);
        }
    }

    private void onBroadCastReceived(final String pTitle, final String pDescription) {
        final AddNotificationDialog addNotificationDialog = AddNotificationDialog.newInstance(pTitle);
        addNotificationDialog.show(getFragmentManager(), AddNotificationDialog.class.getSimpleName());

        addNotificationDialog.setOnClickListener(new AddNotificationDialog.OnClickListener() {
            @Override
            public void onYesClicked() {
                goToCreationNote(false, pTitle, pDescription);
            }

            @Override
            public void onSkipClicked() {

            }
        });
    }

    private class PushNotificationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final Bundle extras = intent.getExtras();

            if (extras != null) {
                final String title = extras.getString(PushNotificationDataKeys.TITLE);
                final String description = extras.getString(PushNotificationDataKeys.DESCRIPTION);

                if (UiUtils.isContextAlive(MainActivity.this)) {
                    onBroadCastReceived(title, description);
                }
            }
        }
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
                replaceFragment(SettingsFragment.getInstance(true), true);
            } else if (id == R.id.nav_notes) {
                goToDisplayingNotes();
            }

            mDrawer.closeDrawer(GravityCompat.START);

            return true;
        }
    }

    private class OnBackStackChangedListener implements FragmentManager.OnBackStackChangedListener {
        @Override
        public void onBackStackChanged() {
            updateToolbar(mSupportFragmentManager.findFragmentById(R.id.content));
        }
    }
}
