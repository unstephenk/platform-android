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

package com.ushahidi.android.test.model;

import com.ushahidi.android.model.UserModel;

import junit.framework.TestCase;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests {@link com.ushahidi.android.model.UserModel}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserModelTest extends TestCase {

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_EMAIL = "email@example.com";

    private static final String DUMMY_REAL_NAME = "Real Name";

    private static final String DUMMY_USERNAME = "dudebro";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415716223);

    private static final long DUMMY_DEPLOYMENT = 1;

    private static final UserModel.Role DUMMY_USER_ENTITY_ROLE = UserModel.Role.USER;

    private UserModel mUserModel;

    @Override
    public void setUp() throws Exception {
        mUserModel = new UserModel();
    }

    public void testCreateUserModel() throws Exception {
        mUserModel.setId(DUMMY_ID);
        mUserModel.setEmail(DUMMY_EMAIL);
        mUserModel.setUsername(DUMMY_USERNAME);
        mUserModel.setRealName(DUMMY_REAL_NAME);
        mUserModel.setCreated(DUMMY_CREATED);
        mUserModel.setUpdated(DUMMY_UPDATED);
        mUserModel.setDeployment(DUMMY_DEPLOYMENT);
        mUserModel.setRole(DUMMY_USER_ENTITY_ROLE);

        assertThat(mUserModel).isInstanceOf(UserModel.class);
        assertThat(mUserModel.getId()).isEqualTo(DUMMY_ID);
        assertThat(mUserModel.getEmail()).isEqualTo(DUMMY_EMAIL);
        assertThat(mUserModel.getRealName()).isEqualTo(DUMMY_REAL_NAME);
        assertThat(mUserModel.getUsername()).isEqualTo(DUMMY_USERNAME);
        assertThat(mUserModel.getCreated()).isEqualTo(DUMMY_CREATED);
        assertThat(mUserModel.getUpdated()).isEqualTo(DUMMY_UPDATED);
        assertThat(mUserModel.getDeployment()).isEqualTo(DUMMY_DEPLOYMENT);
        assertThat(mUserModel.getRole()).isEqualTo(DUMMY_USER_ENTITY_ROLE);

    }

}
