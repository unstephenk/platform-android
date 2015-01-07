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

package com.ushahidi.android.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * User Entity
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserEntity extends Entity {

    @SerializedName("id")
    private Long _id;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("realname")
    private String mRealName;

    @SerializedName("username")
    private String mUsername;

    @SerializedName("role")
    private Role mRole;

    private long mDeployment;

    @SerializedName("created")
    private Date mCreated;

    @SerializedName("updated")
    private Date mUpdated;

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getRealName() {
        return mRealName;
    }

    public void setRealName(String realName) {
        mRealName = realName;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public Role getRole() {
        return mRole;
    }

    public void setRole(Role role) {
        mRole = role;
    }

    public long getDeployment() {
        return mDeployment;
    }

    public void setDeployment(long deployment) {
        mDeployment = deployment;
    }

    public Date getCreated() {
        return mCreated;
    }

    public void setCreated(Date created) {
        mCreated = created;
    }

    public Date getUpdated() {
        return mUpdated;
    }

    public void setUpdated(Date updated) {
        mUpdated = updated;
    }

    @Override
    public String toString() {
        return "UserEntity {" +
                "_id=" + _id +
                ", mEmail='" + mEmail + '\'' +
                ", mRealName='" + mRealName + '\'' +
                ", mUsername='" + mUsername + '\'' +
                ", mRole=" + mRole +
                ", mDeployment=" + mDeployment +
                ", mCreated=" + mCreated +
                ", mUpdated=" + mUpdated +
                '}';
    }

    public enum Role {
        @SerializedName("admin")
        ADMIN("admin"),

        @SerializedName("user")
        USER("user");

        public final String value;

        Role(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
