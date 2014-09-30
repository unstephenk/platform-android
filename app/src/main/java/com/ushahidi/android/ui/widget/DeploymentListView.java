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

import com.ushahidi.android.R;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.presenter.DeleteDeploymentPresenter;
import com.ushahidi.android.ui.adapter.DeploymentAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListView;

import java.util.LinkedHashSet;

/**
 * Custom ListView to handle CAB events for deployments list
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentListView extends ListView {

    private Activity mActivity;

    private boolean mSelectionMode = false;

    private int mStartPosition;

    private ActionMode mActionMode;

    private DeploymentAdapter mDeploymentAdapter;

    private int mNumberOfItemsDeleted = 0;

    private DeleteDeploymentPresenter mDeleteDeploymentPresenter;

    private LinkedHashSet<DeploymentModel> mDeploymentModels;

    // Check if confirmation isDialog was initiated
    private boolean isDialog = false;

    public DeploymentListView(Context context) {
        this(context, null, 0);
    }

    public DeploymentListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeploymentListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mActivity = (Activity) context;
        mDeploymentModels = new LinkedHashSet<>();
    }

    @Override
    public boolean performItemClick(View view, int position, long id) {
        OnItemClickListener mOnItemClickListener = getOnItemClickListener();

        if (mOnItemClickListener != null) {
            playSoundEffect(SoundEffectConstants.CLICK);
            if (view != null) {
                view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
            }
            mOnItemClickListener.onItemClick(this, view, position, id);
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        final int x = (int) ev.getX();
        final int y = (int) ev.getY();

        // Get the right most part of the list view screen
        if (action == MotionEvent.ACTION_DOWN && x > getWidth() - getContext().getResources()
                .getInteger(R.integer.screen_right_side)) {
            mSelectionMode = true;
            mStartPosition = pointToPosition(x, y);
        }
        if (!mSelectionMode) {
            return super.onTouchEvent(ev);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointToPosition(x, y) != mStartPosition) {
                    mSelectionMode = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            default:
                mSelectionMode = false;
                int mItemPosition = pointToPosition(x, y);
                if (mStartPosition != ListView.INVALID_POSITION) {
                    setItemChecked(mItemPosition, !isItemChecked(mItemPosition));
                }
        }

        return true;
    }

    @Override
    public void setItemChecked(int position, boolean value) {
        super.setItemChecked(position, value);

        int checkedCount = getCheckedItemCount();

        if (checkedCount == 0) {
            if (mActionMode != null) {
                mActionMode.finish();
            }
            return;
        }
        if (mActionMode == null) {
            mActionMode = mActivity.startActionMode(new ActionBarModeCallback());
        }

        mDeploymentAdapter = (DeploymentAdapter) getAdapter();

        if (mDeploymentAdapter != null) {
            mDeploymentModels.add(mDeploymentAdapter.getItem(position));
        }

        mActionMode.setTitle(mActivity.getString(R.string.selected, checkedCount));

    }

    private void clearSelectedItems() {

        SparseBooleanArray cItems = getCheckedItemPositions();
        for (int i = 0; i < cItems.size(); i++) {
            if (cItems.valueAt(i)) {
                super.setItemChecked(cItems.keyAt(i), false);
            }
        }
    }

    public void clearItems() {
        clearSelectedItems();
        mDeploymentModels.clear();
        isDialog = false;
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
            // Reset any previous selected number items
            mNumberOfItemsDeleted = 0;
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            boolean result = false;

            if (menuItem.getItemId() == R.id.list_deployment_delete) {
                performDelete();
                result = true;
            }

            if (mActionMode != null) {
                mActionMode.finish();
            }
            return result;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            if (!isDialog) {
                clearItems();
            }
            mActionMode = null;
        }
    }

    public void setDeleteDeploymentPresenter(DeleteDeploymentPresenter deleteDeploymentPresenter) {
        mDeleteDeploymentPresenter = deleteDeploymentPresenter;
    }

    public int getNumberOfItemsDeleted() {
        return mNumberOfItemsDeleted;
    }

    private void performDeletion() {
        if (mDeleteDeploymentPresenter != null) {
            if (!mDeploymentModels.isEmpty()) {
                mNumberOfItemsDeleted = mDeploymentModels.size();
                for (DeploymentModel deploymentModel : mDeploymentModels) {
                    mDeleteDeploymentPresenter.deleteDeployment(deploymentModel);
                }
                clearItems();
            }

        }
    }

    /**
     * Deletes selected deployments
     */
    private void performDelete() {
        isDialog = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(mActivity.getString(R.string.confirm_action))
                .setCancelable(false)
                .setNegativeButton(mActivity.getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                clearItems();
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(mActivity.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Perform deletion
                                performDeletion();

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
