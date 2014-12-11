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

import com.ushahidi.android.R;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class LoginFragment extends BaseFragment {

    @InjectView(R.id.login_username)
    EditText mUsername;

    @InjectView(R.id.login_password)
    EditText mPassword;

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
    AutoCompleteTextView mEmailAutoComplete;

    public static final String LOGIN_FRAGMENT_TAG = "login_fragment";

    public LoginFragment() {
        super(R.layout.fragment_login, 0);
    }

    @Override
    void initPresenter() {

    }

    @OnClick(R.id.login_submit_btn)
    public void onClickSubmit() {
        //TODO Implement login
    }

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }
}
