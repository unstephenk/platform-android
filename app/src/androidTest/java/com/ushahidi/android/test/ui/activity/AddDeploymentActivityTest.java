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

package com.ushahidi.android.test.ui.activity;

import com.squareup.spoon.Spoon;
import com.ushahidi.android.R;
import com.ushahidi.android.ui.activity.AddDeploymentActivity;
import com.ushahidi.android.ui.fragment.AddDeploymentFragment;

import android.app.Fragment;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import static org.assertj.android.api.Assertions.assertThat;


/**
 * Tests {@link com.ushahidi.android.ui.activity.AddDeploymentActivity}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddDeploymentActivityTest extends
        ActivityInstrumentationTestCase2<AddDeploymentActivity> {

    private AddDeploymentActivity mAddDeploymentActivity;

    private Instrumentation instrumentation;

    private EditText deploymentTitle;

    private EditText deploymentUrl;

    private Button addButton;

    public AddDeploymentActivityTest() {
        super(AddDeploymentActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instrumentation = getInstrumentation();
        mAddDeploymentActivity = getActivity();

        deploymentTitle = (EditText) mAddDeploymentActivity.findViewById(R.id.add_deployment_title);
        deploymentUrl = (EditText) mAddDeploymentActivity.findViewById(R.id.add_deployment_url);
        addButton = (Button) mAddDeploymentActivity.findViewById(R.id.add_deployment_add);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

    }

    public void testHasAddDeploymentFragment() {
        Spoon.screenshot(mAddDeploymentActivity, "initial_state");
        Fragment addDeploymentFragment = mAddDeploymentActivity.getFragmentManager()
                .findFragmentByTag(AddDeploymentFragment.ADD_FRAGMENT_TAG);
        assertThat(addDeploymentFragment).isNotNull();
    }

    public void testUrlEditTextOnTouchListener() {
        Spoon.screenshot(mAddDeploymentActivity, "initial_state");

        TouchUtils.tapView(AddDeploymentActivityTest.this, deploymentUrl);

        Spoon.screenshot(mAddDeploymentActivity, "deployment_url_clicked");

        instrumentation.waitForIdleSync();

        assertThat(deploymentUrl).hasTextString("http://");
    }

    public void testEmptyFormFieldsShouldShowErrors() {
        Spoon.screenshot(mAddDeploymentActivity, "initial_state");
        assertThat(deploymentTitle).hasNoError();
        assertThat(deploymentUrl).hasNoError();

        // Click the "addButton" field.
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                addButton.performClick();
            }
        });

        instrumentation.waitForIdleSync();

        Spoon.screenshot(mAddDeploymentActivity, "addButton_Clicked");

        assertThat(deploymentTitle).hasError(R.string.add_deployment_empty_title);
        assertThat(deploymentUrl).hasError(R.string.error_field_must_not_be_empty);

    }

}
