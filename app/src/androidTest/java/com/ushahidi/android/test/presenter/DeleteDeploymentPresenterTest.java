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
import com.ushahidi.android.core.usecase.deployment.DeleteDeployment;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.presenter.DeleteDeploymentPresenter;
import com.ushahidi.android.test.CustomAndroidTestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Context;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * Tests {@link com.ushahidi.android.presenter.DeleteDeploymentPresenter}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeleteDeploymentPresenterTest extends CustomAndroidTestCase {

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_TITLE = "Dummy Deployment Title";

    private static final DeploymentModel.Status DUMMY_STATUS = DeploymentModel.Status.DEACTIVATED;

    private static final String DUMMY_URL = "http://deployment.com";

    private DeleteDeploymentPresenter mDeleteDeploymentPresenter;

    @Mock
    private Context mMockContext;

    @Mock
    private DeleteDeploymentPresenter.View mMockView;

    @Mock
    private DeploymentModelDataMapper mMockDeploymentModelDataMapper;

    @Mock
    private DeleteDeployment mMockDeleteDeployment;

    private DeploymentModel mDeploymentModel;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mDeploymentModel = new DeploymentModel();
        mDeploymentModel.setId(DUMMY_ID);
        mDeploymentModel.setTitle(DUMMY_TITLE);
        mDeploymentModel.setUrl(DUMMY_URL);
        mDeploymentModel.setStatus(DUMMY_STATUS);

        mDeleteDeploymentPresenter = new DeleteDeploymentPresenter(
                mMockDeleteDeployment, mMockDeploymentModelDataMapper);
        mDeleteDeploymentPresenter.setView(mMockView);
    }

    public void testInitializingDeleteDeploymentPresenterWithNullValues() {
        final String expectedMessage = "DeleteDeployment cannot be null";
        try {
            new DeleteDeploymentPresenter(null, null);
        } catch (NullPointerException e) {
            assertEquals(expectedMessage, e.getMessage());
        }

    }

    public void testDeleteDeployment() {
        doNothing().when(mMockDeleteDeployment)
                .execute(any(Deployment.class), any(DeleteDeployment.Callback.class));

        given(mMockView.getAppContext()).willReturn(mMockContext);

        mDeleteDeploymentPresenter.deleteDeployment(mDeploymentModel);

        verify(mMockDeploymentModelDataMapper).unmap(mDeploymentModel);
        verify(mMockDeleteDeployment)
                .execute(any(Deployment.class), any(DeleteDeployment.Callback.class));
    }
}
