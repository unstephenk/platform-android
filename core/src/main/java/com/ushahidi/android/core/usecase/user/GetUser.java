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
import com.ushahidi.android.core.executor.PostExecutionThread;
import com.ushahidi.android.core.executor.ThreadExecutor;
import com.ushahidi.android.core.respository.UserRepository;

/**
 * This class is an implementation of {@link IGetUser} that represents a use case for retrieving
 * data related to a specific {@link User}.
 *
 * @author  Ushahidi Team <team@ushahidi.com>
 */
public class GetUser implements IGetUser {

    private final UserRepository mUserRepository;

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final UserRepository.UserCallback mUserCallback = new UserRepository.UserCallback() {
        @Override
        public void onUserLoaded(User user) {

        }

        @Override
        public void onError(ErrorWrap error) {

        }
    };

    private Callback mCallback;

    private int mId;

    public GetUser(UserRepository userRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (userRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mUserRepository = userRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(int id, Callback callback) {
        if (id < 0) {
            throw new IllegalArgumentException("User id cannot be less than zero");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Interactor callback cannot be null");
        }

        mCallback = callback;
        mId = id;
        mThreadExecutor.execute(this);
    }

    @Override
    public void run() {
        mUserRepository.getUser(mId, mUserCallback);
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
}
