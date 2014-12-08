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

import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.model.TagModel;
import com.ushahidi.android.model.mapper.TagModelDataMapper;
import com.ushahidi.android.test.CustomAndroidTestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link com.ushahidi.android.model.mapper.TagModelDataMapper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagModelDataMapperTest extends CustomAndroidTestCase {

    private TagModelDataMapper mTagModelDataMapper;

    private TagModel mTagModel;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_DESCRIPTION = "dummy description";

    private static final String DUMMY_ICON = "fork";

    private static final int DUMMY_PRIORITY = 1;

    private static final Date DUMMY_DATE = new Date(1415718024);

    private static final long DUMMY_PARENT = 1;

    private static final String DUMMY_TAG = "tag";

    private static final String DUMMY_SLUG = "slug";

    private static final TagModel.Type DUMMY_TAG_ENTITY_TYPE = TagModel.Type.CATEGORY;

    private static final Tag.Type DUMMY_TAG_TYPE = Tag.Type.CATEGORY;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mTagModelDataMapper = new TagModelDataMapper();
    }

    public void testTagModelMap() throws Exception {
        Tag tag = new Tag();
        tag.setId(DUMMY_ID);
        tag.setDescription(DUMMY_DESCRIPTION);
        tag.setIcon(DUMMY_ICON);
        tag.setPriority(DUMMY_PRIORITY);
        tag.setCreated(DUMMY_DATE);
        tag.setParentId(DUMMY_PARENT);
        tag.setTag(DUMMY_TAG);
        tag.setSlug(DUMMY_SLUG);
        tag.setType(DUMMY_TAG_TYPE);

        TagModel tagModel = mTagModelDataMapper.map(tag);

        assertThat(tagModel).isInstanceOf(TagModel.class);
        assertThat(tagModel.getId()).isEqualTo(DUMMY_ID);
        assertThat(tagModel.getDescription()).isEqualTo(DUMMY_DESCRIPTION);
        assertThat(tagModel.getIcon()).isEqualTo(DUMMY_ICON);
        assertThat(tagModel.getCreated()).isEqualTo(DUMMY_DATE);
        assertThat(tagModel.getParentId()).isEqualTo(DUMMY_PARENT);
        assertThat(tagModel.getPriority()).isEqualTo(DUMMY_PRIORITY);
        assertThat(tagModel.getTag()).isEqualTo(DUMMY_TAG);
        assertThat(tagModel.getSlug()).isEqualTo(DUMMY_SLUG);
        assertThat(tagModel.getType()).isEqualTo(DUMMY_TAG_ENTITY_TYPE);
    }

    public void testTagModelUnMap() throws Exception {
        mTagModel = new TagModel();
        mTagModel.setId(DUMMY_ID);
        mTagModel.setDescription(DUMMY_DESCRIPTION);
        mTagModel.setIcon(DUMMY_ICON);
        mTagModel.setPriority(DUMMY_PRIORITY);
        mTagModel.setCreated(DUMMY_DATE);
        mTagModel.setParentId(DUMMY_PARENT);
        mTagModel.setSlug(DUMMY_SLUG);
        mTagModel.setTag(DUMMY_TAG);
        mTagModel.setType(DUMMY_TAG_ENTITY_TYPE);

        Tag tag = mTagModelDataMapper.unmap(mTagModel);

        assertThat(tag).isInstanceOf(Tag.class);
        assertThat(tag.getId()).isEqualTo(DUMMY_ID);
        assertThat(tag.getDescription()).isEqualTo(DUMMY_DESCRIPTION);
        assertThat(tag.getIcon()).isEqualTo(DUMMY_ICON);
        assertThat(tag.getCreated()).isEqualTo(DUMMY_DATE);
        assertThat(tag.getParentId()).isEqualTo(DUMMY_PARENT);
        assertThat(tag.getPriority()).isEqualTo(DUMMY_PRIORITY);
        assertThat(tag.getSlug()).isEqualTo(DUMMY_SLUG);
        assertThat(tag.getTag()).isEqualTo(DUMMY_TAG);
        assertThat(tag.getType()).isEqualTo(DUMMY_TAG_TYPE);
    }

    public void testTagListMap() {
        Tag tag = new Tag();
        tag.setId(DUMMY_ID);
        tag.setDescription(DUMMY_DESCRIPTION);
        tag.setIcon(DUMMY_ICON);
        tag.setPriority(DUMMY_PRIORITY);
        tag.setCreated(DUMMY_DATE);
        tag.setParentId(DUMMY_PARENT);
        tag.setTag(DUMMY_TAG);
        tag.setSlug(DUMMY_SLUG);
        tag.setType(DUMMY_TAG_TYPE);

        Tag tagTwo = new Tag();
        tagTwo.setId(DUMMY_ID);
        tagTwo.setDescription(DUMMY_DESCRIPTION);
        tagTwo.setIcon(DUMMY_ICON);
        tagTwo.setPriority(DUMMY_PRIORITY);
        tagTwo.setCreated(DUMMY_DATE);
        tagTwo.setParentId(DUMMY_PARENT);
        tagTwo.setTag(DUMMY_TAG);
        tagTwo.setSlug(DUMMY_SLUG);
        tagTwo.setType(DUMMY_TAG_TYPE);

        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        tagList.add(tagTwo);

        List<TagModel> tagModelList = mTagModelDataMapper.map(tagList);

        assertThat(tagModelList.get(0)).isInstanceOf(TagModel.class);
        assertThat(tagModelList.get(1)).isInstanceOf(TagModel.class);
        assertThat(tagModelList.size()).isEqualTo(2);
    }
}
