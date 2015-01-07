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

import com.squareup.okhttp.OkHttpClient;
import com.ushahidi.android.Util.PrefsUtils;
import com.ushahidi.android.data.api.ApiHeader;
import com.ushahidi.android.data.api.qualifier.Bearer;
import com.ushahidi.android.data.api.service.PostService;
import com.ushahidi.android.data.api.service.UserService;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.ui.prefs.Prefs;

import java.util.Timer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import timber.log.Timber;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Module(
        complete = false,
        library = true
)
public final class ApiModule {

    @Provides
    @Singleton
    Client provideClient(OkHttpClient client) {
        return new OkClient(client);
    }
}
