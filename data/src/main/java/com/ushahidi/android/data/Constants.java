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

package com.ushahidi.android.data;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class Constants {

    public static final String USHAHIDI_ACCOUNT_TYPE = "com.ushahidi.android.account";

    public static final String USHAHIDI_AUTHTOKEN_PASSWORD_TYPE = "password";

    public static final String USHAHIDI_AUTHTOKEN_BEARER_TYPE = "Bearer";

    public static final String OAUTH_CLIENT_SECRET = "35e7f0bca957836d05ca0492211b0ac707671261";

    public static final String OAUTH_CLIENT_ID = "ushahidiui";

    public static final String SCOPE = "posts media forms api tags sets users stats layers config messages dataproviders";

    public static final String API_PATH = "/api/v2";

    public static final String POSTS = API_PATH + "/posts";

    public static final String USERS_ME = API_PATH + "/users/me";
}
