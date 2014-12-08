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

import com.ushahidi.android.core.Entity;
import com.ushahidi.android.core.entity.User;

import java.util.Date;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MediaEntity extends Entity {

    private Long _id;

    private UserEntity mUser;

    private String mCaption;

    private String mMime;

    private String mOriginalFileUrl;

    private Date mCreated;

    private Date mUpdated;

    public MediaEntity() {
        // Do nothing
    }

    @Override
    public Long getId() {
        return this._id;
    }

    @Override
    public void setId(Long id) {
        this._id = id;
    }

    public UserEntity getUser() {
        return mUser;
    }

    public void setUser(UserEntity user) {
        mUser = user;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getMime() {
        return mMime;
    }

    public void setMime(String mime) {
        mMime = mime;
    }

    public String getOriginalFileUrl() {
        return mOriginalFileUrl;
    }

    public void setOriginalFileUrl(String originalFileUrl) {
        mOriginalFileUrl = originalFileUrl;
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

    @Override
    public String toString() {
        return "Media{" +
                "id=" + _id +
                ", User=" + mUser.toString() +
                ", Caption='" + mCaption + '\'' +
                ", Mime='" + mMime + '\'' +
                ", OriginalFileUrl='" + mOriginalFileUrl + '\'' +
                ", Created=" + mCreated +
                ", Updated=" + mUpdated +
                '}';
    }
}
