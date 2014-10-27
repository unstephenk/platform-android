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

package com.ushahidi.android.ui.form;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

/**
 * Select/Spinner form widget
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class SelectFormWidget extends FormWidget {

    private List<String> mOptions;

    private Spinner mSpinner;

    private ArrayAdapter<String> mAdapter;

    public SelectFormWidget(Context context, String name, String label, List<String> options) {
        super(context, name, label);
        mOptions = options;
        initUi(context);
    }

    private void initUi(Context context) {

        mSpinner = new Spinner(context);
        mSpinner.setLayoutParams(DEFAULT_LAYOUT_PARAMS);
        mAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (String item : mOptions) {
            mAdapter.add(item);
        }
        mSpinner.setAdapter(mAdapter);
        mLayout.addView(mSpinner);
    }

    @Override
    public void setValue(String value) {
        mSpinner.setSelection(mAdapter.getPosition(value));
    }

    @Override
    public String getValue() {
        return mAdapter.getItem(mSpinner.getSelectedItemPosition());
    }

    @Override
    public boolean validate() {
        return true;
    }

    public ArrayAdapter<String> getAdapter() {
        return mAdapter;
    }

    public Spinner getSpinner() {
        return mSpinner;
    }
}
