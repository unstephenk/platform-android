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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ushahidi.android.R;
import com.ushahidi.android.model.PostModel;
import com.ushahidi.android.model.TagModel;
import com.ushahidi.android.ui.widget.CapitalizedTextView;
import com.ushahidi.android.util.Util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Manages list of posts
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostAdapter extends BaseRecyclerViewAdapter<PostModel> {

    RecyclerviewViewHolder mRecyclerviewViewHolder = new RecyclerviewViewHolder() {
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            ((Widgets) viewHolder).title.setText(getItem(position).getTitle());
            ((Widgets) viewHolder).content.setText(getItem(position).getContent());
            //TODO: Remove this. Was for demo
            ((Widgets) viewHolder).renderImage();

            ((Widgets) viewHolder).date.setText(getRelativeTimeDisplay(
                getItem(position).getCreated()));
            ((Widgets) viewHolder).status.setText(getItem(position).getStatus());
            //TODO: change hardcoded status type to an enum
            if(getItem(position).getStatus().equalsIgnoreCase("published")) {
                ((Widgets) viewHolder).status.setTextColor(((Widgets) viewHolder).context.getResources().getColor(R.color.published));
            } else {
                ((Widgets) viewHolder).status.setBackgroundColor(((Widgets) viewHolder).context.getResources().getColor(R.color.draft));
            }
            final List<TagModel> tags = getItem(position).getTags();
            if(!Util.isCollectionEmpty(tags)) {
                ((Widgets) viewHolder).renderTagBadge(tags);
            } else {
                //Don't show post that don't have tags. Hide the horizontal scroll view otherwise
                // It shows tags from previous posts.
                ((Widgets) viewHolder).tagContainer.setVisibility(View.GONE);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new Widgets(viewGroup.getContext(), LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_post_item, viewGroup, false));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    };

    public PostAdapter() {
        this.setRecyclerviewViewHolder(mRecyclerviewViewHolder);
    }

    public void sortByDate() {
        Collections.sort(mItems, new Comparator<PostModel>() {
            @Override
            public int compare(PostModel one, PostModel other) {
                return one.getCreated().compareTo(other.getCreated());
            }
        });
        notifyDataSetChanged();
    }

    public void sortByTitle() {

        Collections.sort(mItems, new Comparator<PostModel>() {
            @Override
            public int compare(PostModel one, PostModel other) {
                return one.getTitle().compareTo(other.getTitle());
            }
        });

        notifyDataSetChanged();
    }

    /**
     * Date into a relative time display
     */
    private String getRelativeTimeDisplay(Date pastTime) {
        long timeNow = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(pastTime.getTime(), timeNow, DateUtils.MINUTE_IN_MILLIS).toString();
    }

    public class Widgets extends RecyclerView.ViewHolder {

        TextView title;

        TextView content;

        TextView date;

        CapitalizedTextView status;

        ImageView postImage;

        LinearLayout tag;

        ViewGroup tagContainer;

        Context context;

        int tagColorSize;

        public Widgets(Context ctxt, View convertView) {
            super(convertView);
            this.context = ctxt;
            tagColorSize = this.context.getResources().getDimensionPixelSize(R.dimen.tag_badge_color_size);
            title = (TextView) convertView.findViewById(R.id.post_title);
            content = (TextView) convertView.findViewById(R.id.post_content);
            postImage = (ImageView) convertView.findViewById(R.id.post_image);
            date = (TextView) convertView.findViewById(R.id.post_date);
            status = (CapitalizedTextView) convertView.findViewById(R.id.post_status);
            tag = (LinearLayout) convertView.findViewById(R.id.post_tags);
            tagContainer = (ViewGroup) convertView.findViewById(R.id.post_tags_container);
        }

        public void renderTagBadge(List<TagModel> tags) {

            tagContainer.setVisibility(View.VISIBLE);
            //Remove all child views from the tags container otherwise
            //The previous items get appended when the recyclerview refreshes
            tag.removeAllViews();
            for(final TagModel tagModel: tags) {
                TextView tagBadge = (TextView) LayoutInflater.from(context).inflate(R.layout.include_tag_badge, tag, false);
                tagBadge.setText(tagModel.getTag());

                if ((tagModel.getColor() != null) && Util.validateHexColor(tagModel.getColor())) {
                    ShapeDrawable colorDrawable = new ShapeDrawable(new OvalShape());
                    colorDrawable.setIntrinsicWidth(tagColorSize);
                    colorDrawable.setIntrinsicHeight(tagColorSize);
                    colorDrawable.getPaint().setStyle(Paint.Style.FILL);
                    tagBadge.setCompoundDrawablesWithIntrinsicBounds(colorDrawable,
                        null, null, null);
                    colorDrawable.getPaint().setColor(Color.parseColor(tagModel.getColor()));
                }

                tag.addView(tagBadge);
            }
        }

        public void renderImage() {
            Picasso.with(context).load("https://lh3.googleusercontent.com/-CGnI13j4vzM/VNYamMbbc5I/AAAAAAAAN3Q/AXIUMgluJrs/w1479-h832-no/2015-02-06%2B10.38.08%2B1.jpg")
                .into(postImage, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        postImage.setVisibility(View.VISIBLE);
                    }
                });
        }
    }
}
