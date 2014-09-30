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

package com.ushahidi.android.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Custom view to capitalize the first letter of a sentence. Sadly Android doest support this out of
 * the box.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class CapitalizedTextView extends TextView {

    public CapitalizedTextView(Context context) {
        this(context, null, 0);
    }

    public CapitalizedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CapitalizedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text.length() > 0) {
            text = String.valueOf(text.charAt(0)).toUpperCase() + text
                    .subSequence(1, text.length());
        }
        super.setText(text, type);
    }
}
