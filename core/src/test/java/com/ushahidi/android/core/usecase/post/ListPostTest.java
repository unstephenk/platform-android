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

package com.ushahidi.android.core.usecase.post;

import com.ushahidi.android.core.respository.IPostRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.core.usecase.IInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests List Post use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ListPostTest {

    @Mock
    private ThreadExecutor mMockThreadExecutor;

    @Mock
    private PostExecutionThread mMockPostExecutionThread;

    @Mock
    private IPostRepository mMockPostRepository;

    private ListPost mListPost;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mListPost = new ListPost(mMockThreadExecutor,
                mMockPostExecutionThread);
        mListPost.setPostRepository(mMockPostRepository);
    }

    @Test
    public void testListPostExecution() {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));

        ListPost.Callback mockListPostCallback = mock(
                ListPost.Callback.class);

        mListPost.execute(mockListPostCallback);

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockThreadExecutor);
        verifyZeroInteractions(mMockPostRepository);
        verifyZeroInteractions(mMockPostExecutionThread);
    }

    @Test
    public void testListPostRun() {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        ListPost.Callback mockListPostCallback = mock(
                ListPost.Callback.class);
        doNothing().when(mMockPostRepository).getPostList(
                any(IPostRepository.PostListCallback.class));

        mListPost.execute(mockListPostCallback);
        mListPost.run();

        verify(mMockPostRepository).getPostList(
                any(IPostRepository.PostListCallback.class));

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockPostRepository);
        verifyNoMoreInteractions(mMockPostExecutionThread);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPostNullParameter() {
        mListPost.execute(null);
    }
}
