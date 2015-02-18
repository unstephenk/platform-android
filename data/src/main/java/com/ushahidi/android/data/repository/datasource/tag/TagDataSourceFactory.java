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

package com.ushahidi.android.data.repository.datasource.tag;

import com.google.common.base.Preconditions;

import com.ushahidi.android.data.api.TagApi;
import com.ushahidi.android.data.api.service.TagService;
import com.ushahidi.android.data.database.TagDatabaseHelper;

import android.content.Context;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagDataSourceFactory {

    private final Context mContext;

    private TagService mTagService;

    private TagDatabaseHelper mTagDatabaseHelper;

    public TagDataSourceFactory(Context context, TagDatabaseHelper tagDatabaseHelper) {
        mContext = context;
        mTagDatabaseHelper = tagDatabaseHelper;
    }

    public void setTagService(TagService tagService) {
        mTagService = tagService;
    }

    public TagDataSource createTagApiSource() {
        Preconditions.checkNotNull(mTagService, "TagService cannot be null, call setTagService(...)");
        TagApi tagApi = new TagApi(mContext, mTagService);
        return new TagApiDataSource(tagApi);
    }

    public TagDataSource createTagDatabaseSource() {
        return new TagDatabaseDataSource(mTagDatabaseHelper);
    }
}
