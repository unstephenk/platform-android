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

package com.ushahidi.android.core.usecase.tag;

import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.respository.ITagRepository;
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
 * Tests get tag use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class GetTagTest {

    private static final long TEST_TAG_ID = 15;

    private GetTag mGetTag;

    @Mock
    private ThreadExecutor mMockThreadExecutor;

    @Mock
    private PostExecutionThread mMockPostExecutionThread;

    @Mock
    private ITagRepository mMockTagRepository;

    @Mock
    private Tag mMockTag;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mGetTag = new GetTag(mMockTagRepository, mMockThreadExecutor,
                mMockPostExecutionThread);

    }

    @Test
    public void shouldExecuteGetTag() throws Exception {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));

        GetTag.Callback mockGetTagCallback =
                mock(GetTag.Callback.class);

        mGetTag.execute(TEST_TAG_ID, mockGetTagCallback);

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockThreadExecutor);
        verifyZeroInteractions(mMockTagRepository);
        verifyZeroInteractions(mMockPostExecutionThread);
    }


    @Test
    public void shouldRunGetTagInteractor() throws Exception {
        GetTag.Callback mockGetUserDetailsCallback =
                mock(GetTag.Callback.class);

        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        doNothing().when(mMockTagRepository)
                .getTagById(anyLong(),
                        any(ITagRepository.TagDetailsCallback.class));

        mGetTag.execute(TEST_TAG_ID, mockGetUserDetailsCallback);
        mGetTag.run();

        verify(mMockTagRepository)
                .getTagById(anyLong(),
                        any(ITagRepository.TagDetailsCallback.class));
        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockTagRepository);
        verifyNoMoreInteractions(mMockThreadExecutor);
    }

    @Test
    public void shouldSuccessfullyGetTagById() throws Exception {
        final GetTag.Callback mockGetTagCallback =
                mock(GetTag.Callback.class);
        final Tag mockTag = mock(Tag.class);

        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ITagRepository.TagDetailsCallback) invocation.getArguments()[1])
                        .onTagLoaded(
                                mockTag);
                return null;
            }
        }).when(mMockTagRepository)
                .getTagById(anyLong(),
                        any(ITagRepository.TagDetailsCallback.class));

        mGetTag.execute(TEST_TAG_ID, mockGetTagCallback);
        mGetTag.run();

        verify(mMockPostExecutionThread).post(any(Runnable.class));
        verifyNoMoreInteractions(mockGetTagCallback);
        verifyZeroInteractions(mockTag);
    }

    @Test
    public void shouldErrorWhenGettingTag() throws Exception {
        final GetTag.Callback mockGetTagCallback =
                mock(GetTag.Callback.class);
        final ErrorWrap mockError = mock(ErrorWrap.class);

        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ITagRepository.TagDetailsCallback) invocation.getArguments()[1])
                        .onError(
                                mockError);
                return null;
            }
        }).when(mMockTagRepository)
                .getTagById(anyLong(),
                        any(ITagRepository.TagDetailsCallback.class));

        mGetTag.execute(TEST_TAG_ID, mockGetTagCallback);
        mGetTag.run();

        verify(mMockPostExecutionThread).post(any(Runnable.class));
        verifyNoMoreInteractions(mockGetTagCallback);
        verifyZeroInteractions(mockError);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldGetTagNullParameter() throws Exception {
        mGetTag.execute(TEST_TAG_ID, null);
    }
}
