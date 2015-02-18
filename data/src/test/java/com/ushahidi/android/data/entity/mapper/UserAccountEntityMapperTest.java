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

package com.ushahidi.android.data.entity.mapper;

import com.ushahidi.android.core.entity.UserAccount;
import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.entity.UserAccountEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class UserAccountEntityMapperTest extends BaseTestCase {

    private UserAccountEntityMapper mUserAccountEntityMapper;

    private UserAccountEntity mUserAccountEntity;

    private UserAccount mUserAccount;

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_NAME = "admin";

    private static final String DUMMY_AUTH_TOKEN = "token";

    private static final String DUMMY_AUTH_TYPE = "password";

    private static final String DUMMY_PASSWORD = "password";

    @Before
    public void setUp() throws Exception {
        mUserAccountEntityMapper = new UserAccountEntityMapper();
    }

    @Test
    public void shouldMapUserAcountEntityToUserAccount() throws Exception {
        mUserAccountEntity = new UserAccountEntity();
        mUserAccountEntity.setId(DUMMY_ID);
        mUserAccountEntity.setAccountName(DUMMY_NAME);
        mUserAccountEntity.setAuthToken(DUMMY_AUTH_TOKEN);
        mUserAccountEntity.setAuthTokenType(DUMMY_AUTH_TYPE);
        mUserAccountEntity.setPassword(DUMMY_PASSWORD);
        mUserAccountEntity.setAuthToken(DUMMY_AUTH_TOKEN, DUMMY_AUTH_TYPE);

        UserAccount userAccount = mUserAccountEntityMapper.map(mUserAccountEntity);

        assertThat(userAccount, is(instanceOf(UserAccount.class)));
        assertThat(userAccount.getId(), is(userAccount.getId()));
        assertThat(userAccount.getAccountName(), is(userAccount.getAccountName()));
        assertThat(userAccount.getAuthToken(), is(userAccount.getAuthToken()));
        assertThat(userAccount.getAuthTokenType(), is(userAccount.getAuthTokenType()));
        assertThat(userAccount.getPassword(), is(userAccount.getPassword()));
    }

    @Test
    public void shouldMapUserAcountToUserAccountEntity() throws Exception {
        mUserAccount = new UserAccount();
        mUserAccount.setId(DUMMY_ID);
        mUserAccount.setAccountName(DUMMY_NAME);
        mUserAccount.setAuthToken(DUMMY_AUTH_TOKEN);
        mUserAccount.setAuthTokenType(DUMMY_AUTH_TYPE);
        mUserAccount.setPassword(DUMMY_PASSWORD);
        mUserAccount.setAuthToken(DUMMY_AUTH_TOKEN, DUMMY_AUTH_TYPE);

        UserAccountEntity userAccountEntity = mUserAccountEntityMapper.unmap(mUserAccount);

        assertThat(userAccountEntity, is(instanceOf(UserAccountEntity.class)));
        assertThat(userAccountEntity.getId(), is(DUMMY_ID));
        assertThat(userAccountEntity.getAccountName(), is(DUMMY_NAME));
        assertThat(userAccountEntity.getAuthToken(), is(DUMMY_AUTH_TOKEN));
        assertThat(userAccountEntity.getAuthTokenType(), is(DUMMY_AUTH_TYPE));
        assertThat(userAccountEntity.getPassword(), is(DUMMY_PASSWORD));
    }
}
