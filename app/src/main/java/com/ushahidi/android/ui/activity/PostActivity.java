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
import com.ushahidi.android.core.usecase.deployment.ListDeployment;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.module.PostUiModule;
import com.ushahidi.android.presenter.DeploymentNavPresenter;
import com.ushahidi.android.ui.fragment.ListPostFragment;
import com.ushahidi.android.ui.view.IDeploymentNavView;
import com.ushahidi.android.ui.widget.NavDrawerItem;
import com.ushahidi.android.ui.widget.SlidingTabLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

/**
 * Post Activity
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostActivity extends BaseActivity implements NavDrawerItem.NavDrawerItemListener,
        NavDrawerItem.NavDeploymentItemListener, IDeploymentNavView {

    private static final String SELECTED_TAB = "selected_tab";

    @Inject
    ListDeployment mListDeployment;

    @Inject
    DeploymentModelDataMapper mDeploymentModelDataMapper;

    private DeploymentNavPresenter mDeploymentNavPresenter;

    private SlidingTabLayout mSlidingTabStrip;

    private ViewPager mViewPager;

    private TabPagerAdapter mAdapter;

    private int mCurrentItem;

    private List<String> mTabTitle;

    public PostActivity() {
        super(R.layout.activity_post, R.menu.post, R.id.drawer_layout, R.id.navdrawer_items_list);
    }

    public static Intent getIntent(final Context context) {
        return new Intent(context, PostActivity.class);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeploymentNavPresenter = new DeploymentNavPresenter(this, mListDeployment,
                mDeploymentModelDataMapper);

        mTabTitle = new ArrayList<>();
        mTabTitle.add(getString(R.string.list));
        mTabTitle.add(getString(R.string.map));

        mSlidingTabStrip = (SlidingTabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mAdapter = new TabPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageMargin(
                getResources().getDimensionPixelSize(R.dimen.element_spacing_normal));

        mSlidingTabStrip.setViewPager(mViewPager);
        mSlidingTabStrip.setTabListener(new SlidingTabLayout.TabListener() {
            @Override
            public void onTabSelected(int pos) {
                // NO-OP
            }

            @Override
            public void onTabReSelected(int pos) {
                mAdapter.getItem(pos);

            }
        });

        if (savedInstanceState != null) {
            mCurrentItem = savedInstanceState.getInt(SELECTED_TAB);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mNavDrawerItemViews.clear();
        mDeploymentNavPresenter.resume();
        mSlidingTabStrip.getBackground().setAlpha(255);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCurrentItem = mViewPager.getCurrentItem();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_TAB, mCurrentItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new LinkedList<>();
        modules.add(new PostUiModule());
        return modules;
    }

    @Override
    protected void initNavDrawerItems() {
        mNavDrawerItemViews
                .add(setNavDrawerItem(R.string.manage_deployments, R.drawable.ic_action_map, 3,
                        DeploymentActivity.getIntent(this)));

        mNavDrawerItemViews
                .add(setNavDrawerItem(R.string.settings, R.drawable.ic_action_settings, 4));

        mNavDrawerItemViews.add(setNavDrawerItem(R.string.about, R.drawable.ic_action_info, 6));

        mNavDrawerItemViews
                .add(setNavDrawerItem(R.string.send_feedback, R.drawable.ic_action_help, 7));

        // Select activity
        mNavDrawerItemViews.get(0).setSelected(true);

        setFragments();

    }

    private void setFragments() {

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(createTabFragments(PostTab.LIST));
        fragments.add(createTabFragments(PostTab.MAP));

        mAdapter.setFragments(fragments);
        mSlidingTabStrip.notifyDataSetChanged();
        mViewPager.setCurrentItem(mCurrentItem);
    }

    private Fragment createTabFragments(PostTab tab) {
        switch (tab) {
            case LIST:
                return new ListPostFragment();
            case MAP:
                return new ListPostFragment();
        }
        return null;
    }

    private NavDrawerItem setNavDrawerItem(int titleResId, int iconId, int position) {
        NavDrawerItem navDrawerItem = new NavDrawerItem(this, getResources().getString(titleResId),
                iconId, position);
        navDrawerItem.setOnClickListener(this);
        return navDrawerItem;
    }

    private NavDrawerItem setNavDrawerItem(int titleResId, int iconId, int position,
            Fragment fragment, String tag) {
        return setNavDrawerItem(getResources().getString(titleResId), iconId, position, fragment,
                tag);
    }

    private NavDrawerItem setNavDrawerItem(String title, int iconId, int position,
            Fragment fragment, String tag) {

        NavDrawerItem navDrawerItem = new NavDrawerItem(this, title,
                iconId, position, fragment, R.id.swipe_refresh_layout, tag);
        navDrawerItem.setOnClickListener(this);
        return navDrawerItem;
    }

    private NavDrawerItem setNavDrawerItem(int titleResId, int iconId, int position,
            Intent intent) {
        return setNavDrawerItem(getResources().getString(titleResId), iconId, position, intent);
    }

    private NavDrawerItem setNavDrawerItem(String title, int iconId, int position, Intent intent) {
        NavDrawerItem navDrawerItem = new NavDrawerItem(this, title,
                iconId, position, intent);
        navDrawerItem.setOnClickListener(this);
        return navDrawerItem;
    }

    @Override
    public void onNavDrawerItemClick(NavDrawerItem navDrawerItem) {

        final int position = navDrawerItem.getPosition();
        for (NavDrawerItem item : mNavDrawerItemViews) {
            if (position != item.getPosition()) {
                item.setSelected(false);
            }
        }
        navDrawerItem.launchActivityOrFragment();
        closeNavDrawer();
    }

    @Override
    public void onNavDeploymentItemClick(NavDrawerItem navDrawerItem) {
        //TODO: Implement the action to be taken when Deployment navDrawerItem is clicked
    }

    @Override
    public void renderDeploymentList(List<DeploymentModel> listDeploymentModel) {
        for (int i = 0; i < listDeploymentModel.size(); i++) {
            NavDrawerItem navDrawerItem = setNavDrawerItem(listDeploymentModel.get(i).getTitle(),
                    R.drawable.ic_action_globe, i, null);
            navDrawerItem.setActive(listDeploymentModel.get(i).getStatus());
            navDrawerItem.setOnDeploymentClickListener(this);
            mNavDrawerItemViews.add(navDrawerItem);
        }
    }

    @Override
    public void showError(String message) {
        showToast(message);
    }

    @Override
    public Context getContext() {
        return getContext();
    }

    @Override
    public void createNav() {
        initNavDrawerItems();
        createNavDrawer();
    }

    private static enum PostTab {
        LIST, MAP
    }

    private class TabPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> mFragments;

        private TabPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();
        }

        public void setFragments(List<Fragment> fragments) {
            mFragments.clear();
            mFragments.addAll(fragments);
            notifyDataSetChanged();
        }

        @Override
        public final Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public final int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitle.get(position);
        }
    }
}
