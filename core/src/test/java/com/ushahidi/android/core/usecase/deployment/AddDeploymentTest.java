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
import com.ushahidi.android.core.repository.IDeploymentRepository;
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
 * Test AddDeployment use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddDeploymentTest {

    private AddDeployment mAddDeployment;

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

        mAddDeployment = new AddDeployment(mMockDeploymentRepository, mMockThreadExecutor,
                mMockPostExecutionThread);

    }

    @Test
    public void testAddDeploymentExecution() {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));

        AddDeployment.Callback mockAddDeploymentCallback = mock(AddDeployment.Callback.class);

        mAddDeployment.execute(mMockDeployment, mockAddDeploymentCallback);

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockThreadExecutor);
        verifyZeroInteractions(mMockDeploymentRepository);
        verifyZeroInteractions(mMockPostExecutionThread);
    }

    @Test
    public void testAddDeploymentRun() {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        AddDeployment.Callback mockAddDeploymentCallback = mock(AddDeployment.Callback.class);
        doNothing().when(mMockDeploymentRepository).addDeployment(any(Deployment.class),
                any(IDeploymentRepository.DeploymentAddCallback.class));

        mAddDeployment.execute(mMockDeployment, mockAddDeploymentCallback);
        mAddDeployment.run();

        verify(mMockDeploymentRepository).addDeployment(any(Deployment.class),
                any(IDeploymentRepository.DeploymentAddCallback.class));

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockDeploymentRepository);
        verifyNoMoreInteractions(mMockPostExecutionThread);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDeploymentNullParameter() {
        mAddDeployment.execute(mMockDeployment, null);
    }
}
