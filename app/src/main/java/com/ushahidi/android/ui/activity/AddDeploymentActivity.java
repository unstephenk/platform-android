package com.ushahidi.android.ui.activity;

import com.ushahidi.android.R;
import com.ushahidi.android.module.DeploymentUiModule;
import com.ushahidi.android.ui.fragment.AddDeploymentFragment;

import android.content.Context;
import android.content.Intent;

import java.util.LinkedList;
import java.util.List;

/**
 * Renders {@link com.ushahidi.android.ui.fragment.AddDeploymentFragment}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddDeploymentActivity extends BaseActivity implements
        AddDeploymentFragment.ActionListener {

    public AddDeploymentActivity() {
        super(R.layout.activity_add_deployment, 0);
    }

    public static Intent getIntent(final Context context) {
        return new Intent(context, AddDeploymentActivity.class);
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new LinkedList<Object>();
        modules.add(new DeploymentUiModule());
        return modules;
    }

    @Override
    public void onNavigateOrReloadList() {
        launcher.launchListDeployment();
    }

    @Override
    public void onActionCancelOrClearExecuted() {
        finish();
    }
}
