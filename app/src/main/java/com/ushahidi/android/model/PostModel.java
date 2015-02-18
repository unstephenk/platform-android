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

package com.ushahidi.android.model;

import java.util.Date;
import java.util.List;

/**
 * Post Model
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostModel extends Model {

    private long mParent;

    private String mType;

    private String mTitle;

    private String mSlug;

    private String mContent;

    private String mAuthorEmail;

    private String mAuthorRealname;

    private String mStatus;

    private Date mCreated;

    private Date mUpdated;

    private String mValues;

    private List<TagModel> mTags;

    public long getParent() {
        return mParent;
    }

    public void setParent(long parent) {
        mParent = parent;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSlug() {
        return mSlug;
    }

    public void setSlug(String slug) {
        mSlug = slug;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getAuthorRealname() {
        return mAuthorRealname;
    }

    public void setAuthorRealname(String authorRealname) {
        mAuthorRealname = authorRealname;
    }

    public String getAuthorEmail() {
        return mAuthorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        mAuthorEmail = authorEmail;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public Date getCreated() {
        return mCreated;
    }

    public void setCreated(Date created) {
        mCreated = created;
    }

    public Date getUpdated() {
        return mUpdated;
    }

    public void setUpdated(Date updated) {
        mUpdated = updated;
    }

    // Store the JSON string directly into the db
    public String getValues() {
        return mValues;
    }

    public void setValues(String values) {
        mValues = values;
    }

    public List<TagModel> getTags() {
        return mTags;
    }

    public void setTags(List<TagModel> tags) {
        mTags = tags;
    }

    @Override
    public String toString() {
        return "PostEntity{" +
                "_id=" + _id +
                ", mParent=" + mParent +
                ", mType='" + mType + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mSlug='" + mSlug + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mAuthorEmail='" + mAuthorEmail + '\'' +
                ", mAuthorRealname='" + mAuthorRealname + '\'' +
                ", mStatus='" + mStatus + '\'' +
                ", mCreated=" + mCreated +
                ", mUpdated=" + mUpdated +
                ", mValues='" + mValues + '\'' +
                ", mTags=" + mTags +
                '}';
    }
}
