/*
 * Copyright (c) 2015 Ushahidi.
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

package com.ushahidi.android.data.repository.datasource.tag;

import com.ushahidi.android.data.api.ITagApi;
import com.ushahidi.android.data.api.TagApi;
import com.ushahidi.android.data.entity.TagEntity;

import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagApiDataSource implements TagDataSource {

    private TagApi mTagApi;

    public TagApiDataSource(TagApi tagApi) {
        mTagApi = tagApi;
    }

    @Override
    public void addTag(TagEntity tagEntity, TagEntityAddCallback tagCallback) {

    }

    @Override
    public void getTagEntityList(final TagEntityListCallback tagListCallback) {
        mTagApi.getTagEntityList(new ITagApi.TagEntityListCallback() {

            @Override
            public void onTagEntityListLoaded(List<TagEntity> tagEntityList) {
                tagListCallback.onTagEntityListLoaded(tagEntityList);
            }

            @Override
            public void onError(Exception e) {
                tagListCallback.onError(e);
            }
        });
    }

    @Override
    public void getTagEntityById(long tagId,
            TagEntityDetailsCallback tagDetailsCallback) {

    }

    @Override
    public void updateTagEntity(TagEntity tagEntity,
            TagEntityUpdateCallback callback) {

    }

    @Override
    public void deleteTagEntity(TagEntity tagEntity,
            TagEntityDeletedCallback callback) {

    }
}
