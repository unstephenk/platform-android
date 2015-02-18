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
import com.ushahidi.android.data.entity.MediaEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Tests {@link MediaDatabaseHelper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class MediaDatabaseHelperTest extends BaseTestCase {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ThreadExecutor mMockThreadExecutor;

    @Mock
    private IMediaDatabaseHelper.IMediaEntityPutCallback
            mMockMediaEntityAddedCallback;

    @Mock
    private MediaEntity mMockMediaEntity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        clearSingleton(MediaDatabaseHelper.class);
    }

    @Test
    public void shouldInvalidateConstructorsNullParameters() throws Exception {
        clearSingleton(MediaDatabaseHelper.class);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid null parameter");
        MediaDatabaseHelper.getInstance(null, null);
    }
}
