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

import com.ushahidi.android.core.entity.Post;
import com.ushahidi.android.core.repository.IPostRepository;
import com.ushahidi.android.core.repository.ITagRepository;
import com.ushahidi.android.core.usecase.Search;
import com.ushahidi.android.core.usecase.post.FetchPost;
import com.ushahidi.android.core.usecase.post.ListPost;
import com.ushahidi.android.core.usecase.tag.FetchTag;
import com.ushahidi.android.data.api.service.PostService;
import com.ushahidi.android.data.pref.StringPreference;
import com.ushahidi.android.data.repository.datasource.post.PostDataSourceFactory;
import com.ushahidi.android.data.repository.datasource.tag.TagDataSourceFactory;
import com.ushahidi.android.model.mapper.PostModelDataMapper;
import com.ushahidi.android.presenter.ListPostPresenter;
import com.ushahidi.android.state.IDeploymentState;
import com.ushahidi.android.test.CustomAndroidTestCase;
import com.ushahidi.android.ui.prefs.Prefs;
import com.ushahidi.android.util.ApiServiceUtil;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Context;

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
    private PostDataSourceFactory mockPostDataSourceFactory;

    @Mock
    private TagDataSourceFactory mMockTagDataSourceFactory;


    @Mock
    private Prefs mMockPrefs;

    @Mock
    private ListPost mMockListPost;

    @Mock
    private ApiServiceUtil mMockApiServiceUtil;

    @Mock
    private FetchPost mMockFetchPost;

    @Mock
    private FetchTag mMockFetchTag;

    @Mock
    private Search<Post> mMockSearch;

    @Mock
    private IPostRepository mMockPostRepository;

    @Mock
    private ITagRepository mMockTagRepository;

    @Mock
    private PostService mMockPostService;

    @Mock
    private IDeploymentState mDeploymentState;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
      /*  mMockApiServiceUtil = new ApiServiceUtil(new OkClient(new OkHttpClient()),
                new UnauthorizedAccessErrorHandler(new ApplicationState(new Bus())));*/

        setupPrefsMock();
        mPostListPresenter = new ListPostPresenter(
                mMockListPost, mMockFetchTag,
                mMockSearch, mMockFetchPost, mMockPostModelDataMapper,
                mMockPostRepository,
                mMockTagRepository,
                mockPostDataSourceFactory,
                mMockTagDataSourceFactory,
                mMockPrefs,
                mMockApiServiceUtil,
                mDeploymentState);
        mPostListPresenter.setView(mMockView);
    }

    public void testInitializingPostListPresenterWithNullValues() {
        final String expectedMessage = "ListPost cannot be null";
        try {
            new ListPostPresenter(null, null, null, null, null, null, null, null,null,null,null,null);
        } catch (NullPointerException e) {
            assertEquals(expectedMessage, e.getMessage());
        }

    }

    public void testPostListPresenterInit() {
        doNothing().when(mMockListPost)
                .execute(any(ListPost.Callback.class));

        given(mMockView.getAppContext()).willReturn(mMockContext);
        given(mMockApiServiceUtil.createService(PostService.class,"", "")).willReturn(
                mMockPostService);

        mPostListPresenter.init();

        verify(mockPostDataSourceFactory).setPostService(mMockPostService);
        verify(mMockListPost).setPostRepository(mMockPostRepository);
        verify(mMockFetchPost).setPostRepository(mMockPostRepository);
        verify(mMockSearch).setRepository(mMockPostRepository);

    }

    private void setupPrefsMock() {
        StringPreference preference = mock(StringPreference.class);
        when(mMockPrefs.getActiveDeploymentUrl()).thenReturn(preference);
        when(mMockPrefs.getAccessToken()).thenReturn(preference);
        when(preference.get()).thenReturn("");
    }
}
