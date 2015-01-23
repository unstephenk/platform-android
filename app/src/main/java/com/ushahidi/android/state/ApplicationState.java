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

package com.ushahidi.android.state;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import com.squareup.otto.Bus;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.UserAccountModel;
import com.ushahidi.android.model.UserModel;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ApplicationState implements Istate, IUserState, IDeploymentState {

    private final Bus mEventBus;

    private UserAccountModel mUserAccountModel;

    private UserModel mUserModel;

    private DeploymentModel mDeploymentModel;

    public ApplicationState(Bus eventBus) {
        mEventBus = Preconditions.checkNotNull(eventBus, "eventBus cannot null");
    }


    @Override
    public void setUserProfile(UserModel userProfile) {
        if (!Objects.equal(userProfile, mUserModel)) {
            mUserModel = userProfile;
            mEventBus.post(new UserProfileChangedEvent());
        }
    }

    @Override
    public UserModel getUserModel() {
        return mUserModel;
    }

    @Override
    public DeploymentModel getActiveDeployment() {
        return mDeploymentModel;
    }

    @Override
    public void setActiveUserAccount(UserAccountModel account) {
        if (!Objects.equal(mUserAccountModel, account)) {
            mUserAccountModel = account;
            mEventBus.post(new AccountChangedEvent());
        }
    }

    @Override
    public void unauthorized() {
        mEventBus.post(new UnauthorizedAccessEvent());
    }

    @Override
    public UserAccountModel getActiveUserAccount() {
        return mUserAccountModel;
    }

    @Override
    public void registerEvent(Object receiver) {
        mEventBus.register(receiver);
    }

    @Override
    public void unregisterEvent(Object receiver) {
        mEventBus.unregister(receiver);
    }

    @Override
    public void setActiveDeployment(DeploymentModel deploymentModel) {
        if (!Objects.equal(mDeploymentModel, deploymentModel)) {
            mDeploymentModel = deploymentModel;
            mEventBus.post(new ActivatedDeploymentChangedEvent());
        }
    }

    public void onSwipe() {
        mEventBus.post(new SwipeRefreshEvent());
    }

    public static class SwipeRefreshEvent {

    }
}
