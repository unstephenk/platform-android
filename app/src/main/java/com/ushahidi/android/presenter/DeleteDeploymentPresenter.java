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
import com.ushahidi.android.core.usecase.deployment.DeleteDeployment;
import com.ushahidi.android.exception.ErrorMessageFactory;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.ui.prefs.Prefs;
import com.ushahidi.android.ui.view.IView;

import javax.inject.Inject;

/**
 * Presenter for deployment deletion. Facilitates communication between DeploymentListing view
 * and Deployment models and Use cases.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeleteDeploymentPresenter implements IPresenter {

    private final DeploymentModelDataMapper mDeploymentModelDataMapper;

    private final DeleteDeployment mDeleteDeployment;

    @Inject
    Prefs mPrefs;

    private DeploymentModel mDeploymentModelToBeDeleted;

    private final DeleteDeployment.Callback mDeleteCallback = new DeleteDeployment.Callback() {

        @Override
        public void onDeploymentDeleted() {
            mView.onDeploymentDeleted();
            cleanActiveDeploymentState();
        }

        @Override
        public void onError(ErrorWrap error) {
            showErrorMessage(error);
        }
    };
    
    private void cleanActiveDeploymentState() {
        if (mDeploymentModelToBeDeleted != null && mDeploymentModelToBeDeleted.getStatus() == DeploymentModel.Status.ACTIVATED) {
            // Delete the active deployment URL saved in the shared prefs
            mPrefs.getActiveDeploymentUrl().delete();

            // Delete the active user's account
            mPrefs.getActiveUserAccount().delete();

            mPrefs.getAccessToken().delete();
        }
    }

    private View mView;

    @Inject
    public DeleteDeploymentPresenter(DeleteDeployment deleteDeployment,
                                     DeploymentModelDataMapper deploymentModelDataMapper) {

        mDeleteDeployment = Preconditions
            .checkNotNull(deleteDeployment, "DeleteDeployment cannot be null");
        mDeploymentModelDataMapper = Preconditions.checkNotNull(deploymentModelDataMapper,
            "DeploymentModelDataMapper cannot be null");

    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View cannot be null.");
        }
        mView = view;
    }


    @Override
    public void resume() {
        // DO nothing
    }

    @Override
    public void pause() {
        // Do nothing
    }

    public void deleteDeployment(DeploymentModel deploymentModel) {
        mDeploymentModelToBeDeleted = deploymentModel;
        final Deployment deployment = mDeploymentModelDataMapper.unmap(deploymentModel);
        mDeleteDeployment.execute(deployment, mDeleteCallback);

    }

    private void showErrorMessage(ErrorWrap errorWrap) {
        String errorMessage = ErrorMessageFactory.create(mView.getAppContext(),
            errorWrap.getException());
        mView.showError(errorMessage);
    }

    /**
     * @author Ushahidi Team <team@ushahidi.com>
     */
    public interface View extends IView {

        void onDeploymentDeleted();

        /**
         * Shows an error message
         *
         * @param message A string resource representing an error.
         */
        void showError(String message);

    }
}
