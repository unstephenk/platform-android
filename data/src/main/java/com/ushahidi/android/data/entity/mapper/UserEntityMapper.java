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

package com.ushahidi.android.data.entity.mapper;

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.data.entity.UserEntity;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps UserEntity to User entity in core module
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserEntityMapper {

    public UserEntityMapper() {
        // Do nothing
    }

    /**
     * Maps {@link com.ushahidi.android.data.entity.UserEntity} to {@link
     * com.ushahidi.android.core.entity.User}
     *
     * @param userEntity The {@link com.ushahidi.android.data.entity.UserEntity} to be mapped
     * @return The {@link com.ushahidi.android.core.entity.User} entity
     */
    public User map(UserEntity userEntity) {
        User user = new User();
        if (userEntity != null) {
            user.setId(userEntity.getId());
            user.setEmail(userEntity.getEmail());
            user.setRealName(userEntity.getRealName());
            user.setUsername(userEntity.getUsername());
            user.setRole(User.Role.valueOf(userEntity.getRole().name()));
            user.setUpdated(userEntity.getUpdated());
            user.setCreated(userEntity.getCreated());
            user.setDeployment(userEntity.getDeployment());
        }

        return user;
    }

    public UserEntity unmap(User user) {
        UserEntity userEntity = new UserEntity();

        if (user != null) {
            userEntity.setId(user.getId());
            userEntity.setEmail(user.getEmail());
            userEntity.setRealName(user.getRealName());
            userEntity.setUsername(user.getUsername());
            userEntity.setRole(UserEntity.Role.valueOf(user.getRole().name()));
            userEntity.setUpdated(user.getUpdated());
            userEntity.setCreated(user.getCreated());
            userEntity.setDeployment(user.getDeployment());
        }
        return userEntity;
    }

    /**
     * Maps a list {@link com.ushahidi.android.data.entity.UserEntity} into a list of {@link
     * com.ushahidi.android.core.entity.User}.
     *
     * @param userEntityList List to be mapped.
     * @return {@link com.ushahidi.android.core.entity.User}
     */
    public List<User> map(List<UserEntity> userEntityList) {
        List<User> userList = new ArrayList<>();
        User user;
        for (UserEntity userEntity : userEntityList) {
            user = map(userEntity);
            if (user != null) {
                userList.add(user);
            }
        }

        return userList;
    }
}
