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

package com.ushahidi.android.data.api;

import com.google.common.base.Preconditions;

import com.ushahidi.android.data.api.model.Posts;
import com.ushahidi.android.data.api.service.PostService;
import com.ushahidi.android.data.exception.NetworkConnectionException;

import android.content.Context;
import android.util.Log;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostApi implements IPostApi {

    private final Context mContext;

    private final PostService mPostService;

    public PostApi(Context context, PostService postService) {
        mContext = Preconditions.checkNotNull(context, "Context cannot be null,");
        mPostService = Preconditions.checkNotNull(postService, "PostService cannot be null.");
    }

    @Override
    public void getPostEntityList(final PostEntityListCallback postEntityListCallback) {
        Preconditions.checkNotNull(postEntityListCallback);
        if (isDeviceConnectedToInternet(mContext)) {
            mPostService.posts(new Callback<Posts>() {

                @Override
                public void success(Posts posts, Response response) {
                    postEntityListCallback.onPostEntityListLoaded(posts.getPosts());
                }

                @Override
                public void failure(RetrofitError error) {
                    postEntityListCallback.onError(error);
                }
            });
        } else {
            postEntityListCallback.onError(new NetworkConnectionException());
        }
    }

    // Workaround for testing static methods
    public boolean isDeviceConnectedToInternet(Context context) {
        return ApiUtil.isDeviceConnectedToInternet(context);
    }
}
