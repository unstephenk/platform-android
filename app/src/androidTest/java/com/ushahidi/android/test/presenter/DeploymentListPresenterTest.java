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

import com.ushahidi.android.core.usecase.deployment.ListDeployment;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.presenter.DeploymentListPresenter;
import com.ushahidi.android.ui.view.IDeploymentListView;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Context;
import android.test.AndroidTestCase;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * Tests {@link DeploymentListPresenterTest}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentListPresenterTest extends AndroidTestCase {

    private DeploymentListPresenter mDeploymentListPresenter;

    @Mock
    private Context mMockContext;

    @Mock
    private IDeploymentListView mMockIDeploymentListView;

    @Mock
    private DeploymentModelDataMapper mMockDeploymentModelDataMapper;

    @Mock
    private ListDeployment mMockListDeployment;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        mDeploymentListPresenter = new DeploymentListPresenter(mMockIDeploymentListView,
                mMockListDeployment, mMockDeploymentModelDataMapper);
    }

    public void testInitializingDeploymentListPresenterWithNullValues() {
        final String expectedMessage = "Constructor parameters cannot be null!!!";
        try {
            new DeploymentListPresenter(null, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals(expectedMessage, e.getMessage());
        }

    }

    public void testDeploymentListPresenterInit() {
        doNothing().when(mMockListDeployment)
                .execute(any(ListDeployment.Callback.class));

        given(mMockIDeploymentListView.getContext()).willReturn(mMockContext);

        mDeploymentListPresenter.init();

        verify(mMockIDeploymentListView).hideRetry();
        verify(mMockIDeploymentListView).showLoading();
        verify(mMockListDeployment).execute(any(ListDeployment.Callback.class));
    }
}
