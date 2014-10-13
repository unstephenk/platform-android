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

package com.ushahidi.android.test.model.mapper;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link com.ushahidi.android.model.mapper.DeploymentModelDataMapper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentModelDataMapperTest extends TestCase {

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_TITLE = "Dummy Deployment Title";

    private static final int DUMMY_STATUS = 0;

    private static final String DUMMY_URL = "http://deployment.com";

    private DeploymentModelDataMapper mDeploymentModelDataMapper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mDeploymentModelDataMapper = new DeploymentModelDataMapper();
    }

    public void testDeploymentMap() {
        Deployment deployment = new Deployment();
        deployment.setStatus(DUMMY_STATUS);
        deployment.setTitle(DUMMY_TITLE);
        deployment.setUrl(DUMMY_URL);
        deployment.setId(DUMMY_ID);

        DeploymentModel deploymentModel = mDeploymentModelDataMapper.map(deployment);

        assertThat(deploymentModel, is(instanceOf(DeploymentModel.class)));
        assertThat(deploymentModel.getTitle(), is(DUMMY_TITLE));
        assertThat(deploymentModel.getUrl(), is(DUMMY_URL));
        assertThat(deploymentModel.getId(), is(DUMMY_ID));
        assertThat(deploymentModel.getStatus(), is(DUMMY_STATUS));
    }

    public void testDeploymentUnMap() {
        DeploymentModel deploymentModel = new DeploymentModel();
        deploymentModel.setStatus(DUMMY_STATUS);
        deploymentModel.setTitle(DUMMY_TITLE);
        deploymentModel.setUrl(DUMMY_URL);
        deploymentModel.setId(DUMMY_ID);

        Deployment deployment = mDeploymentModelDataMapper.unmap(deploymentModel);
        assertThat(deployment, is(instanceOf(Deployment.class)));
        assertThat(deployment.getTitle(), is(DUMMY_TITLE));
        assertThat(deployment.getUrl(), is(DUMMY_URL));
        assertThat(deployment.getId(), is(DUMMY_ID));
        assertThat(deployment.getStatus(), is(DUMMY_STATUS));

    }

    public void testDeploymentListMap() {
        Deployment mockDeploymentOne = mock(Deployment.class);
        Deployment mockDeploymentTwo = mock(Deployment.class);

        List<Deployment> deploymentList = new ArrayList<>();
        deploymentList.add(mockDeploymentOne);
        deploymentList.add(mockDeploymentTwo);

        List<DeploymentModel> deploymentModelList = mDeploymentModelDataMapper.map(deploymentList);

        assertThat(deploymentModelList.get(0), is(instanceOf(DeploymentModel.class)));
        assertThat(deploymentModelList.get(1), is(instanceOf(DeploymentModel.class)));
        assertThat(deploymentModelList.size(), is(2));
    }
}
