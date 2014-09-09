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

package com.ushahidi.android.core.usecase.deployment;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.executor.PostExecutionThread;
import com.ushahidi.android.core.executor.ThreadExecutor;
import com.ushahidi.android.core.respository.IDeploymentRepository;

/**
 * Use case for adding a new deployment
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddDeployment implements IAddDeployment{

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final IDeploymentRepository mDeploymentRepository;

    private Deployment mDeployment;

    private Callback mCallback;

    public AddDeployment(IDeploymentRepository deploymentRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (deploymentRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mDeploymentRepository = deploymentRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(Deployment deployment, Callback callback) {
        if(deployment == null) {
            throw new IllegalArgumentException("Deployment cannot be null");
        }

        if(callback == null) {
            throw new IllegalArgumentException("Use case callback cannot be null");
        }

        mDeployment = deployment;
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    @Override
    public void run() {
        mDeploymentRepository.addDeployment(mDeployment, mRepositoryCallback);
    }

    private final IDeploymentRepository.DeploymentAddCallback mRepositoryCallback =
            new IDeploymentRepository.DeploymentAddCallback() {

                @Override
                public void onDeploymentAdded(Deployment deployment) {
                    notifySuccess(deployment);
                }

                @Override
                public void onError(ErrorWrap error) {
                    notifyFailure(error);
                }

            };

    private void notifySuccess(final Deployment deployment) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onDeploymentAdded(deployment);
            }
        });
    }

    private void notifyFailure(final ErrorWrap errorWrap) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(errorWrap);
            }
        });
    }
}
