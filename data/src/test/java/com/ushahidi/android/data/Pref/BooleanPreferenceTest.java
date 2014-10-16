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

package com.ushahidi.android.data.Pref;

import com.ushahidi.android.data.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests {@link com.ushahidi.android.data.Pref.BooleanPreference}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class BooleanPreferenceTest extends BaseTestCase {

    private SharedPreferences mSharedPreferences;

    private BooleanPreference mBooleanPreference;

    @Before
    public void setup() {
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Robolectric.application);
    }

    @Test
    public void shouldSaveBoolean() {
        boolean expected = true;
        String key = "testKey";
        mBooleanPreference = new BooleanPreference(mSharedPreferences, key);
        mBooleanPreference.set(expected);
        assertThat(mSharedPreferences.getBoolean(key, false), is(true));
    }

    @Test
    public void shouldGetValue() {
        boolean expected = false;
        String key = "testKey2";
        mSharedPreferences.edit().putBoolean(key, expected).commit();
        mBooleanPreference = new BooleanPreference(mSharedPreferences, key);
        boolean result = mBooleanPreference.get();
        assertThat(result, is(expected));
    }

    @Test
    public void shouldIsSetTrue() {
        String key = "testKey3";
        mSharedPreferences.edit().putBoolean(key, true).commit();
        mBooleanPreference = new BooleanPreference(mSharedPreferences, key);
        boolean result = mBooleanPreference.isSet();
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsSetFalse() {
        String key = "testKey4";
        mBooleanPreference = new BooleanPreference(mSharedPreferences, key);
        boolean result = mBooleanPreference.isSet();
        assertThat(result, is(false));
    }

    @Test
    public void shouldDeletePreference() {
        String key = "testKey5";
        mSharedPreferences.edit().putBoolean(key, true).commit();
        mBooleanPreference = new BooleanPreference(mSharedPreferences, key);
        mBooleanPreference.delete();
        assertThat(mSharedPreferences.contains(key), is(false));
    }
}
