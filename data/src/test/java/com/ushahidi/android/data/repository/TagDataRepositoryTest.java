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

import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.database.TagDatabaseHelper;
import com.ushahidi.android.data.database.ITagDatabaseHelper;
import com.ushahidi.android.data.entity.TagEntity;
import com.ushahidi.android.data.entity.mapper.TagEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.validator.UrlValidator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests {@link com.ushahidi.android.data.database.TagDatabaseHelper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagDataRepositoryTest extends BaseTestCase {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private TagDataRepository mTagDataRepository;

    @Mock
    private TagEntityMapper mMockTagEntityMapper;

    @Mock
    private TagEntity mMockTagEntity;

    @Mock
    private Tag mMockTag;

    @Mock
    private TagDatabaseHelper mMockTagDatabaseHelper;

    @Mock
    private TagDataRepository.TagAddCallback mMockTagAddCallback;

    @Mock
    private TagDataRepository.TagUpdateCallback mMockTagUpdateCallback;

    @Mock
    private TagDataRepository.TagDeletedCallback mMockTagDeletedCallback;

    @Mock
    private UrlValidator mMockUrlValidator;

    private Tag mTag;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_DESCRIPTION = "dummy description";

    private static final String DUMMY_ICON = "fork";

    private static final int DUMMY_PRIORITY = 1;

    private static final Date DUMMY_DATE = new Date(1415718024);

    private static final long DUMMY_PARENT = 1;

    private static final String DUMMY_TAG = "tag";

    private static final String DUMMY_SLUG ="slug";

    private static final Tag.Type DUMMY_TAG_TYPE = Tag.Type.CATEGORY;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        clearSingleton(TagDataRepository.class);
        mTagDataRepository = TagDataRepository
                .getInstance(mMockTagDatabaseHelper, mMockTagEntityMapper,
                        mMockUrlValidator);
        mTag = new Tag();
        mTag.setId(DUMMY_ID);
        mTag.setTag(DUMMY_TAG);
        mTag.setDescription(DUMMY_DESCRIPTION);
        mTag.setIcon(DUMMY_ICON);
        mTag.setPriority(DUMMY_PRIORITY);
        mTag.setCreated(DUMMY_DATE);
        mTag.setParentId(DUMMY_PARENT);
        mTag.setSlug(DUMMY_SLUG);
        mTag.setType(DUMMY_TAG_TYPE);

    }

    @Test
    public void shouldInvalidateConstructorsNullParameters() throws Exception {
        clearSingleton(TagDataRepository.class);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid null parameter");
        mTagDataRepository = TagDataRepository.getInstance(null, null, null);
    }

    @Test
    public void shouldSuccessfullyAddATag() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ITagDatabaseHelper.ITagEntityPutCallback) invocation
                        .getArguments()[1]).onTagEntityPut();
                return null;
            }
        }).when(mMockTagDatabaseHelper).put(any(TagEntity.class),
                any(ITagDatabaseHelper.ITagEntityPutCallback.class));

        given(mMockTagEntityMapper.unmap(mTag)).willReturn(mMockTagEntity);

        mTagDataRepository.addTag(mTag, mMockTagAddCallback);

        verify(mMockTagEntityMapper).unmap(mTag);
        verify(mMockTagAddCallback).onTagAdded();
    }


    @Test
    public void shouldFailToAddATag() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ITagDatabaseHelper.ITagEntityPutCallback) invocation
                        .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockTagDatabaseHelper).put(any(TagEntity.class),
                any(ITagDatabaseHelper.ITagEntityPutCallback.class));

        mTagDataRepository.addTag(mMockTag, mMockTagAddCallback);

        verify(mMockTagAddCallback, times(2)).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyTestForInvalidSlug() throws Exception {
        mTag.setSlug(null);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ITagDatabaseHelper.ITagEntityPutCallback) invocation
                        .getArguments()[1]).onError(any(ValidationException.class));
                return null;
            }
        }).when(mMockTagDatabaseHelper).put(any(TagEntity.class),
                any(ITagDatabaseHelper.ITagEntityPutCallback.class));

        mTagDataRepository.addTag(mTag, mMockTagAddCallback);

        verify(mMockTagAddCallback).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyTestForEmptyOrNullTag() throws Exception {
        mTag.setTag(null);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ITagDatabaseHelper.ITagEntityPutCallback) invocation
                        .getArguments()[1]).onError(any(ValidationException.class));
                return null;
            }
        }).when(mMockTagDatabaseHelper).put(any(TagEntity.class),
                any(ITagDatabaseHelper.ITagEntityPutCallback.class));

        mTagDataRepository.addTag(mTag, mMockTagAddCallback);

        verify(mMockTagAddCallback).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyUpdateATag() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ITagDatabaseHelper.ITagEntityPutCallback) invocation
                        .getArguments()[1]).onTagEntityPut();
                return null;
            }
        }).when(mMockTagDatabaseHelper).put(any(TagEntity.class),
                any(ITagDatabaseHelper.ITagEntityPutCallback.class));

        given(mMockTagEntityMapper.unmap(mTag)).willReturn(mMockTagEntity);

        mTagDataRepository.updateTag(mTag, mMockTagUpdateCallback);

        verify(mMockTagEntityMapper).unmap(mTag);
        verify(mMockTagUpdateCallback).onTagUpdated();
    }

    @Test
    public void shouldFailToUpdateATag() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ITagDatabaseHelper.ITagEntityPutCallback) invocation
                        .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockTagDatabaseHelper).put(any(TagEntity.class),
                any(ITagDatabaseHelper.ITagEntityPutCallback.class));

        mTagDataRepository.updateTag(mMockTag, mMockTagUpdateCallback);

        verify(mMockTagUpdateCallback, times(2)).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyDeleteATag() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ITagDatabaseHelper.ITagEntityDeletedCallback) invocation
                        .getArguments()[1]).onTagEntityDeleted();
                return null;
            }
        }).when(mMockTagDatabaseHelper).delete(any(TagEntity.class),
                any(ITagDatabaseHelper.ITagEntityDeletedCallback.class));

        given(mMockTagEntityMapper.unmap(mTag)).willReturn(mMockTagEntity);

        mTagDataRepository.deleteTag(mTag, mMockTagDeletedCallback);

        verify(mMockTagEntityMapper).unmap(mTag);
        verify(mMockTagDeletedCallback).onTagDeleted();
    }

    @Test
    public void shouldFailToDeleteATag() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ITagDatabaseHelper.ITagEntityDeletedCallback) invocation
                        .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockTagDatabaseHelper).delete(any(TagEntity.class),
                any(ITagDatabaseHelper.ITagEntityDeletedCallback.class));

        mTagDataRepository.deleteTag(mMockTag, mMockTagDeletedCallback);

        verify(mMockTagDeletedCallback).onError(any(RepositoryError.class));
    }

}
