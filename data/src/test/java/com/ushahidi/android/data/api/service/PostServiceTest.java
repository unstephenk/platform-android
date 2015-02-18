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
import com.ushahidi.android.data.api.model.Posts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

import static com.ushahidi.android.data.TestHelper.getResource;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(manifest=Config.NONE)
//@RunWith(RobolectricTestRunner.class)
public class PostServiceTest extends BaseApiTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

   //@Test
    public void shouldSuccessfullyFetchPost() throws IOException {
        mMockWebServer.play();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setExecutors(httpExecutor, callbackExecutor)
                .setConverter(new GsonConverter(mGson))
                .setEndpoint(mMockWebServer.getUrl("/").toString()).build();

        final String postJson = getResource("posts.json");
        PostService postService = restAdapter.create(PostService.class);
        mMockWebServer.enqueue(new MockResponse().setBody(postJson));
        postService.posts(new Callback<Posts>() {
            @Override
            public void success(Posts posts, Response response) {
                assertNotNull(posts.getPosts());
                assertThat(posts.getPosts().get(0).getId(), equalTo(1l));
            }

            @Override
            public void failure(RetrofitError error) {
                assertThat(error, nullValue());
            }
        });

        mMockWebServer.shutdown();
    }
}
