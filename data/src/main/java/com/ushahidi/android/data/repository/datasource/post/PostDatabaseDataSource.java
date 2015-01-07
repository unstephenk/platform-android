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

import com.ushahidi.android.data.database.PostDatabaseHelper;
import com.ushahidi.android.data.entity.PostEntity;

import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostDatabaseDataSource implements PostDataSource {

    private final PostDatabaseHelper mPostDatabaseHelper;

    public PostDatabaseDataSource(PostDatabaseHelper postDatabaseHelper) {
        mPostDatabaseHelper = postDatabaseHelper;
    }

    @Override
    public void putPostEntity(final PostEntity postEntity, final PostEntityAddCallback postCallback) {
        mPostDatabaseHelper.put(postEntity, new PostDatabaseHelper.IPostEntityPutCallback() {
            @Override
            public void onPostEntityPut() {
                postCallback.onPostEntityAdded();
            }

            @Override
            public void onError(Exception exception) {
                postCallback.onError(exception);
            }
        });
    }

    @Override
    public void getPostEntityList(final PostEntityListCallback postListCallback) {
        mPostDatabaseHelper.getPostEntities(new PostDatabaseHelper.IPostEntitiesCallback() {
            @Override
            public void onPostEntitiesLoaded(List<PostEntity> postEntities) {
                postListCallback.onPostEntityListLoaded(postEntities);
            }

            @Override
            public void onError(Exception exception) {
                postListCallback.onError(exception);
            }
        });
    }

    @Override
    public void getPostEntityById(long postId,
            final PostEntityDetailsCallback postDetailsCallback) {
        mPostDatabaseHelper.get(postId, new PostDatabaseHelper.IPostEntityCallback() {
            @Override
            public void onPostEntityLoaded(PostEntity postEntity) {
                postDetailsCallback.onPostEntityLoaded(postEntity);
            }

            @Override
            public void onError(Exception exception) {
                postDetailsCallback.onError(exception);
            }
        });
    }
    @Override
    public void deletePostEntity(PostEntity postEntity,
            final PostEntityDeletedCallback callback) {
        mPostDatabaseHelper.delete(postEntity, new PostDatabaseHelper.IPostEntityDeletedCallback() {
            @Override
            public void onPostEntityDeleted() {
                callback.onPostEntityDeleted();
            }

            @Override
            public void onError(Exception exception) {
                callback.onError(exception);
            }
        });
    }
}
