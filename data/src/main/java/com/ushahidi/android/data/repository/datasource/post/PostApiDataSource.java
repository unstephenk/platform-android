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

import com.ushahidi.android.data.api.PostApi;
import com.ushahidi.android.data.database.IPostDatabaseHelper;
import com.ushahidi.android.data.database.PostDatabaseHelper;
import com.ushahidi.android.data.entity.PostEntity;

import java.util.List;

/**
 * Restful API data source. Retrieves data from the API and caches them into the database.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostApiDataSource implements PostDataSource {

    private final PostApi mPostApi;

    private final PostDatabaseHelper mPostDatabaseHelper;

    public PostApiDataSource(PostApi postApi, PostDatabaseHelper postDatabaseHelper) {
        mPostApi = postApi;
        mPostDatabaseHelper = postDatabaseHelper;
    }

    @Override
    public void putPostEntity(PostEntity postEntity, PostEntityAddCallback postCallback) {
        //TODO Implement POST via the API
    }

    @Override
    public void getPostEntityList(final long deploymentId, final PostEntityListCallback postEntityListCallback) {
        mPostApi.getPostEntityList(new PostApi.PostEntityListCallback() {
            @Override
            public void onPostEntityListLoaded(List<PostEntity> postEntityList) {
                postEntityListCallback.onPostEntityListLoaded(postEntityList);
                putPostEntityList(postEntityList, postEntityListCallback);
            }

            @Override
            public void onError(Exception e) {
                postEntityListCallback.onError(e);
            }
        });
    }

    @Override
    public void getPostEntityById(long postId,
                                  PostEntityDetailsCallback postEntityDetailsCallback) {
        //TODO Implement GET via the API
    }

    @Override
    public void deletePostEntity(PostEntity postEntity,
                                 PostEntityDeletedCallback callback) {
        //TODO implement DELETE via the API
    }

    @Override
    public void search(String query, SearchCallback callback) {
        // Do nothing for now
    }

    private void putPostEntityList(final List<PostEntity> postEntityList,
                                   final PostEntityListCallback postListCallback) {
        mPostDatabaseHelper.put(postEntityList, new IPostDatabaseHelper.IPostEntityPutCallback() {
            @Override
            public void onPostEntityPut() {
                postListCallback.onPostEntityListLoaded(postEntityList);
            }

            @Override
            public void onError(Exception exception) {
                postListCallback.onError(exception);
            }
        });
    }
}
