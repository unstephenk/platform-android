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

package com.ushahidi.android.core.repository;

import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.core.exception.ErrorWrap;

import java.util.List;

/**
 * Interface for Tag repository
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface ITagRepository {

    /**
     * Add a {@link com.ushahidi.android.core.entity.Tag}.
     *
     * @param tag         The Tag to be saved.
     * @param tagCallback A {@link TagAddCallback} used for notifying clients about the status of
     *                    the operation.
     * @author Ushahidi Team <team@ushahidi.com>
     */
    void addTag(Tag tag, TagAddCallback tagCallback);

    /**
     * Get a list of {@link com.ushahidi.android.core.entity.Tag}.
     *
     * @param tagListCallback A {@link TagListCallback} used for notifying clients about the status
     *                        of the operation.
     */
    void getTagList(TagListCallback tagListCallback);

    /**
     * Get a list of {@link com.ushahidi.android.core.entity.Tag}.
     *
     * @param tagListCallback A {@link TagListCallback} used for notifying clients about the status
     *                        of the operation.
     */
    void getTagListViaApi(TagListCallback tagListCallback);

    /**
     * Get an {@link com.ushahidi.android.core.entity.Tag} by id.
     *
     * @param tagId              The tag id used for retrieving tag data.
     * @param tagDetailsCallback A {@link TagDetailsCallback} used for notifying clients about the
     *                           status of the operation.
     */
    void getTagById(final long tagId,
            TagDetailsCallback tagDetailsCallback);

    /**
     * Update a {@link com.ushahidi.android.core.entity.Tag}
     *
     * @param tag      The Tag to be deleted
     * @param callback A {@link TagUpdateCallback} for notifying clients about tag updates status.
     */
    void updateTag(Tag tag, TagUpdateCallback callback);

    /**
     * Delete a {@link com.ushahidi.android.core.entity.Tag}
     *
     * @param tag      The tag to be deleted.
     * @param callback A {@link TagDeletedCallback} used for notifying clients about the delete
     *                 status.
     */
    void deleteTag(final Tag tag, TagDeletedCallback callback);

    /**
     * Callback used for notifying the client when either a tag has been successfully added to the
     * database or an error occurred during the process.
     */
    interface TagAddCallback {

        void onTagAdded();

        void onError(ErrorWrap error);
    }

    /**
     * Callback used for notifying the client when either a tag list has been loaded successfully or
     * an error occurred during the process.
     */
    interface TagListCallback {

        void onTagListLoaded(List<Tag> tagList);

        void onError(ErrorWrap errorWrap);
    }

    /**
     * Callback used for notifying the client when either a tag has been loaded or an error occurred
     * during the process.
     */
    interface TagDetailsCallback {

        void onTagLoaded(Tag tag);

        void onError(ErrorWrap errorWrap);
    }

    /**
     * Callback used for notifying the client when either a tag has been updated or failed to be
     * updated.
     */
    interface TagUpdateCallback {

        void onTagUpdated();

        void onError(ErrorWrap error);
    }

    /**
     * Callback used for notifying the client when either a tag has been deleted or failed to be
     * deleted.
     */
    interface TagDeletedCallback {

        void onTagDeleted();

        void onError(ErrorWrap error);
    }
}
