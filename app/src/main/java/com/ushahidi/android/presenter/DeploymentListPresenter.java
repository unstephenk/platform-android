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
import com.ushahidi.android.ui.view.IDeploymentListView;

import java.util.List;

/**
 * {@link IPresenter} facilitates interactions between deployment list view and deployment models.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentListPresenter implements IPresenter {

    private final IDeploymentListView mIDeploymentListView;

    private final DeploymentModelDataMapper mDeploymentModelDataMapper;

    private final ListDeployment mListDeployment;

    private final ListDeployment.Callback mListCallback = new ListDeployment.Callback() {

        @Override
        public void onDeploymentListLoaded(List<Deployment> listDeployment) {
            showDeploymentsListInView(listDeployment);
            hideViewLoading();
        }

        @Override
        public void onError(ErrorWrap error) {
            hideViewLoading();
            showErrorMessage(error);
            showViewRetry();
        }
    };

    public DeploymentListPresenter(IDeploymentListView deploymentListView,
            ListDeployment listDeployment,
            DeploymentModelDataMapper deploymentModelDataMapper) {
        if (deploymentListView == null || listDeployment == null
                || deploymentModelDataMapper == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }

        mListDeployment = listDeployment;
        mIDeploymentListView = deploymentListView;
        mDeploymentModelDataMapper = deploymentModelDataMapper;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    public void init() {
        loadList();
    }

    private void loadList() {
        hideViewRetry();
        showViewLoading();
        getDeploymentList();
    }

    public void onDeploymentClicked(DeploymentModel deploymentModel) {
        // TODO implement when a deployment is clicked
    }

    private void showViewLoading() {
        mIDeploymentListView.showLoading();
    }

    private void hideViewLoading() {
        mIDeploymentListView.hideLoading();
    }

    private void showViewRetry() {
        mIDeploymentListView.showRetry();
    }

    private void hideViewRetry() {
        mIDeploymentListView.hideRetry();
    }

    private void showErrorMessage(ErrorWrap errorWrap) {
        String errorMessage = ErrorMessageFactory.create(mIDeploymentListView.getContext(),
                errorWrap.getException());
        mIDeploymentListView.showError(errorMessage);
    }

    private void showDeploymentsListInView(List<Deployment> listDeployments) {
        final List<DeploymentModel> deploymentModelsList =
                mDeploymentModelDataMapper.map(listDeployments);
        mIDeploymentListView.renderDeploymentList(deploymentModelsList);
    }

    private void getDeploymentList() {
        mListDeployment.execute(mListCallback);
    }
}
