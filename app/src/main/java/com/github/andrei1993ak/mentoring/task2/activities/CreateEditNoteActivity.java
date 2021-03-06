package com.github.andrei1993ak.mentoring.task2.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.core.ICallExecutor;
import com.github.andrei1993ak.mentoring.task2.core.ICallable;
import com.github.andrei1993ak.mentoring.task2.core.ISuccess;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.model.note.factory.INotesModelFactory;
import com.github.andrei1993ak.mentoring.task2.utils.TextUtils;
import com.github.andrei1993ak.mentoring.task2.utils.UiUtils;

public class CreateEditNoteActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE_KEY = "noteKey";
    private static final String EXTRA_NOTE_ID_KEY = "noteIdKey";
    private static final String EXTRA_NOTE_FAVOURITE_KEY = "isFavouriteNoteIdKey";
    private static final String CREATE_NOTE_ACTION = "create_note";
    private static final String EDIT_NOTE_ACTION = "edit_note";

    private boolean mIsCreationMode;
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private AppCompatCheckBox mIsFavouriteCheckBox;
    private long mId;

    static Intent getCreateNoteIntent(final Context pContext, final boolean pIsFavouriteTabSelected) {
        final Intent intent = new Intent(pContext, CreateEditNoteActivity.class);
        intent.setAction(CREATE_NOTE_ACTION);
        intent.putExtra(EXTRA_NOTE_FAVOURITE_KEY, pIsFavouriteTabSelected);

        return intent;
    }

    static Intent getEditNoteIntent(final Context pContext, @NonNull final INote pINote) {
        final Intent intent = new Intent(pContext, CreateEditNoteActivity.class);
        intent.setAction(EDIT_NOTE_ACTION);
        intent.putExtra(EXTRA_NOTE_KEY, pINote);
        intent.putExtra(EXTRA_NOTE_ID_KEY, pINote.getId());

        return intent;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_edit_note);

        final Intent intent = getIntent();
        final String action = intent.getAction();

        mTitleEditText = findViewById(R.id.create_edit_note_title);
        mDescriptionEditText = findViewById(R.id.create_edit_note_description);
        mIsFavouriteCheckBox = findViewById(R.id.create_edit_note_is_favouriote_check_box);

        if (CREATE_NOTE_ACTION.equals(action)) {
            mIsCreationMode = true;
            mIsFavouriteCheckBox.setChecked(intent.getBooleanExtra(EXTRA_NOTE_FAVOURITE_KEY, false));
        } else {
            mId = intent.getLongExtra(EXTRA_NOTE_ID_KEY, -1);
            final INote editableNote = (INote) intent.getSerializableExtra(EXTRA_NOTE_KEY);

            mTitleEditText.setText(editableNote.getTitle());
            mDescriptionEditText.setText(editableNote.getDescription());
            mIsFavouriteCheckBox.setChecked(editableNote.isFavourite());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu pMenu) {
        getMenuInflater().inflate(R.menu.menu_create_note, pMenu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.action_save) {

            final String title = mTitleEditText.getText().toString();
            final String description = mDescriptionEditText.getText().toString();

            if (TextUtils.isEmpty(title) && TextUtils.isEmpty(description)) {
                Toast.makeText(this, R.string.title_or_description_should_be_not_empty, Toast.LENGTH_LONG).show();

                return true;
            } else {
                final INotesModelFactory notesLoaderFactory = INotesModelFactory.Impl.get(this);

                final ICallable<Boolean> callable;

                if (mIsCreationMode) {
                    callable = notesLoaderFactory.getCreateNoteCallable(title, description, mIsFavouriteCheckBox.isChecked());
                } else {
                    callable = notesLoaderFactory.getUpdateNoteCallable(mId, title, description, mIsFavouriteCheckBox.isChecked());
                }

                ICallExecutor.Impl.newInstance(callable).enqueue(new OperationSuccess());
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class OperationSuccess implements ISuccess<Boolean> {
        @Override
        public void onResult(final Boolean pBoolean) {
            finish();
        }

        @Override
        public void onError(final Throwable pThrowable) {
            finish();
        }

        @Override
        public boolean isAlive() {
            return UiUtils.isContextAlive(CreateEditNoteActivity.this);
        }
    }
}
