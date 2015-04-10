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

package com.ushahidi.android.ui.activity;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ushahidi.android.R;
import com.ushahidi.android.data.Constants;
import com.ushahidi.android.model.UserAccountModel;
import com.ushahidi.android.module.AccountModule;
import com.ushahidi.android.ui.fragment.LoginFragment;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class LoginActivity extends BaseActivity implements LoginFragment.LoginListener {

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;

    public static final String PARAM_DEPLOYMENT_ID = "deployment_id";


    public LoginActivity() {
        super(R.layout.activity_login, 0);
    }

    public static Intent getIntent(final Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        mAccountAuthenticatorResponse = getIntent().getParcelableExtra(
            AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }


    public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);

            } else {
                mAccountAuthenticatorResponse.onError(
                    AccountManager.ERROR_CODE_CANCELED, "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }

    @Override
    public void setAccountAuthenticatorResult(UserAccountModel userAccountModel,
                                              Long deploymentId) {
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, userAccountModel.getAccountName());
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, Constants.USHAHIDI_AUTHTOKEN_PASSWORD_TYPE);
        bundle.putString(AccountManager.KEY_AUTHTOKEN, userAccountModel.getAuthToken());
        setAccountAuthenticatorResult(bundle);
        mUserState.setActiveUserAccount(userAccountModel);
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new LinkedList<>();
        modules.add(new AccountModule());
        return modules;
    }

    @Override
    protected void initNavDrawerItems() {
        // Do nothing as this activity doesn't support navigation drawer
    }

    private void init() {
        addFragment(R.id.fragment_container, LoginFragment.newInstance(),
            LoginFragment.LOGIN_FRAGMENT_TAG);
    }
}
