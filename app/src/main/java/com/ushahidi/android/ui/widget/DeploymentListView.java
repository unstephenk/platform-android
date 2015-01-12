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
import android.os.Parcel;
import android.os.Parcelable;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Custom ListView to handle CAB events for deployments list
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentListView extends ListView {

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
        mDeploymentModels = new LinkedHashMap();
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

        // Get the right most part of the item in the list
        // This will enable us to have a bigger touch area for the checkbox
        if (action == MotionEvent.ACTION_DOWN && x > getWidth() - getContext().getResources()
                .getInteger(R.integer.screen_right_side)) {
            mSelectionMode = true;
            mStartPosition = pointToPosition(x, y);
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
            mDeploymentModels.put(position, mDeploymentAdapter.getItem(position));
        }

        // Set the CAB title with the number of selected items
        mActionMode.setTitle(mActivity.getString(R.string.selected, checkedCount));

    }

    /**
     * Shows a toast like message giving the user a feedback of the action taken.
     *
     * @param interactiveToast The {@link com.ushahidi.android.ui.widget.InteractiveToast}
     */
    public void setInteractiveToast(InteractiveToast interactiveToast) {
        mInteractiveToast = interactiveToast;
    }

    /**
     * Clear all checked items in the list.
     */
    private void clearSelectedItems() {

        SparseBooleanArray cItems = getCheckedItemPositions();
        for (int i = 0; i < cItems.size(); i++) {
            if (cItems.valueAt(i)) {
                super.setItemChecked(cItems.keyAt(i), false);
            }
        }
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

        /*mInteractiveToast
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
                                mDeploymentAdapter.addItem(deploymentModel.mPosition,
                                        deploymentModel.mDeploymentModel);
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
                        mActivity.getString(R.string.undo), R.drawable.ic_undo, b);*/
    }

    /**
     * Facilitates in passing the {@link com.ushahidi.android.model.DeploymentModel} as a {@link
     * android.os.Parcelable} object.
     */
    public static class DeploymentParcelable implements Parcelable {

        public static final Parcelable.Creator<DeploymentParcelable> CREATOR
                = new Parcelable.Creator<DeploymentParcelable>() {
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
