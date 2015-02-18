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

package com.ushahidi.android.data.entity.mapper;

import com.ushahidi.android.core.entity.Post;
import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.entity.PostEntity;
import com.ushahidi.android.data.entity.TagEntity;
import com.ushahidi.android.data.entity.UserEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link PostEntityMapper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class PostEntityMapperTest extends BaseTestCase {

    private static final long DUMMY_ID = 1;

    private static final User DUMMY_USER = new User();

    private static final UserEntity DUMMY_USER_ENTITY = new UserEntity();

    private static final String DUMMY_CONTENT = "dummy description";

    private static final String DUMMY_SLUG = "slug";

    private static final String DUMMY_ARTHUR_EMAIL = "email@example.com";

    private static final String DUMMY_REAL_NAME = "dude cash";

    private static final String DUMMY_STATUS = "published";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415718024);

    private static final Long DUMMY_PARENT = 1l;

    private static final String DUMMY_TYPE = "report";

    private static final List<Tag> DUMMY_TAGS = new ArrayList<>();

    private static final List<TagEntity> DUMMY_TAG_ENTITIES = new ArrayList<>();

    private static final String DUMMY_POST_TITLE = "post title";



    private PostEntityMapper mPostEntityMapper;

    private PostEntity mPostEntity;

    private Post mPost;

    @Before
    public void setUp() throws Exception {
        mPostEntityMapper = new PostEntityMapper();
        DUMMY_USER.setId(DUMMY_ID);
        DUMMY_USER.setCreated(DUMMY_CREATED);
        DUMMY_USER.setUpdated(DUMMY_UPDATED);
        DUMMY_USER.setEmail(DUMMY_ARTHUR_EMAIL);
        DUMMY_USER.setUsername("dudebro");
        DUMMY_USER.setRealName(DUMMY_REAL_NAME);
        DUMMY_USER.setRole(User.Role.USER);

        DUMMY_USER_ENTITY.setId(DUMMY_ID);
        DUMMY_USER_ENTITY.setRole(UserEntity.Role.USER);
        DUMMY_USER_ENTITY.setCreated(new com.ushahidi.android.data.api.Date(DUMMY_CREATED));
        DUMMY_USER_ENTITY.setUpdated(new com.ushahidi.android.data.api.Date(DUMMY_UPDATED));
        DUMMY_USER_ENTITY.setEmail(DUMMY_ARTHUR_EMAIL);
        DUMMY_USER_ENTITY.setUsername("dudebro");
        DUMMY_USER_ENTITY.setRealName(DUMMY_REAL_NAME);
    }

    @Test
    public void shouldMapPostEntityToPost() throws Exception {
        mPostEntity = new PostEntity();
        mPostEntity.setId(DUMMY_ID);
        mPostEntity.setContent(DUMMY_CONTENT);
        mPostEntity.setSlug(DUMMY_SLUG);
        mPostEntity.setAuthorEmail(DUMMY_ARTHUR_EMAIL);
        mPostEntity.setAuthorRealname(DUMMY_REAL_NAME);
        mPostEntity.setStatus(DUMMY_STATUS);
        mPostEntity.setCreated(DUMMY_CREATED);
        mPostEntity.setUpdated(DUMMY_UPDATED);
        mPostEntity.setParent(DUMMY_PARENT);
        mPostEntity.setType(DUMMY_TYPE);
        mPostEntity.setTags(DUMMY_TAG_ENTITIES);
        mPostEntity.setTitle(DUMMY_POST_TITLE);

        Post post = mPostEntityMapper.map(mPostEntity);

        assertThat(post, is(instanceOf(Post.class)));
        assertThat(post.getId(), is(DUMMY_ID));
        assertThat(post.getContent(), is(DUMMY_CONTENT));
        assertThat(post.getUpdated(), is(DUMMY_UPDATED));
        assertThat(post.getSlug(), is(DUMMY_SLUG));
        assertThat(post.getAuthorEmail(), is(DUMMY_ARTHUR_EMAIL));
        assertThat(post.getAuthorRealname(), is(DUMMY_REAL_NAME));
        assertThat(post.getType(), is(DUMMY_TYPE));
        assertThat(post.getTags(), is(DUMMY_TAGS));
        assertThat(post.getTitle(), is(DUMMY_POST_TITLE));
    }

    @Test
    public void shouldUnMapFromPostToPostEntity() throws Exception {
        mPost = new Post();
        mPost.setId(DUMMY_ID);
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

        PostEntity postEntity = mPostEntityMapper.unmap(mPost);

        assertThat(postEntity, is(instanceOf(PostEntity.class)));
        assertThat(postEntity.getId(), is(DUMMY_ID));
        assertThat(postEntity.getContent(), is(DUMMY_CONTENT));
        assertThat(postEntity.getUpdated(), is(DUMMY_UPDATED));
        assertThat(postEntity.getSlug(), is(DUMMY_SLUG));
        assertThat(postEntity.getAuthorEmail(), is(DUMMY_ARTHUR_EMAIL));
        assertThat(postEntity.getAuthorRealname(), is(DUMMY_REAL_NAME));
        assertThat(postEntity.getType(), is(DUMMY_TYPE));
        assertThat(postEntity.getTags(), is(DUMMY_TAG_ENTITIES));
        assertThat(postEntity.getTitle(), is(DUMMY_POST_TITLE));
    }
}
