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

import android.content.Context;
import android.content.Intent;

import com.ushahidi.android.qualifier.ActivityContext;

import javax.inject.Inject;

/**
 * Launches Activity. This is the main class for navigating through the app
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ActivityLauncher {

    private final Context mContext;

    @Inject
    public ActivityLauncher(@ActivityContext Context context) {
        mContext = context;
    }

    public void launchListDeployment() {
        Intent intent = DeploymentActivity.getIntent(mContext);
        mContext.startActivity(intent);
    }

    /**
     * Launches Add deployment activity for adding a new deployment
     */
    public void launchAddDeployment() {
        final Intent intent = AddDeploymentActivity.getIntent(mContext);
        mContext.startActivity(intent);
    }

    /**
     * Launches update deployment activity for editing
     *
     * @param deploymentId The deployment ID to use to fetch the deployment for editing
     */
    public void launchUpdateDeployment(long deploymentId) {
        final Intent intent = UpdateDeploymentActivity.getIntent(mContext, deploymentId);
        mContext.startActivity(intent);
    }

    /**
     * Launches login activity
     */
    public void launchLogin() {
        final Intent intent = LoginActivity.getIntent(mContext);
        mContext.startActivity(intent);
    }

    /**
     * Launches login activity
     */
    public void post() {
        final Intent intent = PostActivity.getIntent(mContext);
        mContext.startActivity(intent);
    }
}
