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

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MovableFab extends FloatingActionButton {

    private static final int TRANSLATE_DURATION_MILLIS = 500;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private boolean mHidden = false;

    private float mYDisplayed = -1;

    private float mYHidden = -1;

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
        animate().translationY(translationY).setInterpolator(
            new AccelerateInterpolator(2)).start();
    }

    public void moveDown(int level) {
        animate().translationY(level).setInterpolator(
            new DecelerateInterpolator(2)).start();

    }

    private void move(int level) {
        animate().setInterpolator(mInterpolator)
            .setDuration(TRANSLATE_DURATION_MILLIS)
            .translationYBy(level);
    }

    public void hide(boolean hide) {
        // If the hidden state is being updated
        if (mHidden != hide) {

            // Store the new hidden state
            mHidden = hide;

            animate().setInterpolator(mInterpolator)
                .setDuration(TRANSLATE_DURATION_MILLIS)
                .translationY(mHidden ? mYHidden : mYDisplayed);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mYDisplayed == -1) {
            mYDisplayed = getY();
        }
    }

}
