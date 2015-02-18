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

package com.ushahidi.android.test.model.mapper;

import com.ushahidi.android.core.entity.Post;
import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.model.PostModel;
import com.ushahidi.android.model.mapper.PostModelDataMapper;
import com.ushahidi.android.test.CustomAndroidTestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link com.ushahidi.android.model.mapper.PostModelDataMapper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostModelDataMapperTest extends CustomAndroidTestCase {

    private static final long DUMMY_ID = 1;

    private static final User DUMMY_USER = new User();

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

    private static final String DUMMY_POST_TITLE = "post title";

    private PostModelDataMapper mPostModelDataMapper;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mPostModelDataMapper = new PostModelDataMapper();
    }

    public void testPostMap() throws Exception {
        Post post = new Post();
        post.setId(DUMMY_ID);
        post.setContent(DUMMY_CONTENT);
        post.setSlug(DUMMY_SLUG);
        post.setAuthorEmail(DUMMY_ARTHUR_EMAIL);
        post.setAuthorRealname(DUMMY_REAL_NAME);
        post.setStatus(DUMMY_STATUS);
        post.setCreated(DUMMY_CREATED);
        post.setUpdated(DUMMY_UPDATED);
        post.setParent(DUMMY_PARENT);
        post.setType(DUMMY_TYPE);
        post.setTags(DUMMY_TAGS);
        post.setTitle(DUMMY_POST_TITLE);

        PostModel postModel = mPostModelDataMapper.map(post);

        assertThat(postModel).isInstanceOf(PostModel.class);
        assertThat(postModel.getId()).isEqualTo(DUMMY_ID);
        assertThat(postModel.getContent()).isEqualTo(DUMMY_CONTENT);
        assertThat(postModel.getUpdated()).isEqualTo(DUMMY_UPDATED);
        assertThat(postModel.getSlug()).isEqualTo(DUMMY_SLUG);
        assertThat(postModel.getAuthorEmail()).isEqualTo(DUMMY_ARTHUR_EMAIL);
        assertThat(postModel.getAuthorRealname()).isEqualTo(DUMMY_REAL_NAME);
        assertThat(postModel.getType()).isEqualTo(DUMMY_TYPE);
        assertThat(postModel.getTags()).isEqualTo(DUMMY_TAGS);
        assertThat(postModel.getTitle()).isEqualTo(DUMMY_POST_TITLE);
    }

    public void testPostUnMap() throws Exception {

        PostModel postModel = new PostModel();
        postModel.setId(DUMMY_ID);
        postModel.setContent(DUMMY_CONTENT);
        postModel.setSlug(DUMMY_SLUG);
        postModel.setAuthorEmail(DUMMY_ARTHUR_EMAIL);
        postModel.setAuthorRealname(DUMMY_REAL_NAME);
        postModel.setStatus(DUMMY_STATUS);
        postModel.setCreated(DUMMY_CREATED);
        postModel.setUpdated(DUMMY_UPDATED);
        postModel.setParent(DUMMY_PARENT);
        postModel.setType(DUMMY_TYPE);
        postModel.setTitle(DUMMY_POST_TITLE);

        Post post = mPostModelDataMapper.unmap(postModel);

        assertThat(post).isInstanceOf(Post.class);
        assertThat(post.getId()).isEqualTo(DUMMY_ID);
        assertThat(post.getContent()).isEqualTo(DUMMY_CONTENT);
        assertThat(post.getUpdated()).isEqualTo(DUMMY_UPDATED);
        assertThat(post.getSlug()).isEqualTo(DUMMY_SLUG);
        assertThat(post.getAuthorEmail()).isEqualTo(DUMMY_ARTHUR_EMAIL);
        assertThat(post.getAuthorRealname()).isEqualTo(DUMMY_REAL_NAME);
        assertThat(post.getType()).isEqualTo(DUMMY_TYPE);
        assertThat(post.getTags()).isEqualTo(DUMMY_TAGS);
        assertThat(post.getTitle()).isEqualTo(DUMMY_POST_TITLE);

    }

    public void testPostListMap() {
        Post mockPostOne = mock(Post.class);
        Post mockPostTwo = mock(Post.class);

        List<Post> postList = new ArrayList<>();
        postList.add(mockPostOne);
        postList.add(mockPostTwo);

        List<PostModel> postModelList = mPostModelDataMapper.map(postList);

        assertThat(postModelList.get(0)).isInstanceOf(PostModel.class);
        assertThat(postModelList.get(1)).isInstanceOf(PostModel.class);
        assertThat(postModelList.size()).isEqualTo(2);
    }

}
