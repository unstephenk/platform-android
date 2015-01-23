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

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.core.exception.ErrorWrap;

import java.util.List;

/**
 * Interface that represents a Repository for getting {@link User} related data.
 */
public interface IUserRepository {

    /**
     * Get a list of {@link com.ushahidi.android.core.entity.User}.
     *
     * @param userListCallback A {@link UserListCallback} used for notifying clients
     *                               about the status of the operation.
     */
    void getUserList(UserListCallback userListCallback);

    /**
     * Get an {@link com.ushahidi.android.core.entity.User} by id.
     *
     * @param deploymentId              The user id used for retrieving user data.
     * @param userListCallback A {@link UserListCallback} used for notifying clients
     *                               about the status of the operation.
     */
    void getUserListByDeploymentId(final Long deploymentId,
            UserListCallback userListCallback);

    /**
     * Get a list of {@link com.ushahidi.android.core.entity.User} by .
     *
     * @param userId              The user id used for retrieving user data.
     * @param userDetailsCallback A {@link UserDetailsCallback} used for notifying
     *                                  clients about the status of the operation.
     */
    void getUserById(final long userId,
            UserDetailsCallback userDetailsCallback);

    void getUserViaApi(UserDetailsCallback userDetailsCallback);

    /**
     * Update a {@link com.ushahidi.android.core.entity.User}
     *
     * @param user The User to be deleted
     * @param callback   A {@link UserUpdateCallback} for notifying clients about user
     *                   updates status.
     */
    void updateUser(User user, UserUpdateCallback callback);

    /**
     * Delete a {@link com.ushahidi.android.core.entity.User}
     *
     * @param user The user to be deleted.
     * @param callback   A {@link UserDeletedCallback} used for notifying clients about the
     *                   delete status.
     */
    void deleteUser(final User user, UserDeletedCallback callback);

    /**
     * @param user       The user to be added/saved.
     * @param postCallback A {@link AddCallback} used for notifying clients about the status of the
     *                     operation.
     * @author Ushahidi Team <team@ushahidi.com>
     */
    void addUser(final User user, AddCallback postCallback);

    /**
     * Callback used for notifying the client when either an entity has been successfully added to
     * the database or an error occurred during the process.
     */
    interface AddCallback {

        void onAdded();

        void onError(ErrorWrap error);
    }

    /**
     * Callback used for notifying the client when either a user list has been loaded
     * successfully or an error occurred during the process.
     */
    interface UserListCallback {

        void onUserListLoaded(List<User> userList);

        void onError(ErrorWrap errorWrap);
    }

    /**
     * Callback used for notifying the client when either a user has been loaded or an error
     * occurred during the process.
     */
    interface UserDetailsCallback {

        void onUserLoaded(User user);

        void onError(ErrorWrap errorWrap);
    }

    /**
     * Callback used for notifying the client when either a user has been updated or failed to
     * be updated.
     */
    interface UserUpdateCallback {

        void onUserUpdated();

        void onError(ErrorWrap error);
    }

    /**
     * Callback used for notifying the client when either a user has been deleted or failed to
     * be deleted.
     */
    interface UserDeletedCallback {

        void onUserDeleted();

        void onError(ErrorWrap error);
    }
}
