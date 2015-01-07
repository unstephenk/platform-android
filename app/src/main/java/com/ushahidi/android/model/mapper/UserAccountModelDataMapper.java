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

import com.ushahidi.android.core.entity.UserAccount;
import com.ushahidi.android.model.UserAccountModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserAccountModelDataMapper {

    /**
     * Maps {@link com.ushahidi.android.data.entity.UserEntity} to {@link
     * com.ushahidi.android.core.entity.User}
     *
     * @param userAccount The {@link com.ushahidi.android.data.entity.UserEntity} to be mapped
     * @return The {@link com.ushahidi.android.core.entity.User} entity
     */
    public UserAccountModel map(UserAccount userAccount) {
        Preconditions.checkNotNull(userAccount);
        UserAccountModel userAccountModel = new UserAccountModel();

        userAccountModel.setId(userAccount.getId());
        userAccountModel.setAuthToken(userAccount.getAuthToken());
        userAccountModel.setAuthTokenType(userAccount.getAuthTokenType());
        userAccountModel.setAccountName(userAccount.getAccountName());
        userAccountModel.setPassword(userAccount.getPassword());

        return userAccountModel;
    }

    public UserAccount unmap(UserAccountModel userAccountModel) {
        Preconditions.checkNotNull(userAccountModel);
        UserAccount userAccount = new UserAccount();
        userAccount.setId(userAccountModel.getId());
        userAccount.setAuthToken(userAccountModel.getAuthToken());
        userAccount.setAuthTokenType(userAccountModel.getAuthTokenType());
        userAccount.setAccountName(userAccountModel.getAccountName());
        userAccount.setPassword(userAccountModel.getPassword());
        return userAccount;
    }

    /**
     * Maps a list {@link com.ushahidi.android.data.entity.UserEntity} into a list of {@link
     * com.ushahidi.android.core.entity.User}.
     *
     * @param userAccountList List to be mapped.
     * @return {@link com.ushahidi.android.core.entity.User}
     */
    public List<UserAccountModel> map(List<UserAccount> userAccountList) {
        List<UserAccountModel> userAccountModelList = new ArrayList<>();
        UserAccountModel userAccountModel;
        for (UserAccount userAccount : userAccountList) {
            userAccountModel = map(userAccount);
            if (userAccountModel != null) {
                userAccountModelList.add(userAccountModel);
            }
        }

        return userAccountModelList;
    }
}
