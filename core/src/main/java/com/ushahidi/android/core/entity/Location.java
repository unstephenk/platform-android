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
 * Location entity
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class Location extends Entity {

    private Long _id;

    private double mLongitude;

    private double mLatitude;

    public Location() {
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

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "_id=" + _id +
                ", Longitude=" + mLongitude +
                ", Latitude=" + mLatitude +
                '}';
    }
}
