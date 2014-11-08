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
import com.ushahidi.android.core.respository.ITagRepository;
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
 * Tests Delete Tag use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeleteTagTest {

    @Mock
    private ThreadExecutor mMockThreadExecutor;

    @Mock
    private PostExecutionThread mMockPostExecutionThread;

    @Mock
    private ITagRepository mMockTagRepository;

    @Mock
    private Tag mMockTag;

    private DeleteTag mDeleteTag;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mDeleteTag = new DeleteTag(mMockTagRepository, mMockThreadExecutor,
                mMockPostExecutionThread);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldValidateDeleteTagNullParameter() throws Exception {
        mDeleteTag.execute(mMockTag, null);
    }

    @Test
    public void shouldExecuteDeleteTagRunMethod() throws Exception {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        DeleteTag.Callback mockDeleteTagCallback = mock(
                DeleteTag.Callback.class);
        doNothing().when(mMockTagRepository).updateTag(any(Tag.class),
                any(ITagRepository.TagUpdateCallback.class));

        mDeleteTag.execute(mMockTag, mockDeleteTagCallback);
        mDeleteTag.run();

        verify(mMockTagRepository).deleteTag(any(Tag.class),
                any(ITagRepository.TagDeletedCallback.class));

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockTagRepository);
        verifyNoMoreInteractions(mMockPostExecutionThread);
    }

    @Test
    public void shouldRunDeleteTagExecuteMethod() throws Exception {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));

        DeleteTag.Callback mockDeleteTagCallback = mock(
                DeleteTag.Callback.class);

        mDeleteTag.execute(mMockTag, mockDeleteTagCallback);

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockThreadExecutor);
        verifyZeroInteractions(mMockTagRepository);
        verifyZeroInteractions(mMockPostExecutionThread);
    }
}
