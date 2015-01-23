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

import com.google.gson.annotations.SerializedName;


/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AccessToken extends BaseResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    private Long expires;

    @SerializedName("expires_in")
    private Long expiresIn;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("refresh_token_expires_in")
    private Long refreshTokenExpiresIn;

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpires() {
        return expires;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    @Override
    public String toString() {

        if (super.getError() != null) {
            return "AccessToken{error='" + super.getError() + ", errorDescription='" + super
                    .getErrorDescription() + "}";
        }

        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expires=" + expires +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", refreshTokenExpiresIn=" + refreshTokenExpiresIn +
                '}';
    }
}

