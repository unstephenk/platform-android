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

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.ushahidi.android.model.DeploymentModel.Status;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class NavDrawerItem implements View.OnTouchListener {

    private static final Long INVALID_DEPLOYMENT_ID = -1l;

    private Long mNavDrawerItemId = INVALID_DEPLOYMENT_ID;

    private Status mStatus = Status.DEACTIVATED;

    private int mPosition;

    private View mView;

    private TextView mText;

    private ImageView mIcon;

    private ImageView mActiveIcon;

    private NavDrawerItemListener mListener;

    private NavDeploymentItemListener mDeploymentListener;

    private boolean mSelected;

    private Fragment mFragment;

    private Intent mIntent;

    private ActionBarActivity mActivity;

    private int mFragmentContainerId = 0;

    private String mFragmentTag;

    public NavDrawerItem(ActionBarActivity context, int position) {
        this(context, null, 0, position, true);
    }

    public NavDrawerItem(ActionBarActivity context, String title, int iconId, int position) {
        this(context, title, iconId, position, false);
    }

    public NavDrawerItem(ActionBarActivity context, String title, int iconId, int position,
            Fragment fragment, int fragmentContainerId, String fragmentTag) {
        this(context, title, iconId, position, false, fragment, fragmentContainerId, fragmentTag);
    }

    public NavDrawerItem(ActionBarActivity context, String title, int iconId, int position,
            Intent intent) {
        this(context, title, iconId, position, false, intent);
    }

    public NavDrawerItem(ActionBarActivity context, String title, int iconId, int position,
            boolean separator, Fragment fragment, int fragmentContainerId, String fragmentTag) {
        this(context, title, iconId, position, separator);
        mFragment = fragment;
        mFragmentContainerId = fragmentContainerId;
        mFragmentTag = fragmentTag;
    }

    public NavDrawerItem(ActionBarActivity context, String title, int iconId, int position,
            boolean separator, Intent intent) {
        this(context, title, iconId, position, separator);
        mIntent = intent;
    }

    public NavDrawerItem(ActionBarActivity context, String title, int iconId, int position,
            boolean separator) {

        mSelected = false;
        mPosition = position;
        mActivity = context;

        int inflateLayout;
        if (separator) {
            inflateLayout = R.layout.nav_drawer_separator;
        } else {
            inflateLayout = R.layout.nav_drawer_item;
        }

        mView = LayoutInflater.from(context).inflate(inflateLayout, null, false);
        mText = (TextView) mView.findViewById(R.id.title);
        mActiveIcon = (ImageView) mView.findViewById(R.id.status);

        if (iconId > 0) {
            // Set Icon and Text
            mIcon = (ImageView) mView.findViewById(R.id.icon);
            mIcon.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
            mIcon.setImageResource(iconId);
            mIcon.setColorFilter(isSelected() ?
                    context.getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                    context.getResources().getColor(R.color.navdrawer_icon_tint));
        }

        if (!separator) {
            mText = (TextView) mView.findViewById(R.id.title);
            mText.setText(title);
            mText.setTextColor(isSelected() ?
                    context.getResources().getColor(R.color.navdrawer_text_color_selected) :
                    context.getResources().getColor(R.color.navdrawer_text_color));
            mView.setOnTouchListener(this);

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            view.setBackgroundColor(
                    mActivity.getResources().getColor(R.color.navdrawer_item_pressed_state));
            return true;
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            mSelected = true;
            view.setBackgroundColor(
                    mActivity.getResources().getColor(R.color.navdrawer_item_highlight));
            setNavDrawerItemColor();
            if (mListener != null) {
                mListener.onNavDrawerItemClick(this);
            }

            return true;
        }

        return false;
    }

    public void setNavDrawerItemColor() {
        mView.setBackgroundColor(isSelected() ?
                mActivity.getResources().getColor(R.color.navdrawer_item_pressed_state)
                : mActivity.getResources().getColor(R.color.navdrawer_item_unpressed_state));

        if (mIcon != null) {
            mIcon.setColorFilter(isSelected() ?
                    mActivity.getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                    mActivity.getResources().getColor(R.color.navdrawer_icon_tint));
        }

        if (mText != null) {
            mText.setTextColor(isSelected() ?
                    mActivity.getResources().getColor(R.color.navdrawer_text_color_selected) :
                    mActivity.getResources().getColor(R.color.navdrawer_text_color));
        }

    }

    public int getPosition() {
        return mPosition;
    }

    public void setOnClickListener(NavDrawerItemListener listener) {
        mListener = listener;
    }

    public void setOnDeploymentClickListener(NavDeploymentItemListener listener) {
        mDeploymentListener = listener;
    }

    public View getView() {
        return mView;
    }

    public void launchActivityOrFragment() {
        if (mIntent != null) {
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(mIntent);
        }

        if (mFragment != null) {
            FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(mFragmentContainerId, mFragment, mFragmentTag);
            fragmentTransaction.commit();

        }

        if (isDeployment()) {
            if (mDeploymentListener != null) {
                mDeploymentListener.onNavDeploymentItemClick(this);
            }
        }
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
        setNavDrawerItemColor();
    }

    public boolean isActive() {
        return ((mStatus == Status.ACTIVATED) && (isDeployment()));
    }

    public void setStatus(DeploymentModel.Status status) {
        mStatus = status;
        markStatus();
    }

    public void markStatus() {
        mActiveIcon.setVisibility(isActive() ? View.VISIBLE : View.GONE);
    }

    public boolean isDeployment() {
        return getNavDrawerItemId() != INVALID_DEPLOYMENT_ID;
    }

    public Long getNavDrawerItemId() {
        return mNavDrawerItemId;
    }

    public void setNavDrawerItemId(Long navDrawerItemId) {
        mNavDrawerItemId = navDrawerItemId;
    }

    public interface NavDrawerItemListener {

        public void onNavDrawerItemClick(NavDrawerItem navDrawerItem);
    }

    public interface NavDeploymentItemListener {

        public void onNavDeploymentItemClick(NavDrawerItem navDrawerItem);

    }
}
