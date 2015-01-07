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

package com.ushahidi.android.data.pref;

import com.ushahidi.android.data.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests {@link com.ushahidi.android.data.pref.StringPreference}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class StringPreferenceTest extends BaseTestCase {

    private SharedPreferences mSharedPreferences;

    private StringPreference mStringPreference;

    @Before
    public void setup() {
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Robolectric.application);
    }

    @Test
    public void shouldSaveString() {
        String expected = "dummyTest";
        String key = "testKey";
        mStringPreference = new StringPreference(mSharedPreferences, key);
        mStringPreference.set(expected);
        assertThat(mSharedPreferences.getString(key, null), is(equalTo(expected)));
    }

    @Test
    public void shouldGetValue() {
        String expected = "dummyTest";
        String key = "testKey2";
        mSharedPreferences.edit().putString(key, expected).commit();
        mStringPreference = new StringPreference(mSharedPreferences, key);
        String result = mStringPreference.get();
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void shouldIsSetTrue() {
        String key = "testKey3";
        mSharedPreferences.edit().putString(key, "dummyTest").commit();
        mStringPreference = new StringPreference(mSharedPreferences, key);
        boolean result = mStringPreference.isSet();
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsSetFalse() {
        String key = "testKey4";
        mStringPreference = new StringPreference(mSharedPreferences, key);
        boolean result = mStringPreference.isSet();
        assertThat(result, is(false));
    }

    @Test
    public void shouldDeletePreference() {
        String key = "testKey5";
        mSharedPreferences.edit().putString(key, "dummyTest").commit();
        mStringPreference = new StringPreference(mSharedPreferences, key);
        mStringPreference.delete();
        assertThat(mSharedPreferences.contains(key), is(false));
    }
}
