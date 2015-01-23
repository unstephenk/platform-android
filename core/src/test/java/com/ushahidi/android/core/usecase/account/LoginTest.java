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
import com.ushahidi.android.core.repository.IUserAccountRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.core.usecase.IInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests User login use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class LoginTest {

    private Login mLogin;

    @Mock
    private ThreadExecutor mMockThreadExecutor;

    @Mock
    private PostExecutionThread mMockPostExecutionThread;

    @Mock
    private IUserAccountRepository mMockUserAccountRepository;

    @Mock
    private UserAccount mMockUserAccount;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mLogin = new Login(mMockThreadExecutor, mMockPostExecutionThread);
        mLogin.setUserAccountRepository(mMockUserAccountRepository);
    }

    @Test
    public void testLoginUserExecution() {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));

        Login.Callback mockLoginCallback = mock(
                Login.Callback.class);

        mLogin.execute(mMockUserAccount, mockLoginCallback);

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockThreadExecutor);
        verifyZeroInteractions(mMockUserAccountRepository);
        verifyZeroInteractions(mMockPostExecutionThread);
    }

    @Test
    public void testLoginUserRun() {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        Login.Callback mockLoginCallback = mock(
                Login.Callback.class);
        doNothing().when(mMockUserAccountRepository).loginUserAccount(any(UserAccount.class),
                any(IUserAccountRepository.UserAccountLoggedInCallback.class));

        mLogin.execute(mMockUserAccount, mockLoginCallback);
        mLogin.run();

        verify(mMockUserAccountRepository).loginUserAccount(any(UserAccount.class),
                any(IUserAccountRepository.UserAccountLoggedInCallback.class));

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockUserAccountRepository);
        verifyNoMoreInteractions(mMockPostExecutionThread);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginUserNullParameter() {
        mLogin.execute(mMockUserAccount, null);
    }
}
