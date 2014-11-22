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
 * Interface for user
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
package com.ushahidi.android.data.database;

import com.ushahidi.android.data.entity.UserEntity;

import java.util.List;

/**
 * An interface representing user database
 */
public interface IUserDatabaseHelper {

    /**
     * Puts a user entity into the database.
     *
     * @param userEntity User to insert into the database.
     * @param callback   The {@link com.ushahidi.android.data.database.IUserDatabaseHelper.IUserEntityPutCallback}
     *                   use to notify client.
     */
    public void put(final UserEntity userEntity,
            final IUserEntityPutCallback callback);

    /**
     * Gets a user from the database using a {@link IUserEntityCallback}.
     *
     * @param id       The user id to retrieve data.
     * @param callback The {@link IUserEntityCallback} to notify the client.
     */
    public void get(final long id, final IUserEntityCallback callback);

    /**
     * Gets a list of user entities.
     *
     * @param callback The {@link IUserEntitiesCallback} to notify the client.
     */
    public void getUserEntities(final IUserEntitiesCallback callback);

    /**
     * Puts list of user entity into the database
     *
     * @param userEntities The list of {@link com.ushahidi.android.data.entity.UserEntity} to be
     *                     added to the database.
     * @param callback     The {@link com.ushahidi.android.data.database.IUserDatabaseHelper.IUserEntityPutCallback}
     *                     use to notify client.
     */
    public void put(final List<UserEntity> userEntities,
            final IUserEntityPutCallback callback);

    /**
     * Deletes all user entities
     *
     * @param callback The {@link IUserEntityDeletedCallback} use to notify client.
     */
    public void deleteAll(final IUserEntityDeletedCallback callback);

    /**
     * Deletes a user entity
     *
     * @param userEntity The {@link com.ushahidi.android.data.entity.UserEntity} to be deleted
     * @param callback   The {@link IUserEntityDeletedCallback} use to notify client.
     */
    public void delete(final UserEntity userEntity,
            final IUserEntityDeletedCallback callback);

    /**
     * Callback use to notify client when a {@link com.ushahidi.android.data.entity.UserEntity} has
     * been loaded from the database.
     */
    public interface IUserEntityCallback {

        void onUserEntityLoaded(UserEntity userEntity);

        void onError(Exception exception);
    }

    /**
     * Callback use to notify the client when a list of {@link com.ushahidi.android.data.entity.UserEntity}
     * have been loaded from the database.
     */
    public interface IUserEntitiesCallback {

        void onUserEntitiesLoaded(List<UserEntity> userEntities);

        void onError(Exception exception);
    }

    /**
     * Callback use to notify client when {@link com.ushahidi.android.data.entity.UserEntity} has
     * been added to the database.
     */
    public interface IUserEntityPutCallback {

        void onUserEntityPut();

        void onError(Exception exception);
    }

    /**
     * Callback use to notify client when {@link com.ushahidi.android.data.entity.UserEntity} has
     * been deleted from the database.
     */
    public interface IUserEntityDeletedCallback {

        void onUserEntityDeleted();

        void onError(Exception exception);
    }
}
