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
 * Deletes deployment
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeleteDeployment implements IDeleteDeployment {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private IDeleteDeployment.Callback mCallback;

    private final IDeploymentRepository mDeploymentRepository;

    private Deployment mDeployment;

    public DeleteDeployment(IDeploymentRepository deploymentRepository,
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (deploymentRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mDeploymentRepository = deploymentRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    private final IDeploymentRepository.DeploymentDeletedCallback mDeploymentDeletedCallback =
            new IDeploymentRepository.DeploymentDeletedCallback() {

                @Override
                public void onDeploymentDeleted() {
                    notifySuccess();
                }

                @Override
                public void onError(ErrorWrap error) {
                    notifyFailure(error);
                }
            };

    @Override
    public void execute(Deployment deployment, IDeleteDeployment.Callback callback) {
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
        mDeploymentRepository.deleteDeployment(mDeployment, mDeploymentDeletedCallback);
    }

    private void notifySuccess() {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onDeploymentDeleted();
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
