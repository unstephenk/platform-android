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

package com.ushahidi.android.model;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserAccountModel extends Model {

    private String mAccountName;

    private String mPassword;

    private String mAuthToken;

    private String mAuthTokenType;

    public void setAccountName(String accountName) {
        mAccountName = accountName;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public void setAuthToken(String authToken) {
        mAuthToken = authToken;
    }

    public void setAuthTokenType(String authTokenType) {
        mAuthTokenType = authTokenType;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setAuthToken(String authToken, String type) {
        mAuthToken = authToken;
        mAuthTokenType = type;
    }

    public String getAuthToken() {
        return mAuthToken;
    }

    public String getAuthTokenType() {
        return mAuthTokenType;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "_id=" + getId() +
                ", mAccountName='" + mAccountName + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mAuthToken='" + mAuthToken + '\'' +
                ", mAuthTokenType='" + mAuthTokenType + '\'' +
                '}';
    }
}
