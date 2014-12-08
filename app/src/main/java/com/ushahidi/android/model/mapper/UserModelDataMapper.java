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

package com.ushahidi.android.model.mapper;

import com.google.common.base.Preconditions;

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps User to UserModel and vice versa
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserModelDataMapper {

    public UserModelDataMapper() {
        // Do nothing
    }

    /**
     * Maps {@link com.ushahidi.android.model.UserModel} to {@link
     * com.ushahidi.android.core.entity.User}
     *
     * @param user The {@link com.ushahidi.android.core.entity.User} to be mapped
     * @return The {@link com.ushahidi.android.model.UserModel} entity
     */
    public UserModel map(User user) {

        Preconditions.checkNotNull(user, "User entity cannot be null");
        UserModel userModel = new UserModel();
        userModel.setId(user.getId());
        userModel.setEmail(user.getEmail());
        userModel.setRealName(user.getRealName());
        userModel.setUsername(user.getUsername());
        userModel.setRole(UserModel.Role.valueOf(user.getRole().name()));
        userModel.setUpdated(user.getUpdated());
        userModel.setCreated(user.getCreated());
        userModel.setDeployment(user.getDeployment());

        return userModel;
    }

    public User unmap(UserModel userModel) {
        Preconditions.checkNotNull(userModel, "Cannot unmap a null value");
        User user = new User();
        user.setId(userModel.getId());
        user.setEmail(userModel.getEmail());
        user.setRealName(userModel.getRealName());
        user.setUsername(userModel.getUsername());
        user.setRole(User.Role.valueOf(userModel.getRole().name()));
        user.setUpdated(userModel.getUpdated());
        user.setCreated(userModel.getCreated());
        user.setDeployment(userModel.getDeployment());

        return user;
    }

    /**
     * Maps a list {@link com.ushahidi.android.model.UserModel} into a list of {@link
     * com.ushahidi.android.core.entity.User}.
     *
     * @param userList List to be mapped.
     * @return {@link com.ushahidi.android.model.UserModel}
     */
    public List<UserModel> map(List<User> userList) {
        List<UserModel> userModelList = new ArrayList<>();
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                userModelList.add(map(user));
            }
        }
        return userModelList;
    }
}
