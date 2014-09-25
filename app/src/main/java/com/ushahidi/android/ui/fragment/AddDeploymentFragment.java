package com.ushahidi.android.ui.fragment;

import com.andreabaccega.widget.FormEditText;
import com.ushahidi.android.R;
import com.ushahidi.android.core.usecase.deployment.AddDeployment;
import com.ushahidi.android.core.usecase.deployment.UpdateDeployment;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.presenter.AddDeploymentPresenter;
import com.ushahidi.android.ui.view.IAddDeploymentView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddDeploymentFragment extends BaseFragment implements IAddDeploymentView {

    @Inject
    DeploymentModelDataMapper mDeploymentModelDataMapper;

    @Inject
    UpdateDeployment mUpdateDeployment;

    @Inject
    AddDeployment mAddDeployment;

    @InjectView(R.id.add_deployment_title)
    FormEditText title;

    @InjectView(R.id.add_deployment_url)
    FormEditText url;

    private ActionListener mActionListener;

    private AddDeploymentPresenter mAddDeploymentPresenter;

    /**
     * Add Deployment  Fragment
     */
    public AddDeploymentFragment() {
        super(R.layout.fragment_add_deployment, R.menu.add_deployment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ActionListener) {
            this.mActionListener = (ActionListener) activity;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAddDeploymentPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAddDeploymentPresenter.pause();
    }

    @Override
    void initPresenter() {

        mAddDeploymentPresenter = new AddDeploymentPresenter(this, mAddDeployment,
                mUpdateDeployment,
                mDeploymentModelDataMapper);
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        showToast(message);
    }

    @Override
    public void navigateOrReloadList() {
        if (mActionListener != null) {
            mActionListener.onNavigateOrReloadList();
        }
    }

    @OnClick(R.id.add_deployment_add)
    public void onClickValidate() {

        if (validateForms(title, url)) {
            DeploymentModel deploymentModel = new DeploymentModel();
            deploymentModel.setTitle(title.getText().toString());
            deploymentModel.setUrl(url.getText().toString());
            mAddDeploymentPresenter.addDeployment(deploymentModel);
        }

    }

    @OnClick(R.id.add_deployment_cancel)
    public void onClickCancel() {
        if (mActionListener != null) {
            mActionListener.onActionCancelOrClearExecuted();
        }
    }

    /**
     * Listener interface when a button pressed
     */
    public interface ActionListener {

        /**
         * Executes when a button is pressed to either navigate away from the screen or reload an
         * existing list.
         */
        void onNavigateOrReloadList();

        /**
         * Executes when a button is pressed to either cancel or clear.
         */
        void onActionCancelOrClearExecuted();
    }
}
