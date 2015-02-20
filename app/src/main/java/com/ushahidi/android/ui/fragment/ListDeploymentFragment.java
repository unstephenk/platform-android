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


import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;
import com.ushahidi.android.R;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.presenter.DeleteDeploymentPresenter;
import com.ushahidi.android.presenter.ListDeploymentPresenter;
import com.ushahidi.android.ui.adapter.DeploymentAdapter;
import com.ushahidi.android.ui.listener.SwipeDismissRecyclerViewTouchListener;
import com.ushahidi.android.ui.prefs.Prefs;
import com.ushahidi.android.ui.widget.DeploymentRecyclerView;
import com.ushahidi.android.ui.widget.DeploymentRecyclerView.DeploymentParcelable;
import com.ushahidi.android.ui.widget.MovableFab;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
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
        RecyclerView.OnItemTouchListener {

    @Inject
    ListDeploymentPresenter mDeploymentListPresenter;

    @Inject
    DeleteDeploymentPresenter mDeleteDeploymentPresenter;

    @InjectView(R.id.fab)
    MovableFab mFab;

    @InjectView(android.R.id.empty)
    TextView mEmptyView;

    @Inject
    Prefs mPrefs;

    private DeploymentListListener mDeploymentListListener;

    private DeploymentRecyclerView mDeploymentRecyclerView;

    private boolean isDismissToDelete = false;

    private GestureDetectorCompat mGestureDetector;

    private SwipeDismissRecyclerViewTouchListener mListViewTouchListener;

    public ListDeploymentFragment() {
        super(DeploymentAdapter.class, R.layout.list_deployment, 0,
                android.R.id.list);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDeploymentListPresenter.init();
        mDeploymentRecyclerView = (DeploymentRecyclerView) mRecyclerView;
        mDeploymentRecyclerView.setDeleteDeploymentPresenter(mDeleteDeploymentPresenter);

        swipeToDeleteUndo();
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
        mDeploymentRecyclerView.setOnTouchListener(mListViewTouchListener);
        mRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setEmptyView();
            }
        });
        mDeploymentRecyclerView.addOnItemTouchListener(this);
        mGestureDetector =
                new GestureDetectorCompat(getActivity(), new RecyclerViewOnGestureListener());
        mDeploymentRecyclerView.setMovableFab(mFab);
        mDeploymentRecyclerView.setAdapter(mRecyclerViewAdapter);
        setEmptyView();
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
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
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void onDeploymentDeleted() {
        if (!isDismissToDelete) {
            showToast(getString(R.string.items_deleted,
                    mDeploymentRecyclerView.getNumberOfItemsDeleted()));
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

    private void onItemClick(int position) {
        if (mRecyclerViewAdapter.getItemCount() > 0) {
            DeploymentModel deploymentModel = mRecyclerViewAdapter.getItem(position);
            onDeplomentClicked(deploymentModel);
        }
    }

    private void swipeToDeleteUndo() {
        mListViewTouchListener =
                new SwipeDismissRecyclerViewTouchListener(mDeploymentRecyclerView,
                        new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {

                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerView listView,
                                    int[] reverseSortedPositions) {
                                isDismissToDelete = true;
                                final ArrayList<DeploymentParcelable> items = new ArrayList<>();

                                if (reverseSortedPositions.length > 0) {

                                    for (int i : reverseSortedPositions) {
                                        DeploymentParcelable parcelable = new DeploymentParcelable(
                                                mRecyclerViewAdapter.getItem(i),
                                                i);
                                        items.add(parcelable);
                                        mRecyclerViewAdapter
                                                .removeItem(mRecyclerViewAdapter.getItem(i));
                                    }

                                }

                                SnackbarManager.show(
                                        Snackbar.with(getActivity().getApplicationContext())
                                                .text(getContext()
                                                        .getString(R.string.items_deleted,
                                                                items.size()))
                                                .actionLabel(
                                                        getContext().getString(R.string.undo))
                                                .attachToRecyclerView(mDeploymentRecyclerView)
                                                .actionListener(new ActionClickListener() {
                                                    @Override
                                                    public void onActionClicked(
                                                            Snackbar snackbar) {
                                                        if (!items.isEmpty()) {

                                                            for (DeploymentRecyclerView.DeploymentParcelable deploymentModel : items) {
                                                                mRecyclerViewAdapter.addItem(
                                                                        deploymentModel
                                                                                .getDeploymentModel(),
                                                                        deploymentModel
                                                                                .getPosition());
                                                            }
                                                            items.clear();
                                                        }

                                                    }
                                                })
                                                .eventListener(new EventListener() {
                                                    @Override
                                                    public void onShow(Snackbar snackbar) {
                                                        mFab.moveUp(snackbar.getHeight() + 80);
                                                    }

                                                    @Override
                                                    public void onShowByReplace(
                                                            Snackbar snackbar) {
                                                        // Do nothing
                                                    }

                                                    @Override
                                                    public void onShown(Snackbar snackbar) {
                                                        // Do nothing
                                                    }

                                                    @Override
                                                    public void onDismiss(
                                                            Snackbar snackbar) {
                                                        mFab.moveDown(snackbar.getHeight() + 80);
                                                    }

                                                    @Override
                                                    public void onDismissByReplace(
                                                            Snackbar snackbar) {
                                                        // Do nothing
                                                    }

                                                    @Override
                                                    public void onDismissed(
                                                            Snackbar snackbar) {
                                                        if (!items.isEmpty()) {

                                                            for (DeploymentParcelable deploymentModel : items) {
                                                                if (deploymentModel
                                                                        .getDeploymentModel()
                                                                        .getStatus() ==
                                                                        DeploymentModel.Status.ACTIVATED) {
                                                                    mPrefs.getActiveDeploymentUrl()
                                                                            .delete();
                                                                }
                                                                mDeleteDeploymentPresenter
                                                                        .deleteDeployment(
                                                                                deploymentModel
                                                                                        .getDeploymentModel());
                                                            }
                                                            items.clear();
                                                        }
                                                    }
                                                }), getActivity());


                            }
                        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

    }

    /**
     * Listens for deployment list events
     */
    public interface DeploymentListListener {

        void onDeploymentClicked(final DeploymentModel deploymentModel);

        void onFabClicked();

    }

    private class RecyclerViewOnGestureListener extends
            GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = mDeploymentRecyclerView.findChildViewUnder(e.getX(), e.getY());
            onItemClick(mDeploymentRecyclerView.getChildPosition(view));
            return super.onSingleTapConfirmed(e);
        }
    }

}
