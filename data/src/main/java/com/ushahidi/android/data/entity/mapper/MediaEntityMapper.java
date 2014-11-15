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

import com.ushahidi.android.core.entity.Media;
import com.ushahidi.android.data.entity.MediaEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class used to map {@link com.ushahidi.android.data.entity.MediaEntity} to {@link
 * com.ushahidi.android.core.entity.Media} in core
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MediaEntityMapper {

    public MediaEntityMapper() {
    }

    /**
     * Maps {@link com.ushahidi.android.data.entity.MediaEntity} to {@link
     * com.ushahidi.android.core.entity.Media}
     *
     * @param mediaEntity The {@link com.ushahidi.android.data.entity.MediaEntity} to be mapped
     * @return The {@link com.ushahidi.android.core.entity.Media} entity
     */
    public Media map(MediaEntity mediaEntity) {
        Media media = null;

        if (mediaEntity != null) {
            media = new Media();
            media.setId(mediaEntity.getId());
            media.setCaption(mediaEntity.getCaption());
            media.setCreated(mediaEntity.getCreated());
            media.setMime(mediaEntity.getMime());
            media.setUser(mediaEntity.getUser());
            media.setOriginalFileUrl(mediaEntity.getOriginalFileUrl());
            media.setUpdated(mediaEntity.getUpdated());
        }

        return media;
    }

    public MediaEntity unmap(Media media) {
        MediaEntity mediaEntity = null;

        if (media != null) {
            mediaEntity = new MediaEntity();
            mediaEntity.setId(media.getId());
            mediaEntity.setCaption(media.getCaption());
            mediaEntity.setCreated(media.getCreated());
            mediaEntity.setMime(media.getMime());
            mediaEntity.setOriginalFileUrl(media.getOriginalFileUrl());
            mediaEntity.setUpdated(media.getUpdated());
        }
        return mediaEntity;
    }

    /**
     * Maps a list {@link MediaEntity} into a list of {@link Media}.
     *
     * @param mediaEntityList List to be mapped.
     * @return {@link Media}
     */
    public List<Media> map(List<MediaEntity> mediaEntityList) {
        List<Media> mediaList = new ArrayList<>();
        Media media;
        for (MediaEntity mediaEntity : mediaEntityList) {
            media = map(mediaEntity);
            if (media != null) {
                mediaList.add(media);
            }
        }

        return mediaList;
    }

}
