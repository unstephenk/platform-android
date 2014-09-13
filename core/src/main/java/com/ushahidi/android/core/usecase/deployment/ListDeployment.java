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
import com.ushahidi.android.core.respository.IDeploymentRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;

import java.util.List;

/**
 * Implements {@link com.ushahidi.android.core.usecase.deployment.IListDeployment} that fetches list
 * of {@link com.ushahidi.android.core.entity.Deployment}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ListDeployment implements IListDeployment {

    private final IDeploymentRepository mIDeploymentRepository;

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final IDeploymentRepository.DeploymentListCallback mRepositoryCallback
            = new IDeploymentRepository.DeploymentListCallback() {

        @Override
        public void onDeploymentListLoaded(List<Deployment> deploymentList) {
            notifySuccess(deploymentList);
        }

        @Override
        public void onError(ErrorWrap error) {
            notifyFailure(error);
        }
    };

    private Callback mCallback;

    /**
     * Constructor.
     *
     * @param deploymentRepository A {@link com.ushahidi.android.core.respository.IDeploymentRepository}
     *                             as a source for retrieving data.
     * @param threadExecutor       {@link ThreadExecutor} used to execute this use case in a
     *                             background thread.
     * @param postExecutionThread  {@link PostExecutionThread} used to post updates when the use
     *                             case has been executed.
     */
    public ListDeployment(IDeploymentRepository deploymentRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (deploymentRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        mIDeploymentRepository = deploymentRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be null!!!");
        }
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    @Override
    public void run() {
        mIDeploymentRepository.getDeploymentList(mRepositoryCallback);
    }

    private void notifySuccess(final List<Deployment> deploymentList) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onDeploymentListLoaded(deploymentList);
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
