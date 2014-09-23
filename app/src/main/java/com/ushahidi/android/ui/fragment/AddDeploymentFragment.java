package com.ushahidi.android.ui.fragment;

import com.andreabaccega.widget.FormEditText;
import com.ushahidi.android.R;
import com.ushahidi.android.core.usecase.deployment.AddDeployment;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.presenter.AddDeploymentPresenter;
import com.ushahidi.android.ui.activity.DeploymentActivity;
import com.ushahidi.android.ui.view.IAddDeploymentView;

import android.content.Context;
import android.content.Intent;
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
    AddDeployment mAddDeployment;

    private AddDeploymentPresenter mAddDeploymentPresenter;

    @InjectView(R.id.add_deployment_title)
    FormEditText title;

    @InjectView(R.id.add_deployment_url)
    FormEditText url;

    /**
     * Add Deployment  Fragment
     */
    public AddDeploymentFragment() {
        super(R.layout.fragment_add_deployment, R.menu.add_deployment);
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
    public void navigateToReportListing() {
        Intent intentToLaunch = new Intent(getContext(), DeploymentActivity.class);
        getActivity().startActivity(intentToLaunch);
        getActivity().finish();
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
        getActivity().finish();
    }
}
