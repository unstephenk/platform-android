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

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.data.BaseTestCase;
import java.util.Date;
import com.ushahidi.android.data.entity.UserEntity;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Tests {@link com.ushahidi.android.data.entity.mapper.UserEntityMapper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserEntityMapperTest extends BaseTestCase {

    private UserEntityMapper mUserEntityMapper;

    private UserEntity mUserEntity;

    private User mUser;

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
        mUserEntityMapper = new UserEntityMapper();
    }

    @Test
    public void shouldMapUserEntityToUser() throws Exception {
        mUserEntity = new UserEntity();
        mUserEntity.setId(DUMMY_ID);
        mUserEntity.setEmail(DUMMY_EMAIL);
        mUserEntity.setRealName(DUMMY_REAL_NAME);
        mUserEntity.setUsername(DUMMY_USERNAME);
        mUserEntity.setCreated(DUMMY_CREATED);
        mUserEntity.setUpdated(DUMMY_UPDATED);
        mUserEntity.setDeployment(DUMMY_DEPLOYMENT);
        mUserEntity.setRole(DUMMY_USER_ENTITY_ROLE);

        User user = mUserEntityMapper.map(mUserEntity);

        assertThat(user, is(instanceOf(User.class)));
        assertThat(user.getId(), is(DUMMY_ID));
        assertThat(user.getEmail(), is(DUMMY_EMAIL));
        assertThat(user.getRealName(), is(DUMMY_REAL_NAME));
        assertThat(user.getUsername(), is(DUMMY_USERNAME));
        assertThat(user.getCreated(), is(new java.util.Date(1415718024)));
        assertThat(user.getUpdated(), is(new java.util.Date(1415716223)));
        assertThat(user.getDeployment(), is(DUMMY_DEPLOYMENT));
        assertThat(user.getRole(), is(DUMMY_ROLE));
    }

    @Test
    public void shouldUnMapFromUserToUserEntity() throws Exception {
        mUser = new User();
        mUser.setId(DUMMY_ID);
        mUser.setEmail(DUMMY_EMAIL);
        mUser.setRealName(DUMMY_REAL_NAME);
        mUser.setUsername(DUMMY_USERNAME);
        mUser.setCreated(DUMMY_CREATED);
        mUser.setUpdated(DUMMY_UPDATED);
        mUser.setDeployment(DUMMY_DEPLOYMENT);
        mUser.setRole(DUMMY_ROLE);

        UserEntity userEntity = mUserEntityMapper.unmap(mUser);

        assertThat(userEntity, is(instanceOf(UserEntity.class)));
        assertThat(userEntity.getId(), is(DUMMY_ID));
        assertThat(userEntity.getEmail(), is(DUMMY_EMAIL));
        assertThat(userEntity.getUsername(), is(DUMMY_USERNAME));
        assertThat(userEntity.getRealName(), is(DUMMY_REAL_NAME));
        assertThat(userEntity.getCreated(), is(DUMMY_CREATED));
        assertThat(userEntity.getUpdated(), is(DUMMY_UPDATED));
        assertThat(userEntity.getDeployment(), is(DUMMY_DEPLOYMENT));
        assertThat(userEntity.getRole(), is(DUMMY_USER_ENTITY_ROLE));
    }
}
