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

import com.squareup.okhttp.OkHttpClient;
import com.ushahidi.android.Util.ApiServiceUtil;
import com.ushahidi.android.core.usecase.post.FetchPost;
import com.ushahidi.android.core.usecase.post.ListPost;
import com.ushahidi.android.data.api.service.PostService;
import com.ushahidi.android.data.database.PostDatabaseHelper;
import com.ushahidi.android.data.entity.mapper.PostEntityMapper;
import com.ushahidi.android.data.pref.StringPreference;
import com.ushahidi.android.model.mapper.PostModelDataMapper;
import com.ushahidi.android.presenter.ListPostPresenter;
import com.ushahidi.android.test.CustomAndroidTestCase;
import com.ushahidi.android.ui.prefs.Prefs;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Context;

import retrofit.client.OkClient;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@link ListPostPresenterTest}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ListPostPresenterTest extends CustomAndroidTestCase {

    private ListPostPresenter mPostListPresenter;

    @Mock
    private Context mMockContext;

    @Mock
    private ListPostPresenter.View mMockView;

    @Mock
    private PostModelDataMapper mMockPostModelDataMapper;

    @Mock
    private PostDatabaseHelper mMockPostDatabaseHelper;

    @Mock
    private Prefs mMockPrefs;

    @Mock
    private ListPost mMockListPost;

    private ApiServiceUtil mMockApiServiceUtil;

    @Mock
    private FetchPost mMockFetchPost;

    @Mock
    private PostEntityMapper mMockPostEntityMapper;

    @Mock
    private PostService mPostService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mMockApiServiceUtil = new ApiServiceUtil(new OkClient(new OkHttpClient()));

        setupPrefsMock();
        mPostListPresenter = new ListPostPresenter(
                mMockListPost, mMockFetchPost, mMockPostModelDataMapper,
                mMockPostEntityMapper,
                mMockPostDatabaseHelper,
                mMockPrefs,
                mMockApiServiceUtil,
                mMockContext);
        //mPostListPresenter.setPostService(mPostService);
        mPostListPresenter.setView(mMockView);
    }

    public void testInitializingPostListPresenterWithNullValues() {
        final String expectedMessage = "ListPost cannot be null";
        try {
            new ListPostPresenter(null, null, null, null, null, null, null, null);
        } catch (NullPointerException e) {
            assertEquals(expectedMessage, e.getMessage());
        }

    }

    public void testPostListPresenterInit() {
        doNothing().when(mMockListPost)
                .execute(any(ListPost.Callback.class));

        given(mMockView.getContext()).willReturn(mMockContext);

        mPostListPresenter.init();

        verify(mMockView).hideRetry();
        verify(mMockView).showLoading();
        verify(mMockListPost).execute(any(ListPost.Callback.class));
    }

    private void setupPrefsMock() {
        StringPreference preference = mock(StringPreference.class);
        when(mMockPrefs.getActiveDeploymentUrl()).thenReturn(preference);
        when(mMockPrefs.getAccessToken()).thenReturn(preference);
        when(preference.get()).thenReturn("");
    }
}
