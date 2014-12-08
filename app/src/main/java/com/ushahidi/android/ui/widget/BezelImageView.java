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

import com.ushahidi.android.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An {@link android.widget.ImageView} that draws its contents inside a mask and draws a border
 * drawable on top. This is useful for applying a beveled look to image contents, but is also
 * flexible enough for use with other desired aesthetics.
 *
 * Taken from Google IO 2014 sample app.
 */
public class BezelImageView extends ImageView {

    private Paint mBlackPaint;

    private Paint mMaskedPaint;

    private Rect mBounds;

    private RectF mBoundsF;

    private Drawable mBorderDrawable;

    private Drawable mMaskDrawable;

    private ColorMatrixColorFilter mDesaturateColorFilter;

    private boolean mDesaturateOnPress = false;

    private boolean mCacheValid = false;

    private Bitmap mCacheBitmap;

    private int mCachedWidth;

    private int mCachedHeight;

    public BezelImageView(Context context) {
        this(context, null);
    }

    public BezelImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezelImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Attribute initialization
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BezelImageView,
                defStyle, 0);

        mMaskDrawable = a.getDrawable(R.styleable.BezelImageView_maskDrawable);
        if (mMaskDrawable != null) {
            mMaskDrawable.setCallback(this);
        }

        mBorderDrawable = a.getDrawable(R.styleable.BezelImageView_borderDrawable);
        if (mBorderDrawable != null) {
            mBorderDrawable.setCallback(this);
        }

        mDesaturateOnPress = a.getBoolean(R.styleable.BezelImageView_desaturateOnPress,
                mDesaturateOnPress);

        a.recycle();

        // Other initialization
        mBlackPaint = new Paint();
        mBlackPaint.setColor(0xff000000);

        mMaskedPaint = new Paint();
        mMaskedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // Always want a cache allocated.
        mCacheBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

        if (mDesaturateOnPress) {
            // Create a desaturate color filter for pressed state.
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            mDesaturateColorFilter = new ColorMatrixColorFilter(cm);
        }
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        final boolean changed = super.setFrame(l, t, r, b);
        mBounds = new Rect(0, 0, r - l, b - t);
        mBoundsF = new RectF(mBounds);

        if (mBorderDrawable != null) {
            mBorderDrawable.setBounds(mBounds);
        }
        if (mMaskDrawable != null) {
            mMaskDrawable.setBounds(mBounds);
        }

        if (changed) {
            mCacheValid = false;
        }

        return changed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBounds == null) {
            return;
        }

        int width = mBounds.width();
        int height = mBounds.height();

        if (width == 0 || height == 0) {
            return;
        }

        if (!mCacheValid || width != mCachedWidth || height != mCachedHeight) {
            // Need to redraw the cache
            if (width == mCachedWidth && height == mCachedHeight) {
                // Have a correct-sized bitmap cache already allocated. Just erase it.
                mCacheBitmap.eraseColor(0);
            } else {
                // Allocate a new bitmap with the correct dimensions.
                mCacheBitmap.recycle();
                //noinspection AndroidLintDrawAllocation
                mCacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                mCachedWidth = width;
                mCachedHeight = height;
            }

            Canvas cacheCanvas = new Canvas(mCacheBitmap);
            if (mMaskDrawable != null) {
                int sc = cacheCanvas.save();
                mMaskDrawable.draw(cacheCanvas);
                mMaskedPaint.setColorFilter((mDesaturateOnPress && isPressed())
                        ? mDesaturateColorFilter : null);
                cacheCanvas.saveLayer(mBoundsF, mMaskedPaint,
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
                super.onDraw(cacheCanvas);
                cacheCanvas.restoreToCount(sc);
            } else if (mDesaturateOnPress && isPressed()) {
                int sc = cacheCanvas.save();
                cacheCanvas.drawRect(0, 0, mCachedWidth, mCachedHeight, mBlackPaint);
                mMaskedPaint.setColorFilter(mDesaturateColorFilter);
                cacheCanvas.saveLayer(mBoundsF, mMaskedPaint,
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
                super.onDraw(cacheCanvas);
                cacheCanvas.restoreToCount(sc);
            } else {
                super.onDraw(cacheCanvas);
            }

            if (mBorderDrawable != null) {
                mBorderDrawable.draw(cacheCanvas);
            }
        }

        // Draw from cache
        canvas.drawBitmap(mCacheBitmap, mBounds.left, mBounds.top, null);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mBorderDrawable != null && mBorderDrawable.isStateful()) {
            mBorderDrawable.setState(getDrawableState());
        }
        if (mMaskDrawable != null && mMaskDrawable.isStateful()) {
            mMaskDrawable.setState(getDrawableState());
        }
        if (isDuplicateParentStateEnabled()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        if (who == mBorderDrawable || who == mMaskDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(who);
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mBorderDrawable || who == mMaskDrawable || super.verifyDrawable(who);
    }
}
