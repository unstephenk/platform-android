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

package com.ushahidi.android.test.ui.activity;

import com.andreabaccega.widget.FormEditText;
import com.robotium.solo.Solo;
import com.squareup.spoon.Spoon;
import com.ushahidi.android.R;
import com.ushahidi.android.ui.activity.LoginActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.Spinner;

import static org.assertj.android.api.Assertions.assertThat;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo mSolo;

    private LoginActivity mLoginActivity;

    FormEditText mUsername;

    FormEditText mPassword;

    Button mLoginButton;

    Spinner mSpinner;

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSolo = new Solo(getInstrumentation(), getActivity());
        mLoginActivity = (LoginActivity) mSolo.getCurrentActivity();
        mUsername = (FormEditText) mSolo.getView(R.id.login_username);
        mPassword = (FormEditText) mSolo.getView(R.id.login_password);
        mLoginButton = (Button) mSolo.getView(R.id.login_submit_btn);
        mSpinner = (Spinner) mSolo.getView(R.id.select_deployment);
    }

    public void testCorrectCredentials() {
        Spoon.screenshot(mLoginActivity, "initial_state");
    }

    public void testEmptyFormFieldsShouldShowErrors() {
        Spoon.screenshot(mLoginActivity, "initial_state");
        assertThat(mUsername).hasNoError();
        assertThat(mPassword).hasNoError();
        mSolo.clickOnView(mLoginButton);
        Spoon.screenshot(mLoginActivity, "loginButton_Clicked");

        assertThat(mUsername).hasError(R.string.login_empty_username);
        assertThat(mPassword).hasError(R.string.login_empty_password);

    }
}
