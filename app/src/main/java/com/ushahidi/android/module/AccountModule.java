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

import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.core.usecase.account.Login;
import com.ushahidi.android.core.usecase.user.AddUser;
import com.ushahidi.android.core.usecase.user.FetchUser;
import com.ushahidi.android.ui.activity.LoginActivity;
import com.ushahidi.android.ui.fragment.LoginFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Module(library = true, complete = false, includes = DeploymentUiModule.class, injects = {
        LoginFragment.class, LoginActivity.class
})
public class AccountModule {

    @Provides
    @Singleton
    Login provideLogin(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new Login(threadExecutor, postExecutionThread);
    }

    @Provides
    @Singleton
    FetchUser provideFetchUser(ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        return new FetchUser(threadExecutor, postExecutionThread);
    }

    @Provides
    @Singleton
    AddUser provideAddUser(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new AddUser(threadExecutor, postExecutionThread);
    }
}
