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

import com.ushahidi.android.core.usecase.post.ListPost;
import com.ushahidi.android.model.mapper.PostModelDataMapper;
import com.ushahidi.android.presenter.ListPostPresenter;
import com.ushahidi.android.test.CustomAndroidTestCase;
import com.ushahidi.android.ui.view.IPostListView;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Context;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

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
    private IPostListView mMockIPostListView;

    @Mock
    private PostModelDataMapper mMockPostModelDataMapper;

    @Mock
    private ListPost mMockListPost;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        mPostListPresenter = new ListPostPresenter(mMockIPostListView,
                mMockListPost, mMockPostModelDataMapper);
    }

    public void testInitializingPostListPresenterWithNullValues() {
        final String expectedMessage = "Post list view cannot be null";
        try {
            new ListPostPresenter(null, null, null);
        } catch (NullPointerException e) {
            assertEquals(expectedMessage, e.getMessage());
        }

    }

    public void testPostListPresenterInit() {
        doNothing().when(mMockListPost)
                .execute(any(ListPost.Callback.class));

        given(mMockIPostListView.getContext()).willReturn(mMockContext);

        mPostListPresenter.init();

        verify(mMockIPostListView).hideRetry();
        verify(mMockIPostListView).showLoading();
        verify(mMockListPost).execute(any(ListPost.Callback.class));
    }
}
