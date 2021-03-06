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

package com.ushahidi.android.presenter;

import android.text.TextUtils;

import com.google.common.base.Preconditions;
import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.deployment.ActivateDeployment;
import com.ushahidi.android.core.usecase.deployment.GetActiveDeployment;
import com.ushahidi.android.core.usecase.deployment.ListDeployment;
import com.ushahidi.android.exception.ErrorMessageFactory;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.ui.activity.ActivityLauncher;
import com.ushahidi.android.ui.prefs.Prefs;
import com.ushahidi.android.ui.view.IView;
import com.ushahidi.android.util.Util;

import java.util.List;

import javax.inject.Inject;

/**
 * Facilitates interactions between DeploymentActivity and DeploymentModels and Use cases
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ActivateDeploymentPresenter implements IPresenter {

    private final DeploymentModelDataMapper mDeploymentModelDataMapper;

    private final ActivateDeployment mActivateDeployment;

    private final GetActiveDeployment mGetActiveDeployment;

    @Inject
    ListDeployment mListDeployment;

    @Inject
    ActivityLauncher launcher;

    @Inject
    Prefs mPrefs;

    private final ActivateDeployment.Callback mCallback = new ActivateDeployment.Callback() {
        @Override
        public void onDeploymentActivated() {
            mView.markStatus();
            fetchActivateDeployment();
        }

        @Override
        public void onError(ErrorWrap error) {
            showErrorMessage(error);
        }
    };

    private final GetActiveDeployment.Callback mGetActiveDeploymentCallback
        = new GetActiveDeployment.Callback() {

        @Override
        public void onActiveDeploymentLoaded(Deployment deployment) {
            final DeploymentModel deploymentModel = mDeploymentModelDataMapper.map(deployment);
            mView.getActiveDeployment(deploymentModel);
        }

        @Override
        public void onError(ErrorWrap error) {
            showErrorMessage(error);
        }
    };

    private final ListDeployment.Callback mListDeploymentCallback = new ListDeployment.Callback() {

        @Override
        public void onDeploymentListLoaded(List<Deployment> listDeployment) {
            // Deployment List is empty, launch activity for adding a new deployment.
            if (Util.isCollectionEmpty(listDeployment)) {
                // Launch Activity to add a new deployment.
                launcher.launchAddDeployment();
            } else {
                // Make the first deployment an active one.
                activateDeployment(mDeploymentModelDataMapper.map(listDeployment), 0);
            }

        }

        @Override
        public void onError(ErrorWrap error) {
            mView.showError(error.getErrorMessage());
        }
    };

    private View mView;

    @Inject
    public ActivateDeploymentPresenter(ActivateDeployment activateDeployment,
                                       GetActiveDeployment getActiveDeployment,
                                       DeploymentModelDataMapper deploymentModelDataMapper) {
        mActivateDeployment = Preconditions
            .checkNotNull(activateDeployment, "Activate deployment usecase cannot be null");
        mGetActiveDeployment = Preconditions.checkNotNull(getActiveDeployment,
            "Get active deployment cannot be null");
        mDeploymentModelDataMapper = Preconditions.checkNotNull(deploymentModelDataMapper,
            "DeploymentModelDataMapper cannot be null");

    }

    private void showErrorMessage(ErrorWrap errorWrap) {
        String errorMessage = ErrorMessageFactory.create(mView.getAppContext(),
            errorWrap.getException());
        mView.showError(errorMessage);
    }

    public void activateDeployment(List<DeploymentModel> deploymentModels, int position) {
        List<Deployment> deployments = mDeploymentModelDataMapper.unmap(deploymentModels);
        mActivateDeployment.execute(deployments, position, mCallback);
    }

    public void fetchActivateDeployment() {
        mGetActiveDeployment.execute(mGetActiveDeploymentCallback);
    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View cannot be null.");
        }
        mView = view;
    }

    @Override
    public void resume() {
        checkForActiveDeployment();
        fetchActivateDeployment();
    }

    @Override
    public void pause() {
        // Do nothing
    }

    /**
     * Checks if there is an active deployment, otherwise make the first item in the deployment
     * the active one.
     */
    private void checkForActiveDeployment() {
        if (TextUtils.isEmpty(mPrefs.getActiveDeploymentUrl().get())) {
            // Get all
            mListDeployment.execute(mListDeploymentCallback);
        }
    }

    public interface View extends IView {

        void markStatus();

        void getActiveDeployment(DeploymentModel deploymentModel);

    }
}
