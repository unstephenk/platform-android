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

import com.ushahidi.android.core.entity.Media;
import com.ushahidi.android.core.repository.IMediaRepository;
import com.ushahidi.android.data.database.IMediaDatabaseHelper;
import com.ushahidi.android.data.database.MediaDatabaseHelper;
import com.ushahidi.android.data.entity.MediaEntity;
import com.ushahidi.android.data.entity.mapper.MediaEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;

import java.util.List;

/**
 * Media's data repository
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MediaDataRepository implements IMediaRepository {

    private static MediaDataRepository sInstance;

    private final MediaEntityMapper mMediaEntityMapper;

    private final MediaDatabaseHelper mMediaDatabaseHelper;


    protected MediaDataRepository(MediaDatabaseHelper mediaDatabaseHelper,
            MediaEntityMapper entityMapper) {
        if (entityMapper == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        Preconditions.checkNotNull(mediaDatabaseHelper, "DatabaseHelper cannot be null");
        Preconditions.checkNotNull(entityMapper, "Entity mapper cannot be null");
        mMediaEntityMapper = entityMapper;
        mMediaDatabaseHelper = mediaDatabaseHelper;

    }

    public static synchronized MediaDataRepository getInstance(MediaDatabaseHelper
            mediaDatabaseHelper, MediaEntityMapper entityMapper) {
        if (sInstance == null) {
            sInstance = new MediaDataRepository(mediaDatabaseHelper,
                    entityMapper);
        }
        return sInstance;
    }

    /**
     * {@inheritDoc}
     *
     * @param media         The Media to be saved.
     * @param mediaCallback A {@link com.ushahidi.android.core.repository.IMediaRepository.MediaAddCallback}
     *                      used for notifying clients.
     */
    @Override
    public void addMedia(Media media,
            final MediaAddCallback mediaCallback) {
        // Check for required fields
        boolean isValid = true;
        if (Strings.isNullOrEmpty(media.getOriginalFileUrl())) {
            isValid = false;
            mediaCallback.onError(new RepositoryError(
                    new ValidationException("Media original url cannot be null or empty")));
        }

        if (isValid) {
            mMediaDatabaseHelper.put(mMediaEntityMapper.unmap(media),
                    new IMediaDatabaseHelper.IMediaEntityPutCallback() {

                        @Override
                        public void onMediaEntityPut() {
                            mediaCallback.onMediaAdded();
                        }

                        @Override
                        public void onError(Exception exception) {
                            mediaCallback.onError(new RepositoryError(exception));
                        }
                    });
        }
    }

    @Override
    public void getMediaList(final MediaListCallback mediaListCallback) {
        mMediaDatabaseHelper.getMediaEntities(
                new IMediaDatabaseHelper.IMediaEntitiesCallback() {

                    @Override
                    public void onMediaEntitiesLoaded(
                            List<MediaEntity> mediaEntities) {
                        final List<Media> medias = mMediaEntityMapper
                                .map(mediaEntities);
                        mediaListCallback.onMediaListLoaded(medias);
                    }

                    @Override
                    public void onError(Exception exception) {
                        mediaListCallback.onError(new RepositoryError(exception));
                    }
                });
    }

    @Override
    public void getMediaById(long mediaId,
            final MediaDetailsCallback mediaDetailsCallback) {
        mMediaDatabaseHelper.get(mediaId,
                new IMediaDatabaseHelper.IMediaEntityCallback() {

                    @Override
                    public void onMediaEntityLoaded(MediaEntity mediaEntity) {
                        final Media media = mMediaEntityMapper.map(mediaEntity);
                        mediaDetailsCallback.onMediaLoaded(media);
                    }

                    @Override
                    public void onError(Exception exception) {
                        mediaDetailsCallback.onError(new RepositoryError(exception));
                    }
                });
    }

    /**
     * {@inheritDoc}
     *
     * @param media         The Media to be saved.
     * @param mediaCallback A {@link com.ushahidi.android.core.repository.IMediaRepository.MediaUpdateCallback}
     *                      used for notifying clients.
     */
    @Override
    public void updateMedia(Media media,
            final MediaUpdateCallback mediaCallback) {
        // Check for required fields
        boolean isValid = true;
        if (Strings.isNullOrEmpty(media.getOriginalFileUrl())) {
            isValid = false;
            mediaCallback.onError(new RepositoryError(
                    new ValidationException("Media original url cannot be null or empty")));
        }

        if (isValid) {
            mMediaDatabaseHelper.put(mMediaEntityMapper.unmap(media),
                    new IMediaDatabaseHelper.IMediaEntityPutCallback() {

                        @Override
                        public void onMediaEntityPut() {
                            mediaCallback.onMediaUpdated();
                        }

                        @Override
                        public void onError(Exception exception) {
                            mediaCallback.onError(new RepositoryError(exception));
                        }
                    });
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param media    The ID of the media to be deleted.
     * @param callback A {@link com.ushahidi.android.core.repository.IMediaRepository.MediaDeletedCallback}
     *                 used for notifying clients.
     */
    @Override
    public void deleteMedia(final Media media,
            final MediaDeletedCallback callback) {
        mMediaDatabaseHelper.delete(mMediaEntityMapper.unmap(media),
                new IMediaDatabaseHelper.IMediaEntityDeletedCallback() {
                    @Override
                    public void onMediaEntityDeleted() {
                        callback.onMediaDeleted();
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(new RepositoryError(exception));
                    }

                });
    }

}
