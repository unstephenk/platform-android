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

package com.ushahidi.android.data.api;

import com.ushahidi.android.core.entity.UserAccount;
import com.ushahidi.android.data.api.auth.AccessToken;
import com.ushahidi.android.data.api.auth.Payload;
import com.ushahidi.android.data.entity.UserEntity;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface IUserApi {

    /**
     * Get a list of {@link com.ushahidi.android.data.entity.UserEntity}.
     *
     * @param payload                payload
     * @param userEntityListCallback A {@link com.ushahidi.android.data.api.IUserApi.UserAccountLoggedInCallback} used for notifying
     *                               clients about the status of the operation.
     */
    void loginUserAccount(Payload payload, UserAccountLoggedInCallback userEntityListCallback);

    /**
     * Gets user profile from the API {@link com.ushahidi.android.data.entity.UserEntity}
     *
     * @param userEntityCallback A {@link UserProfileCallback} used for notifying clients about the
     *                           status of the operation.
     */
    void getUserProfile(UserProfileCallback userEntityCallback);

    /**
     * Callback used for notifying the client when either a post list has been loaded successfully
     * or an error occurred during the process.
     */
    interface UserAccountLoggedInCallback {

        void onUserAccountLoggedIn(AccessToken accessToken);
        void onError(Exception exception);
    }

    interface UserProfileCallback {
        void onUserProfileLoaded(UserEntity userEntity);
        void onError(Exception exception);
    }
}
