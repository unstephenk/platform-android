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

import com.google.common.base.Preconditions;

import com.ushahidi.android.data.api.UserApi;
import com.ushahidi.android.data.api.service.UserService;
import com.ushahidi.android.data.database.UserDatabaseHelper;

import android.content.Context;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserDataSourceFactory {

    private final Context mContext;

    private UserService mUserService;

    private UserDatabaseHelper mUserDatabaseHelper;

    public UserDataSourceFactory(Context context, UserDatabaseHelper userDatabaseHelper) {
        mContext = context;
        mUserDatabaseHelper = userDatabaseHelper;
    }

    public void setUserService(UserService userService) {
        mUserService = userService;
    }

    public UserDataSource createUserApiDataSource() {
        Preconditions.checkNotNull(mUserService, "mUserService cannot be null, call setUserService(...)");
        UserApi userApi = new UserApi(mContext, mUserService);
        return new UserApiDataSource(userApi);
    }

    public UserDataSource createUserDatabaseDataSource() {
        return new UserDatabaseDataSource(mUserDatabaseHelper);
    }
}
