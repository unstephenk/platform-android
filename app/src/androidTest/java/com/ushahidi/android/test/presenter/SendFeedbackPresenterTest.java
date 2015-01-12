/*
 * Copyright (c) 2015 Ushahidi.
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

import com.ushahidi.android.presenter.SendFeedbackPresenter;
import com.ushahidi.android.test.CustomAndroidTestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Context;

import java.io.IOException;

import static org.mockito.Mockito.spy;

/**
 * Tests {@link com.ushahidi.android.presenter.SendFeedbackPresenter}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class SendFeedbackPresenterTest extends CustomAndroidTestCase {

    private SendFeedbackPresenter mSendFeedbackPresenter;

    @Mock
    private SendFeedbackPresenter.View mMockView;

    @Mock
    private Context mMockContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mSendFeedbackPresenter = spy(new SendFeedbackPresenter(getContext()));
        mSendFeedbackPresenter.setView(mMockView);
    }

    public void testGetDeviceInfo() throws IOException {
        String deviceInfo = mSendFeedbackPresenter.getDeviceInfo();
        assertNotNull(deviceInfo);
    }

}
