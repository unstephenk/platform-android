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

import android.app.Application;
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
            File cacheDir = new File(app.getApplicationContext().getCacheDir(), "http");
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
        return app.getApplicationContext().getSharedPreferences("ushahidi-android", MODE_PRIVATE);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Context app) {
        return createOkHttpClient(app.getApplicationContext());
    }
}
