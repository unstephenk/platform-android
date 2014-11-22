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
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.module.DeploymentUiModule;
import com.ushahidi.android.ui.fragment.AddDeploymentFragment;
import com.ushahidi.android.ui.fragment.ListDeploymentFragment;
import com.ushahidi.android.ui.fragment.UpdateDeploymentFragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.FrameLayout;

import java.util.LinkedList;
import java.util.List;

/**
 * List Deployment Activity
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentActivity extends BaseActivity
        implements ListDeploymentFragment.DeploymentListListener,
        UpdateDeploymentFragment.DeploymentUpdateListener,
        AddDeploymentFragment.AddDeploymentListener {

    private FrameLayout addDeploymentLayout;

    private ListDeploymentFragment mListDeploymentFragment;

    SearchView mSearchView = null;

    String mQuery = "";

    public DeploymentActivity() {
        super(R.layout.activity_deployment_list, R.menu.list_deployment);
    }

    private void init() {

        addDeploymentLayout
                = (FrameLayout) findViewById(R.id.add_deployment_fragment_container);

        mListDeploymentFragment = (ListDeploymentFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_list_deployments);

        if (addDeploymentLayout != null) {
            addFragment(R.id.add_deployment_fragment_container, AddDeploymentFragment.newInstance(),
                    AddDeploymentFragment.ADD_FRAGMENT_TAG);
        }

        // Actionbar title
        setTitle(null);
    }

    private void replaceFragment(Long deploymentId) {
        replaceFragment(R.id.add_deployment_fragment_container,
                UpdateDeploymentFragment.newInstance(deploymentId),
                UpdateDeploymentFragment.UPDATE_FRAGMENT_TAG);
    }

    private void replaceFragment() {
        replaceFragment(R.id.add_deployment_fragment_container, AddDeploymentFragment.newInstance(),
                AddDeploymentFragment.ADD_FRAGMENT_TAG);
    }

    private void refreshList() {
        if (mListDeploymentFragment != null) {
            mListDeploymentFragment.refreshList();
            replaceFragment();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        handleSearchIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleSearchIntent(intent);
    }

    private void handleSearchIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            query = query == null ? "" : query;
            mQuery = query;
            performQuery(query);
            if (mSearchView != null) {
                mSearchView.setQuery(query, false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        mSearchView =
                (SearchView) menu.findItem(R.id.menu_add_search).getActionView();
        initSearchView();
        return true;
    }

    private void initSearchView() {
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconified(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchView.clearFocus();
                performQuery(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    mListDeploymentFragment.refreshList();
                } else {
                    performQuery(s);
                }

                return true;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mListDeploymentFragment.refreshList();
                return false;
            }
        });

        if (!TextUtils.isEmpty(mQuery)) {
            mSearchView.setQuery(mQuery, false);
        }
        SearchView.SearchAutoComplete searchAutoComplete
                = (SearchView.SearchAutoComplete) mSearchView
                .findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.white));
        searchAutoComplete.setTextSize(14);
    }

    private void performQuery(String query) {
        if (null != mListDeploymentFragment) {
            mListDeploymentFragment.requestQuery(query);
        }
    }

    @Override
    protected List<Object> getModules() {
        List<Object> modules = new LinkedList<>();
        modules.add(new DeploymentUiModule());
        return modules;
    }

    @Override
    protected void initNavDrawerItems() {
        // DO Nothing as this activity doesn't support navigation drawer
    }

    public static Intent getIntent(final Context context) {
        return new Intent(context, DeploymentActivity.class);
    }


    @Override
    public void onDeploymentClicked(DeploymentModel deploymentModel) {

        if (addDeploymentLayout != null) {
            replaceFragment(deploymentModel.getId());
        } else {
            launcher.launchUpdateDeployment(deploymentModel.getId());
        }
    }

    @Override
    public void onFabClicked() {
        launcher.launchAddDeployment();
    }

    @Override
    public void onUpdateNavigateOrReloadList() {
        refreshList();
    }

    @Override
    public void onCancelUpdate() {
        replaceFragment();
    }

    @Override
    public void onAddNavigateOrReloadList() {
        refreshList();
    }

    @Override
    public void onCancelAdd() {
        replaceFragment();
    }
}
