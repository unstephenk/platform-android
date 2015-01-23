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

package com.ushahidi.android.core.usecase.deployment;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.repository.IDeploymentRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.core.usecase.IInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests get deployment use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class GetDeploymentTest {

    private static final long TEST_DEPLOYMENT_ID = 15;

    private GetDeployment mGetDeployment;

    @Mock
    private ThreadExecutor mMockThreadExecutor;

    @Mock
    private PostExecutionThread mMockPostExecutionThread;

    @Mock
    private IDeploymentRepository mMockDeploymentRepository;

    @Mock
    private Deployment mMockDeployment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mGetDeployment = new GetDeployment(mMockDeploymentRepository, mMockThreadExecutor,
                mMockPostExecutionThread);

    }

    @Test
    public void shouldExecuteGetDeployment() throws Exception {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));

        GetDeployment.Callback mockGetDeploymentCallback =
                mock(GetDeployment.Callback.class);

        mGetDeployment.execute(TEST_DEPLOYMENT_ID, mockGetDeploymentCallback);

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockThreadExecutor);
        verifyZeroInteractions(mMockDeploymentRepository);
        verifyZeroInteractions(mMockPostExecutionThread);
    }


    @Test
    public void shouldRunGetDeploymentInteractor() throws Exception {
        GetDeployment.Callback mockGetUserDetailsCallback =
                mock(GetDeployment.Callback.class);

        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        doNothing().when(mMockDeploymentRepository)
                .getDeploymentById(anyLong(),
                        any(IDeploymentRepository.DeploymentDetailsCallback.class));

        mGetDeployment.execute(TEST_DEPLOYMENT_ID, mockGetUserDetailsCallback);
        mGetDeployment.run();

        verify(mMockDeploymentRepository)
                .getDeploymentById(anyLong(),
                        any(IDeploymentRepository.DeploymentDetailsCallback.class));
        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockDeploymentRepository);
        verifyNoMoreInteractions(mMockThreadExecutor);
    }

    @Test
    public void shouldSuccessfullyGetDeploymentById() throws Exception {
        final GetDeployment.Callback mockGetDeploymentCallback =
                mock(GetDeployment.Callback.class);
        final Deployment mockDeployment = mock(Deployment.class);

        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IDeploymentRepository.DeploymentDetailsCallback) invocation.getArguments()[1])
                        .onDeploymentLoaded(
                                mockDeployment);
                return null;
            }
        }).when(mMockDeploymentRepository)
                .getDeploymentById(anyLong(),
                        any(IDeploymentRepository.DeploymentDetailsCallback.class));

        mGetDeployment.execute(TEST_DEPLOYMENT_ID, mockGetDeploymentCallback);
        mGetDeployment.run();

        verify(mMockPostExecutionThread).post(any(Runnable.class));
        verifyNoMoreInteractions(mockGetDeploymentCallback);
        verifyZeroInteractions(mockDeployment);
    }

    @Test
    public void shouldErrorWhenGettingDeployment() throws Exception {
        final GetDeployment.Callback mockGetDeploymentCallback =
                mock(GetDeployment.Callback.class);
        final ErrorWrap mockError = mock(ErrorWrap.class);

        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IDeploymentRepository.DeploymentDetailsCallback) invocation.getArguments()[1])
                        .onError(
                                mockError);
                return null;
            }
        }).when(mMockDeploymentRepository)
                .getDeploymentById(anyLong(),
                        any(IDeploymentRepository.DeploymentDetailsCallback.class));

        mGetDeployment.execute(TEST_DEPLOYMENT_ID, mockGetDeploymentCallback);
        mGetDeployment.run();

        verify(mMockPostExecutionThread).post(any(Runnable.class));
        verifyNoMoreInteractions(mockGetDeploymentCallback);
        verifyZeroInteractions(mockError);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldGetDeploymentNullParameter() throws Exception {
        mGetDeployment.execute(TEST_DEPLOYMENT_ID, null);
    }
}
