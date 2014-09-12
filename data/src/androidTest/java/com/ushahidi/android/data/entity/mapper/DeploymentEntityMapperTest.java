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

package com.ushahidi.android.data.entity.mapper;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.entity.DeploymentEntity;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Tests {@link com.ushahidi.android.data.entity.mapper.DeploymentEntityMapper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(emulateSdk = 18)
public class DeploymentEntityMapperTest extends BaseTestCase {

    private static final int DUMMY_ID = 1;

    private static final String DUMMY_TITLE = "Dummy Deployment Title";

    private static final String DUMMY_DESCRIPTION = "Dummy Deployment Description";

    private static final String DUMMY_URL = "http://deployment.com";

    private static final int DEPLOYMENT_DUMMY_ID = 1;

    private static final String DEPLOYMENT_DUMMY_TITLE = "Dummy Deployment Title";

    private static final String DEPLOYMENT_DUMMY_DESCRIPTION = "Dummy Deployment Description";

    private static final String DEPLOYMENT_DUMMY_URL = "http://deployment.com";

    private DeploymentEntityMapper mDeploymentEntityMapper;

    private DeploymentEntity mDeploymentEntity;

    private Deployment mDeployment;

    @Before
    public void setUp() throws Exception {
        mDeploymentEntityMapper = new DeploymentEntityMapper();
    }

    @Test
    public void shouldMapDeploymentEntityToDeployment() throws Exception {
        mDeploymentEntity = new DeploymentEntity();
        mDeploymentEntity.setId(DUMMY_ID);
        mDeploymentEntity.setTitle(DUMMY_TITLE);
        mDeploymentEntity.setDescription(DUMMY_DESCRIPTION);
        mDeploymentEntity.setUrl(DUMMY_URL);

        Deployment deployment = mDeploymentEntityMapper.map(mDeploymentEntity);

        assertThat(deployment, is(instanceOf(Deployment.class)));
        assertThat(deployment.getId(), is(DUMMY_ID));
        assertThat(deployment.getTitle(), is(DUMMY_TITLE));
        assertThat(deployment.getDescription(), is(DUMMY_DESCRIPTION));
        assertThat(deployment.getUrl(), is(DUMMY_URL));
    }

    @Test
    public void shouldUnMapFromDeploymentToDeploymentEntity() throws Exception {
        mDeployment = new Deployment();
        mDeployment.setId(DEPLOYMENT_DUMMY_ID);
        mDeployment.setTitle(DEPLOYMENT_DUMMY_TITLE);
        mDeployment.setDescription(DEPLOYMENT_DUMMY_DESCRIPTION);
        mDeployment.setUrl(DEPLOYMENT_DUMMY_URL);

        DeploymentEntity deploymentEntity = mDeploymentEntityMapper.unmap(mDeployment);

        assertThat(deploymentEntity, is(instanceOf(DeploymentEntity.class)));
        assertThat(deploymentEntity.getDescription(), is(DEPLOYMENT_DUMMY_DESCRIPTION));
        assertThat(deploymentEntity.getTitle(), is(DEPLOYMENT_DUMMY_TITLE));
        assertThat(deploymentEntity.getUrl(), is(DEPLOYMENT_DUMMY_URL));
    }
}
