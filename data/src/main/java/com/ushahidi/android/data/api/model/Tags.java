/*
 * Copyright (c) 2015 Ushahidi.
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

package com.ushahidi.android.data.api.model;

import com.google.gson.annotations.SerializedName;

import com.ushahidi.android.data.entity.TagEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class Tags extends Response implements Serializable {

    private static final long serialVersionUID = 4889644823525958176L;

    @SerializedName("results")
    private List<TagEntity> tags;

    public List<TagEntity> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "Tags{" +
                "response=" + super.toString() +
                "tags=" + tags +
                '}';
    }
}
