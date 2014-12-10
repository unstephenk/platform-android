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

package com.ushahidi.android.data.repository.datasource;

import com.ushahidi.android.data.entity.PostEntity;

import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface PostDataSource {

    /**
     * Add/Update a {@link com.ushahidi.android.data.entity.PostEntity}.
     *
     * @param postEntity   The post entity to be saved.
     * @param postEntityCallback A {@link PostEntityAddCallback} used for notifying clients about the status of
     *                     the operation.
     * @author Ushahidi Team <team@ushahidi.com>
     */
    void putPostEntity(PostEntity postEntity, PostEntityAddCallback postEntityCallback);

    /**
     * Get a list of {@link com.ushahidi.android.data.entity.PostEntity}.
     *
     * @param postListCallback A {@link PostEntityListCallback} used for notifying clients about the
     *                         status of the operation.
     */
    void getPostEntityList(PostEntityListCallback postListCallback);

    /**
     * Get an {@link com.ushahidi.android.data.entity.PostEntity} by id.
     *
     * @param postEntityId              The post entity id used for retrieving post data.
     * @param postDetailsCallback A {@link PostEntityDetailsCallback} used for notifying clients about the
     *                            status of the operation.
     */
    void getPostEntityById(final long postEntityId,
            PostEntityDetailsCallback postDetailsCallback);

    /**
     * Delete a {@link com.ushahidi.android.data.entity.PostEntity}
     *
     * @param postEntity The post to be deleted.
     * @param callback   A {@link PostEntityDeletedCallback} used for notifying clients about the delete
     *                   status.
     */
    void deletePostEntity(final PostEntity postEntity, PostEntityDeletedCallback callback);

    /**
     * Callback used for notifying the client when either a post has been successfully added to the
     * database or an error occurred during the process.
     */
    interface PostEntityAddCallback {

        void onPostEntityAdded();

        void onError(Exception e);
    }

    /**
     * Callback used for notifying the client when either a post list has been loaded successfully
     * or an error occurred during the process.
     */
    interface PostEntityListCallback {

        void onPostEntityListLoaded(List<PostEntity> postEntityList);

        void onError(Exception e);
    }

    /**
     * Callback used for notifying the client when either a post has been loaded or an error
     * occurred during the process.
     */
    interface PostEntityDetailsCallback {

        void onPostEntityLoaded(PostEntity postEntity);

        void onError(Exception e);
    }

    /**
     * Callback used for notifying the client when either a post has been deleted or failed to be
     * deleted.
     */
    interface PostEntityDeletedCallback {

        void onPostEntityDeleted();

        void onError(Exception e);
    }
}
