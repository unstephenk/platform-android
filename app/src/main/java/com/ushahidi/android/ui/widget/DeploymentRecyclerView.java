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
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Custom ListView to handle CAB events for deployments list
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentRecyclerView extends RecyclerView {

    public static final int INVALID_POSITION = -1;

    private static final String BUNDLE_KEY = "selected";

    private Activity mActivity;

    private boolean mSelectionMode = false;

    private int mStartPosition;

    private ActionMode mActionMode;

    private DeploymentAdapter mDeploymentAdapter;

    private int mNumberOfItemsDeleted = 0;

    private DeleteDeploymentPresenter mDeleteDeploymentPresenter;

    private Map<Integer, DeploymentModel> mDeploymentModels;

    private InteractiveToast mInteractiveToast;

    public DeploymentRecyclerView(Context context) {
        this(context, null, 0);
    }

    public DeploymentRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeploymentRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mActivity = (Activity) context;
        mDeploymentModels = new LinkedHashMap();
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
            mDeploymentModels.put(position, mDeploymentAdapter.getItem(position));
        }

        // Set the CAB title with the number of selected items
        mActionMode.setTitle(mActivity.getString(R.string.selected, checkedCount));

    }

    /**
     * Shows a toast like message giving the user a feedback of the action taken.
     *
     * @param interactiveToast The {@link InteractiveToast}
     */
    public void setInteractiveToast(InteractiveToast interactiveToast) {
        mInteractiveToast = interactiveToast;
    }

    /**
     * Clear all checked items in the list.
     */
    private void clearSelectedItems() {

        mDeploymentAdapter.clearSelections();
    }

    /**
     * Clear all checked items in the list and the selected {@link com.ushahidi.android.model.DeploymentModel}
     */
    public void clearItems() {
        clearSelectedItems();
        mDeploymentModels.clear();
    }

    public void setDeleteDeploymentPresenter(DeleteDeploymentPresenter deleteDeploymentPresenter) {
        mDeleteDeploymentPresenter = deleteDeploymentPresenter;
    }

    public int getNumberOfItemsDeleted() {
        return mNumberOfItemsDeleted;
    }

    private void performDeletion(ArrayList<DeploymentParcelable> items) {
        if (mDeleteDeploymentPresenter != null) {

            if (!items.isEmpty()) {
                mNumberOfItemsDeleted = items.size();
                for (DeploymentParcelable deploymentModel : items) {
                    Timber.i("Deleting deployments...");
                    mDeleteDeploymentPresenter
                            .deleteDeployment(deploymentModel.mDeploymentModel);
                }

                items.clear();
            }

            clearItems();
        }
    }

    /**
     * Deletes selected {@link com.ushahidi.android.model.DeploymentModel} from the database.
     */
    private void performDelete() {

        mInteractiveToast
                .setInteractiveToastListener(new InteractiveToast.InteractiveToastListener() {
                    @Override
                    public void onPressed(Parcelable token) {

                        // Gets all the selected items from the bundle
                        Bundle b = (Bundle) token;
                        final ArrayList<DeploymentParcelable> items = b
                                .getParcelableArrayList(BUNDLE_KEY);

                        if (!items.isEmpty()) {
                            mNumberOfItemsDeleted = items.size();
                            for (DeploymentParcelable deploymentModel : items) {

                                // Restores all the removed DeploymentModels from the list view's adapter
                                // back and in their original position.
                                mDeploymentAdapter.addItem(deploymentModel.mDeploymentModel,
                                        deploymentModel.mPosition);
                            }

                            // Clear
                            items.clear();
                        }
                        // Clears all selected items
                        clearItems();
                    }
                });

        mInteractiveToast.setOnHideListener(new InteractiveToast.OnHideListener() {
            @Override
            public void onHide(Parcelable token) {

                // On hide, retrieve the selected items and perform the actual deletion from the database
                Bundle b = (Bundle) token;
                final ArrayList<DeploymentParcelable> items = b
                        .getParcelableArrayList(BUNDLE_KEY);

                // Delete items
                performDeletion(items);
            }
        });

        // Pass the selected DeploymentModels as a parcelable objects to be used by
        // the InteractiveToast callback functions
        ArrayList<DeploymentParcelable> items = new ArrayList<>();

        if (!mDeploymentModels.isEmpty()) {
            mNumberOfItemsDeleted = mDeploymentModels.size();
            for (Map.Entry<Integer, DeploymentModel> entry : mDeploymentModels.entrySet()) {

                // Initializes the DeploymentParcelable with the DeploymentModel and it's position in the list view.

                // Storing the position helps in restoring the deleted DeploymentModel to it's original
                // position.
                DeploymentParcelable parcelable = new DeploymentParcelable(entry.getValue(),
                        entry.getKey());
                items.add(parcelable);

                // Removes the selected DeploymentModel from the list view's adapter.
                mDeploymentAdapter.removeItem(entry.getValue());
            }

        }

        // Stores the selected models into a bundle for later reuse.
        Bundle b = new Bundle();
        b.putParcelableArrayList(BUNDLE_KEY, items);
        mInteractiveToast
                .show(mActivity.getString(R.string.items_deleted, mDeploymentModels.size()),
                        mActivity.getString(R.string.undo), R.drawable.ic_undo, b);
    }

    /**
     * Facilitates in passing the {@link com.ushahidi.android.model.DeploymentModel} as a {@link
     * android.os.Parcelable} object.
     */
    public static class DeploymentParcelable implements Parcelable {

        public static final Creator<DeploymentParcelable> CREATOR
                = new Creator<DeploymentParcelable>() {
            public DeploymentParcelable createFromParcel(Parcel in) {
                return new DeploymentParcelable(in);
            }

            public DeploymentParcelable[] newArray(int size) {
                return new DeploymentParcelable[size];
            }
        };

        private DeploymentModel mDeploymentModel;

        private int mPosition;

        public DeploymentParcelable(DeploymentModel deploymentModel, int position) {
            mDeploymentModel = deploymentModel;
            mPosition = position;
        }

        public DeploymentParcelable(Parcel parcel) {
            mPosition = parcel.readInt();

            // Makes sure the object to be retrieved is retrieved as a
            // DeploymentModel object.
            mDeploymentModel = (DeploymentModel) parcel
                    .readValue(DeploymentModel.class.getClassLoader());
        }

        public DeploymentModel getDeploymentModel() {
            return mDeploymentModel;
        }

        public int getPosition() {
            return mPosition;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(mPosition);
            parcel.writeValue(mDeploymentModel);
        }

        @Override
        public String toString() {
            return "DeploymentParcelable{" +
                    "mDeploymentModel=" + mDeploymentModel +
                    ", mPosition=" + mPosition +
                    '}';
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
            clearItems();
            mActionMode = null;
        }
    }
}
