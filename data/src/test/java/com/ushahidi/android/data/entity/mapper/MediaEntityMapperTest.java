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

import com.ushahidi.android.core.entity.Media;
import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.entity.MediaEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Tests {@link MediaEntityMapper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class MediaEntityMapperTest extends BaseTestCase {

    private MediaEntityMapper mMediaEntityMapper;

    private MediaEntity mMediaEntity;

    private Media mMedia;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_CAPTION = "dummy description";

    private static final String DUMMY_ORIGINAL_URL = "fork";

    private static final String DUMMY_MIME = "mime";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415718034);

    @Before
    public void setUp() throws Exception {
        mMediaEntityMapper = new MediaEntityMapper();
    }

    @Test
    public void shouldMapMediaEntityToMedia() throws Exception {
        mMediaEntity = new MediaEntity();
        mMediaEntity.setId(DUMMY_ID);
        mMediaEntity.setCaption(DUMMY_CAPTION);
        mMediaEntity.setOriginalFileUrl(DUMMY_ORIGINAL_URL);
        mMediaEntity.setCreated(DUMMY_CREATED);
        mMediaEntity.setUpdated(DUMMY_UPDATED);
        mMediaEntity.setMime(DUMMY_MIME);

        Media media = mMediaEntityMapper.map(mMediaEntity);

        assertThat(media, is(instanceOf(Media.class)));
        assertThat(media.getId(), is(DUMMY_ID));
        assertThat(media.getCaption(), is(DUMMY_CAPTION));
        assertThat(media.getOriginalFileUrl(), is(DUMMY_ORIGINAL_URL));
        assertThat(media.getCreated(), is(DUMMY_CREATED));
        assertThat(media.getUpdated(), is(DUMMY_UPDATED));
        assertThat(media.getMime(), is(DUMMY_MIME));
    }

    @Test
    public void shouldUnMapFromMediaToMediaEntity() throws Exception {
        mMedia = new Media();
        mMedia.setId(DUMMY_ID);
        mMedia.setCaption(DUMMY_CAPTION);
        mMedia.setOriginalFileUrl(DUMMY_ORIGINAL_URL);
        mMedia.setCreated(DUMMY_CREATED);
        mMedia.setUpdated(DUMMY_UPDATED);
        mMedia.setMime(DUMMY_MIME);

        MediaEntity mediaEntity = mMediaEntityMapper.unmap(mMedia);

        assertThat(mediaEntity, is(instanceOf(MediaEntity.class)));
        assertThat(mediaEntity.getId(), is(DUMMY_ID));
        assertThat(mediaEntity.getCaption(), is(DUMMY_CAPTION));
        assertThat(mediaEntity.getOriginalFileUrl(), is(DUMMY_ORIGINAL_URL));
        assertThat(mediaEntity.getCreated(), is(DUMMY_CREATED));
        assertThat(mediaEntity.getUpdated(), is(DUMMY_UPDATED));
        assertThat(mediaEntity.getMime(), is(DUMMY_MIME));
    }
}
