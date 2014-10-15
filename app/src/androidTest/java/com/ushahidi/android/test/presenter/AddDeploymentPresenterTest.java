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

package com.ushahidi.android.test.presenter;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.usecase.deployment.AddDeployment;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.presenter.AddDeploymentPresenter;
import com.ushahidi.android.test.CustomAndroidTestCase;
import com.ushahidi.android.ui.view.IAddDeploymentView;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Context;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * Tests {@link com.ushahidi.android.presenter.AddDeploymentPresenter}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddDeploymentPresenterTest extends CustomAndroidTestCase {

    private static final String DUMMY_TITLE = "Dummy Deployment Title";

    private static final int DUMMY_STATUS = 0;

    private static final String DUMMY_URL = "http://deployment.com";

    private AddDeploymentPresenter mAddDeploymentPresenter;

    private DeploymentModel mDeploymentModel;

    @Mock
    private Context mMockContext;

    @Mock
    private DeploymentModelDataMapper mMockDeploymentModelDataMapper;

    @Mock
    private IAddDeploymentView mMockIAddDeploymentView;

    @Mock
    private AddDeployment mMockAddDeployment;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mAddDeploymentPresenter = new AddDeploymentPresenter(mMockIAddDeploymentView,
                mMockAddDeployment,
                mMockDeploymentModelDataMapper);
        mDeploymentModel = new DeploymentModel();
        mDeploymentModel.setTitle(DUMMY_TITLE);
        mDeploymentModel.setUrl(DUMMY_URL);
        mDeploymentModel.setStatus(DUMMY_STATUS);
    }

    public void testInitializingAddDeploymentPresenterWithNullValues() {
        final String expectedMessage = "IAddDeploymentView cannot be null";
        try {
            new AddDeploymentPresenter(null, null, null);
        } catch (NullPointerException e) {
            assertEquals(expectedMessage, e.getMessage());
        }

    }

    public void testAddDeployment() {
        doNothing().when(mMockAddDeployment)
                .execute(any(Deployment.class), any(AddDeployment.Callback.class));

        given(mMockIAddDeploymentView.getContext()).willReturn(mMockContext);

        mAddDeploymentPresenter.addDeployment(mDeploymentModel);

        verify(mMockIAddDeploymentView).showLoading();
        verify(mMockDeploymentModelDataMapper).unmap(mDeploymentModel);
        verify(mMockAddDeployment)
                .execute(any(Deployment.class), any(AddDeployment.Callback.class));
    }

}
