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

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.repository.IDeploymentRepository;
import com.ushahidi.android.data.database.DeploymentDatabaseHelper;
import com.ushahidi.android.data.database.IDeploymentDatabaseHelper;
import com.ushahidi.android.data.entity.DeploymentEntity;
import com.ushahidi.android.data.entity.mapper.DeploymentEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.data.validator.Validator;

import java.util.List;

/**
 * {@link com.ushahidi.android.core.repository.IDeploymentRepository} for retrieving deployment
 * data
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentDataRepository implements IDeploymentRepository {

    private static DeploymentDataRepository sInstance;

    private final DeploymentEntityMapper mDeploymentEntityMapper;

    private final DeploymentDatabaseHelper mDeploymentDatabaseHelper;

    private final Validator mValidator;

    protected DeploymentDataRepository(DeploymentDatabaseHelper deploymentDatabaseHelper,
            DeploymentEntityMapper entityMapper, Validator validator) {
        if (entityMapper == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        Preconditions.checkNotNull(deploymentDatabaseHelper, "DatabaseHelper cannot be null");
        Preconditions.checkNotNull(entityMapper, "Entity mapper cannot be null");
        Preconditions.checkNotNull(validator, "Validator cannot be null");
        mDeploymentEntityMapper = entityMapper;
        mDeploymentDatabaseHelper = deploymentDatabaseHelper;
        mValidator = validator;
    }

    public static synchronized DeploymentDataRepository getInstance(DeploymentDatabaseHelper
            deploymentDatabaseHelper, DeploymentEntityMapper entityMapper, Validator validator) {
        if (sInstance == null) {
            sInstance = new DeploymentDataRepository(deploymentDatabaseHelper,
                    entityMapper, validator);
        }
        return sInstance;
    }

    /**
     * {@inheritDoc}
     *
     * @param deployment         The Deployment to be saved.
     * @param deploymentCallback A {@link DeploymentAddCallback} used for notifying clients.
     */
    @Override
    public void addDeployment(Deployment deployment,
            final DeploymentAddCallback deploymentCallback) {
        // Check for required fields
        boolean isValid = true;
        if (Strings.isNullOrEmpty(deployment.getTitle())) {
            isValid = false;
            deploymentCallback.onError(new RepositoryError(
                    new ValidationException("Deployment URL cannot be null or empty")));
        }

        if (!mValidator.isValid(deployment.getUrl())) {
            isValid = false;
            deploymentCallback.onError(
                    new RepositoryError(new ValidationException("Deployment URL is invalid")));
        }

        if (isValid) {
            mDeploymentDatabaseHelper.put(mDeploymentEntityMapper.unmap(deployment),
                    new IDeploymentDatabaseHelper.IDeploymentEntityPutCallback() {

                        @Override
                        public void onDeploymentEntityPut() {
                            deploymentCallback.onDeploymentAdded();
                        }

                        @Override
                        public void onError(Exception exception) {
                            deploymentCallback.onError(new RepositoryError(exception));
                        }
                    });
        }
    }

    @Override
    public void getDeploymentList(final DeploymentListCallback deploymentListCallback) {
        mDeploymentDatabaseHelper.getDeploymentEntities(
                new IDeploymentDatabaseHelper.IDeploymentEntitiesCallback() {

                    @Override
                    public void onDeploymentEntitiesLoaded(
                            List<DeploymentEntity> deploymentEntities) {
                        final List<Deployment> deployments = mDeploymentEntityMapper
                                .map(deploymentEntities);
                        deploymentListCallback.onDeploymentListLoaded(deployments);
                    }

                    @Override
                    public void onError(Exception exception) {
                        deploymentListCallback.onError(new RepositoryError(exception));
                    }
                });
    }

    @Override
    public void getDeploymentById(long deploymentId,
            final DeploymentDetailsCallback deploymentDetailsCallback) {
        mDeploymentDatabaseHelper.get(deploymentId,
                new IDeploymentDatabaseHelper.IDeploymentEntityCallback() {

                    @Override
                    public void onDeploymentEntityLoaded(DeploymentEntity deploymentEntity) {
                        final Deployment deployment = mDeploymentEntityMapper.map(deploymentEntity);
                        deploymentDetailsCallback.onDeploymentLoaded(deployment);
                    }

                    @Override
                    public void onError(Exception exception) {
                        deploymentDetailsCallback.onError(new RepositoryError(exception));
                    }
                });
    }

    @Override
    public void getDeploymentByStatus(Deployment.Status status,
            final DeploymentStatusCallback deploymentStatusCallback) {
        mDeploymentDatabaseHelper.get(mDeploymentEntityMapper.unmap(status),
                new IDeploymentDatabaseHelper.IDeploymentEntityCallback() {

                    @Override
                    public void onDeploymentEntityLoaded(DeploymentEntity deploymentEntity) {
                        final Deployment deployment = mDeploymentEntityMapper
                                .map(deploymentEntity);
                        deploymentStatusCallback.onActiveDeploymentLoaded(deployment);
                    }

                    @Override
                    public void onError(Exception exception) {
                        deploymentStatusCallback.onError(new RepositoryError(exception));
                    }
                });
    }

    /**
     * {@inheritDoc}
     *
     * @param deployment         The Deployment to be saved.
     * @param deploymentCallback A {@link DeploymentUpdateCallback} used for notifying clients.
     */
    @Override
    public void updateDeployment(Deployment deployment,
            final DeploymentUpdateCallback deploymentCallback) {
        // Check for required fields
        boolean isValid = true;
        if (Strings.isNullOrEmpty(deployment.getTitle())) {
            isValid = false;
            deploymentCallback.onError(new RepositoryError(
                    new ValidationException("Deployment URL cannot be null or empty")));
        }

        if (!mValidator.isValid(deployment.getUrl())) {
            isValid = false;
            deploymentCallback.onError(
                    new RepositoryError(new ValidationException("Deployment URL is invalid")));
        }

        if (isValid) {
            mDeploymentDatabaseHelper.put(mDeploymentEntityMapper.unmap(deployment),
                    new IDeploymentDatabaseHelper.IDeploymentEntityPutCallback() {

                        @Override
                        public void onDeploymentEntityPut() {
                            deploymentCallback.onDeploymentUpdated();
                        }

                        @Override
                        public void onError(Exception exception) {
                            deploymentCallback.onError(new RepositoryError(exception));
                        }
                    });
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param deployment The ID of the deployment to be deleted.
     * @param callback   A {@link DeploymentDeletedCallback} used for notifying clients.
     */
    @Override
    public void deleteDeployment(final Deployment deployment,
            final DeploymentDeletedCallback callback) {
        mDeploymentDatabaseHelper.delete(mDeploymentEntityMapper.unmap(deployment),
                new IDeploymentDatabaseHelper.IDeploymentEntityDeletedCallback() {
                    @Override
                    public void onDeploymentEntityDeleted() {
                        callback.onDeploymentDeleted();
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(new RepositoryError(exception));
                    }

                });
    }
}
