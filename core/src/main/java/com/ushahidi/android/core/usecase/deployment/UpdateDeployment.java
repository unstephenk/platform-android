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
import com.ushahidi.android.core.repository.IDeploymentRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;

/**
 * Implements update deployment use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UpdateDeployment implements IUpdateDeployment {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final IDeploymentRepository mDeploymentRepository;

    private final IDeploymentRepository.DeploymentUpdateCallback mDeploymentUpdateCallback =
            new IDeploymentRepository.DeploymentUpdateCallback() {

                @Override
                public void onDeploymentUpdated() {
                    notifySuccess();
                }

                @Override
                public void onError(ErrorWrap error) {
                    notifyFailure(error);
                }
            };

    private Deployment mDeployment;

    private Callback mCallback;

    public UpdateDeployment(IDeploymentRepository deploymentRepository,
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (deploymentRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mDeploymentRepository = deploymentRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(Deployment deployment, IUpdateDeployment.Callback callback) {
        if (deployment == null) {
            throw new IllegalArgumentException("Deployment cannot be null");
        }

        if (callback == null) {
            throw new IllegalArgumentException("Use case callback cannot be null");
        }

        mDeployment = deployment;
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    @Override
    public void run() {
        mDeploymentRepository.updateDeployment(mDeployment, mDeploymentUpdateCallback);
    }

    /**
     * Notifies client when a successful update occurs.
     */
    private void notifySuccess() {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onDeploymentUpdated();
            }
        });
    }

    /**
     * Notifies client of any failure that may occuring during the update process
     *
     * @param errorWrap The {@link com.ushahidi.android.core.exception.ErrorWrap}
     */
    private void notifyFailure(final ErrorWrap errorWrap) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(errorWrap);
            }
        });
    }
}
