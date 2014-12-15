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
import com.ushahidi.android.core.usecase.deployment.ActivateDeployment;
import com.ushahidi.android.exception.ErrorMessageFactory;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.ui.view.IView;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ActivateDeploymentPresenter implements IPresenter {

    private final DeploymentModelDataMapper mDeploymentModelDataMapper;

    private final ActivateDeployment mActivateDeployment;

    private View mView;

    private final ActivateDeployment.Callback mCallback = new ActivateDeployment.Callback() {
        @Override
        public void onDeploymentActivated() {
            mView.markStatus();
        }

        @Override
        public void onError(ErrorWrap error) {
            showErrorMessage(error);
        }
    };

    @Inject
    public ActivateDeploymentPresenter(ActivateDeployment activateDeployment,
            DeploymentModelDataMapper deploymentModelDataMapper) {

        mActivateDeployment = Preconditions
                .checkNotNull(activateDeployment, "Activate deployment usecase cannot be null");
        mDeploymentModelDataMapper = Preconditions.checkNotNull(deploymentModelDataMapper,
                "DeploymentModelDataMapper cannot be null");

    }

    private void showErrorMessage(ErrorWrap errorWrap) {
        String errorMessage = ErrorMessageFactory.create(mView.getContext(),
                errorWrap.getException());
        mView.showError(errorMessage);
    }

    public void activateDeployment(List<DeploymentModel> deploymentModels, int position) {
        List<Deployment> deployments = mDeploymentModelDataMapper.unmap(deploymentModels);
        mActivateDeployment.execute(deployments, position, mCallback);
    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View cannot be null.");
        }
        mView = view;
    }

    @Override
    public void resume() {
        // Do nothing
    }

    @Override
    public void pause() {
        // Do nothing
    }

    public interface View extends IView {

        public void markStatus();
    }
}
