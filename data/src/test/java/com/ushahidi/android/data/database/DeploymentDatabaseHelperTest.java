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

package com.ushahidi.android.data.database;

import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.entity.DeploymentEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;

/**
 * Tests {@link com.ushahidi.android.data.database.BaseDatabseHelper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentDatabaseHelperTest extends BaseTestCase {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DeploymentDatabaseHelper mBaseDatabseHelper;

    @Mock
    private ThreadExecutor mMockThreadExecutor;

    @Mock
    private IDeploymentDatabaseHelper.IDeploymentEntityPutCallback
            mMockDeploymentEntityAddedCallback;

    @Mock
    private DeploymentEntity mMockDeploymentEntity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        clearSingleton(DeploymentDatabaseHelper.class);
        mBaseDatabseHelper = DeploymentDatabaseHelper
                .getInstance(Robolectric.application, mMockThreadExecutor);
    }

    @Test
    public void shouldInvalidateConstructorsNullParameters() throws Exception {
        clearSingleton(DeploymentDatabaseHelper.class);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid null parameter");
        mBaseDatabseHelper = DeploymentDatabaseHelper.getInstance(null, null);
    }

}
