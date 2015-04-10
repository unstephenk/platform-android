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

package com.ushahidi.android.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.InflateException;
import android.view.View;

import com.ushahidi.android.model.Model;
import com.ushahidi.android.ui.adapter.BaseRecyclerViewAdapter;
import com.ushahidi.android.ui.widget.BloatedRecyclerView;

import butterknife.InjectView;
import timber.log.Timber;

/**
 * Base {@link android.app.ListFragment} that every fragment list will extend from.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseRecyclerViewFragment<M extends Model, L extends BaseRecyclerViewAdapter>
    extends BaseFragment {

    private static String TAG = BaseRecyclerViewFragment.class.getSimpleName();

    /**
     * RecyclerView resource mId
     */
    private final int mRecyclerViewId;

    /**
     * RecyclerViewAdapter class
     */
    private final Class<L> mRecyclerViewAdapterClass;

    /**
     * RecyclerViewAdapter
     */
    protected L mRecyclerViewAdapter;

    /**
     * RecyclerView
     */
    @InjectView(android.R.id.list)
    protected BloatedRecyclerView mBloatedRecyclerView;

    protected BaseRecyclerViewFragment(Class<L> adapterClass, int layout, int menu,
                                       int recyclerViewId) {
        super(layout, menu);
        mRecyclerViewAdapterClass = adapterClass;
        mRecyclerViewId = recyclerViewId;
    }

    /**
     * Uses reflection to create a new instance of a class
     *
     * @param targetClass The class to create an instance
     * @return The created instance
     */
    private static <T> T createInstance(Class<?> targetClass) {
        try {
            return (T) targetClass.newInstance();
        } catch (IllegalAccessException e) {
            Timber.e(TAG, "IllegalAccessException", e);
        } catch (IllegalStateException e) {
            Timber.e(TAG, "IllegalStateException", e);
        } catch (SecurityException e) {
            Timber.e(TAG, "SecurityException", e);
            for (StackTraceElement exception : e.getStackTrace()) {
                Timber.e(TAG,
                    String.format("%s", exception.toString()));
            }
        } catch (InflateException e) {
            Timber.e(TAG, "InflateException", e);
        } catch (java.lang.InstantiationException e) {
            Timber.e(TAG, "InstantiationException", e);
        }
        return null;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mRecyclerViewId != 0) {
            mRecyclerViewAdapter = BaseRecyclerViewFragment
                .createInstance(mRecyclerViewAdapterClass);
            mBloatedRecyclerView.setFocusable(true);
            mBloatedRecyclerView.setFocusableInTouchMode(true);
            mBloatedRecyclerView.setAdapter(mRecyclerViewAdapter);
            mBloatedRecyclerView.setHasFixedSize(true);
            mBloatedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }
}
