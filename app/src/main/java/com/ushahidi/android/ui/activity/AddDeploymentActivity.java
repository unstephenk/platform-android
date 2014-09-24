package com.ushahidi.android.ui.activity;

import com.ushahidi.android.R;
import com.ushahidi.android.module.DeploymentUiModule;

import java.util.LinkedList;
import java.util.List;

/**
 * Renders {@link com.ushahidi.android.ui.fragment.AddDeploymentFragment}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddDeploymentActivity extends BaseActivity {

    public AddDeploymentActivity() {
        super(R.layout.activity_add_deployment, 0);
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new LinkedList<Object>();
        modules.add(new DeploymentUiModule());
        return modules;
    }
}
