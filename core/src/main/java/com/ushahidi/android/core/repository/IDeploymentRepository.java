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
package com.ushahidi.android.core.repository;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.exception.ErrorWrap;

import java.util.List;

/**
 * IDeploymentRepository
 */
public interface IDeploymentRepository {

    /**
     * Add a {@link com.ushahidi.android.core.entity.Deployment}.
     *
     * @param deployment         The Deployment to be saved.
     * @param deploymentCallback A {@link DeploymentAddCallback} used for notifying clients about
     *                           the status of the operation.
     * @author Ushahidi Team <team@ushahidi.com>
     */
    void addDeployment(Deployment deployment, DeploymentAddCallback deploymentCallback);

    /**
     * Get a list of {@link com.ushahidi.android.core.entity.Deployment}.
     *
     * @param deploymentListCallback A {@link DeploymentListCallback} used for notifying clients
     *                               about the status of the operation.
     */
    void getDeploymentList(DeploymentListCallback deploymentListCallback);

    /**
     * Get an {@link com.ushahidi.android.core.entity.Deployment} by id.
     *
     * @param deploymentId              The deployment id used for retrieving deployment data.
     * @param deploymentDetailsCallback A {@link DeploymentDetailsCallback} used for notifying
     *                                  clients about the status of the operation.
     */
    void getDeploymentById(final long deploymentId,
            DeploymentDetailsCallback deploymentDetailsCallback);

    /**
     * Get an {@link com.ushahidi.android.core.entity.Deployment} by its status.
     *
     * @param status              The deployment used for retrieving deployment data.
     * @param deploymentStatusCallback A {@link DeploymentDetailsCallback} used for notifying
     *                                  clients about the status of the operation.
     */
    void getDeploymentByStatus(final Deployment.Status status,
            DeploymentStatusCallback deploymentStatusCallback);

    /**
     * Update a {@link com.ushahidi.android.core.entity.Deployment}
     *
     * @param deployment The Deployment to be deleted
     * @param callback   A {@link DeploymentUpdateCallback} for notifying clients about deployment
     *                   updates status.
     */
    void updateDeployment(Deployment deployment, DeploymentUpdateCallback callback);

    /**
     * Delete a {@link com.ushahidi.android.core.entity.Deployment}
     *
     * @param deployment The deployment to be deleted.
     * @param callback   A {@link DeploymentDeletedCallback} used for notifying clients about the
     *                   delete status.
     */
    void deleteDeployment(final Deployment deployment, DeploymentDeletedCallback callback);

    /**
     * Callback used for notifying the client when either a deployment has been successfully added
     * to the database or an error occurred during the process.
     */
    interface DeploymentAddCallback {

        void onDeploymentAdded();

        void onError(ErrorWrap error);
    }

    /**
     * Callback used for notifying the client when either a deployment list has been loaded
     * successfully or an error occurred during the process.
     */
    interface DeploymentListCallback {

        void onDeploymentListLoaded(List<Deployment> deploymentList);

        void onError(ErrorWrap errorWrap);
    }

    /**
     * Callback used for notifying the client when either a deployment has been loaded or an error
     * occurred during the process.
     */
    interface DeploymentDetailsCallback {

        void onDeploymentLoaded(Deployment deployment);

        void onError(ErrorWrap errorWrap);
    }

    /**
     * Callback used for notifying the client when either a deployment has been loaded or an error
     * occurred during the process.
     */
    interface DeploymentStatusCallback {

        void onActiveDeploymentLoaded(Deployment deployment);

        void onError(ErrorWrap errorWrap);
    }

    /**
     * Callback used for notifying the client when either a deployment has been updated or failed to
     * be updated.
     */
    interface DeploymentUpdateCallback {

        void onDeploymentUpdated();

        void onError(ErrorWrap error);
    }

    /**
     * Callback used for notifying the client when either a deployment has been deleted or failed to
     * be deleted.
     */
    interface DeploymentDeletedCallback {

        void onDeploymentDeleted();

        void onError(ErrorWrap error);
    }
}
