/*
 *  Copyright (c) 2015 Ushahidi.
 *
 *   This program is free software: you can redistribute it and/or modify it under
 *   the terms of the GNU Affero General Public License as published by the Free
 *   Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 *   This program is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *   FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program in the file LICENSE-AGPL. If not, see
 *   https://www.gnu.org/licenses/agpl-3.0.html
 *
 */

package com.ushahidi.android.module;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */

import com.ushahidi.android.core.repository.IDeploymentRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.core.usecase.deployment.ActivateDeployment;
import com.ushahidi.android.core.usecase.deployment.GetActiveDeployment;
import com.ushahidi.android.core.usecase.deployment.ListDeployment;
import com.ushahidi.android.ui.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module created to provide Main UI dependencies like data module, use cases, etc.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Module(library = true, complete = false, injects = {
    MainActivity.class
})
public final class MainUiModule {

    @Provides
    ListDeployment providesListDeployment(IDeploymentRepository deploymentRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new ListDeployment(deploymentRepository, threadExecutor,
            postExecutionThread);

    }

    @Provides
    @Singleton
    ActivateDeployment providesActivateDeployment(IDeploymentRepository deploymentRepository,
                                                  ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new ActivateDeployment(deploymentRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @Singleton
    GetActiveDeployment providesActiveDeployment(IDeploymentRepository deploymentRepository,
                                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetActiveDeployment(deploymentRepository, threadExecutor, postExecutionThread);
    }
}
