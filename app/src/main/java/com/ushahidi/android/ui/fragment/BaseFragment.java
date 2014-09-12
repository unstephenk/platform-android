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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Base {@link android.app.Fragment} class that every Fragment in this app will have to implement.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseFragment extends Fragment {

    /**
     * Layout resource mId
     */
    protected final int mLayout;

    /**
     * Menu resource mId
     */
    protected final int mMenu;

    /**
     * BaseActivity
     *
     * @param menu mMenu resource mId
     */
    protected BaseFragment(int layout, int menu) {
        this.mLayout = layout;
        this.mMenu = menu;
    }

    /**
     * Initializes the {@link com.ushahidi.android.presenter.IPresenter} for this fragment in a MVP
     * pattern used to architect the application presentation layer.
     */
    abstract void initPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        android.view.View root = null;
        if (mLayout != 0) {
            root = inflater.inflate(mLayout, container, false);
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.mMenu != 0) {
            inflater.inflate(this.mMenu, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    protected View fadeIn(final android.view.View view, final boolean animate) {
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

    protected View fadeOut(final android.view.View view, final boolean animate) {
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
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message A message resource
     */
    protected void showToast(int message) {
        Toast.makeText(getActivity(), getText(message), Toast.LENGTH_LONG)
                .show();
    }
}
