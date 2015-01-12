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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests {@link com.ushahidi.android.model.DeploymentModel}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentModelTest extends TestCase {

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_TITLE = "Dummy Deployment Title";

    private static final DeploymentModel.Status DUMMY_STATUS = DeploymentModel.Status.DEACTIVATED;

    private static final String DUMMY_URL = "http://deployment.com";

    private DeploymentModel mDeploymentModel;

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

        assertThat(mDeploymentModel.getTitle()).isEqualTo(DUMMY_TITLE);
        assertThat(mDeploymentModel.getUrl()).isEqualTo(DUMMY_URL);
        assertThat(mDeploymentModel.getId()).isEqualTo(DUMMY_ID);
        assertThat(mDeploymentModel.getStatus()).isEqualTo(DUMMY_STATUS);
    }
}
