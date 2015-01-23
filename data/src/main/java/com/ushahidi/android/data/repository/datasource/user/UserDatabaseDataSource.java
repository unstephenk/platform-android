/*
 * Copyright (c) 2015 Ushahidi.
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

import com.ushahidi.android.data.database.UserDatabaseHelper;
import com.ushahidi.android.data.entity.UserEntity;

import java.util.List;

/**
 * {@link UserDataSource} implementation based on SQLite database
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserDatabaseDataSource implements UserDataSource {

    private final UserDatabaseHelper mUserDatabaseHelper;

    public UserDatabaseDataSource(UserDatabaseHelper userDatabaseHelper) {
        mUserDatabaseHelper = userDatabaseHelper;
    }

    @Override
    public void addUserEntity(UserEntity userEntity,
            final UserEntityAddCallback userEntityCallback) {
        mUserDatabaseHelper.put(userEntity, new UserDatabaseHelper.IUserEntityPutCallback() {
            @Override
            public void onUserEntityPut() {
                userEntityCallback.onUserEntityAdded();
            }

            @Override
            public void onError(Exception exception) {
                userEntityCallback.onError(exception);
            }
        });
    }

    @Override
    public void getUserEntityList(final UserEntityListCallback userEntityListCallback) {
        mUserDatabaseHelper.getUserEntities(new UserDatabaseHelper.IUserEntitiesCallback() {
            @Override
            public void onUserEntitiesLoaded(List<UserEntity> userEntities) {
                userEntityListCallback.onUserEntityListLoaded(userEntities);
            }

            @Override
            public void onError(Exception exception) {
                userEntityListCallback.onError(exception);
            }
        });
    }

    @Override
    public void getUserEntityListByDeploymentId(Long deploymentId,
            final UserEntityListCallback userEntityListCallback) {
        mUserDatabaseHelper.getUserEntitiesByDeploymentId(deploymentId, new UserDatabaseHelper.IUserEntitiesCallback() {
            @Override
            public void onUserEntitiesLoaded(List<UserEntity> userEntities) {
                userEntityListCallback.onUserEntityListLoaded(userEntities);
            }

            @Override
            public void onError(Exception exception) {
                userEntityListCallback.onError(exception);
            }
        });
    }

    @Override
    public void getUserEntityById(long userEntityId,
            final UserEntityDetailsCallback userEntityDetailsCallback) {
        mUserDatabaseHelper.get(userEntityId, new UserDatabaseHelper.IUserEntityCallback() {
            @Override
            public void onUserEntityLoaded(UserEntity userEntity) {
                userEntityDetailsCallback.onUserEntityLoaded(userEntity);
            }

            @Override
            public void onError(Exception exception) {
                userEntityDetailsCallback.onError(exception);
            }
        });
    }

    @Override
    public void updateUserEntity(UserEntity userEntity,
            final UserEntityUpdateCallback userEntityUpdateCallback) {
        mUserDatabaseHelper.put(userEntity, new UserDatabaseHelper.IUserEntityPutCallback() {
            @Override
            public void onUserEntityPut() {
                userEntityUpdateCallback.onUserEntityUpdated();
            }

            @Override
            public void onError(Exception exception) {
                userEntityUpdateCallback.onError(exception);
            }
        });
    }

    @Override
    public void deleteUserEntity(UserEntity userEntity,
            final UserEntityDeletedCallback userEntityDeletedCallback) {
        mUserDatabaseHelper.delete(userEntity, new UserDatabaseHelper.IUserEntityDeletedCallback() {
            @Override
            public void onUserEntityDeleted() {
                userEntityDeletedCallback.onUserEntityDeleted();
            }

            @Override
            public void onError(Exception exception) {
                userEntityDeletedCallback.onError(exception);
            }
        });
    }
}
