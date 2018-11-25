package com.github.andrei1993ak.mentoring.task2.uiutils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;

public class ContextMenuRecyclerView extends RecyclerView {

    private RecyclerViewContextMenuInfo mContextMenuInfo;

    public ContextMenuRecyclerView(final Context context) {
        super(context);
    }

    public ContextMenuRecyclerView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public ContextMenuRecyclerView(final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return mContextMenuInfo;
    }

    @Override
    public boolean showContextMenuForChild(final View originalView) {
        final int longPressPosition = getChildAdapterPosition(originalView);

        if (longPressPosition >= 0) {
            final long longPressId = getAdapter().getItemId(longPressPosition);

            mContextMenuInfo = new RecyclerViewContextMenuInfo(longPressPosition, longPressId);
            return super.showContextMenuForChild(originalView);
        }
        return false;
    }

    public static class RecyclerViewContextMenuInfo implements ContextMenu.ContextMenuInfo {

        RecyclerViewContextMenuInfo(final int position, final long id) {
            this.position = position;
            this.id = id;
        }

        final public int position;
        final public long id;
    }
}