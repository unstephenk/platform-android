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

import android.content.Context;

import com.ushahidi.android.core.usecase.deployment.ListDeployment;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.presenter.ListDeploymentPresenter;
import com.ushahidi.android.test.CustomAndroidTestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * Tests {@link DeploymentListPresenterTest}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentListPresenterTest extends CustomAndroidTestCase {

    private ListDeploymentPresenter mDeploymentListPresenter;

    @Mock
    private Context mMockContext;

    @Mock
    private ListDeploymentPresenter.View mMockView;

    @Mock
    private DeploymentModelDataMapper mMockDeploymentModelDataMapper;

    @Mock
    private ListDeployment mMockListDeployment;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        mDeploymentListPresenter = new ListDeploymentPresenter(
            mMockListDeployment, mMockDeploymentModelDataMapper);
        mDeploymentListPresenter.setView(mMockView);
    }

    public void testInitializingDeploymentListPresenterWithNullValues() {
        final String expectedMessage = "Constructor parameters cannot be null!!!";
        try {
            new ListDeploymentPresenter(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals(expectedMessage, e.getMessage());
        }

    }

    public void testDeploymentListPresenterInit() {
        doNothing().when(mMockListDeployment)
            .execute(any(ListDeployment.Callback.class));

        given(mMockView.getAppContext()).willReturn(mMockContext);

        mDeploymentListPresenter.init();

        verify(mMockView).showLoading();
        verify(mMockListDeployment).execute(any(ListDeployment.Callback.class));
    }
}
