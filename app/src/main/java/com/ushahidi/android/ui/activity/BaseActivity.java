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

package com.ushahidi.android.ui.activity;

import com.ushahidi.android.R;
import com.ushahidi.android.UshahidiApplication;
import com.ushahidi.android.module.ActivityModule;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.ObjectGraph;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Adds shared functionality that exists between all extended Activities
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseActivity extends ActionBarActivity {

    /**
     * Layout resource id
     */
    protected final int mLayout;

    /**
     * Menu resource id
     */
    protected final int mMenu;

    /**
     * Navigation Drawer resource ID
     */
    protected final int mDrawerLayoutId;

    private ObjectGraph activityScopeGraph;

    @Inject
    ActivityLauncher launcher;

    // Navigation drawer:
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * Get a list of Dagger modules with Activity scope needed for this Activity.
     *
     * @return modules.
     */
    protected abstract List<Object> getModules();

    /**
     * Initialize nav drawer menu items.
     */
    protected abstract void initNavDrawerItems();

    private ActionBar mActionBar = null;

    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;

    public BaseActivity(int layout, int menu, int drawerLayoutId) {
        mLayout = layout;
        mMenu = menu;
        mDrawerLayoutId = drawerLayoutId;
    }

    public BaseActivity(int layout, int menu) {
        this(layout, menu, 0);
    }

    /**
     * Method used to resolve dependencies provided by Dagger modules. Inject an object to provide
     * every @Inject annotation contained.
     *
     * @param object to inject.
     */
    public void inject(Object object) {
        activityScopeGraph.inject(object);
    }

    /**
     * Creates a new Dagger ObjectGraph to add new dependencies using a plus operation and inject
     * the declared one in the activity. This new graph will be destroyed once the activity
     * lifecycle finishes.
     */
    private void injectDependencies() {
        UshahidiApplication ushahidiApplication = (UshahidiApplication) getApplication();
        List<Object> activityScopeModules = getModules();

        if (activityScopeModules != null) {
            activityScopeModules.add(new ActivityModule(this));
            activityScopeGraph = ushahidiApplication.add(activityScopeModules);
            inject(this);
        }
    }

    protected void createNavDrawer() {
        if (mDrawerLayoutId != 0) {
            mDrawerLayout = (DrawerLayout) findViewById(mDrawerLayoutId);
        }

        if (mDrawerLayout == null) {
            return;
        }

        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.theme_primary_dark));

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mActionBarToolbar,
                R.string.open, R.string.close
        );

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        initNavDrawerItems();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        injectViews();
        if (mLayout != 0) {
            setContentView(mLayout);
        }

        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
        }

        createNavDrawer();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    protected void setActionBarTitle(String title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }

    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mMenu != 0) {
            getMenuInflater().inflate(mMenu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    protected android.view.View fadeIn(final android.view.View view, final boolean animate) {
        if (view != null) {
            if (animate) {
                view.startAnimation(AnimationUtils.loadAnimation(this,
                        android.R.anim.fade_in));
            } else {

                view.clearAnimation();
            }
        }

        return view;

    }

    protected android.view.View fadeOut(final android.view.View view, final boolean animate) {
        if (view != null) {
            if (animate) {
                view.startAnimation(AnimationUtils.loadAnimation(this,
                        android.R.anim.fade_out));
            } else {
                view.clearAnimation();
            }
        }
        return view;

    }

    protected <V extends android.view.View> V setViewGone(final V view) {
        return setViewGone(view, true);
    }

    protected <V extends android.view.View> V setViewGone(final V view, final boolean gone) {
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
     * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
     * value.
     */
    private void injectViews() {
        ButterKnife.inject(this);
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message A message resource
     */
    protected void showToast(int message) {
        Toast.makeText(this, getText(message), Toast.LENGTH_LONG)
                .show();
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message A message resource
     */
    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
                .show();
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view where to add the fragment.
     * @param fragment        The fragment to be added.
     * @param tag             The tag for the fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }

    /**
     * Replaces an {@link Fragment} in this activity's layout.
     *
     * @param containerViewId The container view where to add the fragment.
     * @param fragment        The fragment to be replaced.
     * @param tag             The tag for the fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }

}
