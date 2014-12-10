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
 * Interface for Post Database
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
package com.ushahidi.android.data.database;

import com.ushahidi.android.data.entity.PostEntity;

import java.util.List;

/**
 * An interface representing post database
 */
public interface IPostDatabaseHelper {

    /**
     * Puts a post entity into the database.
     *
     * @param postEntity Post to insert into the database.
     * @param callback         The {@link com.ushahidi.android.data.database.IPostDatabaseHelper.IPostEntityPutCallback}
     *                         use to notify client.
     */
    public void put(PostEntity postEntity,
            final IPostEntityPutCallback callback);

    /**
     * Gets a post from the database using a {@link IPostEntityCallback}.
     *
     * @param id       The post id to retrieve data.
     * @param callback The {@link IPostEntityCallback} to notify the client.
     */
    public void get(final long id, final IPostEntityCallback callback);

    /**
     * Gets a list of post entities.
     *
     * @param callback The {@link IPostEntitiesCallback} to notify the client.
     */
    public void getPostEntities(final IPostEntitiesCallback callback);

    /**
     * Puts list of post entity into the database
     *
     * @param postEntities The list of {@link com.ushahidi.android.data.entity.PostEntity}
     *                           to be added to the database.
     * @param callback           The {@link com.ushahidi.android.data.database.IPostDatabaseHelper.IPostEntityPutCallback}
     *                           use to notify client.
     */
    public void put(final List<PostEntity> postEntities,
            final IPostEntityPutCallback callback);

    /**
     * Deletes all post entities
     *
     * @param callback The {@link IPostEntityDeletedCallback} use to notify client.
     */
    public void deleteAll(final IPostEntityDeletedCallback callback);

    /**
     * Deletes a post entity
     *
     * @param postEntity The {@link com.ushahidi.android.data.entity.PostEntity} to be
     *                         deleted
     * @param callback         The {@link IPostEntityDeletedCallback} use to notify client.
     */
    public void delete(final PostEntity postEntity,
            final IPostEntityDeletedCallback callback);

    /**
     * Callback use to notify client when a {@link com.ushahidi.android.data.entity.PostEntity} has been loaded from the
     * database.
     */
    public interface IPostEntityCallback {

        void onPostEntityLoaded(PostEntity postEntity);

        void onError(Exception exception);
    }

    /**
     * Callback use to notify the client when a list of {@link com.ushahidi.android.data.entity.PostEntity} have been loaded
     * from the database.
     */
    public interface IPostEntitiesCallback {

        void onPostEntitiesLoaded(List<PostEntity> postEntities);

        void onError(Exception exception);
    }

    /**
     * Callback use to notify client when {@link com.ushahidi.android.data.entity.PostEntity} has been added to the database.
     */
    public interface IPostEntityPutCallback {

        void onPostEntityPut();

        void onError(Exception exception);
    }

    /**
     * Callback use to notify client when {@link com.ushahidi.android.data.entity.PostEntity} has been deleted from the
     * database.
     */
    public interface IPostEntityDeletedCallback {

        void onPostEntityDeleted();

        void onError(Exception exception);
    }
}
