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

package com.ushahidi.android.data.entity;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import nl.qbusict.cupboard.annotation.Ignore;

/**
 * Post entity
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostEntity extends Entity implements Comparable<PostEntity> {

    @SerializedName("id")
    private Long _id;

    @SerializedName("parent")
    @Ignore // Make cupboard ignore this field
    private Parent parent;

    private transient Long mParent;

    @SerializedName("user")
    private UserEntity mUser;

    @SerializedName("type")
    private String mType;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("slug")
    private String mSlug;

    @SerializedName("content")
    private String mContent;

    @SerializedName("author_email")
    private String mAuthorEmail;

    @SerializedName("author_realname")
    private String mAuthorRealname;

    @SerializedName("status")
    private String mStatus;

    @SerializedName("created")
    private Date mCreated;

    @SerializedName("updated")
    private Date mUpdated;

    @SerializedName("values")
    private PostValueEntity mValues;

    @SerializedName("tags")
    @Ignore
    private List<PostTagEntity> mPostTagEntityList;

    private transient List<TagEntity> mTags;

    public List<PostTagEntity> getPostTagEntityList() {
        return mPostTagEntityList;
    }

    public void setPostTagEntityList(List<PostTagEntity> postTagEntityList) {
        mPostTagEntityList = postTagEntityList;
    }

    @Override
    public Long getId() {
        return this._id;
    }

    @Override
    public void setId(Long id) {
        this._id = id;
    }

    public Long getParent() {
        return mParent;
    }

    public void setParent(Long parent) {
        if(this.parent !=null) {
            mParent = this.parent.getId();
        }
        mParent = parent;
    }

    public UserEntity getUser() {
        return mUser;
    }

    public void setUser(UserEntity user) {
        mUser = user;
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
    public PostValueEntity getValues() {
        return mValues;
    }

    public void setValues(PostValueEntity values) {
        mValues = values;
    }


    public List<TagEntity> getTags() {
        return mTags;
    }

    public void setTags(List<TagEntity> tags) {
        mTags = tags;
    }

    @Override
    public String toString() {
        return "PostEntity{" +
                "_id=" + _id +
                ", mParent=" + mParent +
                ", mUser=" + mUser +
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

    @Override
    public int compareTo(PostEntity another) {
        return getTitle().compareToIgnoreCase(another.getTitle());
    }

    public static class Parent {

        @SerializedName("id")
        private Long id;

        public Long getId() {
            return id;
        }
    }
}
