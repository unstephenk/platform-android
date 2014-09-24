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
package com.ushahidi.android.core.respository;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.exception.ErrorWrap;

import java.util.List;

/**
 * IDeploymentRepository
 */
public interface IDeploymentRepository {

    /**
     * Add an {@link com.ushahidi.android.core.entity.Deployment}.
     *
     * @param deployment         The Deployment to be saved.
     * @param deploymentCallback A {@link DeploymentAddCallback} used for notifying clients.
     * @author Ushahidi Team <team@ushahidi.com>
     */
    void addDeployment(Deployment deployment, DeploymentAddCallback deploymentCallback);

    /**
     * Get a list of {@link com.ushahidi.android.core.entity.Deployment}.
     *
     * @param deploymentListCallback A {@link DeploymentListCallback} used for notifying clients.
     */
    void getDeploymentList(DeploymentListCallback deploymentListCallback);

    /**
     * Get an {@link com.ushahidi.android.core.entity.Deployment} by id.
     *
     * @param deploymentId              The user id used to retrieve user data.
     * @param deploymentDetailsCallback A {@link DeploymentDetailsCallback} used for notifying
     *                                  clients.
     */
    void getDeploymentById(final long deploymentId,
            DeploymentDetailsCallback deploymentDetailsCallback);

    void updateDeployment(Deployment deployment, DeploymentUpdateCallback callback);

    interface DeploymentAddCallback {

        void onDeploymentAdded();

        void onError(ErrorWrap error);
    }

    /**
     * Callback used to be notified when either a deployment list has been loaded or an error
     * occurs.
     */
    interface DeploymentListCallback {

        void onDeploymentListLoaded(List<Deployment> deploymentList);

        void onError(ErrorWrap errorWrap);
    }

    /**
     * Callback used to be notified when either a deployment has been loaded or an error occurs.
     */
    interface DeploymentDetailsCallback {

        void onDeploymentLoaded(Deployment deployment);

        void onError(ErrorWrap errorWrap);
    }

    /**
     * Callback used to be notified when either a deployment has been updated or failed.
     */
    interface DeploymentUpdateCallback {

        void onDeploymentUpdated();

        void onError(ErrorWrap error);
    }
}
