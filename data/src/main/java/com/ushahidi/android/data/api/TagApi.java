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

package com.ushahidi.android.data.api;

import com.google.common.base.Preconditions;

import com.ushahidi.android.data.api.model.Tags;
import com.ushahidi.android.data.api.service.TagService;
import com.ushahidi.android.data.exception.NetworkConnectionException;

import android.content.Context;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagApi implements ITagApi {

    private final Context mContext;

    private final TagService mTagService;

    public TagApi(Context context, TagService tagService) {
        mContext = Preconditions.checkNotNull(context, "Context cannot be null,");
        mTagService =  Preconditions.checkNotNull(tagService, "PostService cannot be null.");
    }

    @Override
    public void getTagEntityList(final TagEntityListCallback tagEntityListCallback) {
        Preconditions.checkNotNull(tagEntityListCallback);
        if (isDeviceConnectedToInternet(mContext)) {
            mTagService.tags(new Callback<Tags>() {
                @Override
                public void success(Tags tags, Response response) {
                    tagEntityListCallback.onTagEntityListLoaded(tags.getTags());
                }

                @Override
                public void failure(RetrofitError error) {
                    tagEntityListCallback.onError(error);
                }
            });
        } else {
            tagEntityListCallback.onError(new NetworkConnectionException());
        }
    }

    // Workaround for testing static methods
    public boolean isDeviceConnectedToInternet(Context context) {
        return ApiUtil.isDeviceConnectedToInternet(context);
    }
}
