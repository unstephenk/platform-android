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

import com.getbase.floatingactionbutton.FloatingActionButton;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import timber.log.Timber;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MovableFab extends FloatingActionButton {

    private static final int TRANSLATE_DURATION_MILLIS = 200;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public MovableFab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MovableFab(Context context) {
        this(context, null);
    }

    public MovableFab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void moveUp(int level) {
        int translationY = -level;
        move(translationY);
    }

    public void moveDown(int level) {
        move(level);
    }

    private void move(int level) {
        animate().setInterpolator(mInterpolator)
                .setDuration(TRANSLATE_DURATION_MILLIS)
                .translationYBy(level);
    }
}
