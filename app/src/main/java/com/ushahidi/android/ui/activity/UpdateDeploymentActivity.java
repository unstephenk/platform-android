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
import com.ushahidi.android.module.DeploymentUiModule;
import com.ushahidi.android.ui.fragment.UpdateDeploymentFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

/**
 * Renders {@link com.ushahidi.android.ui.fragment.AddDeploymentFragment}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UpdateDeploymentActivity extends BaseActivity implements
        UpdateDeploymentFragment.DeploymentUpdateListener {

    private Long mDeploymentId;

    public UpdateDeploymentActivity() {
        super(R.layout.activity_add_deployment, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState == null) {

        } else {
            mDeploymentId = savedInstanceState.getLong(INTENT_STATE_PARAM_DEPLOYMEN_ID);
        }
        mDeploymentId = getIntent().getLongExtra(INTENT_EXTRA_PARAM_DEPLOYMENT_ID, -1);
        addFragment(R.id.fragment_container, UpdateDeploymentFragment.newInstance(mDeploymentId),
                UpdateDeploymentFragment.UPDATE_FRAGMENT_TAG);
    }

    private static final String INTENT_EXTRA_PARAM_DEPLOYMENT_ID
            = "com.ushahidi.android.INTENT_PARAM_DEPLOYMENT_ID";

    private static final String INTENT_STATE_PARAM_DEPLOYMEN_ID
            = "com.ushahidi.android.STATE_PARAM_DEPLOYMENT_ID";

    public static Intent getIntent(final Context context, long deploymentID) {
        Intent intent = new Intent(context, UpdateDeploymentActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_DEPLOYMENT_ID, deploymentID);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putLong(INTENT_EXTRA_PARAM_DEPLOYMENT_ID, mDeploymentId);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new LinkedList<Object>();
        modules.add(new DeploymentUiModule());
        return modules;
    }

    @Override
    public void onUpdateNavigateOrReloadList() {
        launcher.launchListDeployment();
    }

    @Override
    public void onCancelUpdate() {
        finish();
    }
}
