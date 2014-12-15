package com.ushahidi.android.ui.fragment;

import com.andreabaccega.widget.FormEditText;
import com.ushahidi.android.R;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.presenter.AddDeploymentPresenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddDeploymentFragment extends BaseFragment implements AddDeploymentPresenter.View {

    @Inject
    AddDeploymentPresenter mAddDeploymentPresenter;

    @InjectView(R.id.add_deployment_title)
    FormEditText title;

    @InjectView(R.id.add_deployment_url)
    FormEditText url;

    public static final String ADD_FRAGMENT_TAG = "add_fragment";

    private AddDeploymentListener mActionListener;

    /**
     * Add Deployment  Fragment
     */
    public AddDeploymentFragment() {
        super(R.layout.fragment_add_deployment, R.menu.add_deployment);
    }

    public static AddDeploymentFragment newInstance() {
        AddDeploymentFragment addDeploymentFragment = new AddDeploymentFragment();
        return addDeploymentFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AddDeploymentListener) {
            this.mActionListener = (AddDeploymentListener) activity;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        url.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                if (TextUtils.isEmpty(url.getText().toString())) {
                    url.setText("http://");
                }

                return false;
            }

        });
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
        mAddDeploymentPresenter.setView(this);
    }


    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showError(String message) {
        showToast(message);
    }

    @Override
    public void navigateOrReloadList() {
        if (mActionListener != null) {
            mActionListener.onAddNavigateOrReloadList();
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
            mActionListener.onCancelAdd();
        }
    }

    /**
     * Listens for Add Deployment events
     */
    public interface AddDeploymentListener {

        /**
         * Executes when a button is pressed to either navigate away from the screen or reload an
         * existing list.
         */
        void onAddNavigateOrReloadList();

        /**
         * Executes when a button is pressed to either cancel.
         */
        void onCancelAdd();
    }
}