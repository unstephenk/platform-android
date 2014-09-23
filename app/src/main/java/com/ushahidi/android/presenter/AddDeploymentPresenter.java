package com.ushahidi.android.presenter;

import com.google.common.base.Preconditions;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.deployment.AddDeployment;
import com.ushahidi.android.exception.ErrorMessageFactory;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.ui.view.IAddDeploymentView;

/**
 * Facilitates interactions between between add deployment view and deployment models
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddDeploymentPresenter implements IPresenter {

    private final IAddDeploymentView mIAddDeploymentView;

    private final AddDeployment mAddDeployment;

    private final DeploymentModelDataMapper mDeploymentModelDataMapper;

    private final AddDeployment.Callback mCallback = new AddDeployment.Callback() {

        @Override
        public void onDeploymentAdded() {
            mIAddDeploymentView.hideLoading();
            mIAddDeploymentView.navigateToReportListing();
        }

        @Override
        public void onError(ErrorWrap error) {
            showErrorMessage(error);
        }
    };

    public AddDeploymentPresenter(IAddDeploymentView addDeploymentView, AddDeployment addDeployment,
            DeploymentModelDataMapper deploymentModelDataMapper) {
        Preconditions.checkNotNull(addDeployment, "IAddDeploymentView cannot be null");
        Preconditions.checkNotNull(addDeployment, "AddDeployment cannot be null");
        Preconditions.checkNotNull(deploymentModelDataMapper,
                "DeploymentModelDataMapper cannot be null");
        mIAddDeploymentView = addDeploymentView;
        mAddDeployment = addDeployment;
        mDeploymentModelDataMapper = deploymentModelDataMapper;

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    public void addDeployment(DeploymentModel deploymentModel) {
        mIAddDeploymentView.showLoading();
        final Deployment deployment =
                mDeploymentModelDataMapper.unmap(deploymentModel);
        mAddDeployment.execute(deployment, mCallback);
    }

    private void showErrorMessage(ErrorWrap errorWrap) {
        String errorMessage = ErrorMessageFactory.create(mIAddDeploymentView.getContext(),
                errorWrap.getException());
        mIAddDeploymentView.showError(errorMessage);
    }
}
