package com.github.andrei1993ak.mentoring.task2.model.note.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.andrei1993ak.mentoring.task2.R;
import com.github.andrei1993ak.mentoring.task2.model.note.INote;
import com.github.andrei1993ak.mentoring.task2.utils.views.RightDrawableOnTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<INote> mNotes;
    private final IOnNoteActionsClickListener mIOnNoteActionsClickListener;

    public NotesAdapter(final Context pContext, final IOnNoteActionsClickListener pOnNoteActionsClickListener) {
        mLayoutInflater = LayoutInflater.from(pContext);
        mNotes = new ArrayList<>();
        mIOnNoteActionsClickListener = pOnNoteActionsClickListener;
    }

    @Override
    public long getItemId(final int position) {
        return mNotes.get(position).getId();
    }

    public void updateNotes(@Nullable final List<INote> pUpdatedNotes) {
        mNotes.clear();

        if (pUpdatedNotes != null) {
            mNotes.addAll(pUpdatedNotes);
        }

        notifyDataSetChanged();
    }

    public void deleteNote(final long pId) {
        int position = -1;

        for (int i = 0; i < mNotes.size(); i++) {
            if (mNotes.get(i).getId().equals(pId)) {
                position = i;

                break;
            }
        }

        if (position != -1) {
            mNotes.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull final ViewGroup pViewGroup, final int pI) {
        return new NotesViewHolder(mLayoutInflater.inflate(R.layout.adapte_view_note_item, pViewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder pNotesViewHolder, final int pI) {
        pNotesViewHolder.bindData(mNotes.get(pI));
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public INote getItem(final long pId) {
        for (int i = 0; i < mNotes.size(); i++) {
            if (getItemId(i) == pId) {
                return mNotes.get(i);
            }
        }

        return null;
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.note_item_title)
        TextView mNoteTitle;

        @BindView(R.id.note_item_description)
        TextView mNoteDescription;

        private long mNoteId;

        NotesViewHolder(@NonNull final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mNoteTitle.setOnTouchListener(new RightDrawableOnTouchListener(mNoteTitle) {
                @Override
                public boolean onDrawableTouch(final MotionEvent event) {

                    if (mIOnNoteActionsClickListener != null) {
                        mIOnNoteActionsClickListener.onActionClick(mNoteId);
                    }
                    return false;
                }
            });
        }

        void bindData(final INote pNote) {
            mNoteTitle.setText(pNote.getTitle());
            mNoteDescription.setText(pNote.getDescription());
            mNoteId = pNote.getId();
        }
    }
}
