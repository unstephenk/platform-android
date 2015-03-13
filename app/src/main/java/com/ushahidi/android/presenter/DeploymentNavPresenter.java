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

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.deployment.ListDeployment;
import com.ushahidi.android.exception.ErrorMessageFactory;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.ui.view.IView;

import java.util.List;

import javax.inject.Inject;

/**
 * {@link com.ushahidi.android.presenter.IPresenter} facilitates interactions between deployment
 * list view and deployment models.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentNavPresenter implements IPresenter {

    private final DeploymentModelDataMapper mDeploymentModelDataMapper;

    private final ListDeployment mListDeployment;

    private final ListDeployment.Callback mListCallback = new ListDeployment.Callback() {

        @Override
        public void onDeploymentListLoaded(List<Deployment> listDeployment) {
            showDeploymentsListInView(listDeployment);
        }

        @Override
        public void onError(ErrorWrap error) {
            showErrorMessage(error);
        }
    };

    private View mView;

    private List<DeploymentModel> mDeployments;

    @Inject
    public DeploymentNavPresenter(
            ListDeployment listDeployment,
            DeploymentModelDataMapper deploymentModelDataMapper) {
        if (listDeployment == null
                || deploymentModelDataMapper == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }

        mListDeployment = listDeployment;
        mDeploymentModelDataMapper = deploymentModelDataMapper;
    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View cannot be null.");
        }
        mView = view;
    }

    private void showErrorMessage(ErrorWrap errorWrap) {
        String errorMessage = ErrorMessageFactory.create(mView.getAppContext(),
                errorWrap.getException());
        mView.showError(errorMessage);
    }

    private void showDeploymentsListInView(List<Deployment> listDeployments) {
        final List<DeploymentModel> deploymentModelsList =
                mDeploymentModelDataMapper.map(listDeployments);
        mView.renderDeploymentList(deploymentModelsList);
        setDeployments(deploymentModelsList);
        mView.createNav();
    }

    private void getDeploymentList() {
        mListDeployment.execute(mListCallback);

    }

    public List<DeploymentModel> getDeployments() {
        return mDeployments;
    }

    private void setDeployments(List<DeploymentModel> deploymentList) {
        mDeployments = deploymentList;
    }

    @Override
    public void resume() {
        getDeploymentList();
    }

    @Override
    public void pause() {

    }

    public interface View extends IView {

        /**
         * Render a deployment list in the UI.
         *
         * @param deploymentModel The collection of {@link com.ushahidi.android.model.DeploymentModel}
         *                        that will be shown.
         */
        public void renderDeploymentList(List<DeploymentModel> deploymentModel);

        /**
         * Shows an error message
         *
         * @param message A string resource representing an error.
         */
        public void showError(String message);

        public void createNav();

    }
}
