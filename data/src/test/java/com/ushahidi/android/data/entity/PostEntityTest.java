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

package com.ushahidi.android.data.entity;

import com.ushahidi.android.data.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link PostEntity}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class PostEntityTest extends BaseTestCase {

    private PostEntity mPostEntity;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_CONTENT = "dummy description";

    private static final String DUMMY_SLUG = "slug";

    private static final String DUMMY_ARTHUR_EMAIL = "email@example.com";

    private static final String DUMMY_REAL_NAME = "dude cash";

    private static final String DUMMY_STATUS = "published";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415718024);

    private static final Long DUMMY_PARENT = 1l;

    private static final String DUMMY_TYPE = "report";

    private static final List<TagEntity> DUMMY_TAGS = new ArrayList<>();

    private static final String DUMMY_POST_TITLE = "post title";


    @Before
    public void setUp() throws Exception {
        mPostEntity = new PostEntity();
    }

    @Test
    public void shouldCreatePostEntity() throws Exception {
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
        mPostEntity.setTags(DUMMY_TAGS);
        mPostEntity.setTitle(DUMMY_POST_TITLE);

        assertThat(mPostEntity, is(instanceOf(PostEntity.class)));
        assertThat(mPostEntity.getId(), is(DUMMY_ID));
        assertThat(mPostEntity.getContent(), is(DUMMY_CONTENT));
        assertThat(mPostEntity.getUpdated(), is(DUMMY_UPDATED));
        assertThat(mPostEntity.getSlug(), is(DUMMY_SLUG));
        assertThat(mPostEntity.getAuthorEmail(), is(DUMMY_ARTHUR_EMAIL));
        assertThat(mPostEntity.getAuthorRealname(), is(DUMMY_REAL_NAME));
        assertThat(mPostEntity.getType(), is(DUMMY_TYPE));
        assertThat(mPostEntity.getTags(), is(DUMMY_TAGS));
        assertThat(mPostEntity.getTitle(), is(DUMMY_POST_TITLE));
    }
}
