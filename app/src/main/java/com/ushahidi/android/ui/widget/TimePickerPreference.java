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

package com.ushahidi.android.ui.widget;

import com.ushahidi.android.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import static com.ushahidi.android.ui.fragment.SettingsFragment.AUTO_SYNC_TIMES;

/**
 * Custom time Time Picker view for settings screen.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TimePickerPreference extends DialogPreference {

    public static final String DEFAULT_TIME_FREQUENCY = "05:00";

    // Hour field
    private int mLastHour = 0;

    // Minute field
    private int lastMinute = 0;

    private TimePicker mTimePicker = null;

    private SharedPreferences mSharedPreferences;

    public TimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
    }

    private static int getHour(String time) {
        String[] pieces = time.split(":");
        return (Integer.parseInt(pieces[0]));
    }

    private static int getMinute(String time) {
        String[] pieces = time.split(":");
        return (Integer.parseInt(pieces[1]));
    }

    @Override
    protected View onCreateDialogView() {
        mTimePicker = new TimePicker(getContext());
        return mTimePicker;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(mLastHour);
        mTimePicker.setCurrentMinute(lastMinute);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            mLastHour = mTimePicker.getCurrentHour();
            lastMinute = mTimePicker.getCurrentMinute();
            if (callChangeListener(getTimeValueAsString())) {
                persistString(getTimeValueAsString());
                saveTimeFrequency();
            }
        }
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time;

        if (restoreValue) {
            if (defaultValue == null) {
                time = getPersistedString(loadTimeFrequency());
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }

        mLastHour = getHour(time);
        lastMinute = getMinute(time);

    }

    public String getTimeValueAsString() {

        final String h = String.valueOf(mLastHour);
        final String m = String.valueOf(lastMinute);

        String time = appendZeroAtBegin(h) + ":" + appendZeroAtBegin(m);
        return time;
    }

    private String appendZeroAtBegin(String time) {
        StringBuilder sb = new StringBuilder();
        if (time.length() == 1) {
            sb.append(0);
        }
        return sb.append(time).toString();
    }

    private void saveTimeFrequency() {
        getSharedPreferences().edit().putString(AUTO_SYNC_TIMES, getTimeValueAsString()).apply();
    }

    private String loadTimeFrequency() {
        return getSharedPreferences().getString(AUTO_SYNC_TIMES, DEFAULT_TIME_FREQUENCY);
    }
}
