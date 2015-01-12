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

import com.ushahidi.android.model.TagModel;

import junit.framework.TestCase;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests {@link com.ushahidi.android.model.TagModel}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagModelTest extends TestCase {

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_DESCRIPTION = "dummy description";

    private static final String DUMMY_ICON = "fork";

    private static final int DUMMY_PRIORITY = 1;

    private static final Date DUMMY_DATE = new Date(1415718024);

    private static final long DUMMY_PARENT = 1;

    private static final String DUMMY_TAG = "tag";

    private static final String DUMMY_SLUG = "slug";

    private static final TagModel.Type DUMMY_TYPE = TagModel.Type.CATEGORY;

    private TagModel mTagModel;

    @Override
    public void setUp() throws Exception {
        mTagModel = new TagModel();
    }

    public void testCreateTagModel() throws Exception {
        mTagModel.setId(DUMMY_ID);
        mTagModel.setDescription(DUMMY_DESCRIPTION);
        mTagModel.setIcon(DUMMY_ICON);
        mTagModel.setPriority(DUMMY_PRIORITY);
        mTagModel.setCreated(DUMMY_DATE);
        mTagModel.setParentId(DUMMY_PARENT);
        mTagModel.setType(DUMMY_TYPE);
        mTagModel.setSlug(DUMMY_SLUG);
        mTagModel.setTag(DUMMY_TAG);

        assertThat(mTagModel).isInstanceOf(TagModel.class);
        assertThat(mTagModel.getId()).isEqualTo(DUMMY_ID);
        assertThat(mTagModel.getDescription()).isEqualTo(DUMMY_DESCRIPTION);
        assertThat(mTagModel.getIcon()).isEqualTo(DUMMY_ICON);
        assertThat(mTagModel.getCreated()).isEqualTo(DUMMY_DATE);
        assertThat(mTagModel.getParentId()).isEqualTo(DUMMY_PARENT);
        assertThat(mTagModel.getPriority()).isEqualTo(DUMMY_PRIORITY);
        assertThat(mTagModel.getType()).isEqualTo(DUMMY_TYPE);
        assertThat(mTagModel.getSlug()).isEqualTo(DUMMY_SLUG);
        assertThat(mTagModel.getTag()).isEqualTo(DUMMY_TAG);
    }
}
