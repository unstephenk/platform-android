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
import com.ushahidi.android.core.usecase.deployment.ActivateDeployment;
import com.ushahidi.android.core.usecase.deployment.GetActiveDeployment;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.presenter.ActivateDeploymentPresenter;
import com.ushahidi.android.test.CustomAndroidTestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Context;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ActivateDeploymentPresenterTest extends CustomAndroidTestCase {

    @Mock
    private Context mMockContext;

    @Mock
    private DeploymentModelDataMapper mMockDeploymentModelDataMapper;

    @Mock
    private ActivateDeployment mMockActivateDeployment;

    @Mock
    private GetActiveDeployment mMockGetActiveDeployment;

    @Mock
    private ActivateDeploymentPresenter.View mMockView;

    @Mock
    private List<DeploymentModel> mMockDeploymentModelList;

    private ActivateDeploymentPresenter mActivateDeploymentPresenter;

    @Mock
    private DeploymentModel mMockDeploymentModel;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mActivateDeploymentPresenter = new ActivateDeploymentPresenter(
                mMockActivateDeployment,mMockGetActiveDeployment, mMockDeploymentModelDataMapper);
        mActivateDeploymentPresenter.setView(mMockView);
    }

    public void testInitializingActivateDeploymentPresenterWithNullValues() {
        final String expectedMessage = "Activate deployment usecase cannot be null";
        try {
            new ActivateDeploymentPresenter(null, null,null);
        } catch (NullPointerException e) {
            assertEquals(expectedMessage, e.getMessage());
        }

    }

    public void testShouldActivateDeployment() {
        doNothing().when(mMockActivateDeployment)
                .execute(anyListOf(Deployment.class), anyInt(),
                        any(ActivateDeployment.Callback.class));

        given(mMockView.getContext()).willReturn(mMockContext);

        mActivateDeploymentPresenter.activateDeployment(mMockDeploymentModelList, anyInt());
        verify(mMockDeploymentModelDataMapper).unmap(anyListOf(DeploymentModel.class));
        verify(mMockActivateDeployment)
                .execute(anyListOf(Deployment.class), anyInt(),
                        any(ActivateDeployment.Callback.class));
    }
}
