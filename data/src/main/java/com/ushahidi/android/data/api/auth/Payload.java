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

package com.ushahidi.android.data.api.auth;

import com.ushahidi.android.data.api.qualifier.ClientId;
import com.ushahidi.android.data.api.qualifier.ClientSecret;
import com.ushahidi.android.data.api.qualifier.GrantType;
import com.ushahidi.android.data.api.qualifier.Password;
import com.ushahidi.android.data.api.qualifier.Scope;
import com.ushahidi.android.data.api.qualifier.Username;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class Payload {

    private final String username;

    private final String password;

    private final String grantType;

    private final String clientId;

    private final String clientSecret;

    private final String scope;

    public Payload(@Username String username, @Password String password,
            @GrantType String grantType, @ClientId String clientId,
            @ClientSecret String clientSecret, @Scope String scope) {
        this.username = username;
        this.password = password;
        this.grantType = grantType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scope = scope;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", grantType='" + grantType + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
