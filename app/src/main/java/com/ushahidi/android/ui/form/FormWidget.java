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

import com.ushahidi.android.ui.form.validator.Validator;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for form widgets
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class FormWidget {

    protected String mName;

    protected String mLabel;

    protected LinearLayout mLayout;

    private TextView mTextView;

    protected int mPriority;

    protected static final LinearLayout.LayoutParams DEFAULT_LAYOUT_PARAMS
            = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    // Validation
    private boolean mRequired;

    protected List<Validator> mValidators = new ArrayList<>();

    public FormWidget(Context context, String name, String label) {
        mLayout = new LinearLayout(context);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        mLayout.setLayoutParams(DEFAULT_LAYOUT_PARAMS);
        mName = name;
        mLabel = label;

        mTextView = new TextView(context);
        mTextView.setText(getLabel());
        mTextView.setLayoutParams(DEFAULT_LAYOUT_PARAMS);

        mLayout.addView(mTextView);
    }

    public LinearLayout getLayout() {
        return mLayout;
    }

    public void setVisbility(int visbility) {
        mLayout.setVisibility(visbility);
    }

    public void addValidator(Validator validator) {
        mValidators.add(validator);
    }

    public List<Validator> getValidators() {
        return mValidators;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public int getPriority() {
        return mPriority;
    }

    public String getName() {
        return mName;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setRequired(boolean required) {
        mRequired = required;
    }

    public boolean isRequired() {
        return mRequired;
    }

    public TextView getLabelTextView() {
        return mTextView;
    }

    public abstract void setValue(String value);

    public abstract String getValue();

    public abstract boolean validate();

}
