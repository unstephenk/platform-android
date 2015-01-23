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

import com.squareup.otto.Bus;
import com.ushahidi.android.UshahidiApplication;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This module provides every application scope dependencies related with the AndroidSDK.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Module(
        includes = {
                DataModule.class,
                ExecutorModule.class,
                StateModule.class
        },
        injects = {
                UshahidiApplication.class
        }, library = true)
public final class UshahidiModule {

    private final Context mContext;

    public UshahidiModule(Context context) {
        mContext = context;
    }

    @Provides
    Context provideApplicationContext() {
        return mContext;
    }
}
