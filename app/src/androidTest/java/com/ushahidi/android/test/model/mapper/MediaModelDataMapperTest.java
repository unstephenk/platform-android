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

import com.ushahidi.android.core.entity.Media;
import com.ushahidi.android.model.MediaModel;
import com.ushahidi.android.model.mapper.MediaModelDataMapper;
import com.ushahidi.android.test.CustomAndroidTestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link com.ushahidi.android.model.mapper.MediaModelDataMapper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MediaModelDataMapperTest extends CustomAndroidTestCase {

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_CAPTION = "dummy description";

    private static final String DUMMY_ORIGINAL_URL = "fork";

    private static final String DUMMY_MIME = "mime";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415718034);

    private MediaModelDataMapper mMediaModelDataMapper;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mMediaModelDataMapper = new MediaModelDataMapper();
    }

    public void testMediaMap() throws Exception {
        Media media = new Media();
        media.setId(DUMMY_ID);
        media.setCaption(DUMMY_CAPTION);
        media.setOriginalFileUrl(DUMMY_ORIGINAL_URL);
        media.setCreated(DUMMY_CREATED);
        media.setUpdated(DUMMY_UPDATED);
        media.setMime(DUMMY_MIME);

        MediaModel mediaModel = mMediaModelDataMapper.map(media);

        assertThat(mediaModel).isInstanceOf(MediaModel.class);
        assertThat(mediaModel.getId()).isEqualTo(DUMMY_ID);
        assertThat(mediaModel.getCaption()).isEqualTo(DUMMY_CAPTION);
        assertThat(mediaModel.getOriginalFileUrl()).isEqualTo(DUMMY_ORIGINAL_URL);
        assertThat(mediaModel.getCreated()).isEqualTo(DUMMY_CREATED);
        assertThat(mediaModel.getUpdated()).isEqualTo(DUMMY_UPDATED);
        assertThat(mediaModel.getMime()).isEqualTo(DUMMY_MIME);
    }

    public void testMediaUnMap() throws Exception {
        MediaModel mediaModel = new MediaModel();
        mediaModel.setId(DUMMY_ID);
        mediaModel.setCaption(DUMMY_CAPTION);
        mediaModel.setOriginalFileUrl(DUMMY_ORIGINAL_URL);
        mediaModel.setCreated(DUMMY_CREATED);
        mediaModel.setUpdated(DUMMY_UPDATED);
        mediaModel.setMime(DUMMY_MIME);

        Media media = mMediaModelDataMapper.unmap(mediaModel);

        assertThat(media).isInstanceOf(Media.class);
        assertThat(media.getId()).isEqualTo(DUMMY_ID);
        assertThat(media.getCaption()).isEqualTo(DUMMY_CAPTION);
        assertThat(media.getOriginalFileUrl()).isEqualTo(DUMMY_ORIGINAL_URL);
        assertThat(media.getCreated()).isEqualTo(DUMMY_CREATED);
        assertThat(media.getUpdated()).isEqualTo(DUMMY_UPDATED);
        assertThat(media.getMime()).isEqualTo(DUMMY_MIME);
    }

    public void testMediaListMap() {
        Media mockMediaOne = mock(Media.class);
        Media mockMediaTwo = mock(Media.class);

        List<Media> mediaList = new ArrayList<>();
        mediaList.add(mockMediaOne);
        mediaList.add(mockMediaTwo);

        List<MediaModel> mediaModelList = mMediaModelDataMapper.map(mediaList);

        assertThat(mediaModelList.get(0)).isInstanceOf(MediaModel.class);
        assertThat(mediaModelList.get(1)).isInstanceOf(MediaModel.class);
        assertThat(mediaModelList.size()).isEqualTo(2);
    }
}
