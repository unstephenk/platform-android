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

import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.entity.TagEntity;

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
 * Tests {@link com.ushahidi.android.data.entity.mapper.TagEntityMapper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class TagEntityMapperTest extends BaseTestCase {

    private TagEntityMapper mTagEntityMapper;

    private TagEntity mTagEntity;

    private Tag mTag;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_DESCRIPTION = "dummy description";

    private static final String DUMMY_ICON = "fork";

    private static final int DUMMY_PRIORITY = 1;

    private static final Date DUMMY_DATE = new Date(1415718024);

    private static final long DUMMY_PARENT = 1;

    private static final String DUMMY_TAG = "tag";

    private static final String DUMMY_SLUG = "slug";

    private static final TagEntity.Type DUMMY_TAG_ENTITY_TYPE = TagEntity.Type.CATEGORY;

    private static final Tag.Type DUMMY_TAG_TYPE = Tag.Type.CATEGORY;

    @Before
    public void setUp() throws Exception {
        mTagEntityMapper = new TagEntityMapper();
    }

    @Test
    public void shouldMapTagEntityToTag() throws Exception {
        mTagEntity = new TagEntity();
        mTagEntity.setId(DUMMY_ID);
        mTagEntity.setDescription(DUMMY_DESCRIPTION);
        mTagEntity.setIcon(DUMMY_ICON);
        mTagEntity.setPriority(DUMMY_PRIORITY);
        mTagEntity.setCreated(DUMMY_DATE);
        mTagEntity.setParentId(DUMMY_PARENT);
        mTagEntity.setTag(DUMMY_TAG);
        mTagEntity.setColor(DUMMY_SLUG);
        mTagEntity.setType(DUMMY_TAG_ENTITY_TYPE);

        Tag tag = mTagEntityMapper.map(mTagEntity);

        assertThat(tag, is(instanceOf(Tag.class)));
        assertThat(tag.getId(), is(DUMMY_ID));
        assertThat(tag.getDescription(), is(DUMMY_DESCRIPTION));
        assertThat(tag.getIcon(), is(DUMMY_ICON));
        assertThat(tag.getCreated(), is(DUMMY_DATE));
        assertThat(tag.getParentId(), is(DUMMY_PARENT));
        assertThat(tag.getPriority(), is(DUMMY_PRIORITY));
        assertThat(tag.getTag(), is(DUMMY_TAG));
        assertThat(tag.getSlug(), is(DUMMY_SLUG));
        assertThat(tag.getType(), is(DUMMY_TAG_TYPE));
    }

    @Test
    public void shouldUnMapFromTagToTagEntity() throws Exception {
        mTag = new Tag();
        mTag.setId(DUMMY_ID);
        mTag.setDescription(DUMMY_DESCRIPTION);
        mTag.setIcon(DUMMY_ICON);
        mTag.setPriority(DUMMY_PRIORITY);
        mTag.setCreated(DUMMY_DATE);
        mTag.setParentId(DUMMY_PARENT);
        mTag.setSlug(DUMMY_SLUG);
        mTag.setTag(DUMMY_TAG);
        mTag.setType(DUMMY_TAG_TYPE);

        TagEntity tagEntity = mTagEntityMapper.unmap(mTag);

        assertThat(tagEntity, is(instanceOf(TagEntity.class)));
        assertThat(tagEntity.getId(), is(DUMMY_ID));
        assertThat(tagEntity.getDescription(), is(DUMMY_DESCRIPTION));
        assertThat(tagEntity.getIcon(), is(DUMMY_ICON));
        assertThat(tagEntity.getCreated(), is(DUMMY_DATE));
        assertThat(tagEntity.getParentId(), is(DUMMY_PARENT));
        assertThat(tagEntity.getPriority(), is(DUMMY_PRIORITY));
        assertThat(tagEntity.getColor(), is(DUMMY_SLUG));
        assertThat(tagEntity.getTag(), is(DUMMY_TAG));
        assertThat(tagEntity.getType(), is(DUMMY_TAG_ENTITY_TYPE));
    }
}
