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

package com.ushahidi.android.data.repository;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.core.repository.IUserRepository;
import com.ushahidi.android.data.entity.UserEntity;
import com.ushahidi.android.data.entity.mapper.UserEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.data.repository.datasource.user.UserDataSource;
import com.ushahidi.android.data.repository.datasource.user.UserDataSourceFactory;
import com.ushahidi.android.data.validator.Validator;

import java.util.List;

/**
 * {@link com.ushahidi.android.core.repository.IUserRepository} for retrieving user data
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserDataRepository implements IUserRepository {

    private static UserDataRepository sInstance;

    private final UserEntityMapper mUserEntityMapper;

    private final UserDataSourceFactory mUserDataSourceFactory;

    private final Validator mValidator;

    protected UserDataRepository(UserDataSourceFactory userDataSourceFactory,
            UserEntityMapper entityMapper, Validator validator) {
        if (entityMapper == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        Preconditions.checkNotNull(userDataSourceFactory, "UserDataSourceFactory cannot be null.");
        Preconditions.checkNotNull(entityMapper, "UserEntity mapper cannot be null.");
        Preconditions.checkNotNull(validator, "Validator cannot be null.");
        mUserEntityMapper = entityMapper;
        mUserDataSourceFactory = userDataSourceFactory;
        mValidator = validator;
    }

    public static synchronized UserDataRepository getInstance(UserDataSourceFactory
            userDataSourceFactory, UserEntityMapper entityMapper, Validator validator) {
        if (sInstance == null) {
            sInstance = new UserDataRepository(userDataSourceFactory,
                    entityMapper, validator);
        }
        return sInstance;
    }

    /**
     * {@inheritDoc}
     *
     * @param user         The User to be saved.
     * @param userCallback A {@link com.ushahidi.android.core.repository.IUserRepository.AddCallback}
     *                     used for notifying clients.
     */
    @Override
    public void addUser(User user,
            final AddCallback userCallback) {
        // Check for required fields
        boolean isValid = true;
        if (Strings.isNullOrEmpty(user.getUsername())) {
            isValid = false;
            userCallback.onError(new RepositoryError(
                    new ValidationException("Username cannot be null or empty")));
        }

        if (!Strings.isNullOrEmpty(user.getEmail())) {
            if(!mValidator.isValid(user.getEmail())) {
                isValid = false;
                userCallback.onError(
                        new RepositoryError(
                                new ValidationException("The email address is invalid")));
            }
        }

        if (isValid) {
            final UserDataSource userDataSource = mUserDataSourceFactory
                    .createUserDatabaseDataSource();
            userDataSource.addUserEntity(mUserEntityMapper.unmap(user),
                    new UserDataSource.UserEntityAddCallback() {

                        @Override
                        public void onUserEntityAdded() {
                            userCallback.onAdded();
                        }

                        @Override
                        public void onError(Exception exception) {
                            userCallback.onError(new RepositoryError(exception));
                        }
                    });
        }
    }

    @Override
    public void getUserList(final UserListCallback userListCallback) {
        final UserDataSource userDataSource = mUserDataSourceFactory.createUserDatabaseDataSource();
        userDataSource.getUserEntityList(
                new UserDataSource.UserEntityListCallback() {

                    @Override
                    public void onUserEntityListLoaded(
                            List<UserEntity> userEntities) {
                        final List<User> users = mUserEntityMapper.map(userEntities);
                        userListCallback.onUserListLoaded(users);
                    }

                    @Override
                    public void onError(Exception exception) {
                        userListCallback.onError(new RepositoryError(exception));
                    }
                });
    }

    @Override
    public void getUserListByDeploymentId(Long deploymentId,
           final UserListCallback userListCallback) {
        final UserDataSource userDataSource = mUserDataSourceFactory.createUserDatabaseDataSource();
        userDataSource.getUserEntityListByDeploymentId(deploymentId, new UserDataSource.UserEntityListCallback() {

            @Override
            public void onUserEntityListLoaded(
                    List<UserEntity> userEntities) {
                final List<User> users = mUserEntityMapper.map(userEntities);
                userListCallback.onUserListLoaded(users);
            }

            @Override
            public void onError(Exception exception) {
                userListCallback.onError(new RepositoryError(exception));
            }
        });
    }

    @Override
    public void getUserById(long userId,
            final UserDetailsCallback userDetailsCallback) {
        final UserDataSource userDataSource = mUserDataSourceFactory.createUserDatabaseDataSource();
        userDataSource.getUserEntityById(userId,
                new UserDataSource.UserEntityDetailsCallback() {

                    @Override
                    public void onUserEntityLoaded(UserEntity userEntity) {
                        final User user = mUserEntityMapper.map(userEntity);
                        userDetailsCallback.onUserLoaded(user);
                    }

                    @Override
                    public void onError(Exception exception) {
                        userDetailsCallback.onError(new RepositoryError(exception));
                    }
                });
    }

    @Override
    public void getUserViaApi(final UserDetailsCallback userDetailsCallback) {
        final UserDataSource userDataSource = mUserDataSourceFactory.createUserApiDataSource();
        userDataSource.getUserEntityById(0, new UserDataSource.UserEntityDetailsCallback() {
            @Override
            public void onUserEntityLoaded(UserEntity userEntity) {
                final User user = mUserEntityMapper.map(userEntity);
                userDetailsCallback.onUserLoaded(user);
            }

            @Override
            public void onError(Exception exception) {
                userDetailsCallback.onError(new RepositoryError(exception));
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param user         The User to be saved.
     * @param userCallback A {@link com.ushahidi.android.core.repository.IUserRepository.UserUpdateCallback}
     *                     used for notifying clients.
     */
    @Override
    public void updateUser(User user,
            final UserUpdateCallback userCallback) {
        // Check for required fields
        boolean isValid = true;
        if (Strings.isNullOrEmpty(user.getUsername())) {
            isValid = false;
            userCallback.onError(new RepositoryError(
                    new ValidationException("Username cannot be null or empty")));
        }

        if (!mValidator.isValid(user.getEmail())) {
            isValid = false;
            userCallback.onError(
                    new RepositoryError(new ValidationException("The email address is invalid")));
        }

        if (isValid) {
            final UserDataSource userDataSource = mUserDataSourceFactory
                    .createUserDatabaseDataSource();
            userDataSource.updateUserEntity(mUserEntityMapper.unmap(user),
                    new UserDataSource.UserEntityUpdateCallback() {
                        @Override
                        public void onUserEntityUpdated() {
                            userCallback.onUserUpdated();
                        }

                        @Override
                        public void onError(Exception exception) {
                            userCallback.onError(new RepositoryError(exception));
                        }
                    });
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param user     The ID of the user to be deleted.
     * @param callback A {@link com.ushahidi.android.core.repository.IUserRepository.UserDeletedCallback}
     *                 used for notifying clients.
     */
    @Override
    public void deleteUser(final User user,
            final UserDeletedCallback callback) {
        final UserDataSource userDataSource = mUserDataSourceFactory.createUserDatabaseDataSource();
        userDataSource.deleteUserEntity(mUserEntityMapper.unmap(user),
                new UserDataSource.UserEntityDeletedCallback() {
                    @Override
                    public void onUserEntityDeleted() {
                        callback.onUserDeleted();
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(new RepositoryError(exception));
                    }

                });
    }
}
