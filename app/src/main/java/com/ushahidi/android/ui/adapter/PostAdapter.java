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

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ushahidi.android.R;
import com.ushahidi.android.model.PostModel;
import com.ushahidi.android.model.TagModel;
import com.ushahidi.android.ui.animators.ViewHelper;
import com.ushahidi.android.ui.widget.CapitalizedTextView;
import com.ushahidi.android.util.Util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages list of posts
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostAdapter extends BaseRecyclerViewAdapter<PostModel> {

    private int mDuration = 300;
    private int mLastPosition = -1;

    private float mFrom = 0f;

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= getItems().size() : position < getItems().size()) && (customHeaderView != null ? position > 0 : true)) {

            ((Widgets) viewHolder).title.setText(getItem(position).getTitle());
            ((Widgets) viewHolder).content.setText(getItem(position).getContent());
            //TODO: Remove this. Was for demo
            ((Widgets) viewHolder).renderImage(position);

            ((Widgets) viewHolder).date.setText(getRelativeTimeDisplay(
                getItem(position).getCreated()));
            ((Widgets) viewHolder).status.setText(getItem(position).getStatus());
            //TODO: change hardcoded status type to an enum
            if (getItem(position).getStatus().equalsIgnoreCase("published")) {
                ((Widgets) viewHolder).status.setTextColor(((Widgets) viewHolder).context.getResources().getColor(R.color.published));
            } else {
                ((Widgets) viewHolder).status.setBackgroundColor(((Widgets) viewHolder).context.getResources().getColor(R.color.draft));
            }
            final List<TagModel> tags = getItem(position).getTags();
            if (!Util.isCollectionEmpty(tags)) {
                ((Widgets) viewHolder).renderTagBadge(tags);
            } else {
                //Don't show post that don't have tags. Hide the horizontal scroll view otherwise
                // It shows tags from previous posts.
                ((Widgets) viewHolder).tagContainer.setVisibility(View.GONE);
            }

            if (position > mLastPosition) {
                for (Animator anim : getAnimators(viewHolder.itemView)) {
                    anim.setDuration(mDuration).start();
                }
                mLastPosition = position;
            } else {
                ViewHelper.clear(viewHolder.itemView);
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new Widgets(viewGroup.getContext(), LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.list_post_item, viewGroup, false));
    }

    @Override
    public int getAdapterItemCount() {
        return getItems().size();
    }

    public void sortByDate() {
        Collections.sort(getItems(), new Comparator<PostModel>() {
            @Override
            public int compare(PostModel one, PostModel other) {
                return one.getCreated().compareTo(other.getCreated());
            }
        });
        notifyDataSetChanged();
    }

    public void sortByTitle() {

        Collections.sort(getItems(), new Comparator<PostModel>() {
            @Override
            public int compare(PostModel one, PostModel other) {
                return one.getTitle().compareTo(other.getTitle());
            }
        });

        notifyDataSetChanged();
    }

    protected Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f)};
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

        int tagIconSize;

        public Widgets(Context ctxt, View convertView) {
            super(convertView);
            this.context = ctxt;
            tagColorSize = this.context.getResources().getDimensionPixelSize(R.dimen.tag_badge_color_size);
            tagIconSize = this.context.getResources().getDimensionPixelSize(R.dimen.tag_icon_color_size);
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
            for (final TagModel tagModel : tags) {
                TextView tagBadge = (TextView) LayoutInflater.from(context).inflate(R.layout.include_tag_badge, tag, false);
                tagBadge.setText(tagModel.getTag());
                // Tag has both icon and color. Display both
                if (!TextUtils.isEmpty(tagModel.getIcon()) && Util.validateHexColor(tagModel.getColor())) {
                    StringBuilder builder = new StringBuilder("fa_");
                    builder.append(tagModel.getIcon());
                    tagBadge.setCompoundDrawablesWithIntrinsicBounds(getFontAwesomeIconAsDrawable(builder.toString(), tagModel.getColor()),
                        null, null, null);

                    //Tag has only color, display badge
                } else if (Util.validateHexColor(tagModel.getColor())) {
                    ShapeDrawable colorDrawable = new ShapeDrawable(new OvalShape());
                    colorDrawable.setIntrinsicWidth(tagColorSize);
                    colorDrawable.setIntrinsicHeight(tagColorSize);
                    colorDrawable.getPaint().setStyle(Paint.Style.FILL);
                    colorDrawable.getPaint().setColor(Color.parseColor(tagModel.getColor()));
                    tagBadge.setCompoundDrawablesWithIntrinsicBounds(colorDrawable,
                        null, null, null);

                    // Tag has only icon, display it
                } else if (!TextUtils.isEmpty(tagModel.getIcon())) {
                    StringBuilder builder = new StringBuilder("fa_");
                    builder.append(tagModel.getIcon());
                    tagBadge.setCompoundDrawablesWithIntrinsicBounds(getFontAwesomeIconAsDrawable(builder.toString(), null),
                        null, null, null);
                }

                tag.addView(tagBadge);
            }
        }

        private Drawable getFontAwesomeIconAsDrawable(String fontawesomeIcon, String color) {
            if (TextUtils.isEmpty(color)) {
                return new IconDrawable(context, Iconify.IconValue.valueOf(fontawesomeIcon)).colorRes(R.color.body_text_1).sizeDp(tagIconSize);
            }

            return new IconDrawable(context, Iconify.IconValue.valueOf(fontawesomeIcon)).color(Color.parseColor(color)).sizeDp(tagIconSize);
        }

        private void renderImage(int position) {
            // Seed dummy images
            Map<Integer, String> dummyImages = new HashMap();
            dummyImages.put(0, "https://lh3.googleusercontent.com/-CGnI13j4vzM/VNYamMbbc5I/AAAAAAAAN3Q/AXIUMgluJrs/w1479-h832-no/2015-02-06%2B10.38.08%2B1.jpg");
            dummyImages.put(2, "https://farm8.staticflickr.com/7569/15110597684_e46a843af7_b.jpg");
            dummyImages.put(4, "https://farm9.staticflickr.com/8734/16863201508_5685055f10_b.jpg");
            dummyImages.put(5, "https://farm9.staticflickr.com/8800/16862037860_4bd562894e_b.jpg");
            dummyImages.put(6, "https://farm9.staticflickr.com/8786/17054994142_af68cc1df8_b.jpg");
            dummyImages.put(8, "https://farm8.staticflickr.com/7478/16028403009_d2eaa67d47_b.jpg");
            dummyImages.put(11, "https://farm9.staticflickr.com/8805/16870618028_7399699524_b.jpg");

            if (dummyImages.containsKey(position)) {
                Picasso.with(context).load(dummyImages.get(position))
                    .into(postImage, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            postImage.setVisibility(View.VISIBLE);
                        }
                    });
            } else {
                postImage.setVisibility(View.GONE);
            }
        }
    }
}
