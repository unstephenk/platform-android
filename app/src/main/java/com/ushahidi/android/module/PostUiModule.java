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

import com.ushahidi.android.core.entity.Post;
import com.ushahidi.android.core.repository.IDeploymentRepository;
import com.ushahidi.android.core.repository.IUserRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.core.usecase.Search;
import com.ushahidi.android.core.usecase.deployment.ActivateDeployment;
import com.ushahidi.android.core.usecase.deployment.GetActiveDeployment;
import com.ushahidi.android.core.usecase.deployment.ListDeployment;
import com.ushahidi.android.core.usecase.post.FetchPost;
import com.ushahidi.android.core.usecase.post.ListPost;
import com.ushahidi.android.core.usecase.user.ListDeploymentUsers;
import com.ushahidi.android.ui.activity.PostActivity;
import com.ushahidi.android.ui.fragment.ListPostFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module created to provide Post UI dependencies like data module, use cases, etc.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Module(library = true, complete = false, injects = {
        ListPostFragment.class, PostActivity.class
})
public final class PostUiModule {

    @Provides
    ListPost providesListPost(ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        return new ListPost(threadExecutor,
                postExecutionThread);

    }

    @Provides
    FetchPost providesFetchPost(ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        return new FetchPost(threadExecutor,
                postExecutionThread);

    }

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

    @Provides
    @Singleton
    Search<Post> providePostSearch(ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        return new Search<>(threadExecutor, postExecutionThread);
    }
}
