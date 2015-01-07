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

import com.ushahidi.android.R;
import com.ushahidi.android.module.AccountModule;
import com.ushahidi.android.ui.fragment.LoginFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class LoginActivity extends BaseActivity {

    public LoginActivity() {
        super(R.layout.activity_login, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new LinkedList<>();
        modules.add(new AccountModule());
        return modules;
    }

    @Override
    protected void initNavDrawerItems() {
        // DO Nothing as this activity doesn't support navigation drawer
    }

    private void init() {
        addFragment(R.id.fragment_container, LoginFragment.newInstance(),
                LoginFragment.LOGIN_FRAGMENT_TAG);
    }

    public static Intent getIntent(final Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
