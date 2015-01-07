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

import com.google.common.base.Preconditions;

import com.ushahidi.android.data.api.auth.AccessToken;
import com.ushahidi.android.data.api.auth.Payload;
import com.ushahidi.android.data.api.service.UserService;
import com.ushahidi.android.data.exception.NetworkConnectionException;

import android.content.Context;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Access users api
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserApi implements IUserApi {

    private final Context mContext;

    private final UserService mUserService;

    public UserApi(Context context, UserService userService) {
        mContext = Preconditions.checkNotNull(context, "Context cannot be null.");
        mUserService = Preconditions.checkNotNull(userService, "UserService cannot be null.");
    }

    @Override
    public void loginUserAccount(Payload payload,
            final UserAccountLoggedInCallback userAccountLoggedInCallback) {
        Preconditions.checkNotNull(payload);
        Preconditions.checkNotNull(userAccountLoggedInCallback);

        if (isDeviceConnectedToInternet(mContext)) {

            mUserService.getAccessToken(payload, new Callback<AccessToken>() {
                @Override
                public void success(AccessToken accessToken, Response response) {
                    userAccountLoggedInCallback.onUserAccountLoggedIn(accessToken);
                }

                @Override
                public void failure(RetrofitError error) {
                    userAccountLoggedInCallback.onError(error);
                }
            });

        } else {
            userAccountLoggedInCallback.onError(new NetworkConnectionException());
        }
    }

    // Workaround for testing static methods
    public boolean isDeviceConnectedToInternet(Context context) {
        return ApiUtil.isDeviceConnectedToInternet(context);
    }
}
