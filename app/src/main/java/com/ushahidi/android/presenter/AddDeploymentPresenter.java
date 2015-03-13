package com.ushahidi.android.presenter;

import com.google.common.base.Preconditions;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.deployment.AddDeployment;
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
public class AddDeploymentPresenter implements IPresenter {

    private final AddDeployment mAddDeployment;

    private final DeploymentModelDataMapper mDeploymentModelDataMapper;

    private final AddDeployment.Callback mCallback = new AddDeployment.Callback() {

        @Override
        public void onDeploymentAdded() {
            mView.navigateOrReloadList();
        }

        @Override
        public void onError(ErrorWrap error) {
            showErrorMessage(error);
        }
    };

    private View mView;

    @Inject
    public AddDeploymentPresenter(AddDeployment addDeployment,
            DeploymentModelDataMapper deploymentModelDataMapper) {
        mAddDeployment = Preconditions.checkNotNull(addDeployment, "AddDeployment cannot be null");
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

    }

    @Override
    public void pause() {

    }

    public void addDeployment(DeploymentModel deploymentModel) {
        final Deployment deployment =
                mDeploymentModelDataMapper.unmap(deploymentModel);
        mAddDeployment.execute(deployment, mCallback);
    }

    private void showErrorMessage(ErrorWrap errorWrap) {
        String errorMessage = ErrorMessageFactory.create(mView.getAppContext(),
                errorWrap.getException());
        mView.showError(errorMessage);
    }

    /**
     * Interface for adding a deployment to the database
     *
     * @author Ushahidi Team <team@ushahidi.com>
     */
    public interface View extends IView {

        /**
         * Reloads the list or navigates to a different screen depending on the device
         */
        public void navigateOrReloadList();
    }
}
