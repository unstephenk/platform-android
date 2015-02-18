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

package com.ushahidi.android.data.repository;

import com.ushahidi.android.core.entity.Media;
import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.database.IMediaDatabaseHelper;
import com.ushahidi.android.data.database.MediaDatabaseHelper;
import com.ushahidi.android.data.entity.MediaEntity;
import com.ushahidi.android.data.entity.mapper.MediaEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.data.validator.UrlValidator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests {@link com.ushahidi.android.data.database.MediaDatabaseHelper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class MediaDataRepositoryTest extends BaseTestCase {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private MediaDataRepository mMediaDataRepository;

    @Mock
    private MediaEntityMapper mMockMediaEntityMapper;

    @Mock
    private MediaEntity mMockMediaEntity;

    @Mock
    private Media mMockMedia;

    @Mock
    private MediaDatabaseHelper mMockMediaDatabaseHelper;

    @Mock
    private MediaDataRepository.MediaAddCallback mMockMediaAddCallback;

    @Mock
    private MediaDataRepository.MediaUpdateCallback mMockMediaUpdateCallback;

    @Mock
    private MediaDataRepository.MediaDeletedCallback mMockMediaDeletedCallback;

    @Mock
    private UrlValidator mMockUrlValidator;

    private Media mMedia;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_CAPTION = "dummy description";

    private static final String DUMMY_ORIGINAL_URL = "fork";

    private static final String DUMMY_MIME = "mime";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415718034);


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        clearSingleton(MediaDataRepository.class);
        mMediaDataRepository = MediaDataRepository
                .getInstance(mMockMediaDatabaseHelper, mMockMediaEntityMapper);
        mMedia = new Media();
        mMedia.setId(DUMMY_ID);
        mMedia.setCaption(DUMMY_CAPTION);
        mMedia.setOriginalFileUrl(DUMMY_ORIGINAL_URL);
        mMedia.setMime(DUMMY_MIME);
        mMedia.setCreated(DUMMY_CREATED);
        mMedia.setUpdated(DUMMY_UPDATED);

    }

    @Test
    public void shouldInvalidateConstructorsNullParameters() throws Exception {
        clearSingleton(MediaDataRepository.class);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid null parameter");
        mMediaDataRepository = MediaDataRepository.getInstance(null, null);
    }

    @Test
    public void shouldSuccessfullyAddAMedia() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IMediaDatabaseHelper.IMediaEntityPutCallback) invocation
                        .getArguments()[1]).onMediaEntityPut();
                return null;
            }
        }).when(mMockMediaDatabaseHelper).put(any(MediaEntity.class),
                any(IMediaDatabaseHelper.IMediaEntityPutCallback.class));

        given(mMockMediaEntityMapper.unmap(mMedia)).willReturn(mMockMediaEntity);

        mMediaDataRepository.addMedia(mMedia, mMockMediaAddCallback);

        verify(mMockMediaEntityMapper).unmap(mMedia);
        verify(mMockMediaAddCallback).onMediaAdded();
    }


    @Test
    public void shouldFailToAddAMedia() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IMediaDatabaseHelper.IMediaEntityPutCallback) invocation
                        .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockMediaDatabaseHelper).put(any(MediaEntity.class),
                any(IMediaDatabaseHelper.IMediaEntityPutCallback.class));

        mMediaDataRepository.addMedia(mMockMedia, mMockMediaAddCallback);

        verify(mMockMediaAddCallback, times(1)).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyTestForInvalidSlug() throws Exception {
        mMedia.setOriginalFileUrl(null);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IMediaDatabaseHelper.IMediaEntityPutCallback) invocation
                        .getArguments()[1]).onError(any(ValidationException.class));
                return null;
            }
        }).when(mMockMediaDatabaseHelper).put(any(MediaEntity.class),
                any(IMediaDatabaseHelper.IMediaEntityPutCallback.class));

        mMediaDataRepository.addMedia(mMedia, mMockMediaAddCallback);

        verify(mMockMediaAddCallback).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyTestForEmptyOrNullMedia() throws Exception {
        mMedia.setOriginalFileUrl(null);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IMediaDatabaseHelper.IMediaEntityPutCallback) invocation
                        .getArguments()[1]).onError(any(ValidationException.class));
                return null;
            }
        }).when(mMockMediaDatabaseHelper).put(any(MediaEntity.class),
                any(IMediaDatabaseHelper.IMediaEntityPutCallback.class));

        mMediaDataRepository.addMedia(mMedia, mMockMediaAddCallback);

        verify(mMockMediaAddCallback).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyUpdateAMedia() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IMediaDatabaseHelper.IMediaEntityPutCallback) invocation
                        .getArguments()[1]).onMediaEntityPut();
                return null;
            }
        }).when(mMockMediaDatabaseHelper).put(any(MediaEntity.class),
                any(IMediaDatabaseHelper.IMediaEntityPutCallback.class));

        given(mMockMediaEntityMapper.unmap(mMedia)).willReturn(mMockMediaEntity);

        mMediaDataRepository.updateMedia(mMedia, mMockMediaUpdateCallback);

        verify(mMockMediaEntityMapper).unmap(mMedia);
        verify(mMockMediaUpdateCallback).onMediaUpdated();
    }

    @Test
    public void shouldFailToUpdateAMedia() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IMediaDatabaseHelper.IMediaEntityPutCallback) invocation
                        .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockMediaDatabaseHelper).put(any(MediaEntity.class),
                any(IMediaDatabaseHelper.IMediaEntityPutCallback.class));

        mMediaDataRepository.updateMedia(mMockMedia, mMockMediaUpdateCallback);

        verify(mMockMediaUpdateCallback, times(1)).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyDeleteAMedia() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IMediaDatabaseHelper.IMediaEntityDeletedCallback) invocation
                        .getArguments()[1]).onMediaEntityDeleted();
                return null;
            }
        }).when(mMockMediaDatabaseHelper).delete(any(MediaEntity.class),
                any(IMediaDatabaseHelper.IMediaEntityDeletedCallback.class));

        given(mMockMediaEntityMapper.unmap(mMedia)).willReturn(mMockMediaEntity);

        mMediaDataRepository.deleteMedia(mMedia, mMockMediaDeletedCallback);

        verify(mMockMediaEntityMapper).unmap(mMedia);
        verify(mMockMediaDeletedCallback).onMediaDeleted();
    }

    @Test
    public void shouldFailToDeleteAMedia() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IMediaDatabaseHelper.IMediaEntityDeletedCallback) invocation
                        .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockMediaDatabaseHelper).delete(any(MediaEntity.class),
                any(IMediaDatabaseHelper.IMediaEntityDeletedCallback.class));

        mMediaDataRepository.deleteMedia(mMockMedia, mMockMediaDeletedCallback);

        verify(mMockMediaDeletedCallback).onError(any(RepositoryError.class));
    }

}
