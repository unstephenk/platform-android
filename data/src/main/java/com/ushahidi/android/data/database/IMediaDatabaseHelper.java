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

/**
 *
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
package com.ushahidi.android.data.database;

import com.ushahidi.android.data.entity.MediaEntity;

import java.util.List;

/**
 * An interface representing media database
 */
public interface IMediaDatabaseHelper {

    /**
     * Puts a media entity into the database.
     *
     * @param mediaEntity Media to insert into the database.
     * @param callback    The {@link com.ushahidi.android.data.database.IMediaDatabaseHelper.IMediaEntityPutCallback}
     *                    use to notify client.
     */
    public void put(final MediaEntity mediaEntity,
            final IMediaEntityPutCallback callback);

    /**
     * Gets a media from the database using a {@link IMediaEntityCallback}.
     *
     * @param id       The user id to retrieve data.
     * @param callback The {@link IMediaEntityCallback} to notify the client.
     */
    public void get(final long id, final IMediaEntityCallback callback);

    /**
     * Gets a list of media entities.
     *
     * @param callback The {@link IMediaEntitiesCallback} to notify the client.
     */
    public void getMediaEntities(final IMediaEntitiesCallback callback);

    /**
     * Puts list of media entity into the database
     *
     * @param mediaEntities The list of {@link com.ushahidi.android.data.entity.MediaEntity} to be
     *                      added to the database.
     * @param callback      The {@link com.ushahidi.android.data.database.IMediaDatabaseHelper.IMediaEntityPutCallback}
     *                      use to notify client.
     */
    public void put(final List<MediaEntity> mediaEntities,
            final IMediaEntityPutCallback callback);

    /**
     * Deletes all media entities
     *
     * @param callback The {@link IMediaEntityDeletedCallback} use to notify client.
     */
    public void deleteAll(final IMediaEntityDeletedCallback callback);

    /**
     * Deletes a media entity
     *
     * @param mediaEntity The {@link com.ushahidi.android.data.entity.MediaEntity} to be deleted
     * @param callback    The {@link IMediaEntityDeletedCallback} use to notify client.
     */
    public void delete(final MediaEntity mediaEntity,
            final IMediaEntityDeletedCallback callback);

    /**
     * Callback use to notify client when a {@link com.ushahidi.android.data.entity.MediaEntity} has
     * been loaded from the database.
     */
    public interface IMediaEntityCallback {

        void onMediaEntityLoaded(MediaEntity userEntity);

        void onError(Exception exception);
    }

    /**
     * Callback use to notify the client when a list of {@link com.ushahidi.android.data.entity.MediaEntity}
     * have been loaded from the database.
     */
    public interface IMediaEntitiesCallback {

        void onMediaEntitiesLoaded(List<MediaEntity> mediaEntities);

        void onError(Exception exception);
    }

    /**
     * Callback use to notify client when {@link com.ushahidi.android.data.entity.MediaEntity} has
     * been added to the database.
     */
    public interface IMediaEntityPutCallback {

        void onMediaEntityPut();

        void onError(Exception exception);
    }

    /**
     * Callback use to notify client when {@link com.ushahidi.android.data.entity.MediaEntity} has
     * been deleted from the database.
     */
    public interface IMediaEntityDeletedCallback {

        void onMediaEntityDeleted();

        void onError(Exception exception);
    }
}
