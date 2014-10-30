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
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.module.DeploymentUiModule;
import com.ushahidi.android.ui.fragment.AboutDialogFragment;
import com.ushahidi.android.ui.fragment.AddDeploymentFragment;
import com.ushahidi.android.ui.fragment.ListDeploymentFragment;
import com.ushahidi.android.ui.fragment.UpdateDeploymentFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.LinkedList;
import java.util.List;

/**
 * List Deployment Activity
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentActivity extends BaseActivity
        implements ListDeploymentFragment.DeploymentListListener,
        UpdateDeploymentFragment.DeploymentUpdateListener,
        AddDeploymentFragment.AddDeploymentListener {

    private FrameLayout addDeploymentLayout;

    private ListDeploymentFragment mListDeploymentFragment;

    public DeploymentActivity() {
        super(R.layout.activity_deployment_list, R.menu.list_deployment);
    }

    private void init() {

        addDeploymentLayout
                = (FrameLayout) findViewById(R.id.add_deployment_fragment_container);

        mListDeploymentFragment = (ListDeploymentFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_list_deployments);

        if (addDeploymentLayout != null) {
            addFragment(R.id.add_deployment_fragment_container, AddDeploymentFragment.newInstance(),
                    AddDeploymentFragment.ADD_FRAGMENT_TAG);
        }

    }

    private void replaceFragment(Long deploymentId) {
        replaceFragment(R.id.add_deployment_fragment_container,
                UpdateDeploymentFragment.newInstance(deploymentId),
                UpdateDeploymentFragment.UPDATE_FRAGMENT_TAG);
    }

    private void replaceFragment() {
        replaceFragment(R.id.add_deployment_fragment_container, AddDeploymentFragment.newInstance(),
                AddDeploymentFragment.ADD_FRAGMENT_TAG);
    }

    private void refreshList() {
        if (mListDeploymentFragment != null) {
            mListDeploymentFragment.refreshList();
            replaceFragment();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new LinkedList<Object>();
        modules.add(new DeploymentUiModule());
        return modules;
    }

    @Override
    protected void initNavDrawerItems() {
        // DO Nothing as this activity doesn't support navigation drawer
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_account:
                launcher.launchAddDeployment();
                return true;
            case R.id.menu_about:
                AboutDialogFragment dialog = new AboutDialogFragment();
                dialog.show(getFragmentManager(), "AboutDialogFragment");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getIntent(final Context context) {
        return new Intent(context, DeploymentActivity.class);
    }


    @Override
    public void onDeploymentClicked(DeploymentModel deploymentModel) {

        if (addDeploymentLayout != null) {
            replaceFragment(deploymentModel.getId());
        } else {
            launcher.launchUpdateDeployment(deploymentModel.getId());
        }
    }

    @Override
    public void onUpdateNavigateOrReloadList() {
        refreshList();
    }

    @Override
    public void onCancelUpdate() {
        replaceFragment();
    }

    @Override
    public void onAddNavigateOrReloadList() {
        refreshList();
    }

    @Override
    public void onCancelAdd() {
        replaceFragment();
    }
}
