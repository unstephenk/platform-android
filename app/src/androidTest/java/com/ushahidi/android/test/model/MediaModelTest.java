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

import com.ushahidi.android.model.MediaModel;

import junit.framework.TestCase;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests {@link com.ushahidi.android.model.TagModel}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MediaModelTest extends TestCase {

    private MediaModel mMediaModel;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_CAPTION = "dummy description";

    private static final String DUMMY_ORIGINAL_URL = "fork";

    private static final String DUMMY_MIME = "mime";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415718034);


    @Override
    public void setUp() throws Exception {
        mMediaModel = new MediaModel();
    }

    public void testCreateMediaModel() throws Exception {
        mMediaModel.setId(DUMMY_ID);
        mMediaModel.setCaption(DUMMY_CAPTION);
        mMediaModel.setOriginalFileUrl(DUMMY_ORIGINAL_URL);
        mMediaModel.setCreated(DUMMY_CREATED);
        mMediaModel.setUpdated(DUMMY_UPDATED);
        mMediaModel.setMime(DUMMY_MIME);

        assertThat(mMediaModel).isInstanceOf(MediaModel.class);
        assertThat(mMediaModel.getId()).isEqualTo(DUMMY_ID);
        assertThat(mMediaModel.getCaption()).isEqualTo(DUMMY_CAPTION);
        assertThat(mMediaModel.getUpdated()).isEqualTo(DUMMY_UPDATED);
        assertThat(mMediaModel.getMime()).isEqualTo(DUMMY_MIME);
        assertThat(mMediaModel.getOriginalFileUrl()).isEqualTo(DUMMY_ORIGINAL_URL);
    }
}
