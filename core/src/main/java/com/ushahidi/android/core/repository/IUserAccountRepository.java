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

package com.ushahidi.android.core.repository;

import com.ushahidi.android.core.entity.UserAccount;
import com.ushahidi.android.core.exception.ErrorWrap;

import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface IUserAccountRepository {

    List<UserAccount> getAccounts();

    void removeAccount(UserAccount userAccount);

    void updatePassword(UserAccount userAccount);

    /**
     * Login a {@link com.ushahidi.android.core.entity.User}
     *
     * @param userAccount The userAccount to be logged.
     * @param callback    A {@link UserAccountLoggedInCallback} used for notifying clients about the
     *                    delete status.
     */
    void loginUserAccount(final UserAccount userAccount, UserAccountLoggedInCallback callback);

    /**
     * Callback used for notifying the client when either a user has been deleted or failed to be
     * deleted.
     */
    interface UserAccountLoggedInCallback {

        void onUserAccountLoggedIn(UserAccount userAccount);

        void onError(ErrorWrap error);
    }

    interface UserAccountRemoveCallback {

        void onUserAccountRemoved(UserAccount userAccount);
    }

    interface UserAccountUpdatePassword {

        void onUserAccountPasswordUpdated(UserAccount userAccount);
    }
}
