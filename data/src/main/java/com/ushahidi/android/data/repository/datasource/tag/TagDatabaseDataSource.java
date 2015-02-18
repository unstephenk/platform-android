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

import com.ushahidi.android.data.database.ITagDatabaseHelper;
import com.ushahidi.android.data.database.TagDatabaseHelper;
import com.ushahidi.android.data.entity.TagEntity;

import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagDatabaseDataSource implements TagDataSource {

    private final TagDatabaseHelper mTagDatabaseHelper;

    public TagDatabaseDataSource(TagDatabaseHelper tagDatabaseHelper) {
        mTagDatabaseHelper = tagDatabaseHelper;
    }

    @Override
    public void addTag(final TagEntity tagEntity, final TagEntityAddCallback tagCallback) {
        mTagDatabaseHelper.put(tagEntity, new TagDatabaseHelper.ITagEntityPutCallback() {
            @Override
            public void onTagEntityPut() {
                tagCallback.onTagEntityAdded();
            }

            @Override
            public void onError(Exception exception) {
                tagCallback.onError(exception);
            }
        });
    }

    @Override
    public void getTagEntityList(final TagEntityListCallback tagListCallback) {
        mTagDatabaseHelper.getTagEntities(new TagDatabaseHelper.ITagEntitiesCallback() {
            @Override
            public void onTagEntitiesLoaded(List<TagEntity> tagEntities) {
                tagListCallback.onTagEntityListLoaded(tagEntities);
            }

            @Override
            public void onError(Exception exception) {
                tagListCallback.onError(exception);
            }
        });
    }

    @Override
    public void getTagEntityById(final long tagId,
            final TagEntityDetailsCallback tagDetailsCallback) {
        mTagDatabaseHelper.get(tagId, new TagDatabaseHelper.ITagEntityCallback() {
            @Override
            public void onTagEntityLoaded(TagEntity tagEntity) {
                tagDetailsCallback.onTagEntityLoaded(tagEntity);
            }

            @Override
            public void onError(Exception exception) {
                tagDetailsCallback.onError(exception);
            }
        });
    }

    @Override
    public void updateTagEntity(final TagEntity tagEntity,
            final TagEntityUpdateCallback callback) {
        mTagDatabaseHelper.put(tagEntity, new TagDatabaseHelper.ITagEntityPutCallback() {
            @Override
            public void onTagEntityPut() {
                callback.onTagEntityUpdated();
            }

            @Override
            public void onError(Exception exception) {
                callback.onError(exception);
            }
        });
    }

    @Override
    public void deleteTagEntity(final TagEntity tagEntity,
            final TagEntityDeletedCallback callback) {
        mTagDatabaseHelper.delete(tagEntity, new ITagDatabaseHelper.ITagEntityDeletedCallback() {
            @Override
            public void onTagEntityDeleted() {
                callback.onTagEntityDeleted();
            }

            @Override
            public void onError(Exception exception) {
                callback.onError(exception);
            }
        });
    }
}
