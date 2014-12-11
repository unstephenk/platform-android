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

import com.google.common.base.Preconditions;

import com.ushahidi.android.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.LruCache;
import android.widget.TextView;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class FontSupportedTextView extends TextView {

    private TypefaceManager mTypefaceManager;

    public FontSupportedTextView(Context context) {
        this(context, null);
    }

    public FontSupportedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontSupportedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTypefaceManager = new TypefaceManager(context.getAssets());
        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontSupportedTextView);
            if (a.hasValue(R.styleable.FontSupportedTextView_fontFile)) {
                setFont(a.getString(R.styleable.FontSupportedTextView_fontFile));
            }
            a.recycle();
        }
    }

    public void setFont(final String customFont) {
        Typeface typeface = mTypefaceManager.getTypeface(customFont);
        if (typeface != null) {
            setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            setTypeface(typeface);
        }
    }

    private class TypefaceManager {

        private final LruCache<String, Typeface> mCache;

        private final AssetManager mAssetManager;

        public TypefaceManager(AssetManager assetManager) {
            mAssetManager = Preconditions.checkNotNull(assetManager, "AssetManager cannot be null");
            mCache = new LruCache<>(3);
        }

        public Typeface getTypeface(final String filename) {
            Typeface typeface = mCache.get(filename);
            if (typeface == null) {
                typeface = Typeface.createFromAsset(mAssetManager, "fonts/" + filename);
                mCache.put(filename, typeface);
            }
            return typeface;
        }

    }
}
