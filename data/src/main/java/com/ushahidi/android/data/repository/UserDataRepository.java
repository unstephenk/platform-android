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
import com.ushahidi.android.core.respository.IUserRepository;
import com.ushahidi.android.data.database.IUserDatabaseHelper;
import com.ushahidi.android.data.database.UserDatabaseHelper;
import com.ushahidi.android.data.entity.UserEntity;
import com.ushahidi.android.data.entity.mapper.UserEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.data.validator.Validator;

import java.util.List;

/**
 * {@link com.ushahidi.android.core.respository.IUserRepository} for retrieving user data
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserDataRepository implements IUserRepository {

    private static UserDataRepository sInstance;

    private final UserEntityMapper mUserEntityMapper;

    private final UserDatabaseHelper mUserDatabaseHelper;

    private final Validator mValidator;

    protected UserDataRepository(UserDatabaseHelper userDatabaseHelper,
            UserEntityMapper entityMapper, Validator validator) {
        if (entityMapper == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        Preconditions.checkNotNull(userDatabaseHelper, "DatabaseHelper cannot be null");
        Preconditions.checkNotNull(entityMapper, "Entity mapper cannot be null");
        Preconditions.checkNotNull(validator, "Validator cannot be null");
        mUserEntityMapper = entityMapper;
        mUserDatabaseHelper = userDatabaseHelper;
        mValidator = validator;
    }

    public static synchronized UserDataRepository getInstance(UserDatabaseHelper
            userDatabaseHelper, UserEntityMapper entityMapper, Validator validator) {
        if (sInstance == null) {
            sInstance = new UserDataRepository(userDatabaseHelper,
                    entityMapper, validator);
        }
        return sInstance;
    }

    /**
     * {@inheritDoc}
     *
     * @param user         The User to be saved.
     * @param userCallback A {@link com.ushahidi.android.core.respository.IUserRepository.UserAddCallback}
     *                     used for notifying clients.
     */
    @Override
    public void addUser(User user,
            final UserAddCallback userCallback) {
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
                    new RepositoryError(
                            new ValidationException("The email address is invalid")));
        }

        if (isValid) {
            mUserDatabaseHelper.put(mUserEntityMapper.unmap(user),
                    new IUserDatabaseHelper.IUserEntityPutCallback() {

                        @Override
                        public void onUserEntityPut() {
                            userCallback.onUserAdded();
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
        mUserDatabaseHelper.getUserEntities(
                new IUserDatabaseHelper.IUserEntitiesCallback() {

                    @Override
                    public void onUserEntitiesLoaded(
                            List<UserEntity> userEntities) {
                        final List<User> users = mUserEntityMapper
                                .map(userEntities);
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
        mUserDatabaseHelper.get(userId,
                new IUserDatabaseHelper.IUserEntityCallback() {

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
     * @param userCallback A {@link com.ushahidi.android.core.respository.IUserRepository.UserUpdateCallback}
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
            mUserDatabaseHelper.put(mUserEntityMapper.unmap(user),
                    new IUserDatabaseHelper.IUserEntityPutCallback() {

                        @Override
                        public void onUserEntityPut() {
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
     * @param callback A {@link com.ushahidi.android.core.respository.IUserRepository.UserDeletedCallback}
     *                 used for notifying clients.
     */
    @Override
    public void deleteUser(final User user,
            final UserDeletedCallback callback) {
        mUserDatabaseHelper.delete(mUserEntityMapper.unmap(user),
                new IUserDatabaseHelper.IUserEntityDeletedCallback() {
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
