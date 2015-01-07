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

import com.ushahidi.android.core.entity.UserAccount;
import com.ushahidi.android.data.entity.UserAccountEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserAccountEntityMapper {

    /**
     * Maps {@link com.ushahidi.android.data.entity.UserEntity} to {@link
     * com.ushahidi.android.core.entity.User}
     *
     * @param userAccountEntity The {@link com.ushahidi.android.data.entity.UserEntity} to be
     *                          mapped
     * @return The {@link com.ushahidi.android.core.entity.User} entity
     */
    public UserAccount map(UserAccountEntity userAccountEntity) {
        UserAccount userAccount = new UserAccount();
        if (userAccountEntity != null) {
            userAccount.setId(userAccountEntity.getId());
            userAccount.setAuthToken(userAccountEntity.getAuthToken());
            userAccount.setAuthTokenType(userAccountEntity.getAuthTokenType());
            userAccount.setAccountName(userAccountEntity.getAccountName());
            userAccount.setPassword(userAccountEntity.getPassword());

        }
        return userAccount;
    }

    public UserAccountEntity unmap(UserAccount userAccount) {
        UserAccountEntity userAccountEntity = new UserAccountEntity();

        if (userAccount != null) {
            userAccountEntity.setId(userAccount.getId());
            userAccountEntity.setAuthToken(userAccount.getAuthToken());
            userAccountEntity.setAuthTokenType(userAccount.getAuthTokenType());
            userAccountEntity.setAccountName(userAccount.getAccountName());
            userAccountEntity.setPassword(userAccount.getPassword());
        }
        return userAccountEntity;
    }

    /**
     * Maps a list {@link com.ushahidi.android.data.entity.UserEntity} into a list of {@link
     * com.ushahidi.android.core.entity.User}.
     *
     * @param userAccountEntityList List to be mapped.
     * @return {@link com.ushahidi.android.core.entity.User}
     */
    public List<UserAccount> map(List<UserAccountEntity> userAccountEntityList) {
        List<UserAccount> userAccountList = new ArrayList<>();
        UserAccount userAccount;
        for (UserAccountEntity userAccountEntity : userAccountEntityList) {
            userAccount = map(userAccountEntity);
            if (userAccount != null) {
                userAccountList.add(userAccount);
            }
        }

        return userAccountList;
    }
}
