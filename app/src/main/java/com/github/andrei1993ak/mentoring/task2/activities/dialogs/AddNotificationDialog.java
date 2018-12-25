package com.github.andrei1993ak.mentoring.task2.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.github.andrei1993ak.mentoring.task2.R;

public class AddNotificationDialog extends DialogFragment {

    private static final String NOTE_TITLE = "note_title_key";
    private OnClickListener mOnClickListener;

    public static AddNotificationDialog newInstance(final String pNoteTitle) {
        final AddNotificationDialog fragment = new AddNotificationDialog();

        final Bundle args = new Bundle();
        args.putString(NOTE_TITLE, pNoteTitle);
        fragment.setArguments(args);

        return fragment;
    }

    public void setOnClickListener(final OnClickListener pOnClickListener) {
        mOnClickListener = pOnClickListener;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final String noteTitle = getArguments().getString(NOTE_TITLE);

        final Activity activity = getActivity();
        final String title = activity.getString(R.string.add_note_dialog_title);
        final String bodyTemplate = activity.getString(R.string.add_note_dialog_body);

        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(String.format(bodyTemplate, noteTitle))
                .setNegativeButton(activity.getString(R.string.add_note_dialog_skip_button), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface arg0, final int arg1) {
                        if (mOnClickListener != null)
                            mOnClickListener.onSkipClicked();
                    }
                })

                .setPositiveButton(activity.getString(R.string.add_note_dialog_create_button), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface arg0, final int arg1) {
                        if (mOnClickListener != null)
                            mOnClickListener.onYesClicked();
                    }
                })
                .create();
    }

    public interface OnClickListener {
        void onYesClicked();

        void onSkipClicked();
    }
}
