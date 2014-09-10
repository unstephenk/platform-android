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

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.database.DeploymentDatabaseHelper;
import com.ushahidi.android.data.entity.DeploymentEntity;
import com.ushahidi.android.data.entity.mapper.DeploymentEntityMapper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentDataRepositoryTest extends BaseTestCase {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DeploymentDataRepository mDeploymentDataRepository;

    @Mock
    private DeploymentEntityMapper mMockDeploymentEntityMapper;

    @Mock
    private DeploymentEntity mMockDeploymentEntity;

    @Mock
    private Deployment mMock;

    @Mock
    private DeploymentDatabaseHelper mMockDeploymentDatabaseHelper;

    @Mock
    private DeploymentDataRepository.DeploymentAddCallback mMockDeploymentAddCallback;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mDeploymentDataRepository = DeploymentDataRepository
                .getInstance(mMockDeploymentDatabaseHelper, mMockDeploymentEntityMapper);

    }

    @Test
    public void shouldInvalidateConstructorsNullParameters() {
        //resetSingleton(UserDataRepository.class);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid null parameter");

        mDeploymentDataRepository = DeploymentDataRepository.getInstance(null, null);
    }
}
