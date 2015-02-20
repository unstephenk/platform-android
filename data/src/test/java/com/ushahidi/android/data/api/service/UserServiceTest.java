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

package com.ushahidi.android.data.api.service;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.ushahidi.android.data.api.BaseApiTestCase;
import com.ushahidi.android.data.api.auth.AccessToken;
import com.ushahidi.android.data.api.auth.Payload;
import com.ushahidi.android.data.entity.UserEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

import static com.ushahidi.android.data.TestHelper.getResource;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class UserServiceTest extends BaseApiTestCase {

    final SimpleDateFormat PARSER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale
            .getDefault());

    private Payload mPayload = new Payload("", "", "", "", "", "");

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void shouldSuccessfullyAuthenticateUserAccount() throws IOException {
        mMockWebServer.play();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setExecutors(httpExecutor, callbackExecutor)
                .setConverter(new GsonConverter(mGson))
                .setEndpoint(mMockWebServer.getUrl("/").toString()).build();

        final String loginSuccessful = getResource("login_success.json");
        UserService userService = restAdapter.create(UserService.class);
        mMockWebServer.enqueue(new MockResponse().setBody(loginSuccessful));
        userService.getAccessToken(mPayload, new Callback<AccessToken>() {
            @Override
            public void success(AccessToken accessToken, Response response) {

                assertThat(accessToken.getAccessToken(),
                        equalTo("XGOtn8jE6iQ73c7Jupz3hrvRaWJefo0qCuLCXl2e"));
                assertThat(accessToken.getRefreshToken(),
                        equalTo("GyOHosEcJFPI4cMxPtJiCBNsb1L9mFFG4xzc7anc"));
                assertThat(accessToken.getTokenType(), equalTo("Bearer"));
                assertThat(accessToken.getExpires(), equalTo(1418997691l));
            }

            @Override
            public void failure(RetrofitError error) {
                assertThat(error, nullValue());
            }
        });

        mMockWebServer.shutdown();
    }

    @Test
    public void shouldFailToAuthenticateUserAccount() throws IOException {
        mMockWebServer.play();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setExecutors(httpExecutor, callbackExecutor)
                .setConverter(new GsonConverter(mGson))
                .setEndpoint(mMockWebServer.getUrl("/").toString()).build();
        final String loginFailed = getResource("login_failed.json");
        UserService userService = restAdapter.create(UserService.class);
        mMockWebServer.enqueue(new MockResponse().setBody(loginFailed));
        userService.getAccessToken(mPayload, new Callback<AccessToken>() {
            @Override
            public void success(AccessToken accessToken, Response response) {
                assertThat(accessToken.getErrorDescription(),
                        equalTo("The user credentials were incorrect."));
                assertThat(accessToken.getError(), equalTo("invalid_request"));
                assertThat(accessToken.getExpiresIn(), nullValue());
                assertThat(accessToken.getRefreshTokenExpiresIn(), nullValue());
                assertThat(accessToken.getAccessToken(),
                        nullValue());
                assertThat(accessToken.getRefreshToken(),
                        nullValue());
                assertThat(accessToken.getTokenType(), nullValue());
                assertThat(accessToken.getExpires(), nullValue());
            }

            @Override
            public void failure(RetrofitError error) {
                assertThat(error, nullValue());
            }
        });

        mMockWebServer.shutdown();
    }

    @Test
    public void shouldSuccessfullyFetchUser() throws IOException {
        mMockWebServer.play();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setExecutors(httpExecutor, callbackExecutor)
                .setConverter(new GsonConverter(mGson))
                .setEndpoint(mMockWebServer.getUrl("/").toString()).build();

        final String user = getResource("user.json");
        UserService userService = restAdapter.create(UserService.class);
        mMockWebServer.enqueue(new MockResponse().setBody(user));
        userService.getUser(new Callback<UserEntity>() {
            @Override
            public void success(UserEntity userEntity, Response response) {

                assertThat(userEntity.getId(), equalTo(1l));
                assertThat(userEntity.getCreated(), equalTo(parseDateTime(
                        "1970-01-01T00:00:00+00:00")));
                assertThat(userEntity.getRealName(), equalTo("Robbie Mackay"));
                assertThat(userEntity.getEmail(), equalTo("robbie@ushahidi.com"));
                assertThat(userEntity.getUsername(), equalTo("robbie"));
                assertThat(userEntity.getRole(), equalTo(UserEntity.Role.USER));
                assertThat(userEntity.getUpdated(), nullValue());
            }

            @Override
            public void failure(RetrofitError error) {
                assertThat(error, nullValue());
            }
        });

        mMockWebServer.shutdown();
    }

    private Date parseDateTime(String dateTime) {
        try {
            return PARSER.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
