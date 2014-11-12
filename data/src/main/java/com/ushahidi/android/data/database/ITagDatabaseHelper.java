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

/**
 *
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
package com.ushahidi.android.data.database;

import com.ushahidi.android.data.entity.TagEntity;

import java.util.List;

/**
 * An interface representing tag database
 */
public interface ITagDatabaseHelper {

    /**
     * Puts a tag entity into the database.
     *
     * @param tagEntity Tag to insert into the database.
     * @param callback  The {@link com.ushahidi.android.data.database.ITagDatabaseHelper.ITagEntityPutCallback}
     *                  use to notify client.
     */
    public void put(final TagEntity tagEntity,
            final ITagEntityPutCallback callback);

    /**
     * Gets a tag from the database using a {@link ITagEntityCallback}.
     *
     * @param id       The user id to retrieve data.
     * @param callback The {@link ITagEntityCallback} to notify the client.
     */
    public void get(final long id, final ITagEntityCallback callback);

    /**
     * Gets a list of tag entities.
     *
     * @param callback The {@link ITagEntitiesCallback} to notify the client.
     */
    public void getTagEntities(final ITagEntitiesCallback callback);

    /**
     * Puts list of tag entity into the database
     *
     * @param tagEntities The list of {@link com.ushahidi.android.data.entity.TagEntity} to be added
     *                    to the database.
     * @param callback    The {@link com.ushahidi.android.data.database.ITagDatabaseHelper.ITagEntityPutCallback}
     *                    use to notify client.
     */
    public void put(final List<TagEntity> tagEntities,
            final ITagEntityPutCallback callback);

    /**
     * Deletes all tag entities
     *
     * @param callback The {@link ITagEntityDeletedCallback} use to notify client.
     */
    public void deleteAll(final ITagEntityDeletedCallback callback);

    /**
     * Deletes a tag entity
     *
     * @param tagEntity The {@link com.ushahidi.android.data.entity.TagEntity} to be deleted
     * @param callback  The {@link ITagEntityDeletedCallback} use to notify client.
     */
    public void delete(final TagEntity tagEntity,
            final ITagEntityDeletedCallback callback);

    /**
     * Callback use to notify client when a {@link com.ushahidi.android.data.entity.TagEntity} has
     * been loaded from the database.
     */
    public interface ITagEntityCallback {

        void onTagEntityLoaded(TagEntity userEntity);

        void onError(Exception exception);
    }

    /**
     * Callback use to notify the client when a list of {@link com.ushahidi.android.data.entity.TagEntity}
     * have been loaded from the database.
     */
    public interface ITagEntitiesCallback {

        void onTagEntitiesLoaded(List<TagEntity> tagEntities);

        void onError(Exception exception);
    }

    /**
     * Callback use to notify client when {@link com.ushahidi.android.data.entity.TagEntity} has
     * been added to the database.
     */
    public interface ITagEntityPutCallback {

        void onTagEntityPut();

        void onError(Exception exception);
    }

    /**
     * Callback use to notify client when {@link com.ushahidi.android.data.entity.TagEntity} has
     * been deleted from the database.
     */
    public interface ITagEntityDeletedCallback {

        void onTagEntityDeleted();

        void onError(Exception exception);
    }
}
