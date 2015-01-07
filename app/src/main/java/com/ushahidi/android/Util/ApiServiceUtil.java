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

package com.ushahidi.android.Util;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.ushahidi.android.data.api.ApiHeader;
import com.ushahidi.android.data.api.Date;
import com.ushahidi.android.data.api.DateDeserializer;

import javax.inject.Inject;

import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.converter.GsonConverter;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ApiServiceUtil {


    private Client mClient;

    @Inject
    public ApiServiceUtil(Client client) {
        mClient = client;
    }

    public <T> T createService(Class<T> serviceClass, String baseUrl,
            final String accessToken) {

        Preconditions.checkNotNull(serviceClass);

        ApiHeader header = new ApiHeader(accessToken);
        Endpoint endpoint = Endpoints.newFixedEndpoint(baseUrl);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateDeserializer());
        Gson gson = builder.create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setClient(mClient)
                .setEndpoint(endpoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(header)
                .build();

        return restAdapter.create(serviceClass);
    }
}
