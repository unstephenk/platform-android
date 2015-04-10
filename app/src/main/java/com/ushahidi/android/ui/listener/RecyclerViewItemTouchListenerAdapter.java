/*
 *  Copyright (c) 2015 Ushahidi.
 *
 *   This program is free software: you can redistribute it and/or modify it under
 *   the terms of the GNU Affero General Public License as published by the Free
 *   Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 *   This program is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *   FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program in the file LICENSE-AGPL. If not, see
 *   https://www.gnu.org/licenses/agpl-3.0.html
 *
 */

package com.ushahidi.android.ui.listener;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * RecyclerView does not have standard way to add click listeners to the items,
 * this RecyclerView.OnItemTouchListener intercepts touch events and translates them to simple onItemClick and onItemLongClick callbacks.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class RecyclerViewItemTouchListenerAdapter extends GestureDetector.SimpleOnGestureListener implements RecyclerView.OnItemTouchListener {

    private RecyclerViewOnItemClickListener listener;
    private RecyclerView recyclerView;
    private GestureDetector gestureDetector;

    public RecyclerViewItemTouchListenerAdapter(RecyclerView recyclerView, RecyclerViewOnItemClickListener listener) {
        if (recyclerView == null || listener == null) {
            throw new IllegalArgumentException("RecyclerView and Listener arguments can not be null");
        }
        this.recyclerView = recyclerView;
        this.listener = listener;
        this.gestureDetector = new GestureDetector(recyclerView.getContext(), this);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    @Override
    public void onShowPress(MotionEvent e) {
        View view = getChildViewUnder(e);
        if (view != null) {
            view.setPressed(true);
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        View view = getChildViewUnder(e);
        if (view == null) return false;

        view.setPressed(false);
        int position = recyclerView.getChildAdapterPosition(view);
        listener.onItemClick(recyclerView, view, position);
        return true;
    }

    public void onLongPress(MotionEvent e) {
        View view = getChildViewUnder(e);
        if (view == null) return;
        int position = recyclerView.getChildAdapterPosition(view);
        listener.onItemLongClick(recyclerView, view, position);
        view.setPressed(false);
    }

    @Nullable
    private View getChildViewUnder(MotionEvent e) {
        return recyclerView.findChildViewUnder(e.getX(), e.getY());
    }

    public interface RecyclerViewOnItemClickListener {
        void onItemClick(RecyclerView parent, View clickedView, int position);

        void onItemLongClick(RecyclerView parent, View clickedView, int position);
    }
}
