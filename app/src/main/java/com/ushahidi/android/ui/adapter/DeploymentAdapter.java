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

import com.ushahidi.android.R;
import com.ushahidi.android.model.DeploymentModel;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages list of deployments
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentAdapter extends BaseRecyclerViewAdapter<DeploymentModel> implements
        Filterable {

    private SparseBooleanArray mSelectedItems;

    private Filter mFilter = null;

    public DeploymentAdapter() {
        this.setRecyclerviewViewHolder(mRecyclerviewViewHolder);
        mSelectedItems = new SparseBooleanArray();
    }

    RecyclerviewViewHolder mRecyclerviewViewHolder = new RecyclerviewViewHolder() {
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ((Widgets) viewHolder).title.setText(getItem(position).getTitle());
            ((Widgets) viewHolder).url.setText(getItem(position).getUrl());
            ((Widgets) viewHolder).listCheckBox.setChecked(mSelectedItems.get(position, false));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new Widgets(LayoutInflater.from(
                    viewGroup.getContext())
                    .inflate(R.layout.list_deployment_item, viewGroup, false));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    };

    public void toggleSelection(int position) {
        if (mSelectedItems.get(position, false)) {
            mSelectedItems.delete(position);
        } else {
            mSelectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public int getSelectedItemCount() {
        return mSelectedItems.size();
    }

    public void clearSelections() {
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); i++) {
            items.add(mSelectedItems.keyAt(i));
        }
        return items;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new DeploymentFilter();
        }
        return mFilter;
    }

    private class DeploymentFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            constraint = constraint.toString().toLowerCase();
            results.values = mItems;
            results.count = mItems.size();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<DeploymentModel> filteredItems = new ArrayList<>();
                //TODO: query the mItems from the database and use that for comparison
                for (DeploymentModel deployment : mItems) {
                    if (deployment.getTitle().toLowerCase().contains(constraint.toString())) {
                        filteredItems.add(deployment);
                    }
                }
                results.count = filteredItems.size();
                results.values = filteredItems;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            List<DeploymentModel> deploymentModels
                    = (ArrayList<DeploymentModel>) filterResults.values;
            setItems(deploymentModels);
        }
    }

    public class Widgets extends RecyclerView.ViewHolder {

        TextView title;

        TextView url;

        CheckedTextView listCheckBox;

        public Widgets(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.deployment_title);
            url = (TextView) convertView.findViewById(R.id.deployment_description);

            listCheckBox = (CheckedTextView) convertView
                    .findViewById(R.id.deployment_selected);
        }

    }

}
