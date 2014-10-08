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

import com.ushahidi.android.R;
import com.ushahidi.android.core.usecase.deployment.DeleteDeployment;
import com.ushahidi.android.core.usecase.deployment.ListDeployment;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.mapper.DeploymentModelDataMapper;
import com.ushahidi.android.presenter.DeleteDeploymentPresenter;
import com.ushahidi.android.presenter.DeploymentListPresenter;
import com.ushahidi.android.ui.adapter.DeploymentAdapter;
import com.ushahidi.android.ui.listener.SwipeDismissListViewTouchListener;
import com.ushahidi.android.ui.view.IDeleteDeploymentView;
import com.ushahidi.android.ui.view.IDeploymentListView;
import com.ushahidi.android.ui.widget.DeploymentListView;
import com.ushahidi.android.ui.widget.InteractiveToast;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;

import static com.ushahidi.android.ui.widget.DeploymentListView.DeploymentParcelable;

/**
 * Shows list of deployment.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ListDeploymentFragment extends BaseListFragment<DeploymentModel, DeploymentAdapter>
        implements
        IDeploymentListView, IDeleteDeploymentView {

    @Inject
    ListDeployment mListDeployment;

    @Inject
    DeleteDeployment mDeleteDeployment;

    @Inject
    DeploymentModelDataMapper mDeploymentModelDataMapper;

    @InjectView(R.id.loading_list_progress)
    ProgressBar mListLoadingProgress;

    @InjectView(R.id.interactive_toast)
    LinearLayout mInteractiveToastContainer; // Layout container for InteractiveToast

    private static final String INTERACTIVE_TOAST_BUNDLE_KEY = "selected_items";

    private DeploymentListPresenter mDeploymentListPresenter;

    private DeleteDeploymentPresenter mDeleteDeploymentPresenter;

    private DeploymentListListener mDeploymentListListener;

    private DeploymentListView mDeploymentListView;

    private boolean isDismissToDelete = false;

    private InteractiveToast mInteractiveToast;


    public ListDeploymentFragment() {
        super(DeploymentAdapter.class, R.layout.list_deployment, 0,
                android.R.id.list);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDeploymentListPresenter.init();
        mDeploymentListView = (DeploymentListView) mListView;
        mDeploymentListView.setDeleteDeploymentPresenter(mDeleteDeploymentPresenter);
        mInteractiveToast = new InteractiveToast(mInteractiveToastContainer);

        SwipeDismissListViewTouchListener mListViewTouchListener =
                new SwipeDismissListViewTouchListener(mListView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {

                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                mInteractiveToast
                                        .setInteractiveToastListener(
                                                new InteractiveToast.InteractiveToastListener() {
                                                    @Override
                                                    public void onPressed(Parcelable token) {

                                                        Bundle b = (Bundle) token;
                                                        final ArrayList<DeploymentParcelable> items
                                                                = b
                                                                .getParcelableArrayList(
                                                                        INTERACTIVE_TOAST_BUNDLE_KEY);

                                                        if (!items.isEmpty()) {

                                                            for (DeploymentParcelable deploymentModel : items) {
                                                                mAdapter.addItem(deploymentModel
                                                                                .getPosition(),
                                                                        deploymentModel
                                                                                .getDeploymentModel());
                                                            }
                                                            items.clear();
                                                        }

                                                    }
                                                });

                                mInteractiveToast
                                        .setOnHideListener(new InteractiveToast.OnHideListener() {
                                            @Override
                                            public void onHide(Parcelable token) {

                                                Bundle b = (Bundle) token;
                                                final ArrayList<DeploymentParcelable> items = b
                                                        .getParcelableArrayList(
                                                                INTERACTIVE_TOAST_BUNDLE_KEY);
                                                if (!items.isEmpty()) {

                                                    for (DeploymentParcelable deploymentModel : items) {
                                                        mDeleteDeploymentPresenter
                                                                .deleteDeployment(deploymentModel
                                                                        .getDeploymentModel());
                                                    }
                                                    items.clear();
                                                }

                                            }
                                        });
                                ArrayList<DeploymentParcelable> items = new ArrayList<>();

                                if (reverseSortedPositions.length > 0) {

                                    for (int i : reverseSortedPositions) {
                                        DeploymentParcelable parcelable = new DeploymentParcelable(
                                                mAdapter.getItem(i),
                                                i);
                                        items.add(parcelable);
                                        mAdapter.removeItem(mAdapter.getItem(i));
                                    }

                                }

                                // Stores the selected models into a bundle for later reuse.
                                Bundle b = new Bundle();
                                b.putParcelableArrayList(INTERACTIVE_TOAST_BUNDLE_KEY, items);

                                mInteractiveToast
                                        .show(getString(R.string.items_deleted,
                                                        reverseSortedPositions.length),
                                                getString(R.string.undo),
                                                R.drawable.ic_undo, b);

                            }
                        });
        mDeploymentListView.setOnTouchListener(mListViewTouchListener);
        mDeploymentListView.setOnScrollListener(mListViewTouchListener.makeScrollListener());
        mDeploymentListView.setInteractiveToast(mInteractiveToast);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DeploymentListListener) {
            mDeploymentListListener = (DeploymentListListener) activity;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mDeploymentListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDeploymentListPresenter.pause();
    }

    @Override
    void initPresenter() {
        mDeploymentListPresenter = new DeploymentListPresenter(this, mListDeployment,
                mDeploymentModelDataMapper);
        mDeleteDeploymentPresenter = new DeleteDeploymentPresenter(this, mDeleteDeployment,
                mDeploymentModelDataMapper);
    }

    @Override
    public void renderDeploymentList(List<DeploymentModel> deploymentModel) {
        if (deploymentModel != null && mAdapter != null) {
            mAdapter.setItems(deploymentModel);
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showLoading() {
        setViewGone(mListLoadingProgress, false);
    }

    @Override
    public void hideLoading() {
        setViewGone(mListLoadingProgress);
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void onDeploymentDeleted() {
        if (!isDismissToDelete) {
            showToast(getString(R.string.items_deleted,
                    mDeploymentListView.getNumberOfItemsDeleted()));
            mDeploymentListPresenter.refreshList();
        }
    }

    @Override
    public void showError(String message) {
        showToast(message);
    }

    @Override
    public void refreshList() {
        mDeploymentListPresenter.refreshList();
    }

    @Override
    public void editDeployment(DeploymentModel deploymentModel) {
        mDeploymentListListener.onDeploymentClicked(deploymentModel);
    }

    private void onDeplomentClicked(DeploymentModel deploymentModel) {
        if (mDeploymentListPresenter != null) {
            mDeploymentListPresenter.onDeploymentClicked(deploymentModel);
        }

    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        DeploymentModel deploymentModel = mAdapter.getItem(position);
        onDeplomentClicked(deploymentModel);
    }

    /**
     * Listens for deployment list events
     */
    public interface DeploymentListListener {

        void onDeploymentClicked(final DeploymentModel deploymentModel);

    }

}
