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

import com.ushahidi.android.data.entity.TagEntity;

import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface TagDataSource {

    /**
     * Add a {@link com.ushahidi.android.core.entity.Tag}.
     *
     * @param tagEntity   The Tag to be saved.
     * @param tagCallback A {@link TagEntityAddCallback} used for notifying clients about the status
     *                    of the operation.
     * @author Ushahidi Team <team@ushahidi.com>
     */
    void addTag(TagEntity tagEntity, TagEntityAddCallback tagCallback);

    /**
     * Get a list of {@link com.ushahidi.android.core.entity.Tag}.
     *
     * @param tagListCallback A {@link TagEntityListCallback} used for notifying clients about the
     *                        status of the operation.
     */
    void getTagEntityList(TagEntityListCallback tagListCallback);

    /**
     * Get an {@link com.ushahidi.android.core.entity.Tag} by id.
     *
     * @param tagId              The tag id used for retrieving tag data.
     * @param tagDetailsCallback A {@link TagEntityDetailsCallback} used for notifying clients about
     *                           the status of the operation.
     */
    void getTagEntityById(final long tagId,
            TagEntityDetailsCallback tagDetailsCallback);

    /**
     * Update a {@link com.ushahidi.android.core.entity.Tag}
     *
     * @param tagEntity The Tag to be deleted
     * @param callback  A {@link TagEntityUpdateCallback} for notifying clients about tag updates
     *                  status.
     */
    void updateTagEntity(TagEntity tagEntity, TagEntityUpdateCallback callback);

    /**
     * Delete a {@link com.ushahidi.android.core.entity.Tag}
     *
     * @param tagEntity The tag to be deleted.
     * @param callback  A {@link TagEntityDeletedCallback} used for notifying clients about the
     *                  delete status.
     */
    void deleteTagEntity(final TagEntity tagEntity, TagEntityDeletedCallback callback);

    /**
     * Callback used for notifying the client when either a tag has been successfully added to the
     * database or an error occurred during the process.
     */
    interface TagEntityAddCallback {

        void onTagEntityAdded();

        void onError(Exception exception);
    }

    /**
     * Callback used for notifying the client when either a tag list has been loaded successfully or
     * an error occurred during the process.
     */
    interface TagEntityListCallback {

        void onTagEntityListLoaded(List<TagEntity> tagList);

        void onError(Exception exception);
    }

    /**
     * Callback used for notifying the client when either a tag has been loaded or an error occurred
     * during the process.
     */
    interface TagEntityDetailsCallback {

        void onTagEntityLoaded(TagEntity tag);

        void onError(Exception exception);
    }

    /**
     * Callback used for notifying the client when either a tag has been updated or failed to be
     * updated.
     */
    interface TagEntityUpdateCallback {

        void onTagEntityUpdated();

        void onError(Exception exception);
    }

    /**
     * Callback used for notifying the client when either a tag has been deleted or failed to be
     * deleted.
     */
    interface TagEntityDeletedCallback {

        void onTagEntityDeleted();

        void onError(Exception exception);
    }

}
