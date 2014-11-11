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

package com.ushahidi.android.core.usecase.media;

import com.ushahidi.android.core.entity.Media;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.respository.IMediaRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.core.usecase.IInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests get media use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class GetMediaTest {

    private static final long TEST_DEPLOYMENT_ID = 15;

    private GetMedia mGetMedia;

    @Mock
    private ThreadExecutor mMockThreadExecutor;

    @Mock
    private PostExecutionThread mMockPostExecutionThread;

    @Mock
    private IMediaRepository mMockMediaRepository;

    @Mock
    private Media mMockMedia;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mGetMedia = new GetMedia(mMockMediaRepository, mMockThreadExecutor,
                mMockPostExecutionThread);

    }

    @Test
    public void shouldExecuteGetMedia() throws Exception {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));

        GetMedia.Callback mockGetMediaCallback =
                mock(GetMedia.Callback.class);

        mGetMedia.execute(TEST_DEPLOYMENT_ID, mockGetMediaCallback);

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockThreadExecutor);
        verifyZeroInteractions(mMockMediaRepository);
        verifyZeroInteractions(mMockPostExecutionThread);
    }


    @Test
    public void shouldRunGetMediaInteractor() throws Exception {
        GetMedia.Callback mockGetUserDetailsCallback =
                mock(GetMedia.Callback.class);

        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        doNothing().when(mMockMediaRepository)
                .getMediaById(anyLong(),
                        any(IMediaRepository.MediaDetailsCallback.class));

        mGetMedia.execute(TEST_DEPLOYMENT_ID, mockGetUserDetailsCallback);
        mGetMedia.run();

        verify(mMockMediaRepository)
                .getMediaById(anyLong(),
                        any(IMediaRepository.MediaDetailsCallback.class));
        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockMediaRepository);
        verifyNoMoreInteractions(mMockThreadExecutor);
    }

    @Test
    public void shouldSuccessfullyGetMediaById() throws Exception {
        final GetMedia.Callback mockGetMediaCallback =
                mock(GetMedia.Callback.class);
        final Media mockMedia = mock(Media.class);

        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IMediaRepository.MediaDetailsCallback) invocation.getArguments()[1])
                        .onMediaLoaded(
                                mockMedia);
                return null;
            }
        }).when(mMockMediaRepository)
                .getMediaById(anyLong(),
                        any(IMediaRepository.MediaDetailsCallback.class));

        mGetMedia.execute(TEST_DEPLOYMENT_ID, mockGetMediaCallback);
        mGetMedia.run();

        verify(mMockPostExecutionThread).post(any(Runnable.class));
        verifyNoMoreInteractions(mockGetMediaCallback);
        verifyZeroInteractions(mockMedia);
    }

    @Test
    public void shouldErrorWhenGettingMedia() throws Exception {
        final GetMedia.Callback mockGetMediaCallback =
                mock(GetMedia.Callback.class);
        final ErrorWrap mockError = mock(ErrorWrap.class);

        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IMediaRepository.MediaDetailsCallback) invocation.getArguments()[1])
                        .onError(
                                mockError);
                return null;
            }
        }).when(mMockMediaRepository)
                .getMediaById(anyLong(),
                        any(IMediaRepository.MediaDetailsCallback.class));

        mGetMedia.execute(TEST_DEPLOYMENT_ID, mockGetMediaCallback);
        mGetMedia.run();

        verify(mMockPostExecutionThread).post(any(Runnable.class));
        verifyNoMoreInteractions(mockGetMediaCallback);
        verifyZeroInteractions(mockError);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldGetMediaNullParameter() throws Exception {
        mGetMedia.execute(TEST_DEPLOYMENT_ID, null);
    }
}
