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

package com.ushahidi.android.data.repository.datasource.post;

import com.google.common.base.Preconditions;

import com.ushahidi.android.data.api.PostApi;
import com.ushahidi.android.data.api.service.PostService;
import com.ushahidi.android.data.database.PostDatabaseHelper;

import android.content.Context;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostDataSourceFactory {

    private final Context mContext;

    private final PostDatabaseHelper mPostDatabaseHelper;

    private PostService mPostService;

    public PostDataSourceFactory(Context context, PostDatabaseHelper postDatabaseHelper) {
        mContext = Preconditions.checkNotNull(context, "Context cannot be null.");
        mPostDatabaseHelper = Preconditions
                .checkNotNull(postDatabaseHelper, "PostDatabaseHelper cannot be null");
    }

    public void setPostService(PostService postService){
        mPostService = postService;
    }

    public PostDataSource createPostDatabaseDataSource() {
        return new PostDatabaseDataSource(mPostDatabaseHelper);
    }

    public PostDataSource createPostApiDataSource() {
        Preconditions.checkNotNull(mPostService, "mPostService cannot be null, call setPostService(...)");
        PostApi postApi = new PostApi(mContext, mPostService);
        return new PostApiDataSource(postApi, mPostDatabaseHelper);
    }
}
