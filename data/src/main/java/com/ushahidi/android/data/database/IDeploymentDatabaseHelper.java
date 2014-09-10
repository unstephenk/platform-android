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
     */
    public void put(DeploymentEntity deploymentEntity);

    /**
     * Gets a deployment from the database using a {@link IDeploymentEntityCallback}.
     *
     * @param id       The user id to retrieve data.
     * @param callback The {@link IDeploymentEntityCallback} to notify the client.
     */
    public void get(final int id, final IDeploymentEntityCallback callback);

    public void getDeploymentEntities(final IDeploymentDeploymentEntitiesCallback callback);

    /**
     * Puts collection of deployment entity into the database
     *
     * @param deploymentEntities The collection of {@link com.ushahidi.android.data.entity.DeploymentEntity}
     *                           to be added to the database
     */
    public void put(Collection<DeploymentEntity> deploymentEntities);

    /**
     * Delete all deployment entities
     */
    public void deleteAll();

    /**
     * Delete a deployment entity
     *
     * @param deploymentEntity The {@link com.ushahidi.android.data.entity.DeploymentEntity} to be
     *                         deleted
     */
    public void delete(DeploymentEntity deploymentEntity);

    /**
     * Callback used to be notified when a {@link DeploymentEntity} has been loaded.
     */
    public interface IDeploymentEntityCallback {

        void onDeploymentEntityLoaded(DeploymentEntity userEntity);

        void onError(Exception exception);
    }

    /**
     * Callback used to be notified when a list of {@link DeploymentEntity} has been loaded.
     */
    public interface IDeploymentDeploymentEntitiesCallback {

        void onDeploymentEntitiesLoaded(List<DeploymentEntity> deploymentEntities);

        void onError(Exception exception);
    }
}
