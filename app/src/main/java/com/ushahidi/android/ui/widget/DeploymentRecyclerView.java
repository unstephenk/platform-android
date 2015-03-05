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

package com.ushahidi.android.ui.widget;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;
import com.ushahidi.android.R;
import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.presenter.DeleteDeploymentPresenter;
import com.ushahidi.android.ui.adapter.DeploymentAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Custom ListView to handle CAB events for deployments list
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentRecyclerView extends RecyclerView {

    public static final int INVALID_POSITION = -1;

    public List<PendingDeletedDeployment> mPendingDeletedDeployments;

    private Activity mActivity;

    private boolean mSelectionMode = false;

    private int mStartPosition;

    private ActionMode mActionMode;

    private DeploymentAdapter mDeploymentAdapter;

    private int mNumberOfItemsDeleted = 0;

    private DeleteDeploymentPresenter mDeleteDeploymentPresenter;

    private MovableFab mMovableFab;

    public DeploymentRecyclerView(Context context) {
        this(context, null, 0);
    }

    public DeploymentRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeploymentRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mActivity = (Activity) context;
        mPendingDeletedDeployments = new ArrayList<>();
        mDeploymentAdapter = (DeploymentAdapter) getAdapter();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        final int action = ev.getActionMasked();
        final int x = (int) ev.getRawX();

        // Get the right most part of the item in the list
        // This will enable us to have a bigger touch area for the checkbox
        if (action == MotionEvent.ACTION_DOWN && x < getWidth() / 7) {
            mSelectionMode = true;
            mStartPosition = pointToPosition(ev);
        }

        // Resume regular touch event if it's not in the selection mode.
        // The area of the screen being touched is not where the checkbox is.
        if (!mSelectionMode) {
            return super.onTouchEvent(ev);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointToPosition(ev) != mStartPosition) {
                    mSelectionMode = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            default:
                mSelectionMode = false;
                int mItemPosition = pointToPosition(ev);
                if (mStartPosition != INVALID_POSITION) {
                    setItemChecked(mItemPosition);
                }
        }

        return true;
    }

    private int pointToPosition(MotionEvent motionEvent) {

        Rect rect = new Rect();
        View downView = null;

        int childCount = getChildCount();
        int[] listViewCoords = new int[2];
        getLocationOnScreen(listViewCoords);
        int x = (int) motionEvent.getRawX() - listViewCoords[0];
        int y = (int) motionEvent.getRawY() - listViewCoords[1];
        View child;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            child.getHitRect(rect);
            if (rect.contains(x, y)) {
                downView = child;
                break;
            }
        }

        if (downView != null) {
            return getChildPosition(downView);
        }
        return INVALID_POSITION;
    }

    public void setItemChecked(int position) {
        mDeploymentAdapter = (DeploymentAdapter) getAdapter();
        mDeploymentAdapter.toggleSelection(position);

        int checkedCount = mDeploymentAdapter.getSelectedItemCount();

        if (checkedCount == 0) {
            if (mActionMode != null) {
                mActionMode.finish();
            }
            return;
        }
        if (mActionMode == null) {
            mActionMode = mActivity.startActionMode(new ActionBarModeCallback());
        }

        if (mDeploymentAdapter != null) {
            mPendingDeletedDeployments.add(new PendingDeletedDeployment(position,
                    mDeploymentAdapter.getItem(position)));

        }

        // Set the CAB title with the number of selected items
        mActionMode.setTitle(mActivity.getString(R.string.selected, checkedCount));

    }

    /**
     * Set this so FAB can be moved up or down when snackbar shows up.
     *
     * @param movableFab The {@link com.ushahidi.android.ui.widget.MovableFab}
     */
    public void setMovableFab(MovableFab movableFab) {
        mMovableFab = movableFab;
    }


    public void initAdapter(DeploymentAdapter deploymentAdapter) {
        mDeploymentAdapter = deploymentAdapter;
    }

    /**
     * Clear all checked items in the list and the selected {@link com.ushahidi.android.model.DeploymentModel}
     */
    private void clearItems() {
        mDeploymentAdapter.clearSelections();
        if(mPendingDeletedDeployments!=null) {
            mPendingDeletedDeployments.clear();
        }
    }

    public void setDeleteDeploymentPresenter(DeleteDeploymentPresenter deleteDeploymentPresenter) {
        mDeleteDeploymentPresenter = deleteDeploymentPresenter;
    }

    public int getNumberOfItemsDeleted() {
        return mNumberOfItemsDeleted;
    }

    private void setItemsForDeletion() {

        for (PendingDeletedDeployment pendingDeletedDeployment : mPendingDeletedDeployments) {
            mDeploymentAdapter.removeItem(pendingDeletedDeployment.deploymentModel);
        }

        deleteItems();
    }

    public void deleteItems() {

        //Sort in ascending order for restoring deleted items
        Comparator cmp = Collections.reverseOrder();
        Collections.sort(mPendingDeletedDeployments, cmp);

        SnackbarManager.show(Snackbar.with(getContext())
                .text(mActivity
                        .getString(R.string.items_deleted, mPendingDeletedDeployments.size()))
                .actionLabel(getContext().getString(R.string.undo))
                .actionColorResource(R.color.undo_text_color)
                .attachToRecyclerView(this)
                .actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {
                        // Restore items
                        for (DeploymentRecyclerView.PendingDeletedDeployment pendingDeletedDeployment : mPendingDeletedDeployments) {
                            mDeploymentAdapter.addItem(pendingDeletedDeployment.deploymentModel,
                                    pendingDeletedDeployment.position);
                        }
                        clearItems();
                    }
                })
                .eventListener(new EventListener() {
                    @Override
                    public void onShow(Snackbar snackbar) {
                        mMovableFab.moveUp(snackbar.getHeight() + 80);
                    }

                    @Override
                    public void onShowByReplace(Snackbar snackbar) {

                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }

                    @Override
                    public void onDismiss(Snackbar snackbar) {
                        mMovableFab.moveDown(snackbar.getHeight() + 80);
                        if (!snackbar.isActionClicked()) {
                            if (mPendingDeletedDeployments.size() > 0) {
                                mNumberOfItemsDeleted = mPendingDeletedDeployments.size();
                                for (PendingDeletedDeployment pendingDeletedDeployment : mPendingDeletedDeployments) {
                                    mDeleteDeploymentPresenter
                                            .deleteDeployment(
                                                    pendingDeletedDeployment.deploymentModel);
                                }
                                clearItems();
                            }
                        }
                    }

                    @Override
                    public void onDismissByReplace(Snackbar snackbar) {
                    }

                    @Override
                    public void onDismissed(Snackbar snackbar) {

                    }
                }));
    }

    public static class PendingDeletedDeployment implements Comparable<PendingDeletedDeployment> {

        public int position;

        public DeploymentModel deploymentModel;

        public PendingDeletedDeployment(int position, DeploymentModel deploymentModel) {
            this.position = position;
            this.deploymentModel = deploymentModel;
        }

        @Override
        public int compareTo(PendingDeletedDeployment other) {
            // Sort by descending position
            return other.position - position;
        }
    }

    class ActionBarModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater()
                    .inflate(R.menu.list_deployment_contextual_actionbar_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            // Resets any previously selected number of items.
            mNumberOfItemsDeleted = 0;
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            boolean result = false;

            if (menuItem.getItemId() == R.id.list_deployment_delete) {
                setItemsForDeletion();
                result = true;
            }

            if (mActionMode != null) {
                mActionMode.finish();
            }
            return result;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
        }
    }
}
