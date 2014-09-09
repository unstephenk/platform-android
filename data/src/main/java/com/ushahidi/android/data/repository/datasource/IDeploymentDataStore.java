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
package com.ushahidi.android.data.repository.datasource;

import com.ushahidi.android.data.entity.DeploymentEntity;

/**
 * DeploymentDataStore
 */
public interface IDeploymentDataStore {

    /**
     * Add an {@link com.ushahidi.android.core.entity.Deployment}.
     *
     * @param deploymentEntity        The DeploymentEntity to be saved.
     * @param deploymentCallback A {@link DeploymentAddCallback} used for notifying clients.
     * @author Ushahidi Team <team@ushahidi.com>
     */
    void addDeployment(DeploymentEntity deploymentEntity, DeploymentAddCallback deploymentCallback);

    interface DeploymentAddCallback {

        void onDeploymentAdded(DeploymentEntity deploymentEntity);

        void onError(Exception exception);
    }

}
