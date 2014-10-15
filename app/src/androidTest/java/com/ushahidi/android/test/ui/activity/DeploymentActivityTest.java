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
import com.ushahidi.android.ui.activity.DeploymentActivity;
import com.ushahidi.android.ui.fragment.ListDeploymentFragment;

import android.test.ActivityInstrumentationTestCase2;

import static org.assertj.android.api.Assertions.assertThat;

/**
 * Tests {@link com.ushahidi.android.ui.activity.DeploymentActivity}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentActivityTest extends ActivityInstrumentationTestCase2<DeploymentActivity> {

    private DeploymentActivity mDeploymentActivity;

    public DeploymentActivityTest() {
        super(DeploymentActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mDeploymentActivity = getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

    }

    public void testHasListDeploymentFragment() {
        Spoon.screenshot(mDeploymentActivity, "initial_state");
        ListDeploymentFragment listFragment = (ListDeploymentFragment) mDeploymentActivity
                .getFragmentManager()
                .findFragmentById(R.id.fragment_list_deployments);
        assertThat(listFragment).isNotNull();
    }


}
