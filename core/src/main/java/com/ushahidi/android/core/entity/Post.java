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

package com.ushahidi.android.core.entity;

import com.ushahidi.android.core.Entity;

import java.util.Date;
import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class Post extends Entity {

    private Long _id;

    private long mParent;

    private User mUser;

    private String mType;

    private String mTitle;

    private String mSlug;

    private String mContent;

    private String mAuthorEmail;

    private String mAuthorRealname;

    private String mStatus;

    private Date mCreated;

    private Date mUpdated;

    private Value mValues;

    private List<Tag> mTags;

    @Override
    public Long getId() {
        return this._id;
    }

    @Override
    public void setId(Long id) {
        _id = id;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getParent() {
        return mParent;
    }

    public void setParent(long parent) {
        mParent = parent;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
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

    public Value getValues() {
        return mValues;
    }

    public void setValues(Value values) {
        mValues = values;
    }

    public List<Tag> getTags() {
        return mTags;
    }

    public void setTags(List<Tag> tags) {
        mTags = tags;
    }
}
