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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.squareup.picasso.Picasso;
import com.ushahidi.android.R;
import com.ushahidi.android.UshahidiApplication;
import com.ushahidi.android.model.UserModel;
import com.ushahidi.android.module.ActivityModule;
import com.ushahidi.android.state.ApplicationState;
import com.ushahidi.android.state.IDeploymentState;
import com.ushahidi.android.state.IUserState;
import com.ushahidi.android.ui.widget.NavDrawerItem;
import com.ushahidi.android.util.GravatarUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.ObjectGraph;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;

/**
 * Adds shared functionality that exists between all extended Activities
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseActivity extends ActionBarActivity {

    private static final int ACCOUNT_BOX_EXPAND_ANIM_DURATION = 200;
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

    /**
     * Navigation Drawer Item resource ID
     */
    protected final int mDrawerItemsContainerId;

    // Navigation drawer view items
    protected List<NavDrawerItem> mNavDrawerItemViews = new ArrayList<>();

    // User profile
    protected View mLoginLayout;

    protected View mUserProfileLayout;

    @Inject
    ActivityLauncher launcher;

    @Inject
    IDeploymentState mDeploymentState;

    @Inject
    IUserState mUserState;

    @Inject
    ApplicationState mApplicationState;

    private ObjectGraph activityScopeGraph;

    // Navigation drawer:
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private ActionBar mActionBar = null;

    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;

    // ViewGroup that habours the navigation drawer items
    private ViewGroup mDrawerItemsContainer;

    private LinearLayout mUserAccountListContainer;

    private ImageView mUserAccountListExpandIndicator;

    private boolean mUserAccountListExpanded = false;


    public BaseActivity(int layout, int menu, int drawerLayoutId, int drawerItemsContainerId) {
        mLayout = layout;
        mMenu = menu;
        mDrawerLayoutId = drawerLayoutId;
        mDrawerItemsContainerId = drawerItemsContainerId;
    }

    public BaseActivity(int layout, int menu) {
        this(layout, menu, 0, 0);
    }

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

    /**
     * Method used to resolve dependencies provided by Dagger modules. Inject an object to provide
     * every @Inject annotation contained.
     *
     * @param object to inject.
     */
    public void inject(Object object) {
        if (activityScopeGraph == null) {
            injectDependencies();
        }
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
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (mActionBarToolbar != null) {
            mActionBarToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            });
        }

        createNavDrawerItems();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        injectViews();
        if (mLayout != 0) {
            setContentView(mLayout);
            mUserProfileLayout = findViewById(R.id.layout_user_profile);
            mLoginLayout = findViewById(R.id.layout_user_login);
        }

        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        createNavDrawer();
        showLoginOrUserProfile();
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            fadeIn(mainContent, true);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        mApplicationState.registerEvent(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mApplicationState.unregisterEvent(this);
        super.onPause();
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

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void createNavDrawerItems() {
        if (mDrawerItemsContainerId == 0) {
            return;
        }

        mDrawerItemsContainer = (ViewGroup) findViewById(mDrawerItemsContainerId);

        if (mDrawerItemsContainer != null) {
            //Remove previous items
            mDrawerItemsContainer.removeAllViews();
            for (NavDrawerItem view : mNavDrawerItemViews) {
                mDrawerItemsContainer.addView(view.getView());
            }
        }
    }

    protected void displayUserProfile(UserModel profile) {

        if (profile != null) {
            showUserProfile(profile);
        }
    }

    private void showLoginOrUserProfile() {
        if (mApplicationState.getUserModel() != null) {
            showUserProfile(mApplicationState.getUserModel());
        } else {
            if (mLoginLayout == null) {
                return;
            }
            mLoginLayout.setVisibility(VISIBLE);
            mUserProfileLayout.setVisibility(GONE);
            mLoginLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    launcher.launchLogin();
                    closeNavDrawer();
                }
            });
        }
    }

    private void showUserProfile(UserModel profile) {
        if (mUserProfileLayout == null) {
            return;
        }

        mLoginLayout.setVisibility(GONE);
        mUserProfileLayout.setVisibility(VISIBLE);
        mUserProfileLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeNavDrawer();
            }
        });

        TextView usernameTextView = (TextView) findViewById(R.id.user_username);
        TextView roleTextView = (TextView) findViewById(R.id.user_role);
        usernameTextView.setText(profile.getUsername());
        roleTextView.setText(profile.getRole().value);
        ImageView avatarImageView = (ImageView) findViewById(R.id.user_profile_image);

        if (profile.getEmail() != null) {
            Picasso.with(this).load(GravatarUtil.url(profile.getEmail())).into(avatarImageView);
        }
        mUserAccountListExpandIndicator = (ImageView) findViewById(R.id.expand_profile_indicator);

        final View chosenAccountView = findViewById(R.id.chosen_account_content_view);

        mUserAccountListExpandIndicator.setVisibility(View.VISIBLE);
        chosenAccountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserAccountListExpanded = !mUserAccountListExpanded;
                setupUserAccountListToggle();
            }
        });

        setupUserAccountListToggle();

    }

    protected void setUpUserAccountList(List<UserModel> userModels) {
        if (mUserAccountListContainer != null) {
            mUserAccountListContainer.removeAllViews();
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            for (UserModel userModel : userModels) {
                View itemView = layoutInflater.inflate(R.layout.list_item_user_account,
                    mUserAccountListContainer, false);

                TextView username = (TextView) itemView
                    .findViewById(R.id.user_account_profile_user_name);
                username.setText(userModel.getUsername());

                ImageView userProfileImage = (ImageView) itemView
                    .findViewById(R.id.user_account_profile_image);
                if (userModel.getEmail() != null) {
                    Picasso.with(this).load(GravatarUtil.url(userModel.getEmail()))
                        .into(userProfileImage);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: Implement a login process in the background
                        mUserAccountListExpanded = false;
                        setupUserAccountListToggle();
                        mDrawerLayout.closeDrawer(Gravity.START);

                    }
                });
                mUserAccountListContainer.addView(itemView);
            }
        }
    }

    /**
     */
    private void setupUserAccountListToggle() {
        mUserAccountListContainer = (LinearLayout) findViewById(R.id.user_account_list);

        // No user account list
        if (mUserAccountListContainer == null) {
            return;
        }

        mUserAccountListExpandIndicator.setImageResource(mUserAccountListExpanded ?
            R.drawable.ic_drawer_profile_collapse : R.drawable.ic_drawer_profile_expand);

        // Credits: http://goo.gl/yHfwZp
        int hideTranslateY = -mUserAccountListContainer.getHeight() / 4; // last 25% of animation

        if (mUserAccountListExpanded && mUserAccountListContainer.getTranslationY() == 0) {
            // initial setup
            mUserAccountListContainer.setAlpha(0);
            mUserAccountListContainer.setTranslationY(hideTranslateY);
        }

        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                mUserAccountListContainer.setVisibility(mUserAccountListExpanded
                    ? View.VISIBLE : View.INVISIBLE);

                mDrawerItemsContainer.setVisibility(mUserAccountListExpanded
                    ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationEnd(animation);
            }
        });

        if (mUserAccountListExpanded) {
            mUserAccountListContainer.setVisibility(View.VISIBLE);
            animateExpandView(set);
        } else {
            mDrawerItemsContainer.setVisibility(View.VISIBLE);
            animateCollapseView(set, hideTranslateY);
        }

        set.start();

    }

    private void animateExpandView(AnimatorSet set) {
        AnimatorSet subSet = animateExpandOrCollapseView(1, 0);
        set.playSequentially(
            ObjectAnimator.ofFloat(mUserAccountListContainer, View.ALPHA, 0)
                .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION),
            subSet);
        set.start();
    }

    private void animateCollapseView(AnimatorSet set, int hideTranslateY) {
        AnimatorSet subSet = animateExpandOrCollapseView(0, hideTranslateY);
        set.playSequentially(
            subSet,
            ObjectAnimator.ofFloat(mDrawerItemsContainer, View.ALPHA, 1)
                .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION));
        set.start();
    }

    private AnimatorSet animateExpandOrCollapseView(int together, int hideTranslateY) {
        final int ACCOUNT_BOX_EXPAND_ANIM_DURATION = 200;
        AnimatorSet subSet = new AnimatorSet();
        subSet.playTogether(
            ObjectAnimator.ofFloat(mUserAccountListContainer, View.ALPHA, together)
                .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION),
            ObjectAnimator
                .ofFloat(mUserAccountListContainer, View.TRANSLATION_Y, hideTranslateY)
                .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION));
        return subSet;

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

    @Subscribe
    public void onActiveDeploymentChanged(IDeploymentState.ActivatedDeploymentChangedEvent event) {
        setActionBarTitle(mDeploymentState.getActiveDeployment().getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mMenu != 0) {
            getMenuInflater().inflate(mMenu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager()
            .beginTransaction();
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
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager()
            .beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }

}
