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

import com.ushahidi.android.core.entity.Media;
import com.ushahidi.android.model.MediaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MediaModelDataMapper {

    public MediaModelDataMapper() {
        // Do nothing
    }

    /**
     * Maps {@link com.ushahidi.android.data.entity.MediaEntity} to {@link
     * com.ushahidi.android.core.entity.Media}
     *
     * @param media The {@link com.ushahidi.android.core.entity.Media} to be mapped
     * @return The {@link com.ushahidi.android.model.MediaModel} model
     */
    public MediaModel map(Media media) {
        Preconditions.checkNotNull(media, "Media cannot be null");

        MediaModel mediaModel = new MediaModel();
        mediaModel.setId(media.getId());
        mediaModel.setCaption(media.getCaption());
        mediaModel.setCreated(media.getCreated());
        mediaModel.setMime(media.getMime());
        mediaModel.setUser(media.getUser());
        mediaModel.setOriginalFileUrl(media.getOriginalFileUrl());
        mediaModel.setUpdated(media.getUpdated());

        return mediaModel;
    }

    public Media unmap(MediaModel mediaModel) {
        Preconditions.checkNotNull(mediaModel, "Media Model cannot be null");

        Media media = new Media();
        media.setId(mediaModel.getId());
        media.setCaption(mediaModel.getCaption());
        media.setCreated(mediaModel.getCreated());
        media.setMime(mediaModel.getMime());
        media.setOriginalFileUrl(mediaModel.getOriginalFileUrl());
        media.setUpdated(mediaModel.getUpdated());

        return media;
    }

    /**
     * Maps a list {@link com.ushahidi.android.core.entity.Media} into a list of {@link
     * com.ushahidi.android.model.MediaModel}.
     *
     * @param mediaList List to be mapped.
     * @return {@link Media}
     */
    public List<MediaModel> map(List<Media> mediaList) {

        List<MediaModel> mediaModelList = new ArrayList<>();
        if (mediaList != null && !mediaList.isEmpty()) {
            for (Media media : mediaList) {
                mediaModelList.add(map(media));
            }
        }

        return mediaModelList;
    }
}
