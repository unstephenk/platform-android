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

import java.util.Date;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link TagEntity}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MediaEntityTest extends BaseTestCase {

    private MediaEntity mMediaEntity;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_CAPTION = "dummy description";

    private static final String DUMMY_ORIGINAL_URL = "fork";

    private static final String DUMMY_MIME = "mime";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415718034);

    private static final UserEntity DUMMY_USER = mock(UserEntity.class);


    @Before
    public void setUp() throws Exception {
        mMediaEntity = new MediaEntity();
    }

    @Test
    public void shouldCreateMediaEntity() throws Exception {
        mMediaEntity.setId(DUMMY_ID);
        mMediaEntity.setCaption(DUMMY_CAPTION);
        mMediaEntity.setOriginalFileUrl(DUMMY_ORIGINAL_URL);
        mMediaEntity.setCreated(DUMMY_CREATED);
        mMediaEntity.setUpdated(DUMMY_UPDATED);
        mMediaEntity.setMime(DUMMY_MIME);
        mMediaEntity.setUser(DUMMY_USER);

        assertThat(mMediaEntity, is(instanceOf(MediaEntity.class)));
        assertThat(mMediaEntity.getId(), is(DUMMY_ID));
        assertThat(mMediaEntity.getCaption(), is(DUMMY_CAPTION));
        assertThat(mMediaEntity.getUpdated(), is(DUMMY_UPDATED));
        assertThat(mMediaEntity.getMime(), is(DUMMY_MIME));
        assertThat(mMediaEntity.getOriginalFileUrl(), is(DUMMY_ORIGINAL_URL));
        assertThat(mMediaEntity.getUser(), is(DUMMY_USER));
    }
}
