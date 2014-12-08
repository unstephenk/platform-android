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

import com.ushahidi.android.core.respository.IDeploymentRepository;
import com.ushahidi.android.core.respository.IPostRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.core.usecase.deployment.ListDeployment;
import com.ushahidi.android.core.usecase.post.ListPost;
import com.ushahidi.android.data.database.DeploymentDatabaseHelper;
import com.ushahidi.android.data.database.PostDatabaseHelper;
import com.ushahidi.android.data.entity.mapper.DeploymentEntityMapper;
import com.ushahidi.android.data.entity.mapper.PostEntityMapper;
import com.ushahidi.android.data.repository.DeploymentDataRepository;
import com.ushahidi.android.data.repository.PostDataRepository;
import com.ushahidi.android.data.task.TaskExecutor;
import com.ushahidi.android.data.validator.UrlValidator;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.model.mapper.PostModelDataMapper;
import com.ushahidi.android.ui.UiThread;
import com.ushahidi.android.ui.activity.PostActivity;
import com.ushahidi.android.ui.fragment.ListPostFragment;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module created to provide Post UI dependencies like data module, use cases, etc.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
//TODO Improve this class. To many code repetitions
@Module(library = true, complete = false, injects = {
        ListPostFragment.class, PostActivity.class
})
public final class PostUiModule {


    @Provides
    ListPost providesListPost(Context context) {

        ThreadExecutor threadExecutor = TaskExecutor.getInstance();
        PostExecutionThread postExecutionThread = UiThread.getInstance();

        PostEntityMapper entityMapper = new PostEntityMapper();

        PostDatabaseHelper postDatabaseHelper = PostDatabaseHelper
                .getInstance(context,
                        threadExecutor);
        IPostRepository postRepository = PostDataRepository
                .getInstance(postDatabaseHelper, entityMapper);

        return new ListPost(postRepository, threadExecutor,
                postExecutionThread);

    }

    @Provides
    ListDeployment providesListDeployment(Context context) {

        ThreadExecutor threadExecutor = TaskExecutor.getInstance();
        PostExecutionThread postExecutionThread = UiThread.getInstance();

        DeploymentEntityMapper entityMapper = new DeploymentEntityMapper();

        DeploymentDatabaseHelper deploymentDatabaseHelper = DeploymentDatabaseHelper
                .getInstance(context,
                        threadExecutor);
        UrlValidator urlValidator = new UrlValidator();
        IDeploymentRepository deploymentRepository = DeploymentDataRepository
                .getInstance(deploymentDatabaseHelper, entityMapper, urlValidator);

        return new ListDeployment(deploymentRepository, threadExecutor,
                postExecutionThread);

    }

    @Provides
    PostModelDataMapper providesPostModelDataMapper() {
        return new PostModelDataMapper();
    }

    @Provides
    DeploymentModelDataMapper providesDeploymentModelDataMapper() {
        return new DeploymentModelDataMapper();
    }

}
