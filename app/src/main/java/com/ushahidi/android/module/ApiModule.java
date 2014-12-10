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
import com.ushahidi.android.data.api.ApiHeader;
import com.ushahidi.android.data.api.service.PostService;
import com.ushahidi.android.data.api.qualifier.Bearer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Module(
        complete = false,
        library = true
)
public final class ApiModule {

    //TODO: Get this from a shared preference
    public static final String SAMPLE_DEPLOYMENT_URL = "http://192.168.6.14:8081";

    //TODO: Get this from AccountManager
    public static final String  bearer = "password";

    @Provides
    @Singleton
    @Bearer
    String provideBearer() {
        return bearer;
    }

    @Provides
    @Singleton
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(SAMPLE_DEPLOYMENT_URL);
    }

    @Provides
    @Singleton
    Client provideClient(OkHttpClient client) {
        return new OkClient(client);
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint, Client client, ApiHeader header) {
        return new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(endpoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(header)
                .build();
    }

    @Provides
    @Singleton
    PostService providePostService(RestAdapter restAdapter) {
        return restAdapter.create(PostService.class);
    }
}
