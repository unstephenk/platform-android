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

package com.ushahidi.android.test.model;

import com.ushahidi.android.model.DeploymentModel;

import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests {@link com.ushahidi.android.model.DeploymentModel}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentModelTest extends TestCase {

    private DeploymentModel mDeploymentModel;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_TITLE = "Dummy Deployment Title";

    private static final int DUMMY_STATUS = 0;

    private static final String DUMMY_URL = "http://deployment.com";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateDeployment() {

        mDeploymentModel = new DeploymentModel();
        mDeploymentModel.setId(DUMMY_ID);
        mDeploymentModel.setTitle(DUMMY_TITLE);
        mDeploymentModel.setUrl(DUMMY_URL);
        mDeploymentModel.setStatus(DUMMY_STATUS);

        assertThat(mDeploymentModel.getTitle(), is(DUMMY_TITLE));
        assertThat(mDeploymentModel.getUrl(), is(DUMMY_URL));
        assertThat(mDeploymentModel.getId(), is(DUMMY_ID));
        assertThat(mDeploymentModel.getStatus(), is(DUMMY_STATUS));
    }
}
