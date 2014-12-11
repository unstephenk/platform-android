package com.ushahidi.android.ui.activity;

import com.ushahidi.android.R;
import com.ushahidi.android.module.DeploymentUiModule;
import com.ushahidi.android.ui.fragment.AddDeploymentFragment;

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
public class AddDeploymentActivity extends BaseActivity implements
        AddDeploymentFragment.AddDeploymentListener {

    public AddDeploymentActivity() {
        super(R.layout.activity_add_deployment, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        addFragment(R.id.fragment_container, AddDeploymentFragment.newInstance(),
                AddDeploymentFragment.ADD_FRAGMENT_TAG);
    }


    public static Intent getIntent(final Context context) {
        return new Intent(context, AddDeploymentActivity.class);
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new LinkedList<>();
        modules.add(new DeploymentUiModule());
        return modules;
    }

    @Override
    protected void initNavDrawerItems() {
        // DO Nothing as this activity doesn't support navigation drawer
    }

    @Override
    public void onAddNavigateOrReloadList() {
        launcher.launchListDeployment();
    }

    @Override
    public void onCancelAdd() {
        finish();
    }
}
