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
import com.ushahidi.android.core.entity.UserAccount;
import com.ushahidi.android.core.respository.IDeploymentRepository;
import com.ushahidi.android.core.respository.IPostRepository;
import com.ushahidi.android.core.respository.IUserAccountRepository;
import com.ushahidi.android.core.respository.IUserRepository;
import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.data.api.service.PostService;
import com.ushahidi.android.data.api.service.UserService;
import com.ushahidi.android.data.database.DeploymentDatabaseHelper;
import com.ushahidi.android.data.database.PostDatabaseHelper;
import com.ushahidi.android.data.database.UserDatabaseHelper;
import com.ushahidi.android.data.entity.mapper.DeploymentEntityMapper;
import com.ushahidi.android.data.entity.mapper.PostEntityMapper;
import com.ushahidi.android.data.entity.mapper.UserAccountEntityMapper;
import com.ushahidi.android.data.entity.mapper.UserEntityMapper;
import com.ushahidi.android.data.repository.DeploymentDataRepository;
import com.ushahidi.android.data.repository.PostDataRepository;
import com.ushahidi.android.data.repository.UserAccountDataRepository;
import com.ushahidi.android.data.repository.UserDataRepository;
import com.ushahidi.android.data.repository.datasource.account.UserAccountDataSourceFactory;
import com.ushahidi.android.data.repository.datasource.post.PostDataSourceFactory;
import com.ushahidi.android.data.validator.UrlValidator;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.model.mapper.PostModelDataMapper;
import com.ushahidi.android.model.mapper.UserAccountModelDataMapper;
import com.ushahidi.android.model.mapper.UserModelDataMapper;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

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

    static OkHttpClient createOkHttpClient(Context app) {
        OkHttpClient client = new OkHttpClient();
        try {
            File cacheDir = new File(app.getApplicationContext().getCacheDir(), "ushahidi-android-http-cache");
            Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
            client.setCache(cache);
        } catch (IOException e) {
            Timber.e(e, "Unable to install disk cache.");
        }

        return client;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context app) {
        return app.getApplicationContext().getSharedPreferences("ushahidi-android-shared-prefs", MODE_PRIVATE);
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
    @Singleton
    PostDatabaseHelper providesPostDatabaseHelper(Context context, ThreadExecutor threadExecutor) {
        return PostDatabaseHelper.getInstance(context,threadExecutor);
    }

    @Provides
    IDeploymentRepository providesDeploymentRepository(
            DeploymentDatabaseHelper deploymentDatabaseHelper, DeploymentEntityMapper entityMapper,
            UrlValidator urlValidator) {
        return DeploymentDataRepository
                .getInstance(deploymentDatabaseHelper, entityMapper, urlValidator);
    }

    @Provides
    IUserRepository provideUserRepository(
            UserDatabaseHelper userDatabaseHelper, UserEntityMapper entityMapper,
            UrlValidator urlValidator) {
        return UserDataRepository
                .getInstance(userDatabaseHelper, entityMapper, urlValidator);
    }

    @Provides
    @Singleton
    PostEntityMapper providesPostEntityMapper(){
        return new PostEntityMapper();
    }

    @Provides
    @Singleton
    UserEntityMapper provideUserEntityMapper(){
        return new UserEntityMapper();
    }

    @Provides
    @Singleton
    UserAccountEntityMapper provideUserAccountEntityMapper(){
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
        return UserDatabaseHelper.getInstance(context,threadExecutor);
    }

    @Provides
    @Singleton
    public AccountManager provideAccountManager(Context context) {
        return AccountManager.get(context);
    }

}
