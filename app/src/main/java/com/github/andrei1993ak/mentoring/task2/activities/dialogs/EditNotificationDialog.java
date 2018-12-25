package com.github.andrei1993ak.mentoring.task2.activities.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.andrei1993ak.mentoring.task2.R;

public class EditNotificationDialog extends DialogFragment implements View.OnClickListener {

    private static final String NOTE_ID = "noteId";

    private OnClickListener mOnClickListener;
    public TextView mDeleteNoteTextView;
    public TextView mEditNoteTextView;
    private long mNoteId;

    public static EditNotificationDialog newInstance(final long pNoteId) {
        final EditNotificationDialog fragment = new EditNotificationDialog();

        final Bundle args = new Bundle();
        args.putLong(NOTE_ID, pNoteId);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(getResources().getLayout(R.layout.dailog_note_actions), container, false);

        mNoteId = getArguments().getLong(NOTE_ID,0);

        mDeleteNoteTextView = view.findViewById(R.id.action_delete_note);
        mEditNoteTextView = view.findViewById(R.id.action_edit_note);
        mDeleteNoteTextView.setOnClickListener(this);
        mEditNoteTextView.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.action_delete_note:

                if (mOnClickListener != null) {
                    mOnClickListener.onDeleteClicked(mNoteId);
                }
                break;
            case R.id.action_edit_note:

                if (mOnClickListener != null) {
                    mOnClickListener.onEditClicked(mNoteId);
                }
                break;
            default:
                break;
        }

        dismiss();
    }

    public void setOnClickListener(final OnClickListener pOnClickListener) {
        mOnClickListener = pOnClickListener;
    }

    public interface OnClickListener {
        void onDeleteClicked(final long pNoteId);

        void onEditClicked(final long pNoteId);
    }
}

