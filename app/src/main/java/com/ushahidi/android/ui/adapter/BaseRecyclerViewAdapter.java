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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ushahidi.android.model.Model;
import com.ushahidi.android.ui.widget.BloatedRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Base RecyclerView Adapter class for all RecyclerView adapters for a specific Model class
 *
 * @param <M> Model class
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseRecyclerViewAdapter<M extends Model>
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<M> mItems;

    private View mInfiniteScrollView = null;

    public boolean isLoadMoreChanged = false;

    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);

    /**
     * Returns the number of items in the adapter bound to the parent RecyclerView.
     *
     * @return The number of items in the bound adapter
     */
    public abstract int getAdapterItemCount();

    /**
     * Set the header view of the adapter.
     */
    public void setCustomHeaderView(BloatedRecyclerView.CustomRelativeWrapper customHeaderView) {
        this.customHeaderView = customHeaderView;
    }

    public BloatedRecyclerView.CustomRelativeWrapper getCustomHeaderView() {
        return customHeaderView;
    }

    protected BloatedRecyclerView.CustomRelativeWrapper customHeaderView = null;

    public BaseRecyclerViewAdapter() {
        mItems = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        if (position == VIEW_TYPES.FOOTER) {
            RecyclerView.ViewHolder viewHolder = new CustomViewViewHolder(mInfiniteScrollView);
            if (getAdapterItemCount() == 0)
                viewHolder.itemView.setVisibility(View.GONE);
            return viewHolder;
        } else if (position == VIEW_TYPES.HEADER) {
            if (customHeaderView != null)
                return new CustomViewViewHolder(customHeaderView);
        } else if (position == VIEW_TYPES.CHANGED_FOOTER) {
            RecyclerView.ViewHolder viewHolder = new CustomViewViewHolder(mInfiniteScrollView);
            if (getAdapterItemCount() == 0)
                viewHolder.itemView.setVisibility(View.GONE);
            return viewHolder;
        }

        return onCreateViewHolder(viewGroup);
    }

    public List<M> getItems() {
        return mItems;
    }

    public void setItems(List<M> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public M getItem(int position) {
        return mItems.get(position);
    }

    public void addItem(M item, int position) {
        mItems.add(position, item);
        if (customHeaderView != null) position++;
        notifyItemInserted(position);
    }

    public void removeItem(M item) {
        int position = mItems.indexOf(item);
        if (position < 0) {
            return;
        }
        mItems.remove(customHeaderView != null ? position - 1 : position);
        notifyItemRemoved(position);
    }

    public void setInfiniteScrollView(View customview) {
        mInfiniteScrollView = customview;
    }

    public void swipeInfiniteScrollView(View customview) {
        mInfiniteScrollView = customview;
        isLoadMoreChanged = true;
    }

    public View getInfiniteScrollView() {
        return mInfiniteScrollView;
    }

    @Override
    public int getItemCount() {

        int headerOrFooter = 0;

        if (customHeaderView != null) {
            headerOrFooter++;
        }

        if (mInfiniteScrollView != null) {
            headerOrFooter++;
        }

        return getAdapterItemCount() + headerOrFooter;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && mInfiniteScrollView != null) {
            if (isLoadMoreChanged) {
                return VIEW_TYPES.CHANGED_FOOTER;
            } else {
                return VIEW_TYPES.FOOTER;
            }

        } else if (position == 0 && customHeaderView != null) {
            return VIEW_TYPES.HEADER;
        } else
            return VIEW_TYPES.NORMAL;
    }

    class CustomViewViewHolder extends RecyclerView.ViewHolder {
        public CustomViewViewHolder(View itemView) {
            super(itemView);
        }

    }

    private class VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int HEADER = 1;
        public static final int FOOTER = 2;
        public static final int CHANGED_FOOTER = 3;
    }
}
