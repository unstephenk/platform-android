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
package com.ushahidi.android.core.usecase.user;

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.respository.IUserRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;

/**
 * This class is an implementation of {@link IGetUser} that represents a use case for retrieving
 * data related to a specific {@link User}.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class GetUser implements IGetUser {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final IUserRepository mUserRepository;

    private final IUserRepository.UserDetailsCallback mUserDetailsCallback =
            new IUserRepository.UserDetailsCallback() {

                @Override
                public void onUserLoaded(User user) {
                    notifySuccess(user);
                }

                @Override
                public void onError(ErrorWrap errorWrap) {
                    notifyFailure(errorWrap);
                }
            };

    private IGetUser.Callback mCallback;

    private long mUserId;

    public GetUser(IUserRepository userRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (userRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mUserRepository = userRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(long userId, IGetUser.Callback callback) {
        if (userId < 0 || callback == null) {
            throw new IllegalArgumentException("Callback cannot be null!!!");
        }
        mUserId = userId;
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    private void notifySuccess(final User user) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onUserLoaded(user);
            }
        });
    }

    private void notifyFailure(final ErrorWrap errorWrap) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(errorWrap);
            }
        });
    }

    @Override
    public void run() {
        mUserRepository.getUserById(mUserId, mUserDetailsCallback);
    }
}
