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

import com.ushahidi.android.data.api.Date;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AccessToken extends BaseResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    private Date expires;

    @SerializedName("expires_in")
    private Date expiresIn;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("refresh_token_expires_in")
    private Date refreshTokenExpiresIn;

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Date getExpires() {
        return expires;
    }

    public Date getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Date getRefreshTokenExpiresIn() {
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

