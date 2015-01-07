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

package com.ushahidi.android.data.repository.datasource.user;

import com.ushahidi.android.data.api.auth.AccessToken;
import com.ushahidi.android.data.entity.UserAccountEntity;
import com.ushahidi.android.data.entity.UserEntity;

import java.util.List;

/**
 * Data source interface for where to pull or push user data from/to respectively.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface UserDataSource {

    /**
     * Add a {@link com.ushahidi.android.core.entity.User}.
     *
     * @param userEntity         The User to be saved.
     * @param userEntityCallback A {@link com.ushahidi.android.data.repository.datasource.user.UserDataSource.UserEntityAddCallback}
     *                           used for notifying clients about the status of the operation.
     * @author Ushahidi Team <team@ushahidi.com>
     */
    void addUserEntity(UserEntity userEntity, UserEntityAddCallback userEntityCallback);

    /**
     * Get a list of {@link com.ushahidi.android.core.entity.User}.
     *
     * @param userEntityListCallback A {@link UserEntityListCallback} used for notifying clients
     *                               about the status of the operation.
     */
    void getUserEntityList(UserEntityListCallback userEntityListCallback);

    /**
     * Get an {@link com.ushahidi.android.data.entity.UserEntity} by id.
     *
     * @param userEntityId              The user id used for retrieving user data.
     * @param userEntityDetailsCallback A {@link UserEntityDetailsCallback} used for notifying
     *                                  clients about the status of the operation.
     */
    void getUserEntityById(final long userEntityId,
            UserEntityDetailsCallback userEntityDetailsCallback);

    /**
     * Update a {@link com.ushahidi.android.data.entity.UserEntity}
     *
     * @param userEntity The User to be deleted
     * @param callback   A {@link UserEntityUpdateCallback} for notifying clients about user updates
     *                   status.
     */
    void updateUserEntity(UserEntity userEntity, UserEntityUpdateCallback callback);

    /**
     * Delete a {@link com.ushahidi.android.data.entity.UserEntity}
     *
     * @param userEntity The user to be deleted.
     * @param callback   A {@link UserEntityDeletedCallback} used for notifying clients about the
     *                   delete status.
     */
    void deleteUserEntity(final UserEntity userEntity, UserEntityDeletedCallback callback);


    /**
     * Callback used for notifying the client when either a user has been successfully added to the
     * database or an error occurred during the process.
     */
    interface UserEntityAddCallback {

        void onUserEntityAdded();

        void onError(Exception e);
    }

    /**
     * Callback used for notifying the client when either a user list has been loaded successfully
     * or an error occurred during the process.
     */
    interface UserEntityListCallback {

        void onUserEntityListLoaded(List<UserEntity> userList);

        void onError(Exception e);
    }

    /**
     * Callback used for notifying the client when either a user has been loaded or an error
     * occurred during the process.
     */
    interface UserEntityDetailsCallback {

        void onUserEntityLoaded(UserEntity user);

        void onError(Exception e);
    }

    /**
     * Callback used for notifying the client when either a user has been updated or failed to be
     * updated.
     */
    interface UserEntityUpdateCallback {

        void onUserEntityUpdated();

        void onError(Exception e);
    }

    /**
     * Callback used for notifying the client when either a user has been deleted or failed to be
     * deleted.
     */
    interface UserEntityDeletedCallback {

        void onUserEntityDeleted();

        void onError(Exception e);
    }
}
