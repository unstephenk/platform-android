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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.ushahidi.android.data.BaseTestCase;

import org.junit.Before;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseApiTestCase extends BaseTestCase {

    protected Gson mGson;

    @Before
    public void setUp() throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        builder.registerTypeAdapter(Date.class, new DateDeserializer());
        mGson = builder.create();
    }

    public class SynchronousExecutor implements Executor {

        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }
    }
}
