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

package com.ushahidi.android.data.repository;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import com.ushahidi.android.core.entity.UserAccount;
import com.ushahidi.android.core.repository.IUserAccountRepository;
import com.ushahidi.android.data.Constants;
import com.ushahidi.android.data.entity.UserAccountEntity;
import com.ushahidi.android.data.entity.mapper.UserAccountEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.repository.datasource.account.UserAccountDataSource;
import com.ushahidi.android.data.repository.datasource.account.UserAccountDataSourceFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import static com.ushahidi.android.data.Constants.USHAHIDI_ACCOUNT_TYPE;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserAccountDataRepository implements IUserAccountRepository {

    public static final String DEPLOYMENT_ID = "user_id";

    private static UserAccountDataRepository sInstance;

    private final AccountManager mAccountManager;

    private UserAccountDataSourceFactory mUserAccountDataSourceFactory;

    private UserAccountEntityMapper mUserAccountEntityMapper;

    public UserAccountDataRepository(AccountManager accountManager) {
        mAccountManager = Preconditions.checkNotNull(accountManager,
                "accountManager cannot be null");
    }

    public UserAccountDataRepository(AccountManager accountManager,
            UserAccountDataSourceFactory userAccountDataSourceFactory,
            UserAccountEntityMapper userAccountEntityMapper) {
        mUserAccountDataSourceFactory = Preconditions.checkNotNull(userAccountDataSourceFactory,
                "User account data source cannot be null.");
        mAccountManager = Preconditions.checkNotNull(accountManager,
                "accountManager cannot be null");
        mUserAccountEntityMapper = Preconditions.checkNotNull(userAccountEntityMapper,
                "User account entity mapper cannot be null.");
    }

    public static synchronized UserAccountDataRepository getInstance(AccountManager accountManager,
            UserAccountDataSourceFactory userAccountDataSourceFactory,
            UserAccountEntityMapper userAccountEntityMapper) {
        if (sInstance == null) {
            sInstance = new UserAccountDataRepository(accountManager, userAccountDataSourceFactory,
                    userAccountEntityMapper);
        }
        return sInstance;
    }

    @Override
    public List<UserAccount> getAccounts() {
        final Account[] accounts = mAccountManager
                .getAccountsByType(USHAHIDI_ACCOUNT_TYPE);
        ArrayList<UserAccount> ushahidiAccounts = new ArrayList<>(accounts.length);

        for (int i = 0; i < accounts.length; i++) {
            final Account account = accounts[i];

            final String password = mAccountManager.getPassword(account);
            final String deploymentId = mAccountManager.getUserData(account, DEPLOYMENT_ID);
            final String accessToken = mAccountManager.peekAuthToken(account, Constants.USHAHIDI_AUTHTOKEN_BEARER_TYPE);
            UserAccount userAccount = new UserAccount();
            userAccount.setAccountName(account.name);
            userAccount.setPassword(password);
            userAccount.setAuthToken(accessToken);
            if(deploymentId != null)
            userAccount.setId(Long.valueOf(deploymentId));
            ushahidiAccounts.add(userAccount);
        }

        return ushahidiAccounts;
    }

    @Override
    public void removeAccount(UserAccount userAccount) {
        Account account = new Account(userAccount.getAccountName(), USHAHIDI_ACCOUNT_TYPE);
        mAccountManager.removeAccount(account, null, null);
    }

    @Override
    public void updatePassword(UserAccount userAccount) {
        final Account[] accounts = mAccountManager.getAccountsByType(USHAHIDI_ACCOUNT_TYPE);
        for (int i = 0; i < accounts.length; i++) {
            final Account account = accounts[i];

            if (Objects.equal(account.name, userAccount.getAccountName())) {
                mAccountManager.setPassword(account, userAccount.getPassword());
                return;
            }
        }
    }

    @Override
    public void loginUserAccount(final UserAccount userAccount,
            final UserAccountLoggedInCallback callback) {
        UserAccountEntity userAccountEntity = mUserAccountEntityMapper.unmap(userAccount);
        final UserAccountDataSource userAccountDataSource = mUserAccountDataSourceFactory
                .createUserApiDataSource();
        userAccountDataSource.loginUserAccountEntity(userAccountEntity,
                new UserAccountDataSource.UserAccountEntityLoggedInCallback() {
                    @Override
                    public void onUserAccountEntityLoggedIn(UserAccountEntity userAccountEntity) {
                        final UserAccount account = mUserAccountEntityMapper.map(userAccountEntity);
                        addUserAccount(account);
                        callback.onUserAccountLoggedIn(account);
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(new RepositoryError(e));
                    }
                });
    }

    private void addUserAccount(UserAccount userAccount) {
        Account account = new Account(userAccount.getAccountName(), USHAHIDI_ACCOUNT_TYPE);

        mAccountManager.addAccountExplicitly(account, userAccount.getPassword(),null);
        mAccountManager.setUserData(account, DEPLOYMENT_ID, String.valueOf(userAccount.getId()));
        mAccountManager.setAuthToken(account, userAccount.getAuthToken(),
                userAccount.getAuthTokenType());
    }

}
