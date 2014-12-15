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

package com.ushahidi.android.ui.fragment;

import com.andreabaccega.widget.FormEditText;
import com.ushahidi.android.R;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.presenter.UpdateDeploymentPresenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UpdateDeploymentFragment extends BaseFragment
        implements UpdateDeploymentPresenter.View {

    public static final String UPDATE_FRAGMENT_TAG = "update_fragment";

    private static final String ARGUMENT_KEY_DEPLOYMENT_ID
            = "com.ushahidi.android.ARGUMENT_DEPLOYMENT_ID";

    @Inject
    UpdateDeploymentPresenter mUpdateDeploymentPresenter;

    @InjectView(R.id.add_deployment_title)
    FormEditText title;

    @InjectView(R.id.add_deployment_url)
    FormEditText url;

    @InjectView(R.id.add_deployment_add)
    Button button;

    private DeploymentUpdateListener mActionListener;

    private Long mDeploymentId;

    /**
     * Add Deployment  Fragment
     */
    public UpdateDeploymentFragment() {
        super(R.layout.fragment_add_deployment, R.menu.add_deployment);
    }

    public static UpdateDeploymentFragment newInstance(Long deploymentId) {
        UpdateDeploymentFragment updateDeploymentFragment = new UpdateDeploymentFragment();

        Bundle argumentBundle = new Bundle();
        argumentBundle.putLong(ARGUMENT_KEY_DEPLOYMENT_ID, deploymentId);
        updateDeploymentFragment.setArguments(argumentBundle);
        return updateDeploymentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DeploymentUpdateListener) {
            mActionListener = (DeploymentUpdateListener) activity;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mUpdateDeploymentPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mUpdateDeploymentPresenter.pause();
    }

    @Override
    void initPresenter() {
        mUpdateDeploymentPresenter.setView(this);
        mUpdateDeploymentPresenter.init(mDeploymentId);
        initUpdate();
    }

    private void init() {
        mDeploymentId = getArguments().getLong(ARGUMENT_KEY_DEPLOYMENT_ID);

    }

    private void initUpdate() {
        button.setText(R.string.update);
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
            mActionListener.onUpdateNavigateOrReloadList();
        }
    }

    @Override
    public void initForm(DeploymentModel deploymentModel) {
        title.setText(deploymentModel.getTitle());
        url.setText(deploymentModel.getUrl());
    }

    @OnClick(R.id.add_deployment_add)
    public void onClickValidate() {

        if (validateForms(title, url)) {
            DeploymentModel deploymentModel = new DeploymentModel();
            deploymentModel.setTitle(title.getText().toString());
            deploymentModel.setUrl(url.getText().toString());
            deploymentModel.setId(mDeploymentId);
            mUpdateDeploymentPresenter.updateDeployment(deploymentModel);
        }

    }

    @OnClick(R.id.add_deployment_cancel)
    public void onClickCancel() {
        if (mActionListener != null) {
            mActionListener.onCancelUpdate();
        }
    }

    /**
     * Listens for Update deployments events
     */
    public interface DeploymentUpdateListener {

        /**
         * Executes when a button is pressed to either navigate away from the screen or reload an
         * existing list.
         */
        void onUpdateNavigateOrReloadList();

        /**
         * Executes when a button is pressed to either cancel or clear.
         */
        void onCancelUpdate();
    }
}