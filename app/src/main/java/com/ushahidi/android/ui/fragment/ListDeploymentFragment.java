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


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ushahidi.android.R;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.presenter.DeleteDeploymentPresenter;
import com.ushahidi.android.presenter.ListDeploymentPresenter;
import com.ushahidi.android.ui.adapter.DeploymentAdapter;
import com.ushahidi.android.ui.listener.RecyclerViewItemTouchListenerAdapter;
import com.ushahidi.android.ui.listener.SwipeToDismissTouchListener;
import com.ushahidi.android.ui.widget.DeploymentRecyclerView;
import com.ushahidi.android.ui.widget.MovableFab;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;

/**
 * Shows list of deployment.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ListDeploymentFragment
    extends BaseRecyclerViewFragment<DeploymentModel, DeploymentAdapter>
    implements
    ListDeploymentPresenter.View, DeleteDeploymentPresenter.View,
    RecyclerViewItemTouchListenerAdapter.RecyclerViewOnItemClickListener {

    @Inject
    ListDeploymentPresenter mDeploymentListPresenter;

    @Inject
    DeleteDeploymentPresenter mDeleteDeploymentPresenter;

    @InjectView(R.id.fab)
    MovableFab mFab;

    @InjectView(android.R.id.empty)
    TextView mEmptyView;

    private DeploymentListListener mDeploymentListListener;

    private DeploymentRecyclerView mDeploymentRecyclerView;

    private boolean isDismissToDelete = false;

    public ListDeploymentFragment() {
        super(DeploymentAdapter.class, R.layout.list_deployment, 0,
            android.R.id.list);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDeploymentListPresenter.init();
        mDeploymentRecyclerView = (DeploymentRecyclerView) mBloatedRecyclerView;
        mDeploymentRecyclerView.setDeleteDeploymentPresenter(mDeleteDeploymentPresenter);

        if (mFab != null) {
            setViewGone(mFab, false);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeploymentListListener != null) {
                        mDeploymentListListener.onFabClicked();
                    }
                }
            });
        }

        initRecyclerView();
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
        mDeploymentListPresenter.setView(this);
        mDeploymentListPresenter.setView(this);
        mDeleteDeploymentPresenter.setView(this);
    }

    @Override
    public void renderDeploymentList(List<DeploymentModel> deploymentModel) {
        if (deploymentModel != null && mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.setItems(deploymentModel);
        }
    }

    private void setEmptyView() {
        if (mRecyclerViewAdapter != null && mRecyclerViewAdapter.getItemCount() == 0) {
            setViewGone(mEmptyView, false);
        } else {
            setViewGone(mEmptyView);
        }
    }

    private void initRecyclerView() {

        mRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setEmptyView();
            }
        });

        RecyclerViewItemTouchListenerAdapter recyclerViewItemTouchListenerAdapter =
            new RecyclerViewItemTouchListenerAdapter(mDeploymentRecyclerView.recyclerView, this);
        mDeploymentRecyclerView.recyclerView.addOnItemTouchListener(recyclerViewItemTouchListenerAdapter);
        mDeploymentRecyclerView.setMovableFab(mFab);
        mDeploymentRecyclerView.setAdapter(mRecyclerViewAdapter);
        swipeToDeleteUndo();
        setEmptyView();
    }

    @Override
    public Context getAppContext() {
        return getActivity();
    }

    @Override
    public void showLoading() {
        //setViewGone(mListLoadingProgress, false);
    }

    @Override
    public void hideLoading() {
        //setViewGone(mListLoadingProgress);
    }

    @Override
    public void showRetry(String message) {

    }

    @Override
    public void onDeploymentDeleted() {
        if (!isDismissToDelete) {
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

    public void requestQuery(final String query) {
        Handler handler = new Handler();
        final Runnable filterDeployments = new Runnable() {
            public void run() {
                try {
                    mRecyclerViewAdapter.getFilter().filter(query);
                } catch (Exception e) {
                    refreshList();
                }
            }
        };

        handler.post(filterDeployments);
    }

    private void swipeToDeleteUndo() {

        mDeploymentRecyclerView.initAdapter(mRecyclerViewAdapter);

        mDeploymentRecyclerView.setSwipeToDismissCallback(new SwipeToDismissTouchListener.DismissCallbacks() {

            @Override
            public SwipeToDismissTouchListener.SwipeDirection canDismiss(int position) {
                return SwipeToDismissTouchListener.SwipeDirection.BOTH;
            }

            @Override
            public void onDismiss(RecyclerView view,
                                  List<SwipeToDismissTouchListener.PendingDismissData> dismissData) {

                for (SwipeToDismissTouchListener.PendingDismissData data : dismissData) {
                    mDeploymentRecyclerView.mPendingDeletedDeployments.add(
                        new DeploymentRecyclerView.PendingDeletedDeployment(
                            data.position
                            , mRecyclerViewAdapter.getItem(data.position)));
                    mRecyclerViewAdapter.removeItem(
                        mRecyclerViewAdapter.getItem(data.position));
                }
                mDeploymentRecyclerView.deleteItems();
            }
        });
    }

    @Override
    public void onItemClick(RecyclerView parent, View clickedView, int position) {
        if (mRecyclerViewAdapter.getItemCount() > 0) {
            DeploymentModel deploymentModel = mRecyclerViewAdapter.getItem(position);
            onDeplomentClicked(deploymentModel);
        }
    }

    @Override
    public void onItemLongClick(RecyclerView parent, View clickedView, int position) {
        // Do nothing
    }

    /**
     * Listens for deployment list events
     */
    public interface DeploymentListListener {

        void onDeploymentClicked(final DeploymentModel deploymentModel);

        void onFabClicked();

    }

}
