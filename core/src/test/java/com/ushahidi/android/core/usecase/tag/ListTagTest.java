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

import com.ushahidi.android.core.repository.ITagRepository;
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
 * Tests List Tag use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ListTagTest {

    @Mock
    private ThreadExecutor mMockThreadExecutor;

    @Mock
    private PostExecutionThread mPostExecutionThread;

    @Mock
    private ITagRepository mMockTagRepository;

    private ListTag mListTag;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mListTag = new ListTag(mMockThreadExecutor,
                mPostExecutionThread);
        mListTag.setTagRepository(mMockTagRepository);

    }

    @Test
    public void testListTagExecution() {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));

        ListTag.Callback mockListTagCallback = mock(
                ListTag.Callback.class);

        mListTag.execute(mockListTagCallback);

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockThreadExecutor);
        verifyZeroInteractions(mMockTagRepository);
        verifyZeroInteractions(mPostExecutionThread);
    }

    @Test
    public void testListTagRun() {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        ListTag.Callback mockListTagCallback = mock(
                ListTag.Callback.class);
        doNothing().when(mMockTagRepository).getTagList(
                any(ITagRepository.TagListCallback.class));

        mListTag.execute(mockListTagCallback);
        mListTag.run();

        verify(mMockTagRepository).getTagList(
                any(ITagRepository.TagListCallback.class));

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockTagRepository);
        verifyNoMoreInteractions(mPostExecutionThread);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListTagNullParameter() {
        mListTag.execute(null);
    }
}
