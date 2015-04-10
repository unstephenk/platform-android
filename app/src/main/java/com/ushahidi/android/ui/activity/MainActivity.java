/*
 *  Copyright (c) 2015 Ushahidi.
 *
 *   This program is free software: you can redistribute it and/or modify it under
 *   the terms of the GNU Affero General Public License as published by the Free
 *   Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 *   This program is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *   FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program in the file LICENSE-AGPL. If not, see
 *   https://www.gnu.org/licenses/agpl-3.0.html
 *
 */

package com.ushahidi.android.ui.activity;

import android.content.Context;
import android.os.Bundle;

import com.nispok.snackbar.Snackbar;
import com.ushahidi.android.module.MainUiModule;
import com.ushahidi.android.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * This servers as a point of entry to determine which activity to launch. Whether it should launch
 * {@link DeploymentActivity} for adding a new deployment when there is no active deployment or
 * show {@link PostActivity}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MainActivity extends BaseActivity implements MainPresenter.View {

    @Inject
    MainPresenter mMainPresenter;

    public MainActivity() {
        super(0, 0);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.setView(this);
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new ArrayList<>();
        modules.add(new MainUiModule());
        return modules;
    }

    @Override
    protected void initNavDrawerItems() {
        // No Nav Drawer Items.
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMainPresenter.pause();
    }

    @Override
    public void showError(String message) {
        Snackbar.with(getApplicationContext()).text(message).show(this);
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
