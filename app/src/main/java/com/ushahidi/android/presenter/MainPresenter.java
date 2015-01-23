/*
 * Copyright (c) 2015 Ushahidi.
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

package com.ushahidi.android.presenter;

import com.google.common.base.Objects;

import com.squareup.otto.Subscribe;
import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.core.entity.UserAccount;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.repository.IUserAccountRepository;
import com.ushahidi.android.core.usecase.user.IListDeploymentUsers;
import com.ushahidi.android.core.usecase.user.ListDeploymentUsers;
import com.ushahidi.android.data.repository.UserAccountDataRepository;
import com.ushahidi.android.model.UserAccountModel;
import com.ushahidi.android.model.UserModel;
import com.ushahidi.android.model.mapper.UserAccountModelDataMapper;
import com.ushahidi.android.model.mapper.UserModelDataMapper;
import com.ushahidi.android.state.IUserState;
import com.ushahidi.android.ui.view.IView;
import com.ushahidi.android.util.Util;

import android.accounts.AccountManager;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MainPresenter implements IPresenter {

    private final IUserAccountRepository userAccountRepository;

    private final AccountManager mAccountManager;

    private final IUserState mUserState;

    private final ListDeploymentUsers mDeploymentUsers;

    private final UserAccountModelDataMapper mUserAccountModelDataMapper;

    private final UserModelDataMapper mUserModelDataMapper;

    private View mView;

    private UserAccountModel mUserAccountModel;

    private final IListDeploymentUsers.Callback mCallback = new IListDeploymentUsers.Callback() {
        @Override
        public void onUserListLoaded(List<User> users) {
            List<UserModel> userModels = mUserModelDataMapper.map(users);
            getActiveUserProfile(userModels);
            mView.setUserProfiles(userModels);
        }

        @Override
        public void onError(ErrorWrap error) {
            mView.showError(error.getErrorMessage());
        }
    };

    @Inject
    public MainPresenter(ListDeploymentUsers deploymentUsers, AccountManager accountManager,
            UserModelDataMapper userModelDataMapper,
            IUserState userState,
            UserAccountModelDataMapper userAccountModelDataMapper) {
        mDeploymentUsers = deploymentUsers;
        mAccountManager = accountManager;
        mUserModelDataMapper = userModelDataMapper;
        mUserState = userState;
        userAccountRepository = new UserAccountDataRepository(mAccountManager);
        mUserAccountModelDataMapper = userAccountModelDataMapper;
    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View cannot be null.");
        }
        mView = view;
    }

    @Override
    public void resume() {
        mUserState.registerEvent(this);
        mUserAccountModel = mUserState.getActiveUserAccount();

        final List<UserAccount> userAccounts =  userAccountRepository.getAccounts();
        List<UserAccountModel> userAccountModels = mUserAccountModelDataMapper.map(userAccounts);

        if(mUserAccountModel == null) {
            if (!Util.isCollectionEmpty(userAccountModels)) {
                mUserState.setActiveUserAccount(userAccountModels.get(0));
            }
        } else {
            boolean found = false;
            for (int i = 0, z = userAccountModels.size() ; i < z ; i++) {
                if (Objects.equal(userAccountModels.get(i).getAccountName(),
                        mUserAccountModel.getAccountName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                mUserState.setActiveUserAccount(null);
            }

        }
    }
    @Subscribe
    public void onActiveUserChanged(IUserState.AccountChangedEvent event) {
        mUserAccountModel = mUserState.getActiveUserAccount();
        mDeploymentUsers.execute(mUserAccountModel.getId(), mCallback);
    }

    public void onPause() {
        mUserState.unregisterEvent(this);
    }


    public void getActiveUserProfile(List<UserModel> userModels) {
        if(!Util.isCollectionEmpty(userModels) && mUserAccountModel !=null) {
            for (int i = 0, z = userModels.size() ; i < z ; i++) {
                if ((Objects.equal(userModels.get(i).getUsername(),
                        mUserAccountModel.getAccountName())&&(userModels.get(i).getDeployment()== mUserAccountModel.getId()))) {
                    mView.setActiveUserProfile(userModels.get(i));
                    break;
                }
            }
        }
    }

    @Override
    public void pause() {

    }

    public interface View extends IView {

        public void setUserProfiles(List<UserModel> userProfiles);

        public void setActiveUserProfile(UserModel userProfile);
    }
}
