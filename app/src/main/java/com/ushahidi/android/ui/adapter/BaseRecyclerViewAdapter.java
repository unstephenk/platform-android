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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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

    protected List<M> mItems;

    protected RecyclerviewViewHolder mRecyclerviewViewHolder;

    public BaseRecyclerViewAdapter() {
        mItems = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        Preconditions
                .checkNotNull(mRecyclerviewViewHolder, "You must call setRecyclerviewViewHolder");
        return mRecyclerviewViewHolder.onCreateViewHolder(viewGroup, position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        Preconditions
                .checkNotNull(mRecyclerviewViewHolder, "You must call setRecyclerviewViewHolder");
        mRecyclerviewViewHolder.onBindViewHolder(viewHolder, position);
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
        notifyItemInserted(position);
    }

    public void removeItem(M item) {
        int position = mItems.indexOf(item);
        if (position < 0) {
            return;
        }
        mItems.remove(item);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        Preconditions
                .checkNotNull(mRecyclerviewViewHolder, "You must call setRecyclerviewViewHolder");
        return mRecyclerviewViewHolder.getItemCount();
    }

    public void setRecyclerviewViewHolder(RecyclerviewViewHolder recyclerviewViewHolder) {
        mRecyclerviewViewHolder = recyclerviewViewHolder;
    }

    public interface RecyclerviewViewHolder {

        void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position);

        RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position);

        int getItemCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
