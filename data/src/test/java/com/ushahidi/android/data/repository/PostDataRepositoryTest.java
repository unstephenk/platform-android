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

import com.ushahidi.android.core.entity.Post;
import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.database.IPostDatabaseHelper;
import com.ushahidi.android.data.entity.PostEntity;
import com.ushahidi.android.data.entity.mapper.PostEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.data.repository.datasource.post.PostDataSource;
import com.ushahidi.android.data.repository.datasource.post.PostDataSourceFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests {@link com.ushahidi.android.data.database.PostDatabaseHelper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostDataRepositoryTest extends BaseTestCase {

    private static final long DUMMY_ID = 1;

    private static final User DUMMY_USER = mock(User.class);

    private static final String DUMMY_CONTENT = "dummy description";

    private static final String DUMMY_SLUG = "slug";

    private static final String DUMMY_ARTHUR_EMAIL = "email@example.com";

    private static final String DUMMY_REAL_NAME = "dude cash";

    private static final String DUMMY_STATUS = "published";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415718024);

    private static final Long DUMMY_PARENT = 1l;

    private static final String DUMMY_TYPE = "report";

    private static final String DUMMY_POST_TITLE = "post title";

    private static final List<Tag> DUMMY_TAGS = new ArrayList<>();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PostDataRepository mPostDataRepository;

    @Mock
    private PostEntityMapper mMockPostEntityMapper;

    @Mock
    private PostEntity mMockPostEntity;

    @Mock
    private Post mMockPost;

    @Mock
    private PostDataSourceFactory mPostDataSourceFactory;

    @Mock
    private PostDataRepository.PostAddCallback mMockPostAddCallback;

    @Mock
    private PostDataSource mMockPostDataSource;

    @Mock
    private PostDataRepository.PostDeletedCallback mMockPostDeletedCallback;

    private Post mPost;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        clearSingleton(PostDataRepository.class);
        mPostDataRepository = PostDataRepository
                .getInstance(mPostDataSourceFactory, mMockPostEntityMapper);

        given(mPostDataSourceFactory.createPostApiDataSource()).willReturn(mMockPostDataSource);
        given(mPostDataSourceFactory.createPostDatabaseDataSource())
                .willReturn(mMockPostDataSource);

        mPost = new Post();
        mPost.setId(DUMMY_ID);
        mPost.setUser(DUMMY_USER);
        mPost.setContent(DUMMY_CONTENT);
        mPost.setSlug(DUMMY_SLUG);
        mPost.setAuthorEmail(DUMMY_ARTHUR_EMAIL);
        mPost.setAuthorRealname(DUMMY_REAL_NAME);
        mPost.setStatus(DUMMY_STATUS);
        mPost.setCreated(DUMMY_CREATED);
        mPost.setUpdated(DUMMY_UPDATED);
        mPost.setParent(DUMMY_PARENT);
        mPost.setType(DUMMY_TYPE);
        mPost.setTags(DUMMY_TAGS);
        mPost.setTitle(DUMMY_POST_TITLE);

    }

    @Test
    public void shouldInvalidateConstructorsNullParameters() throws Exception {
        clearSingleton(PostDataRepository.class);
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("PostDataSourceFactory cannot be null.");
        mPostDataRepository = PostDataRepository.getInstance(null, null);
    }

    @Test
    public void shouldSuccessfullyAddAPost() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((PostDataSource.PostEntityAddCallback) invocation
                        .getArguments()[1]).onPostEntityAdded();
                return null;
            }
        }).when(mMockPostDataSource).putPostEntity(any(PostEntity.class),
                any(PostDataSource.PostEntityAddCallback.class));
        given(mMockPostEntityMapper.unmap(mPost)).willReturn(mMockPostEntity);

        mPostDataRepository.putPost(mPost, mMockPostAddCallback);

        verify(mMockPostEntityMapper).unmap(mPost);
        verify(mMockPostAddCallback).onPostAdded();
    }


    @Test
    public void shouldFailToAddAPost() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((PostDataSource.PostEntityAddCallback) invocation
                        .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockPostDataSource).putPostEntity(any(PostEntity.class),
                any(PostDataSource.PostEntityAddCallback.class));

        mPostDataRepository.putPost(mMockPost, mMockPostAddCallback);

        verify(mMockPostAddCallback, times(2)).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyTestForInvalidPostContent() throws Exception {
        mPost.setContent(null);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((PostDataSource.PostEntityAddCallback) invocation
                        .getArguments()[1]).onError(any(ValidationException.class));
                return null;
            }
        }).when(mMockPostDataSource).putPostEntity(any(PostEntity.class),
                any(PostDataSource.PostEntityAddCallback.class));

        mPostDataRepository.putPost(mPost, mMockPostAddCallback);

        verify(mMockPostAddCallback).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyTestForEmptyOrNullPostTitle() throws Exception {
        mPost.setTitle(null);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IPostDatabaseHelper.IPostEntityPutCallback) invocation
                        .getArguments()[1]).onError(any(ValidationException.class));
                return null;
            }
        }).when(mMockPostDataSource).putPostEntity(any(PostEntity.class),
                any(PostDataSource.PostEntityAddCallback.class));

        mPostDataRepository.putPost(mPost, mMockPostAddCallback);

        verify(mMockPostAddCallback).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyDeleteAPost() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((PostDataSource.PostEntityDeletedCallback) invocation
                        .getArguments()[1]).onPostEntityDeleted();
                return null;
            }
        }).when(mMockPostDataSource).deletePostEntity(any(PostEntity.class),
                any(PostDataSource.PostEntityDeletedCallback.class));

        given(mMockPostEntityMapper.unmap(mPost)).willReturn(mMockPostEntity);

        mPostDataRepository.deletePost(mPost, mMockPostDeletedCallback);

        verify(mMockPostEntityMapper).unmap(mPost);
        verify(mMockPostDeletedCallback).onPostDeleted();
    }

    @Test
    public void shouldFailToDeleteAPost() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((PostDataSource.PostEntityDeletedCallback) invocation
                        .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockPostDataSource).deletePostEntity(any(PostEntity.class),
                any(PostDataSource.PostEntityDeletedCallback.class));

        mPostDataRepository.deletePost(mMockPost, mMockPostDeletedCallback);

        verify(mMockPostDeletedCallback).onError(any(RepositoryError.class));
    }

}
