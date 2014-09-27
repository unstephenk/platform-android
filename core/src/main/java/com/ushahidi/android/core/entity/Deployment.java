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

/**
 * Deployment entity
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class Deployment extends Entity {

    private Long _id;

    private String mTitle;

    private int mStatus;

    private String mUrl;

    @Override
    public Long getId() {
        return _id;
    }

    @Override
    public void setId(Long id) {
        _id = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return "Deployment{" +
                "_id=" + _id +
                ", mTitle='" + mTitle + '\'' +
                ", mStatus='" + mStatus + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}
