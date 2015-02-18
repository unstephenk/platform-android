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

package com.ushahidi.android.data.api;

import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.ushahidi.android.data.BaseTestCase;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Executor;

import static org.mockito.Mockito.spy;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseApiTestCase extends BaseTestCase {

    protected MockWebServer mMockWebServer;

    protected Executor httpExecutor = spy(new SynchronousExecutor());

    protected Executor callbackExecutor = spy(new SynchronousExecutor());

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mMockWebServer = new MockWebServer();
    }

    public class SynchronousExecutor implements Executor {

        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }
    }
}
