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

import com.ushahidi.android.data.api.UserApi;
import com.ushahidi.android.data.entity.UserEntity;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserApiDataSource implements UserDataSource {

    private final UserApi mUserApi;

    public UserApiDataSource(UserApi userApi) {
        mUserApi = userApi;
    }

    @Override
    public void addUserEntity(UserEntity userEntity,
            UserEntityAddCallback userEntityCallback) {

    }

    @Override
    public void getUserEntityList(UserEntityListCallback userEntityListCallback) {

    }

    @Override
    public void getUserEntityById(long userEntityId,
            UserEntityDetailsCallback userEntityDetailsCallback) {

    }

    @Override
    public void updateUserEntity(UserEntity userEntity,
            UserEntityUpdateCallback callback) {

    }

    @Override
    public void deleteUserEntity(UserEntity userEntity,
            UserEntityDeletedCallback callback) {

    }
}
