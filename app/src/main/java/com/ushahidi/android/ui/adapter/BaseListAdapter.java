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

import com.ushahidi.android.model.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseListAdapter Base class for all list adapters for a specific BaseModel class
 *
 * @param <M> Model class
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseListAdapter<M extends Model> extends BaseAdapter {

    protected final Context mContext;

    protected final LayoutInflater mInflater;

    protected final List<M> mItems;

    public BaseListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mItems = new ArrayList<M>();
    }

    public int getCount() {
        return this.mItems.size();
    }

    @Override
    public boolean isEmpty() {
        return (getCount() == 0);
    }

    public void setItems(List<M> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(M item) {
        this.mItems.add(item);
        notifyDataSetChanged();
    }

    public M getItem(int position) {
        return mItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int indexOf(M item) {
        return mItems.indexOf(item);
    }

    public void clearItems() {
        this.mItems.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.mItems.remove(position);
        notifyDataSetChanged();
    }

    public void removeItem(M item) {
        this.mItems.remove(item);
        notifyDataSetChanged();
    }
}
