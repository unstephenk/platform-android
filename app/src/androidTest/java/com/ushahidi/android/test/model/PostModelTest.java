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

package com.ushahidi.android.test.model;

import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.model.PostModel;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link com.ushahidi.android.model.PostModel}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostModelTest extends TestCase {

    private PostModel mPostModel;

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


    @Override
    public void setUp() throws Exception {
        mPostModel = new PostModel();
    }

    public void testCreatePostModel() throws Exception {
        mPostModel.setId(DUMMY_ID);
        mPostModel.setUser(DUMMY_USER);
        mPostModel.setContent(DUMMY_CONTENT);
        mPostModel.setSlug(DUMMY_SLUG);
        mPostModel.setAuthorEmail(DUMMY_ARTHUR_EMAIL);
        mPostModel.setAuthorRealname(DUMMY_REAL_NAME);
        mPostModel.setStatus(DUMMY_STATUS);
        mPostModel.setCreated(DUMMY_CREATED);
        mPostModel.setUpdated(DUMMY_UPDATED);
        mPostModel.setParent(DUMMY_PARENT);
        mPostModel.setType(DUMMY_TYPE);
        mPostModel.setTags(DUMMY_TAGS);
        mPostModel.setTitle(DUMMY_POST_TITLE);

        assertThat(mPostModel).isInstanceOf(PostModel.class);
        assertThat(mPostModel.getId()).isEqualTo(DUMMY_ID);
        assertThat(mPostModel.getUser()).isEqualTo(DUMMY_USER);
        assertThat(mPostModel.getContent()).isEqualTo(DUMMY_CONTENT);
        assertThat(mPostModel.getUpdated()).isEqualTo(DUMMY_UPDATED);
        assertThat(mPostModel.getSlug()).isEqualTo(DUMMY_SLUG);
        assertThat(mPostModel.getAuthorEmail()).isEqualTo(DUMMY_ARTHUR_EMAIL);
        assertThat(mPostModel.getAuthorRealname()).isEqualTo(DUMMY_REAL_NAME);
        assertThat(mPostModel.getType()).isEqualTo(DUMMY_TYPE);
        assertThat(mPostModel.getTags()).isEqualTo(DUMMY_TAGS);
        assertThat(mPostModel.getTitle()).isEqualTo(DUMMY_POST_TITLE);
    }
}
