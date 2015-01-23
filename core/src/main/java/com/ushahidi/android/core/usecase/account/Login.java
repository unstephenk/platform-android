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

package com.ushahidi.android.core.usecase.account;

import com.ushahidi.android.core.entity.UserAccount;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.repository.IUserAccountRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;

/**
 * Login user
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class Login implements ILogin {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private IUserAccountRepository mUserAccountRepository;

    private final IUserAccountRepository.UserAccountLoggedInCallback mUserAccountLoggedInCallback
            = new IUserAccountRepository.UserAccountLoggedInCallback() {

        @Override
        public void onUserAccountLoggedIn(UserAccount userAccount) {
            notifySuccess(userAccount);
        }

        @Override
        public void onError(ErrorWrap error) {
            notifyFailure(error);
        }
    };

    private UserAccount mUserAccount;

    private Callback mCallback;

    public Login(ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if ( threadExecutor == null
                || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    public void setUserAccountRepository(IUserAccountRepository userRepository) {
        if(userRepository == null) {
            throw new IllegalArgumentException("IUserAccountRepository cannot be null");
        }
        mUserAccountRepository = userRepository;
    }

    @Override
    public void run() {
        mUserAccountRepository.loginUserAccount(mUserAccount, mUserAccountLoggedInCallback);
    }

    @Override
    public void execute(final UserAccount userAccount, final Callback callback) {
        if (userAccount == null || callback == null) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }

        mUserAccount = userAccount;
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    private void notifySuccess(final UserAccount userAccount) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onUserAccountLoggedIn(userAccount);
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
