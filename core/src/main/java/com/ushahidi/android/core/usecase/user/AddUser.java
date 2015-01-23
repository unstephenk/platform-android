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

package com.ushahidi.android.core.usecase.user;

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.repository.IUserRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;

/**
 * Use case for adding a user's profile
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddUser implements IAddUser {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    protected IUserRepository mUserRepository;

    IUserRepository.AddCallback mAddCallback = new IUserRepository.AddCallback() {
        @Override
        public void onAdded() {
            notifySuccess();
        }

        @Override
        public void onError(ErrorWrap error) {
            notifyFailure(error);
        }
    };

    private Callback mCallback;

    private User mUser;

    public AddUser(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(User user, Callback callback) {

        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be null!!!");
        }

        mUser = user;
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    public void setAddRepository(IUserRepository userRepository) {
        if (userRepository == null) {
            throw new NullPointerException("You must call setAddRepository cannot be null");
        }
        mUserRepository = userRepository;
    }

    @Override
    public void run() {
        if (mUserRepository == null) {
            throw new NullPointerException("You must call setAddRepository cannot be null");
        }
        mUserRepository.addUser(mUser, mAddCallback);
    }

    private void notifySuccess() {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onAdded();
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
