package com.github.andrei1993ak.mentoring.task2.activities.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.activities.IAppNavigator;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.utils.TextUtils;
import com.github.andrei1993ak.mentoring.task2.utils.UiUtils;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class CreateEditNoteFragment extends Fragment implements ITitled {

    private static final String EXTRA_NOTE_KEY = "noteKey";
    private static final String EXTRA_NOTE_ID_KEY = "noteIdKey";
    private static final String EXTRA_NOTE_FAVOURITE_KEY = "isFavouriteNoteKey";
    private static final String EXTRA_NOTE_TITLE_KEY = "titletNoteKey";
    private static final String EXTRA_NOTE_DESCRIPTION_KEY = "descriptionNoteKey";
    private static final String CREATE_NOTE_ACTION = "create_note";
    private static final String EDIT_NOTE_ACTION = "edit_note";
    private static final String ACTION_KEY = "action";

    private boolean mIsCreationMode;

    @BindView(R.id.create_edit_note_title)
    EditText mTitleEditText;

    @BindView(R.id.create_edit_note_description)
    EditText mDescriptionEditText;

    @BindView(R.id.create_edit_note_is_favouriote_check_box)
    AppCompatCheckBox mIsFavouriteCheckBox;

    private long mId;

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCompositeDisposable.dispose();
    }

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static CreateEditNoteFragment getCreationInstance(final boolean pIsFavouritePreselected,
                                                             final String pTitle, final String pDescription) {
        final Bundle args = new Bundle();

        args.putString(ACTION_KEY, CREATE_NOTE_ACTION);
        args.putBoolean(EXTRA_NOTE_FAVOURITE_KEY, pIsFavouritePreselected);
        args.putString(EXTRA_NOTE_TITLE_KEY, pTitle == null ? TextUtils.Constants.EMPTY : pTitle);
        args.putString(EXTRA_NOTE_DESCRIPTION_KEY, pDescription == null ? TextUtils.Constants.EMPTY : pDescription);

        final CreateEditNoteFragment createEditNoteFragment = new CreateEditNoteFragment();
        createEditNoteFragment.setArguments(args);

        return createEditNoteFragment;
    }

    public static CreateEditNoteFragment getEditInstance(@NonNull final INote pINote) {
        final Bundle args = new Bundle();

        args.putString(ACTION_KEY, EDIT_NOTE_ACTION);
        args.putLong(EXTRA_NOTE_ID_KEY, pINote.getId());
        args.putSerializable(EXTRA_NOTE_KEY, pINote);

        final CreateEditNoteFragment createEditNoteFragment = new CreateEditNoteFragment();
        createEditNoteFragment.setArguments(args);

        return createEditNoteFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_create_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        final Bundle arguments = getArguments();

        if (arguments != null) {
            final String action = arguments.getString(ACTION_KEY);

            if (CREATE_NOTE_ACTION.equals(action)) {
                mIsCreationMode = true;
                mIsFavouriteCheckBox.setChecked(arguments.getBoolean(EXTRA_NOTE_FAVOURITE_KEY, false));
                mTitleEditText.setText(arguments.getString(EXTRA_NOTE_TITLE_KEY));
                mDescriptionEditText.setText(arguments.getString(EXTRA_NOTE_DESCRIPTION_KEY));
            } else {
                mId = arguments.getLong(EXTRA_NOTE_ID_KEY, -1);
                final INote editableNote = (INote) arguments.getSerializable(EXTRA_NOTE_KEY);

                if (editableNote != null) {
                    mTitleEditText.setText(editableNote.getTitle());
                    mDescriptionEditText.setText(editableNote.getDescription());
                    mIsFavouriteCheckBox.setChecked(editableNote.isFavourite());
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_note, menu);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int itemId = item.getItemId();

        if (itemId == R.id.action_save) {

            final String title = mTitleEditText.getText().toString();
            final String description = mDescriptionEditText.getText().toString();
            final Context context = getContext();

            if (TextUtils.isEmpty(title) && TextUtils.isEmpty(description)) {
                Toast.makeText(context, R.string.title_or_description_should_be_not_empty, Toast.LENGTH_LONG).show();

                return true;
            } else {
                final INotesModelFactory notesLoaderFactory = INotesModelFactory.Impl.get(context);

                final Completable completable;

                if (mIsCreationMode) {
                    completable = notesLoaderFactory.getCreateNoteCompletable(title, description, mIsFavouriteCheckBox.isChecked());
                } else {
                    completable = notesLoaderFactory.getUpdateNoteCompletable(mId, title, description, mIsFavouriteCheckBox.isChecked());
                }

                mCompositeDisposable.add(
                        completable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new CompletableObserver()));
            }
        } else if (itemId == R.id.action_share_note) {
            final StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            saveScreenShot(getScreenShot());

            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(getScreenShotFile()));

            startActivity(shareIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private Bitmap getScreenShot() {
        final FragmentActivity activity = getActivity();

        if (UiUtils.isContextAlive(activity)) {
            final View screenView = activity.getWindow().getDecorView().getRootView();
            screenView.setDrawingCacheEnabled(true);
            final Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
            screenView.setDrawingCacheEnabled(false);

            return bitmap;
        } else {
            return null;
        }

    }

    private void saveScreenShot(final Bitmap bm) {
        final File file = getScreenShotFile();

        try {
            final FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private File getScreenShotFile() {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        final File dir = new File(dirPath);

        if (!dir.exists())
            dir.mkdirs();
        return new File(dirPath, "note.jpg");
    }

    @Override
    public int getTitleResId() {
        return R.string.manage_note;
    }


    public static void hideKeyboardFrom(final Context context, final View view) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class CompletableObserver extends DisposableCompletableObserver {
        @Override
        public void onComplete() {
            onOperationResult();
        }

        @Override
        public void onError(final Throwable e) {
            final Context context = getContext();

            if (UiUtils.isContextAlive(context)) {
                Toast.makeText(context, R.string.error_message, Toast.LENGTH_LONG).show();
            }

            onOperationResult();
        }

        private void onOperationResult() {
            final FragmentActivity activity = getActivity();

            if (activity instanceof IAppNavigator && UiUtils.isContextAlive(activity)) {
                hideKeyboardFrom(activity, activity.getCurrentFocus());
            }

            final FragmentManager fragmentManager = getFragmentManager();

            if (fragmentManager != null) {
                fragmentManager.popBackStack();
            }
        }
    }
}
