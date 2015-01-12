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

package com.ushahidi.android.test.model.mapper;

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.model.UserModel;
import com.ushahidi.android.model.mapper.UserModelDataMapper;
import com.ushahidi.android.test.CustomAndroidTestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests {@link com.ushahidi.android.model.mapper.UserModelDataMapper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserModelDataMapperTest extends CustomAndroidTestCase {

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_EMAIL = "email@example.com";

    private static final String DUMMY_REAL_NAME = "Real Name";

    private static final String DUMMY_USERNAME = "dudebro";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415716223);

    private static final long DUMMY_DEPLOYMENT = 1;

    private static final User.Role DUMMY_ROLE = User.Role.USER;

    private static final UserModel.Role DUMMY_USER_ENTITY_ROLE = UserModel.Role.USER;

    private UserModelDataMapper mUserModelMapper;

    private UserModel mUserModel;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mUserModelMapper = new UserModelDataMapper();
    }

    public void testUserMap() throws Exception {
        User user = new User();
        user.setId(DUMMY_ID);
        user.setEmail(DUMMY_EMAIL);
        user.setRealName(DUMMY_REAL_NAME);
        user.setUsername(DUMMY_USERNAME);
        user.setCreated(DUMMY_CREATED);
        user.setUpdated(DUMMY_UPDATED);
        user.setDeployment(DUMMY_DEPLOYMENT);
        user.setRole(DUMMY_ROLE);

        mUserModel = mUserModelMapper.map(user);

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

    public void testUserUnMap() throws Exception {
        mUserModel = new UserModel();
        mUserModel.setId(DUMMY_ID);
        mUserModel.setEmail(DUMMY_EMAIL);
        mUserModel.setRealName(DUMMY_REAL_NAME);
        mUserModel.setUsername(DUMMY_USERNAME);
        mUserModel.setCreated(DUMMY_CREATED);
        mUserModel.setUpdated(DUMMY_UPDATED);
        mUserModel.setDeployment(DUMMY_DEPLOYMENT);
        mUserModel.setRole(DUMMY_USER_ENTITY_ROLE);

        User user = mUserModelMapper.unmap(mUserModel);

        assertThat(user).isInstanceOf(User.class);
        assertThat(user.getId()).isEqualTo(DUMMY_ID);
        assertThat(user.getEmail()).isEqualTo(DUMMY_EMAIL);
        assertThat(user.getUsername()).isEqualTo(DUMMY_USERNAME);
        assertThat(user.getRealName()).isEqualTo(DUMMY_REAL_NAME);
        assertThat(user.getCreated()).isEqualTo(DUMMY_CREATED);
        assertThat(user.getUpdated()).isEqualTo(DUMMY_UPDATED);
        assertThat(user.getDeployment()).isEqualTo(DUMMY_DEPLOYMENT);
        assertThat(user.getRole()).isEqualTo(DUMMY_ROLE);
    }

    public void testUserListMap() {
        User userOne = new User();
        userOne.setId(DUMMY_ID);
        userOne.setEmail(DUMMY_EMAIL);
        userOne.setRealName(DUMMY_REAL_NAME);
        userOne.setUsername(DUMMY_USERNAME);
        userOne.setCreated(DUMMY_CREATED);
        userOne.setUpdated(DUMMY_UPDATED);
        userOne.setDeployment(DUMMY_DEPLOYMENT);
        userOne.setRole(DUMMY_ROLE);

        User userTwo = new User();
        userTwo.setId(DUMMY_ID);
        userTwo.setEmail(DUMMY_EMAIL);
        userTwo.setRealName(DUMMY_REAL_NAME);
        userTwo.setUsername(DUMMY_USERNAME);
        userTwo.setCreated(DUMMY_CREATED);
        userTwo.setUpdated(DUMMY_UPDATED);
        userTwo.setDeployment(DUMMY_DEPLOYMENT);
        userTwo.setRole(DUMMY_ROLE);

        List<User> userList = new ArrayList<>();
        userList.add(userOne);
        userList.add(userTwo);

        List<UserModel> userModelList = mUserModelMapper.map(userList);

        assertThat(userModelList.get(0)).isInstanceOf(UserModel.class);
        assertThat(userModelList.get(1)).isInstanceOf(UserModel.class);
        assertThat(userModelList.size()).isEqualTo(2);
    }
}
