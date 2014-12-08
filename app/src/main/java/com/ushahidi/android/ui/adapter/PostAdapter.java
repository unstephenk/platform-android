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
import com.ushahidi.android.model.PostModel;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages list of posts
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostAdapter extends BaseRecyclerViewAdapter<PostModel> implements
        Filterable {

    RecyclerviewViewHolder mRecyclerviewViewHolder = new RecyclerviewViewHolder() {
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ((Widgets) viewHolder).title.setText(getItem(position).getTitle());
            ((Widgets) viewHolder).content.setText(getItem(position).getContent());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new Widgets(LayoutInflater.from(
                    viewGroup.getContext())
                    .inflate(R.layout.list_post_item, viewGroup, false));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    };

    private Filter mFilter = null;

    public PostAdapter() {
        this.setRecyclerviewViewHolder(mRecyclerviewViewHolder);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new PostFilter();
        }
        return mFilter;
    }

    private class PostFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            constraint = constraint.toString().toLowerCase();
            results.values = mItems;
            results.count = mItems.size();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<PostModel> filteredItems = new ArrayList<>();
                //TODO: query the mItems from the database and use that for comparison
                for (PostModel post : mItems) {
                    if (post.getTitle().toLowerCase().contains(constraint.toString())) {
                        filteredItems.add(post);
                    }
                }
                results.count = filteredItems.size();
                results.values = filteredItems;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            List<PostModel> postModels
                    = (ArrayList<PostModel>) filterResults.values;
            setItems(postModels);
        }
    }

    public class Widgets extends RecyclerView.ViewHolder {

        TextView title;

        TextView content;

        ImageView post;

        public Widgets(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.post_title);
            content = (TextView) convertView.findViewById(R.id.post_content);
            post = (ImageView) convertView.findViewById(R.id.post_image);
        }
    }
}
