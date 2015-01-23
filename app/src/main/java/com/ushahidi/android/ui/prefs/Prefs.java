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

package com.ushahidi.android.ui.prefs;

import com.ushahidi.android.data.pref.BooleanPreference;
import com.ushahidi.android.data.pref.StringPreference;
import com.ushahidi.android.ui.widget.TimePickerPreference;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class Prefs {

    private SharedPreferences mSharedPreferences;

    @Inject
    public Prefs(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public StringPreference getActiveDeploymentUrl() {
        return new StringPreference(getSharedPreferences(), "active_deployment_url", null);
    }

    public StringPreference getAccessToken() {
        return new StringPreference(getSharedPreferences(), "access_token", null);
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public BooleanPreference enableAutoSync() {
        return new BooleanPreference(getSharedPreferences(), "AutoSync", false);
    }

    public StringPreference getSyncFrequency() {
        return new StringPreference(mSharedPreferences, "post_sync_frequency",
                TimePickerPreference.DEFAULT_TIME_FREQUENCY);
    }

    public StringPreference getActiveUserAccount() {
        return new StringPreference(getSharedPreferences(), "active_user_account", null);
    }
}
