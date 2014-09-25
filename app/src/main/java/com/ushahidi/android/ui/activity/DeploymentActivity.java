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
import com.ushahidi.android.ui.fragment.AboutDialogFragment;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentActivity extends BaseActivity {

    public DeploymentActivity() {
        super(R.layout.activity_deployment_list, R.menu.list_deployment);
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new LinkedList<Object>();
        modules.add(new DeploymentUiModule());
        return modules;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_account:
                final Intent intent = new Intent(this, AddDeploymentActivity.class);
                startActivity(intent);
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
}
