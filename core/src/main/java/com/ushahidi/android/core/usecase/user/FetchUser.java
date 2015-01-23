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
 * Fetches user's profile from the database.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class FetchUser implements IFetchUser {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final IUserRepository.UserDetailsCallback mRepositoryCallback =
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

    private IUserRepository mUserRepository;

    private Callback mCallback;

    /**
     * Default constructor.
     *
     * @param threadExecutor      {@link ThreadExecutor} used to execute this use case in a
     *                            background thread.
     * @param postExecutionThread {@link PostExecutionThread} used to post updates when the use case
     *                            has been executed.
     */
    public FetchUser(ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;

    }

    public void setUserRepository(IUserRepository userRepository) {
        if(userRepository == null) {
            throw new NullPointerException("You must call setUserRepository(...)");
        }

        mUserRepository = userRepository;
    }

    @Override
    public void execute(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be null!!!");
        }
        mCallback = callback;
        mThreadExecutor.execute(this);

    }

    @Override
    public void run() {
        if (mUserRepository == null) {
            throw new NullPointerException("You must call setUserRepository(...)");
        }
        mUserRepository.getUserViaApi(mRepositoryCallback);
    }

    private void notifySuccess(final User user) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onUserProfileFetched(user);
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
