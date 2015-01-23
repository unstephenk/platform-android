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

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.ushahidi.android.core.repository.IDeploymentRepository;
import com.ushahidi.android.core.repository.IPostRepository;
import com.ushahidi.android.core.repository.IUserRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.core.usecase.user.ListDeploymentUsers;
import com.ushahidi.android.data.database.DeploymentDatabaseHelper;
import com.ushahidi.android.data.database.PostDatabaseHelper;
import com.ushahidi.android.data.database.UserDatabaseHelper;
import com.ushahidi.android.data.entity.mapper.DeploymentEntityMapper;
import com.ushahidi.android.data.entity.mapper.PostEntityMapper;
import com.ushahidi.android.data.entity.mapper.UserAccountEntityMapper;
import com.ushahidi.android.data.entity.mapper.UserEntityMapper;
import com.ushahidi.android.data.repository.DeploymentDataRepository;
import com.ushahidi.android.data.repository.PostDataRepository;
import com.ushahidi.android.data.repository.UserDataRepository;
import com.ushahidi.android.data.repository.datasource.post.PostDataSourceFactory;
import com.ushahidi.android.data.repository.datasource.user.UserDataSourceFactory;
import com.ushahidi.android.data.validator.UrlValidator;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.model.mapper.PostModelDataMapper;
import com.ushahidi.android.model.mapper.UserAccountModelDataMapper;
import com.ushahidi.android.model.mapper.UserModelDataMapper;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Module(
        includes = ApiModule.class,
        complete = false,
        library = true
)
public class DataModule {

    static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    private static OkHttpClient createOkHttpClient(Context app) {
        OkHttpClient client = new OkHttpClient();

        File cacheDir = new File(app.getApplicationContext().getCacheDir(),
                    "ushahidi-android-http-cache");

        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);

        client.setCache(cache);
        return client;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context app) {
        return app.getApplicationContext()
                .getSharedPreferences("ushahidi-android-shared-prefs", MODE_PRIVATE);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Context app) {
        return createOkHttpClient(app.getApplicationContext());
    }

    @Provides
    @Singleton
    UrlValidator providesUrlValidator() {
        return new UrlValidator();
    }

    @Provides
    @Singleton
    DeploymentEntityMapper providesDeploymentEntityMapper() {
        return new DeploymentEntityMapper();
    }

    @Provides
    @Singleton
    DeploymentModelDataMapper providesDeploymentModelDataMapper() {
        return new DeploymentModelDataMapper();
    }

    @Provides
    @Singleton
    DeploymentDatabaseHelper providesDeploymentDatabaseHelper(Context context,
            ThreadExecutor threadExecutor) {
        return DeploymentDatabaseHelper.getInstance(context, threadExecutor);
    }

    @Provides
    IDeploymentRepository providesDeploymentRepository(
            DeploymentDatabaseHelper deploymentDatabaseHelper, DeploymentEntityMapper entityMapper,
            UrlValidator urlValidator) {
        return DeploymentDataRepository
                .getInstance(deploymentDatabaseHelper, entityMapper, urlValidator);
    }

    @Provides
    IUserRepository providesUserRepository(
            UserDataSourceFactory userDataSourceFactory, UserEntityMapper entityMapper,
            UrlValidator urlValidator) {
        return UserDataRepository.getInstance(userDataSourceFactory, entityMapper, urlValidator);
    }

    @Provides
    IPostRepository providePostRepository(PostDataSourceFactory postDataSourceFactory, PostEntityMapper entityMapper) {
        return PostDataRepository.getInstance(postDataSourceFactory, entityMapper);
    }

    @Provides
    @Singleton
    PostEntityMapper providesPostEntityMapper() {
        return new PostEntityMapper();
    }

    @Provides
    @Singleton
    UserEntityMapper provideUserEntityMapper() {
        return new UserEntityMapper();
    }

    @Provides
    @Singleton
    UserAccountEntityMapper provideUserAccountEntityMapper() {
        return new UserAccountEntityMapper();
    }

    @Provides
    @Singleton
    PostModelDataMapper providesPostModelDataMapper() {
        return new PostModelDataMapper();
    }

    @Provides
    @Singleton
    UserModelDataMapper provideUserModelDataMapper() {
        return new UserModelDataMapper();
    }

    @Provides
    @Singleton
    UserAccountModelDataMapper provideUserAccountModelDataMapper() {
        return new UserAccountModelDataMapper();
    }

    @Provides
    @Singleton
    UserDatabaseHelper provideUserDatabaseHelper(Context context, ThreadExecutor threadExecutor) {
        return UserDatabaseHelper.getInstance(context, threadExecutor);
    }

    @Provides
    @Singleton
    PostDatabaseHelper providePostDatabaseHelper(Context context, ThreadExecutor threadExecutor) {
        return PostDatabaseHelper.getInstance(context, threadExecutor);
    }

    @Provides
    @Singleton
    public AccountManager provideAccountManager(Context context) {
        return AccountManager.get(context);
    }

    @Provides
    @Singleton
    public UserDataSourceFactory provideUserDataSourceFactory(Context context, UserDatabaseHelper userDatabaseHelper) {
        return new UserDataSourceFactory(context, userDatabaseHelper);
    }

    @Provides
    @Singleton
    public PostDataSourceFactory providePostDataSourceFactory(Context context, PostDatabaseHelper postDatabaseHelper) {
        return  new PostDataSourceFactory(context, postDatabaseHelper);
    }

    @Provides
    @Singleton
    ListDeploymentUsers provideListDeploymentUsers(IUserRepository userRepository,
            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new ListDeploymentUsers(userRepository, threadExecutor, postExecutionThread);
    }
}
