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

package com.ushahidi.android.data.entity;

import com.ushahidi.android.data.BaseTestCase;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests {@link com.ushahidi.android.data.entity.DeploymentEntity}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentEntityTest extends BaseTestCase {

    private DeploymentEntity mDeploymentEntity;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_TITLE = "Dummy Deployment Title";

    private static final int DUMMY_STATUS = 0;

    private static final String DUMMY_URL = "http://deployment.com";

    @Before
    public void setUp() throws Exception {
        mDeploymentEntity = new DeploymentEntity();
    }

    @Test
    public void shouldCreateDeploymentEntity() throws Exception {
        mDeploymentEntity.setId(DUMMY_ID);
        mDeploymentEntity.setTitle(DUMMY_TITLE);
        mDeploymentEntity.setStatus(DUMMY_STATUS);
        mDeploymentEntity.setUrl(DUMMY_URL);

        assertThat(mDeploymentEntity, is(instanceOf(DeploymentEntity.class)));
        assertThat(mDeploymentEntity.getId(), is(DUMMY_ID));
        assertThat(mDeploymentEntity.getTitle(), is(DUMMY_TITLE));
        assertThat(mDeploymentEntity.getStatus(), is(DUMMY_STATUS));
        assertThat(mDeploymentEntity.getUrl(), is(DUMMY_URL));
    }
}
