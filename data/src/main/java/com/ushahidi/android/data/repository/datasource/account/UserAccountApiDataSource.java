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

package com.ushahidi.android.data.repository.datasource.account;

import com.ushahidi.android.data.api.UserApi;
import com.ushahidi.android.data.api.auth.AccessToken;
import com.ushahidi.android.data.api.auth.Payload;
import com.ushahidi.android.data.entity.UserAccountEntity;

import static com.ushahidi.android.data.Constants.OAUTH_CLIENT_ID;
import static com.ushahidi.android.data.Constants.OAUTH_CLIENT_SECRET;
import static com.ushahidi.android.data.Constants.SCOPE;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserAccountApiDataSource implements UserAccountDataSource {

    private final UserApi mUserApi;

    public UserAccountApiDataSource(UserApi userApi) {
        mUserApi = userApi;
    }

    @Override
    public void loginUserAccountEntity(final UserAccountEntity userAccountEntity,
            final UserAccountEntityLoggedInCallback callback) {
        Payload payload = new Payload(userAccountEntity.getAccountName(),
                userAccountEntity.getPassword(), userAccountEntity.getAuthTokenType(),
                OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, SCOPE);
        mUserApi.loginUserAccount(payload, new UserApi.UserAccountLoggedInCallback() {
            @Override
            public void onUserAccountLoggedIn(AccessToken accessToken) {
                userAccountEntity.setAuthToken(accessToken.getAccessToken());
                userAccountEntity.setAuthTokenType(accessToken.getTokenType());
                callback.onUserAccountEntityLoggedIn(userAccountEntity);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}
