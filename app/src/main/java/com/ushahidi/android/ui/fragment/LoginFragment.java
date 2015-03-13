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

import com.andreabaccega.widget.FormAutoCompleteTextView;
import com.andreabaccega.widget.FormEditText;
import com.ushahidi.android.R;
import com.ushahidi.android.data.Constants;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.UserAccountModel;
import com.ushahidi.android.presenter.LoginPresenter;
import com.ushahidi.android.ui.adapter.DeploymentSpinnerAdapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class LoginFragment extends BaseFragment
        implements LoginPresenter.View, RadioGroup.OnCheckedChangeListener {

    public static final String LOGIN_FRAGMENT_TAG = "login_fragment";

    @InjectView(R.id.login_username)
    FormEditText mUsername;

    @InjectView(R.id.login_password)
    FormEditText mPassword;

    @InjectView(R.id.login_submit_btn)
    Button mLoginButton;

    @InjectView(R.id.select_deployment)
    Spinner mSpinner;

    @InjectView(R.id.login_type)
    RadioGroup mTypeRadioGroup;

    @InjectView(R.id.radio_btn_login)
    RadioButton mLoginRadioButton;

    @InjectView(R.id.radio_btn_register)
    RadioButton mRegisterRadioButton;

    @InjectView(R.id.active_email)
    FormAutoCompleteTextView mEmailAutoComplete;

    @Inject
    LoginPresenter mLoginPresenter;

    private DeploymentSpinnerAdapter mDeploymentSpinnerArrayAdapter;

    private DeploymentModel mSelectedDeploymentModel;

    private UserAccountModel mUserAccountModel;

    private LoginListener mLoginListener;

    public LoginFragment() {
        super(R.layout.fragment_login, 0);
    }

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof LoginListener) {
            mLoginListener = (LoginListener) activity;
        }
    }

    @Override
    void initPresenter() {
        mLoginPresenter.setView(this);
        init();
    }

    public void init() {
        mTypeRadioGroup.setOnCheckedChangeListener(this);
        mLoginRadioButton.setChecked(true);
        mEmailAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEmailAutoComplete.showDropDown();
            }
        });
        mLoginPresenter.getDeploymentList();
    }

    @OnClick(R.id.login_submit_btn)
    public void onClickSubmit() {
        submit();
    }

    @OnItemSelected(R.id.select_deployment)
    public void onItemSelected(int position) {
        mSelectedDeploymentModel = mDeploymentSpinnerArrayAdapter.getDeploymentModels()
                .get(position);
        mLoginPresenter.setSelectedDeployment(mSelectedDeploymentModel);
    }


    private void submit() {

        mPassword.setError(null);

        if (validateForms(mUsername, mPassword)) {
            switch (mTypeRadioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_btn_login:
                    UserAccountModel userAccountModel = new UserAccountModel();
                    userAccountModel.setAccountName(mUsername.getText().toString().trim());
                    userAccountModel.setPassword(mPassword.getText().toString().trim());
                    userAccountModel.setAuthTokenType(Constants.USHAHIDI_AUTHTOKEN_PASSWORD_TYPE);
                    mLoginPresenter.performLogin(userAccountModel);

                    break;
                case R.id.radio_btn_register:
                    final String email = mEmailAutoComplete.getText().toString().trim();
                    if (validateForms(mEmailAutoComplete)) {
                        //TODO implement register
                    }
                    break;
            }
        }
    }

    @Override
    public void fetchUserProfile(UserAccountModel userAccountModel) {
        mUserAccountModel = userAccountModel;
        mLoginPresenter.getUserProfile(mUserAccountModel.getAuthToken());
    }

    @Override
    public void loggedIn(Long userId) {
        if (mLoginListener != null && mUserAccountModel != null) {
            mUserAccountModel.setId(userId);
            mLoginListener.finish();
            mLoginListener.setAccountAuthenticatorResult(mUserAccountModel,
                    mSelectedDeploymentModel.getId());
        }
    }

    @Override
    public void deploymentList(List<DeploymentModel> deploymentModelList) {
        mDeploymentSpinnerArrayAdapter = new DeploymentSpinnerAdapter(getAppContext(),
                deploymentModelList);
        mSpinner.setAdapter(mDeploymentSpinnerArrayAdapter);

        // Select active deployment by default
        mSpinner.setSelection(mLoginPresenter.getActiveDeploymentPosition(deploymentModelList));
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
    public Context getAppContext() {
        return getActivity();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.radio_btn_login:
                mLoginButton.setText(R.string.login);
                mEmailAutoComplete.setText(null);
                mEmailAutoComplete.setVisibility(View.GONE);

                // Set actionbar title to performLogin
                getActivity().setTitle(getResources().getString(R.string.login));
                break;
            case R.id.radio_btn_register:
                mLoginButton.setText(R.string.register);
                mEmailAutoComplete.setVisibility(View.VISIBLE);

                // Set actionbar title to register
                getActivity().setTitle(getResources().getString(R.string.register));
                if (mEmailAutoComplete.getAdapter() == null) {
                    final Set<String> emailSet = new HashSet<>();
                    for (Account account : AccountManager.get(getActivity()).getAccounts()) {
                        if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                            emailSet.add(account.name);
                        }
                    }
                    List<String> emails = new ArrayList<>(emailSet);
                    mEmailAutoComplete.setAdapter(new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_dropdown_item, emails));
                }
                break;
        }
    }

    public interface LoginListener {

        void finish();

        void setAccountAuthenticatorResult(UserAccountModel userAccountModel, Long deploymentId);
    }
}
