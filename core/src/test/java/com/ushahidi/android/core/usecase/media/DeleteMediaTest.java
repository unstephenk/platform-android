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
import com.ushahidi.android.core.respository.IMediaRepository;
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
 * Tests Delete Media use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeleteMediaTest {

    @Mock
    private ThreadExecutor mMockThreadExecutor;

    @Mock
    private PostExecutionThread mMockPostExecutionThread;

    @Mock
    private IMediaRepository mMockMediaRepository;

    @Mock
    private Media mMockMedia;

    private DeleteMedia mDeleteMedia;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mDeleteMedia = new DeleteMedia(mMockMediaRepository, mMockThreadExecutor,
                mMockPostExecutionThread);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldValidateDeleteMediaNullParameter() throws Exception {
        mDeleteMedia.execute(mMockMedia, null);
    }

    @Test
    public void shouldExecuteDeleteDeploymentRunMethod() throws Exception {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));
        DeleteMedia.Callback mockDeleteMediaCallback = mock(
                DeleteMedia.Callback.class);

        mDeleteMedia.execute(mMockMedia, mockDeleteMediaCallback);
        mDeleteMedia.run();

        verify(mMockMediaRepository).deleteMedia(any(Media.class),
                any(IMediaRepository.MediaDeletedCallback.class));

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockMediaRepository);
        verifyNoMoreInteractions(mMockPostExecutionThread);
    }

    @Test
    public void shouldRunDeleteDeploymentExecuteMethod() throws Exception {
        doNothing().when(mMockThreadExecutor).execute(any(IInteractor.class));

        DeleteMedia.Callback mockDeleteMediaCallback = mock(
                DeleteMedia.Callback.class);

        mDeleteMedia.execute(mMockMedia, mockDeleteMediaCallback);

        verify(mMockThreadExecutor).execute(any(IInteractor.class));
        verifyNoMoreInteractions(mMockThreadExecutor);
        verifyZeroInteractions(mMockMediaRepository);
        verifyZeroInteractions(mMockPostExecutionThread);
    }
}
