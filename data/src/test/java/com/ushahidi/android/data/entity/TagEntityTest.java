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

/**
 * Tests {@link com.ushahidi.android.data.entity.TagEntity}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagEntityTest extends BaseTestCase {

    private TagEntity mTagEntity;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_DESCRIPTION = "dummy description";

    private static final String DUMMY_ICON = "fork";

    private static final int DUMMY_PRIORITY = 1;

    private static final Date DUMMY_DATE = new Date(1415718024);

    private static final long DUMMY_PARENT = 1;

    private static final String DUMMY_TAG = "tag";

    private static final String DUMMY_SLUG = "slug";

    private static final TagEntity.Type DUMMY_TYPE = TagEntity.Type.CATEGORY;

    @Before
    public void setUp() throws Exception {
        mTagEntity = new TagEntity();
    }

    @Test
    public void shouldCreateTagEntity() throws Exception {
        mTagEntity.setId(DUMMY_ID);
        mTagEntity.setDescription(DUMMY_DESCRIPTION);
        mTagEntity.setIcon(DUMMY_ICON);
        mTagEntity.setPriority(DUMMY_PRIORITY);
        mTagEntity.setCreated(DUMMY_DATE);
        mTagEntity.setParentId(DUMMY_PARENT);
        mTagEntity.setType(DUMMY_TYPE);
        mTagEntity.setSlug(DUMMY_SLUG);
        mTagEntity.setTag(DUMMY_TAG);

        assertThat(mTagEntity, is(instanceOf(TagEntity.class)));
        assertThat(mTagEntity.getId(), is(DUMMY_ID));
        assertThat(mTagEntity.getDescription(), is(DUMMY_DESCRIPTION));
        assertThat(mTagEntity.getIcon(), is(DUMMY_ICON));
        assertThat(mTagEntity.getCreated(), is(DUMMY_DATE));
        assertThat(mTagEntity.getParentId(), is(DUMMY_PARENT));
        assertThat(mTagEntity.getPriority(), is(DUMMY_PRIORITY));
        assertThat(mTagEntity.getType(), is(DUMMY_TYPE));
        assertThat(mTagEntity.getSlug(), is(DUMMY_SLUG));
        assertThat(mTagEntity.getTag(), is(DUMMY_TAG));
    }
}
