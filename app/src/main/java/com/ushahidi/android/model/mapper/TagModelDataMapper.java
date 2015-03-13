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

package com.ushahidi.android.model.mapper;

import com.google.common.base.Preconditions;

import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.model.Model;
import com.ushahidi.android.model.TagModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Tag Model
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagModelDataMapper extends Model {

    public TagModelDataMapper() {
        // Do nothing
    }
    /**
     * Maps {@link com.ushahidi.android.core.entity.Tag} to {@link com.ushahidi.android.model.TagModel}
     *
     * @param tag The {@link com.ushahidi.android.core.entity.Tag} to be mapped
     * @return The {@link com.ushahidi.android.model.TagModel} entity
     */
    public TagModel map(Tag tag) {
        Preconditions.checkNotNull(tag, "Tag entity cannot be null");

        TagModel tagModel = new TagModel();
        tagModel.setId(tag.getId());
        tagModel.setDescription(tag.getDescription());
        tagModel.setPriority(tag.getPriority());
        tagModel.setCreated(tag.getCreated());
        tagModel.setIcon(tag.getIcon());
        tagModel.setParentId(tag.getParentId());
        tagModel.setSlug(tag.getSlug());
        tagModel.setColor(tag.getColor());
        tagModel.setType(TagModel.Type.valueOf(tag.getType().name()));
        tagModel.setTag(tag.getTag());

        return tagModel;
    }

    public Tag unmap(TagModel tagModel) {
        Preconditions.checkNotNull(tagModel, "TagModel cannot be null");
        Tag tag = new Tag();
        tag.setId(tagModel.getId());
        tag.setDescription(tagModel.getDescription());
        tag.setPriority(tagModel.getPriority());
        tag.setParentId(tagModel.getParentId());
        tag.setCreated(tagModel.getCreated());
        tag.setIcon(tagModel.getIcon());
        tag.setTag(tagModel.getTag());
        tag.setSlug(tagModel.getSlug());
        tag.setColor(tagModel.getColor());
        tag.setType(Tag.Type.valueOf(tagModel.getType().name()));

        return tag;
    }

    /**
     * Maps a list {@link com.ushahidi.android.core.entity.Tag} into a list of {@link com.ushahidi.android.model.TagModel}.
     *
     * @param tagList List to be mapped.
     * @return {@link Tag}
     */
    public List<TagModel> map(List<Tag> tagList) {
        List<TagModel> tagModelList = new ArrayList<>();
        if (tagList != null && !tagList.isEmpty()) {
            for (Tag tag : tagList) {
                tagModelList.add(map(tag));

            }
        }
        return tagModelList;
    }

    /**
     * Maps a list {@link com.ushahidi.android.core.entity.Tag} into a list of {@link com.ushahidi.android.model.TagModel}.
     *
     * @param tagList List to be mapped.
     * @return {@link Tag}
     */
    public List<Tag> unmap(List<TagModel> tagList) {
        List<Tag> tagModelList = new ArrayList<>();
        if (tagList != null && !tagList.isEmpty()) {
            for (TagModel tag : tagList) {
                tagModelList.add(unmap(tag));

            }
        }
        return tagModelList;
    }
}
