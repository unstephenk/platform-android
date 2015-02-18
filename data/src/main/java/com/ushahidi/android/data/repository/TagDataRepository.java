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
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.repository.ITagRepository;
import com.ushahidi.android.data.entity.TagEntity;
import com.ushahidi.android.data.entity.mapper.TagEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.data.repository.datasource.tag.TagDataSource;
import com.ushahidi.android.data.repository.datasource.tag.TagDataSourceFactory;

import java.util.List;

/**
 * Tag's data repository
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagDataRepository implements ITagRepository {

    private static TagDataRepository sInstance;

    private final TagEntityMapper mTagEntityMapper;

    private final TagDataSourceFactory mTagDataSourceFactory;

    protected TagDataRepository(TagDataSourceFactory tagDataSourceFactory,
            TagEntityMapper entityMapper) {

        if (entityMapper == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }

        Preconditions.checkNotNull(tagDataSourceFactory, "DataSourceFactory cannot be null");
        Preconditions.checkNotNull(entityMapper, "Entity mapper cannot be null");
        mTagEntityMapper = entityMapper;
        mTagDataSourceFactory = tagDataSourceFactory;
    }

    public static synchronized TagDataRepository getInstance(
            TagDataSourceFactory tagDataSourceFactory,
            TagEntityMapper entityMapper) {
        if (sInstance == null) {
            sInstance = new TagDataRepository(tagDataSourceFactory, entityMapper);
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
            final TagDataSource tagDataSource = mTagDataSourceFactory.createTagDatabaseSource();
            tagDataSource.addTag(mTagEntityMapper.unmap(tag),
                    new TagDataSource.TagEntityAddCallback() {
                        @Override
                        public void onTagEntityAdded() {
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
        final TagDataSource tagDataSource = mTagDataSourceFactory.createTagDatabaseSource();
        tagDataSource.getTagEntityList(new TagDataSource.TagEntityListCallback() {
            @Override
            public void onTagEntityListLoaded(List<TagEntity> tagList) {
                final List<Tag> tags = mTagEntityMapper
                        .map(tagList);
                tagListCallback.onTagListLoaded(tags);
            }

            @Override
            public void onError(Exception exception) {
                tagListCallback.onError(new RepositoryError(exception));
            }
        });
    }

    @Override
    public void getTagListViaApi(final TagListCallback tagListCallback) {
        final TagDataSource tagDataSource = mTagDataSourceFactory.createTagApiSource();
        tagDataSource.getTagEntityList(new TagDataSource.TagEntityListCallback() {
            @Override
            public void onTagEntityListLoaded(List<TagEntity> tagList) {
                final List<Tag> tags = mTagEntityMapper
                        .map(tagList);
                tagListCallback.onTagListLoaded(tags);

                //Cache to local db
                put(tags);
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
        final TagDataSource dataSource = mTagDataSourceFactory.createTagDatabaseSource();
        dataSource.getTagEntityById(tagId, new TagDataSource.TagEntityDetailsCallback() {
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
            final TagDataSource tagDataSource = mTagDataSourceFactory.createTagDatabaseSource();
            tagDataSource.updateTagEntity(mTagEntityMapper.unmap(tag),
                    new TagDataSource.TagEntityUpdateCallback() {
                        @Override
                        public void onTagEntityUpdated() {
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
        final TagDataSource dataSource = mTagDataSourceFactory.createTagDatabaseSource();
        dataSource.deleteTagEntity(mTagEntityMapper.unmap(tag),
                new TagDataSource.TagEntityDeletedCallback() {
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

    private void put(List<Tag> tags) {
        for(Tag tag : tags) {
            addTag(tag, new TagAddCallback() {
                @Override
                public void onTagAdded() {
                    // Do nothing
                }

                @Override
                public void onError(ErrorWrap error) {
                    // Do nothing
                }
            });
        }
    }

}
