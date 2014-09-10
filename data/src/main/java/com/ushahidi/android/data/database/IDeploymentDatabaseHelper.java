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

import com.ushahidi.android.data.entity.DeploymentEntity;

import java.util.Collection;
import java.util.List;

/**
 * An interface representing deployment database
 */
public interface IDeploymentDatabaseHelper {

    /**
     * Puts a deployment entity into the database.
     *
     * @param deploymentEntity Deployment to insert into the database.
     * @param callback         The {@link IDeploymentEntityAddedCallback} use to notify client.
     */
    public void put(DeploymentEntity deploymentEntity,
            final IDeploymentEntityAddedCallback callback);

    /**
     * Gets a deployment from the database using a {@link IDeploymentEntityCallback}.
     *
     * @param id       The user id to retrieve data.
     * @param callback The {@link IDeploymentEntityCallback} to notify the client.
     */
    public void get(final int id, final IDeploymentEntityCallback callback);

    /**
     * Gets a list of deployment entities.
     *
     * @param callback The {@link IDeploymentEntitiesCallback} to notify the client.
     */
    public void getDeploymentEntities(final IDeploymentEntitiesCallback callback);

    /**
     * Puts collection of deployment entity into the database
     *
     * @param deploymentEntities The collection of {@link com.ushahidi.android.data.entity.DeploymentEntity}
     *                           to be added to the database.
     * @param callback           The {@link IDeploymentEntityAddedCallback} use to notify client.
     */
    public void put(final Collection<DeploymentEntity> deploymentEntities,
            final IDeploymentEntityAddedCallback callback);

    /**
     * Deletes all deployment entities
     *
     * @param callback The {@link IDeploymentEntityDeletedCallback} use to notify client.
     */
    public void deleteAll(final IDeploymentEntityDeletedCallback callback);

    /**
     * Deletes a deployment entity
     *
     * @param deploymentEntity The {@link com.ushahidi.android.data.entity.DeploymentEntity} to be
     *                         deleted
     * @param callback         The {@link IDeploymentEntityDeletedCallback} use to notify client.
     */
    public void delete(final DeploymentEntity deploymentEntity,
            final IDeploymentEntityDeletedCallback callback);

    /**
     * Callback use to notify client when a {@link DeploymentEntity} has been loaded from the
     * database.
     */
    public interface IDeploymentEntityCallback {

        void onDeploymentEntityLoaded(DeploymentEntity userEntity);

        void onError(Exception exception);
    }

    /**
     * Callback use to notify the client when a list of {@link DeploymentEntity} have been loaded
     * from the database.
     */
    public interface IDeploymentEntitiesCallback {

        void onDeploymentEntitiesLoaded(List<DeploymentEntity> deploymentEntities);

        void onError(Exception exception);
    }

    /**
     * Callback use to notify client when {@link DeploymentEntity} has been added to the database.
     */
    public interface IDeploymentEntityAddedCallback {

        void onDeploymentEntityAdded();

        void onError(Exception exception);
    }

    /**
     * Callback use to notify client when {@link DeploymentEntity} has been deleted from the
     * database.
     */
    public interface IDeploymentEntityDeletedCallback {

        void onDeploymentEntityDeleted();

        void onError(Exception exception);
    }
}
