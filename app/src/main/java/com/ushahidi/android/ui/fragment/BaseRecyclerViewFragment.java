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

import com.ushahidi.android.model.Model;
import com.ushahidi.android.ui.activity.BaseActivity;
import com.ushahidi.android.ui.adapter.BaseRecyclerViewAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Base {@link android.app.ListFragment} that every fragment list will extend from.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseRecyclerViewFragment<M extends Model, L extends BaseRecyclerViewAdapter>
        extends Fragment {

    private static String TAG = BaseRecyclerViewFragment.class.getSimpleName();

    /**
     * Layout resource mId
     */
    protected final int mLayout;

    /**
     * Menu resource mId
     */
    protected final int mMenu;

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
    protected RecyclerView mRecyclerView;

    protected BaseRecyclerViewFragment(Class<L> adapterClass, int layout, int menu,
            int recyclerViewId) {
        mRecyclerViewAdapterClass = adapterClass;
        mRecyclerViewId = recyclerViewId;
        mMenu = menu;
        mLayout = layout;
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

        if (mRecyclerViewId != 0) {
            mRecyclerViewAdapter = BaseRecyclerViewFragment
                    .createInstance(mRecyclerViewAdapterClass);
            mRecyclerView.setFocusable(true);
            mRecyclerView.setFocusableInTouchMode(true);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        initPresenter();

    }

    /**
     * Initializes the {@link com.ushahidi.android.presenter.IPresenter} for this fragment in a MVP
     * pattern used to architect the application presentation layer.
     */
    abstract void initPresenter();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mMenu != 0) {
            inflater.inflate(mMenu, menu);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        injectDependencies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = null;
        if (mLayout != 0) {
            root = inflater.inflate(mLayout, container, false);
        }
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectViews(view);
    }

    protected View fadeIn(final View view, final boolean animate) {
        if (view != null) {
            if (animate) {
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_in));

            } else {

                view.clearAnimation();
            }
        }

        return view;

    }

    protected View fadeOut(final View view, final boolean animate) {
        if (view != null) {
            if (animate) {
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_out));
            } else {
                view.clearAnimation();
            }
        }
        return view;

    }

    protected View setViewGone(final View view) {
        return setViewGone(view, true);
    }

    protected View setViewGone(final View view, final boolean gone) {
        if (view != null) {
            if (gone) {
                if (GONE != view.getVisibility()) {

                    fadeOut(view, true);

                    view.setVisibility(GONE);
                }
            } else {
                if (VISIBLE != view.getVisibility()) {
                    view.setVisibility(VISIBLE);

                    fadeIn(view, true);

                }
            }
        }
        return view;
    }

    /**
     * Replace every field annotated using @Inject annotation with the provided dependency specified
     * inside a Dagger module value.
     */
    private void injectDependencies() {
        ((BaseActivity) getActivity()).inject(this);
    }


    /**
     * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
     * value.
     *
     * @param view to extract each widget injected in the fragment.
     */
    private void injectViews(final View view) {
        ButterKnife.inject(this, view);
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message A message resource
     */
    protected void showToast(int message) {
        Toast.makeText(getActivity(), getText(message), Toast.LENGTH_LONG)
                .show();
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message A message string
     */
    protected void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG)
                .show();
    }
}
