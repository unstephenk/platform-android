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

package com.ushahidi.android.data.repository;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.core.respository.ITagRepository;
import com.ushahidi.android.data.database.ITagDatabaseHelper;
import com.ushahidi.android.data.database.TagDatabaseHelper;
import com.ushahidi.android.data.entity.TagEntity;
import com.ushahidi.android.data.entity.mapper.TagEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.data.validator.Validator;

import java.util.List;

/**
 * Tag's data repository
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagDataRepository implements ITagRepository {

    private static TagDataRepository sInstance;

    private final TagEntityMapper mTagEntityMapper;

    private final TagDatabaseHelper mTagDatabaseHelper;

    private final Validator mValidator;

    protected TagDataRepository(TagDatabaseHelper tagDatabaseHelper,
            TagEntityMapper entityMapper, Validator validator) {
        if (entityMapper == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        Preconditions.checkNotNull(tagDatabaseHelper, "DatabaseHelper cannot be null");
        Preconditions.checkNotNull(entityMapper, "Entity mapper cannot be null");
        Preconditions.checkNotNull(validator, "Validator cannot be null");
        mTagEntityMapper = entityMapper;
        mTagDatabaseHelper = tagDatabaseHelper;
        mValidator = validator;
    }

    public static synchronized TagDataRepository getInstance(TagDatabaseHelper
            tagDatabaseHelper, TagEntityMapper entityMapper, Validator validator) {
        if (sInstance == null) {
            sInstance = new TagDataRepository(tagDatabaseHelper,
                    entityMapper, validator);
        }
        return sInstance;
    }

    /**
     * {@inheritDoc}
     *
     * @param tag         The Tag to be saved.
     * @param tagCallback A {@link TagAddCallback} used for notifying clients.
     */
    @Override
    public void addTag(Tag tag,
            final TagAddCallback tagCallback) {
        // Check for required fields
        boolean isValid = true;
        if (Strings.isNullOrEmpty(tag.getTag())) {
            isValid = false;
            tagCallback.onError(new RepositoryError(
                    new ValidationException("Tag cannot be null or empty")));
        }

        if (Strings.isNullOrEmpty(tag.getSlug())) {
            isValid = false;
            tagCallback.onError(new RepositoryError(
                    new ValidationException("Tag slug cannot be null or empty")));
        }

        if (isValid) {
            mTagDatabaseHelper.put(mTagEntityMapper.unmap(tag),
                    new ITagDatabaseHelper.ITagEntityPutCallback() {

                        @Override
                        public void onTagEntityPut() {
                            tagCallback.onTagAdded();
                        }

                        @Override
                        public void onError(Exception exception) {
                            tagCallback.onError(new RepositoryError(exception));
                        }
                    });
        }
    }

    @Override
    public void getTagList(final TagListCallback tagListCallback) {
        mTagDatabaseHelper.getTagEntities(
                new ITagDatabaseHelper.ITagEntitiesCallback() {

                    @Override
                    public void onTagEntitiesLoaded(
                            List<TagEntity> tagEntities) {
                        final List<Tag> tags = mTagEntityMapper
                                .map(tagEntities);
                        tagListCallback.onTagListLoaded(tags);
                    }

                    @Override
                    public void onError(Exception exception) {
                        tagListCallback.onError(new RepositoryError(exception));
                    }
                });
    }

    @Override
    public void getTagById(long tagId,
            final TagDetailsCallback tagDetailsCallback) {
        mTagDatabaseHelper.get(tagId,
                new ITagDatabaseHelper.ITagEntityCallback() {

                    @Override
                    public void onTagEntityLoaded(TagEntity tagEntity) {
                        final Tag tag = mTagEntityMapper.map(tagEntity);
                        tagDetailsCallback.onTagLoaded(tag);
                    }

                    @Override
                    public void onError(Exception exception) {
                        tagDetailsCallback.onError(new RepositoryError(exception));
                    }
                });
    }

    /**
     * {@inheritDoc}
     *
     * @param tag         The Tag to be saved.
     * @param tagCallback A {@link TagUpdateCallback} used for notifying clients.
     */
    @Override
    public void updateTag(Tag tag,
            final TagUpdateCallback tagCallback) {
        // Check for required fields
        boolean isValid = true;
        if (Strings.isNullOrEmpty(tag.getTag())) {
            isValid = false;
            tagCallback.onError(new RepositoryError(
                    new ValidationException("Tag cannot be null or empty")));
        }

        if (Strings.isNullOrEmpty(tag.getSlug())) {
            isValid = false;
            tagCallback.onError(new RepositoryError(
                    new ValidationException("Tag slug cannot be null or empty")));
        }

        if (isValid) {
            mTagDatabaseHelper.put(mTagEntityMapper.unmap(tag),
                    new ITagDatabaseHelper.ITagEntityPutCallback() {

                        @Override
                        public void onTagEntityPut() {
                            tagCallback.onTagUpdated();
                        }

                        @Override
                        public void onError(Exception exception) {
                            tagCallback.onError(new RepositoryError(exception));
                        }
                    });
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param tag      The ID of the tag to be deleted.
     * @param callback A {@link TagDeletedCallback} used for notifying clients.
     */
    @Override
    public void deleteTag(final Tag tag,
            final TagDeletedCallback callback) {
        mTagDatabaseHelper.delete(mTagEntityMapper.unmap(tag),
                new ITagDatabaseHelper.ITagEntityDeletedCallback() {
                    @Override
                    public void onTagEntityDeleted() {
                        callback.onTagDeleted();
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(new RepositoryError(exception));
                    }

                });
    }

}
