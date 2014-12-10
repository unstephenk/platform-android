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

import com.google.gson.annotations.SerializedName;

import com.ushahidi.android.core.Entity;

import java.util.Date;

/**
 * Tag entity
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagEntity extends Entity {

    @SerializedName("id")
    private Long _id;

    @SerializedName("parent")
    private Long mParentId;

    @SerializedName("tag")
    private String mTag;

    @SerializedName("color")
    private String mColor;

    @SerializedName("type")
    private Type mType;

    @SerializedName("icon")
    private String mIcon;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("priority")
    private int mPriority;

    @SerializedName("created")
    private Date mCreated;

    public TagEntity() {
        // Do nothing
    }

    @Override
    public Long getId() {
        return _id;
    }

    @Override
    public void setId(Long id) {
        this._id = id;
    }

    public Long getParentId() {
        return mParentId;
    }

    public void setParentId(Long parentId) {
        mParentId = parentId;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getCreated() {
        return mCreated;
    }

    public void setCreated(Date created) {
        mCreated = created;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + _id +
                ", ParentId=" + mParentId +
                ", Tag='" + mTag + '\'' +
                ", Color='" + mColor + '\'' +
                ", Type='" + mType + '\'' +
                ", Icon='" + mIcon + '\'' +
                ", Description='" + mDescription + '\'' +
                ", Priority=" + mPriority +
                ", Created=" + mCreated +
                '}';
    }

    public enum Type {
        CATEGORY("category"),
        STATUS("status");

        public final String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
