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

package com.ushahidi.android.ui.activity;

import com.ushahidi.android.R;
import com.ushahidi.android.module.SettingsModule;
import com.ushahidi.android.ui.fragment.SettingsFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class SettingsActivity extends BaseActivity {

    public SettingsActivity() {
        super(R.layout.settings_activity, 0);
    }

    public static Intent getIntent(final Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.preference_container, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new LinkedList<>();
        modules.add(new SettingsModule());
        return modules;
    }

    @Override
    protected void initNavDrawerItems() {
        // Does not support navigation drawer
    }

}
