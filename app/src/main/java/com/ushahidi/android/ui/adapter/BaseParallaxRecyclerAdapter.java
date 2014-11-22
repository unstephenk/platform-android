/*
 * Copyright (c) 2014 Ushahidi.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program in the file LICENSE-AGPL. If not, see
 * https://www.gnu.org/licenses/agpl-3.0.html
 */

package com.ushahidi.android.ui.adapter;

import com.google.common.base.Preconditions;

import com.ushahidi.android.model.Model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Provides parallax effect when scrolling through recyclerview items
 *
 * @author Ushahidi Team <team@ushahidi.com>
 * @credit Adapted from https://github.com/kanytu/android-parallax-recyclerview/blob/master/src/main/java/ParallaxRecyclerAdapter.java
 */
public class BaseParallaxRecyclerAdapter<M extends Model> extends BaseRecyclerViewAdapter<M> {

    private final float SCROLL_MULTIPLIER = 0.5f;

    private CustomRelativeWrapper mHeader;

    private OnClickEvent mOnClickEvent;

    private OnParallaxScroll mParallaxScroll;

    private RecyclerView mRecyclerView;

    private class VIEW_TYPES {

        public static final int NORMAL = 1;

        public static final int HEADER = 2;

        public static final int FIRST_VIEW = 3;
    }

    public void translateHeader(float of) {
        float ofCalculated = of * SCROLL_MULTIPLIER;
        mHeader.setTranslationY(ofCalculated);
        mHeader.setClipY(Math.round(ofCalculated));
        if (mParallaxScroll != null) {
            float left = Math.min(1, ((ofCalculated) / (mHeader.getHeight() * SCROLL_MULTIPLIER)));
            mParallaxScroll.onParallaxScroll(left, of, mHeader);
        }
    }

    public void setParallaxHeader(View header, final RecyclerView view) {
        mRecyclerView = view;
        mHeader = new CustomRelativeWrapper(header.getContext());
        mHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeader.addView(header, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mHeader != null) {
                    RecyclerView.ViewHolder holder = view.findViewHolderForPosition(0);
                    if (holder != null) {
                        translateHeader(-holder.itemView.getTop());
                    }
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        Preconditions
                .checkNotNull(mRecyclerviewViewHolder, "You must call setRecyclerviewViewHolder");
        if (position != 0 && mHeader != null) {
            mRecyclerviewViewHolder.onBindViewHolder(viewHolder, position - 1);
        } else if (position != 0) {
            mRecyclerviewViewHolder.onBindViewHolder(viewHolder, position);
        }
        if (mOnClickEvent != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickEvent.onClick(v, position - (mHeader == null ? 0 : 1));
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        Preconditions
                .checkNotNull(mRecyclerviewViewHolder, "You must call setRecyclerviewViewHolder");
        if (position == VIEW_TYPES.HEADER && mHeader != null) {
            return new ViewHolder(mHeader);
        }
        if (position == VIEW_TYPES.FIRST_VIEW && mHeader != null && mRecyclerView != null) {
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForPosition(0);
            if (holder != null) {
                translateHeader(-holder.itemView.getTop());
            }
        }
        return mRecyclerviewViewHolder.onCreateViewHolder(viewGroup, position);
    }

    public void setOnClickEvent(OnClickEvent onClickEvent) {
        mOnClickEvent = onClickEvent;
    }

    public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
        mParallaxScroll = parallaxScroll;
        mParallaxScroll.onParallaxScroll(0, 0, mHeader);
    }

    @Override
    public void addItem(M item, int position) {
        mItems.add(position, item);
        notifyItemInserted(position + (mHeader == null ? 0 : 1));
    }

    /*@Override
    public M getItem(int position) {
        return mItems.get(position + (mHeader == null ? 0 : 1));
    }*/

    @Override
    public void removeItem(M item) {
        int position = mItems.indexOf(item);
        if (position < 0) {
            return;
        }
        mItems.remove(item);
        notifyItemRemoved(position + (mHeader == null ? 0 : 1));
    }

    @Override
    public int getItemCount() {
        Preconditions
                .checkNotNull(mRecyclerviewViewHolder, "You must call setRecyclerviewViewHolder");
        return mRecyclerviewViewHolder.getItemCount() + (mHeader == null ? 0 : 1);
    }

    @Override
    public int getItemViewType(int position) {
        Preconditions
                .checkNotNull(mRecyclerviewViewHolder, "You must call setRecyclerviewViewHolder");
        if (position == 1) {
            return VIEW_TYPES.FIRST_VIEW;
        }
        return position == 0 ? VIEW_TYPES.HEADER : VIEW_TYPES.NORMAL;
    }

    public interface OnClickEvent {

        /**
         * Event triggered when you click on a item of the adapter
         *
         * @param v        view
         * @param position position on the array
         */
        void onClick(View v, int position);
    }

    public interface OnParallaxScroll {

        /**
         * Event triggered when the parallax is being scrolled.
         */
        void onParallaxScroll(float percentage, float offset, View parallax);
    }

    static class CustomRelativeWrapper extends RelativeLayout {

        private int mOffset;

        public CustomRelativeWrapper(Context context) {
            super(context);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
            super.dispatchDraw(canvas);
        }

        public void setClipY(int offset) {
            mOffset = offset;
            invalidate();
        }
    }
}
