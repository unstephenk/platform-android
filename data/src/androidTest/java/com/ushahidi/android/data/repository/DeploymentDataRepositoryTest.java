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
import com.ushahidi.android.data.database.IDeploymentDatabaseHelper;
import com.ushahidi.android.data.entity.DeploymentEntity;
import com.ushahidi.android.data.entity.mapper.DeploymentEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.validator.UrlValidator;

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
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests {@link com.ushahidi.android.data.database.DeploymentDatabaseHelper}
 *
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
    private Deployment mMockDeployment;

    @Mock
    private DeploymentDatabaseHelper mMockDeploymentDatabaseHelper;

    @Mock
    private DeploymentDataRepository.DeploymentAddCallback mMockDeploymentAddCallback;

    @Mock
    private UrlValidator mMockUrlValidator;

    private Deployment mDeployment;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_TITLE = "Dummy Deployment Title";

    private static final int DUMMY_STATUS = 0;

    private static final String DUMMY_URL = "http://www.deployment.com";


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        clearSingleton(DeploymentDataRepository.class);
        mDeploymentDataRepository = DeploymentDataRepository
                .getInstance(mMockDeploymentDatabaseHelper, mMockDeploymentEntityMapper,
                        mMockUrlValidator);
        mDeployment = new Deployment();
        mDeployment.setId(DUMMY_ID);
        mDeployment.setTitle(DUMMY_TITLE);
        mDeployment.setStatus(DUMMY_STATUS);
        mDeployment.setUrl(DUMMY_URL);

    }

    @Test
    public void shouldInvalidateConstructorsNullParameters() throws Exception {
        clearSingleton(DeploymentDataRepository.class);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid null parameter");
        mDeploymentDataRepository = DeploymentDataRepository.getInstance(null, null, null);
    }

    @Test
    public void shouldSuccessfullyAddADeployment() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IDeploymentDatabaseHelper.IDeploymentEntityAddedCallback) invocation
                        .getArguments()[1]).onDeploymentEntityAdded();
                return null;
            }
        }).when(mMockDeploymentDatabaseHelper).put(any(DeploymentEntity.class),
                any(IDeploymentDatabaseHelper.IDeploymentEntityAddedCallback.class));
        given(mMockUrlValidator.isValid(mDeployment.getUrl())).willReturn(true);
        given(mMockDeploymentEntityMapper.unmap(mDeployment)).willReturn(mMockDeploymentEntity);

        mDeploymentDataRepository.addDeployment(mDeployment, mMockDeploymentAddCallback);

        verify(mMockDeploymentEntityMapper).unmap(mDeployment);
        verify(mMockDeploymentAddCallback).onDeploymentAdded();
    }


    @Test
    public void shouldFailToAddADeployment() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IDeploymentDatabaseHelper.IDeploymentEntityAddedCallback) invocation
                        .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockDeploymentDatabaseHelper).put(any(DeploymentEntity.class),
                any(IDeploymentDatabaseHelper.IDeploymentEntityAddedCallback.class));

        mDeploymentDataRepository.addDeployment(mMockDeployment, mMockDeploymentAddCallback);

        verify(mMockDeploymentAddCallback, times(2)).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyTestForInvalidDeploymentUrl() throws Exception {
        mDeployment.setTitle(DUMMY_TITLE);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IDeploymentDatabaseHelper.IDeploymentEntityAddedCallback) invocation
                        .getArguments()[1]).onError(any(ValidationException.class));
                return null;
            }
        }).when(mMockDeploymentDatabaseHelper).put(any(DeploymentEntity.class),
                any(IDeploymentDatabaseHelper.IDeploymentEntityAddedCallback.class));

        given(mMockUrlValidator.isValid(mDeployment.getUrl())).willReturn(false);

        mDeploymentDataRepository.addDeployment(mDeployment, mMockDeploymentAddCallback);

        verify(mMockDeploymentAddCallback).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyTestForEmptyOrNullDeploymentTitle() throws Exception {
        mDeployment.setTitle(null);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IDeploymentDatabaseHelper.IDeploymentEntityAddedCallback) invocation
                        .getArguments()[1]).onError(any(ValidationException.class));
                return null;
            }
        }).when(mMockDeploymentDatabaseHelper).put(any(DeploymentEntity.class),
                any(IDeploymentDatabaseHelper.IDeploymentEntityAddedCallback.class));

        given(mMockUrlValidator.isValid(mDeployment.getUrl())).willReturn(true);

        mDeploymentDataRepository.addDeployment(mDeployment, mMockDeploymentAddCallback);

        verify(mMockDeploymentAddCallback).onError(any(RepositoryError.class));
    }
}
