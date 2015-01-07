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

package com.ushahidi.android.data.api;

import com.ushahidi.android.data.api.qualifier.Bearer;

import javax.inject.Inject;

import retrofit.RequestInterceptor;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public final class ApiHeader implements RequestInterceptor {

    private static final String AUTHORIZATION_BEARER = "Bearer";

    private final String authorizationValue;

    public ApiHeader(@Bearer String bearer) {
        authorizationValue = AUTHORIZATION_BEARER + " " + bearer;
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Accept", "application/json");
        request.addHeader("Authorization", authorizationValue);
    }
}
