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

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.data.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@RunWith(RobolectricTestRunner.class)
public class UserEntityTest extends BaseTestCase {

    private UserEntity mUserEntity;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_EMAIL = "email@example.com";

    private static final String DUMMY_REAL_NAME = "Real Name";

    private static final String DUMMY_USERNAME = "dudebro";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415716223);

    private static final long DUMMY_DEPLOYMENT = 1;

    private static final User.Role DUMMY_ROLE = User.Role.USER;

    private static final UserEntity.Role DUMMY_USER_ENTITY_ROLE = UserEntity.Role.USER;

    @Before
    public void setUp() throws Exception {
        mUserEntity = new UserEntity();
    }

    @Test
    public void shouldCreateUserEntity() throws Exception {
        mUserEntity.setId(DUMMY_ID);
        mUserEntity.setEmail(DUMMY_EMAIL);
        mUserEntity.setUsername(DUMMY_USERNAME);
        mUserEntity.setRealName(DUMMY_REAL_NAME);
        mUserEntity.setCreated(DUMMY_CREATED);
        mUserEntity.setUpdated(DUMMY_UPDATED);
        mUserEntity.setDeployment(DUMMY_DEPLOYMENT);
        mUserEntity.setRole(DUMMY_USER_ENTITY_ROLE);

        assertThat(mUserEntity, is(instanceOf(UserEntity.class)));
        assertThat(mUserEntity.getId(), is(DUMMY_ID));
        assertThat(mUserEntity.getEmail(), is(DUMMY_EMAIL));
        assertThat(mUserEntity.getRealName(), is(DUMMY_REAL_NAME));
        assertThat(mUserEntity.getUsername(), is(DUMMY_USERNAME));
        assertThat(mUserEntity.getCreated(), is(DUMMY_CREATED));
        assertThat(mUserEntity.getUpdated(), is(DUMMY_UPDATED));
        assertThat(mUserEntity.getDeployment(), is(DUMMY_DEPLOYMENT));
        assertThat(mUserEntity.getRole(), is(DUMMY_USER_ENTITY_ROLE));

    }

}
