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

package com.ushahidi.android.module;

import com.ushahidi.android.core.repository.IDeploymentRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.core.usecase.deployment.AddDeployment;
import com.ushahidi.android.core.usecase.deployment.DeleteDeployment;
import com.ushahidi.android.core.usecase.deployment.GetDeployment;
import com.ushahidi.android.core.usecase.deployment.ListDeployment;
import com.ushahidi.android.core.usecase.deployment.UpdateDeployment;
import com.ushahidi.android.ui.activity.AddDeploymentActivity;
import com.ushahidi.android.ui.activity.DeploymentActivity;
import com.ushahidi.android.ui.activity.UpdateDeploymentActivity;
import com.ushahidi.android.ui.fragment.AddDeploymentFragment;
import com.ushahidi.android.ui.fragment.ListDeploymentFragment;
import com.ushahidi.android.ui.fragment.UpdateDeploymentFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module created to provide Deployment UI dependencies like data module, use cases, etc.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Module(library = true, complete = false, injects = {
        ListDeploymentFragment.class, DeploymentActivity.class,
        AddDeploymentFragment.class, AddDeploymentActivity.class,
        UpdateDeploymentFragment.class, UpdateDeploymentActivity.class
})
public final class DeploymentUiModule {

    @Provides
    ListDeployment providesListDeployment(IDeploymentRepository deploymentRepository,
            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new ListDeployment(deploymentRepository, threadExecutor,
                postExecutionThread);

    }

    @Provides
    GetDeployment providesGetDeployment(IDeploymentRepository deploymentRepository,
            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetDeployment(deploymentRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    DeleteDeployment providesDeleteDeployment(IDeploymentRepository deploymentRepository,
            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new DeleteDeployment(deploymentRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @Singleton
    AddDeployment providesAddDeployment(IDeploymentRepository deploymentRepository,
            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new AddDeployment(deploymentRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @Singleton
    UpdateDeployment providesUpdateDeployment(IDeploymentRepository deploymentRepository,
            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new UpdateDeployment(deploymentRepository, threadExecutor, postExecutionThread);
    }

}
