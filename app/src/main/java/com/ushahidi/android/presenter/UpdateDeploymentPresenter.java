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

import com.google.common.base.Preconditions;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.deployment.GetDeployment;
import com.ushahidi.android.core.usecase.deployment.UpdateDeployment;
import com.ushahidi.android.exception.ErrorMessageFactory;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.ui.view.IView;

import javax.inject.Inject;

/**
 * Facilitates interactions between between add deployment view and deployment models
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UpdateDeploymentPresenter implements IPresenter {

    private final UpdateDeployment mUpdateDeployment;

    private final GetDeployment mGetDeployment;

    private final DeploymentModelDataMapper mDeploymentModelDataMapper;

    private final UpdateDeployment.Callback mUpdateCallback = new UpdateDeployment.Callback() {

        @Override
        public void onDeploymentUpdated() {
            mView.navigateOrReloadList();
        }

        @Override
        public void onError(ErrorWrap error) {
            showErrorMessage(error);
        }
    };

    private final GetDeployment.Callback mGetDeploymentCallback = new GetDeployment.Callback() {

        @Override
        public void onDeploymentLoaded(Deployment deployment) {
            initForm(deployment);
        }

        @Override
        public void onError(ErrorWrap error) {
            showErrorMessage(error);
        }
    };

    private View mView;

    @Inject
    public UpdateDeploymentPresenter(
            UpdateDeployment updateDeployment,
            GetDeployment getDeployment,
            DeploymentModelDataMapper deploymentModelDataMapper) {
        mGetDeployment = Preconditions.checkNotNull(getDeployment, "GetDeployment cannot be null");
        mUpdateDeployment = Preconditions
                .checkNotNull(updateDeployment, "UpdateDeployment cannot be null");
        mDeploymentModelDataMapper = Preconditions.checkNotNull(deploymentModelDataMapper,
                "DeploymentModelDataMapper cannot be null");
    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View cannot be null.");
        }
        mView = view;
    }

    public void init(long deploymentId) {
        mGetDeployment.execute(deploymentId, mGetDeploymentCallback);
    }

    private void initForm(Deployment deployment) {
        final DeploymentModel deploymentModel = mDeploymentModelDataMapper.map(deployment);
        mView.initForm(deploymentModel);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }


    private void showErrorMessage(ErrorWrap errorWrap) {
        String errorMessage = ErrorMessageFactory.create(mView.getContext(),
                errorWrap.getException());
        mView.showError(errorMessage);
    }

    public void updateDeployment(DeploymentModel deploymentModel) {
        final Deployment deployment = mDeploymentModelDataMapper.unmap(deploymentModel);
        mUpdateDeployment.execute(deployment, mUpdateCallback);
    }

    /**
     * Adds a deployment to a database
     *
     * @author Ushahidi Team <team@ushahidi.com>
     */
    public interface View extends IView {

        /**
         * Navigates or reloads deployment lists
         */
        void navigateOrReloadList();

        /**
         * Initializes the form with {@link com.ushahidi.android.model.DeploymentModel}
         */
        void initForm(DeploymentModel deploymentModel);
    }
}