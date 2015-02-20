/*
 * Copyright (c) 2015 Ushahidi.
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

package com.ushahidi.android.data.api.service;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.ushahidi.android.data.api.BaseApiTestCase;
import com.ushahidi.android.data.api.model.Tags;
import com.ushahidi.android.data.database.PostDatabaseTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

import static com.ushahidi.android.data.TestHelper.getResource;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class TagServiceTest extends BaseApiTestCase {

    private TagService mTagService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void shouldSuccessfullyFetchTags() throws IOException {
        final String tagsJson = getResource("tags.json");
        mMockWebServer.play();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setExecutors(httpExecutor, callbackExecutor)
                .setConverter(new GsonConverter(mGson))
                .setEndpoint(mMockWebServer.getUrl("/").toString()).build();

        mTagService = restAdapter.create(TagService.class);
        mMockWebServer.enqueue(new MockResponse().setBody(tagsJson));
        mTagService.tags(new Callback<Tags>() {
            @Override
            public void success(Tags tags, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                assertThat(error, nullValue());
            }
        });
    }
}
