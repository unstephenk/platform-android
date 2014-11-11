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

package com.ushahidi.android.core.respository;

import com.ushahidi.android.core.entity.Media;
import com.ushahidi.android.core.exception.ErrorWrap;

import java.util.List;

/**
 * Media repository
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface IMediaRepository {

    /**
     * Add a {@link com.ushahidi.android.core.entity.Media}.
     *
     * @param media         The Media to be saved.
     * @param mediaCallback A {@link MediaAddCallback} used for notifying clients about the status
     *                      of the operation.
     * @author Ushahidi Team <team@ushahidi.com>
     */
    void addMedia(Media media, MediaAddCallback mediaCallback);

    /**
     * Get a list of {@link com.ushahidi.android.core.entity.Media}.
     *
     * @param mediaListCallback A {@link MediaListCallback} used for notifying clients about the
     *                          status of the operation.
     */
    void getMediaList(MediaListCallback mediaListCallback);

    /**
     * Get an {@link com.ushahidi.android.core.entity.Deployment} by id.
     *
     * @param mediaId              The media id used for retrieving media data.
     * @param mediaDetailsCallback A {@link MediaDetailsCallback} used for notifying clients about
     *                             the status of the operation.
     */
    void getMediaById(final long mediaId,
            MediaDetailsCallback mediaDetailsCallback);

    /**
     * Update a {@link com.ushahidi.android.core.entity.Media}
     *
     * @param media    The Media to be deleted
     * @param callback A {@link MediaUpdateCallback} for notifying clients about media updates
     *                 status.
     */
    void updateMedia(Media media, MediaUpdateCallback callback);

    /**
     * Delete a {@link com.ushahidi.android.core.entity.Deployment}
     *
     * @param media    The media to be deleted.
     * @param callback A {@link MediaDeletedCallback} used for notifying clients about the delete
     *                 status.
     */
    void deleteMedia(final Media media, MediaDeletedCallback callback);

    /**
     * Callback used for notifying the client when either a media has been successfully added to the
     * database or an error occurred during the process.
     */
    interface MediaAddCallback {

        void onMediaAdded();

        void onError(ErrorWrap error);
    }

    /**
     * Callback used for notifying the client when either a media list has been loaded successfully
     * or an error occurred during the process.
     */
    interface MediaListCallback {

        void onMediaListLoaded(List<Media> mediaList);

        void onError(ErrorWrap errorWrap);
    }

    /**
     * Callback used for notifying the client when either a media has been loaded or an error
     * occurred during the process.
     */
    interface MediaDetailsCallback {

        void onMediaLoaded(Media media);

        void onError(ErrorWrap errorWrap);
    }

    /**
     * Callback used for notifying the client when either a media has been updated or failed to be
     * updated.
     */
    interface MediaUpdateCallback {

        void onMediaUpdated();

        void onError(ErrorWrap error);
    }

    /**
     * Callback used for notifying the client when either a media has been deleted or failed to be
     * deleted.
     */
    interface MediaDeletedCallback {

        void onMediaDeleted();

        void onError(ErrorWrap error);
    }
}
