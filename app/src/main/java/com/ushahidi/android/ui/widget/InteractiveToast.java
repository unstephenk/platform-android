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

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * A widget that mimics a {@link android.widget.Toast} but being able to interact with it. You can
 * implement it to take action when a view is pressed or when the view vanishes without any
 * interaction.
 *
 * Heavily based on the works of http://simonvt.github.io/MessageBar/
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class InteractiveToast {

    private InteractiveToastListener mInteractiveToastListener;

    private OnHideListener mOnHideListener;

    private Handler mHandler;

    private AlphaAnimation mFadeInAnimation;

    private AlphaAnimation mFadeOutAnimation;

    private static final String STATE_MESSAGE = "com.ushahidi.android.widget.message";

    private TextView mMessageView;

    private TextView mButton;

    private View mContainer;

    private boolean mShowing;

    private static final int HIDE_DELAY = 5000;

    private static final int ANIMATION_DURATION = 600;

    private InteractiveToastMessage mMessage;

    /**
     * Event listener when a button is clicked
     */
    public interface InteractiveToastListener {

        void onPressed(Parcelable token);
    }

    /**
     * Callback when the view vanishes without any interaction. The client can take certain actions
     * as the widget vanishes.
     */
    public interface OnHideListener {

        void onHide(Parcelable token);
    }

    /**
     * The default constructor
     *
     * @param container The {@link android.view.View} the view to show to the user. Usually it's a
     *                  {@link android.view.ViewGroup}
     */
    public InteractiveToast(View container) {
        mContainer = container;
        mContainer.setVisibility(View.GONE);
        mMessageView = (TextView) container.findViewById(R.id.interactive_toast_message);
        mButton = (TextView) container.findViewById(R.id.interactive_toast_button);
        mButton.setOnClickListener(mButtonListener);

        mFadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        mFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnimation.setDuration(ANIMATION_DURATION);
        mFadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mMessage = null;
                mContainer.setVisibility(View.GONE);
                mShowing = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mHandler = new Handler();
    }

    public void onRestoreInstanceState(Bundle state) {
        InteractiveToastMessage message = state.getParcelable(STATE_MESSAGE);
        if (message != null) {
            show(message, true);
        }
    }

    /**
     * Shows a message to the user. Similar to what {@link android.widget.Toast} does
     *
     * @param message The message to show to the user
     */
    public void show(String message) {
        show(message, null);
    }

    /**
     * Shows a message to the user. Similar to what {@link android.widget.Toast} does
     *
     * @param message       The message to show to the user.
     * @param actionMessage The label for the action button.
     */
    public void show(String message, String actionMessage) {
        show(message, actionMessage, 0);
    }

    /**
     * Shows a message to the user. Similar to what {@link android.widget.Toast} does
     *
     * @param message       The message to show to the user
     * @param actionMessage The label for the action button.
     * @param actionIcon    A resource id of the action button's icon
     */
    public void show(String message, String actionMessage, int actionIcon) {
        show(message, actionMessage, actionIcon, null);
    }

    /**
     * Shows a message to the user. Similar to what {@link android.widget.Toast} does
     *
     * @param message       The message to show to the user
     * @param actionMessage The label for the action button.
     * @param actionIcon    A resource id for the action button's icon
     */
    public void show(String message, String actionMessage, int actionIcon, Parcelable token) {

        InteractiveToastMessage msg = new InteractiveToastMessage(message, actionMessage,
                actionIcon, token);
        if (!mShowing) {
            show(msg);
        }
    }

    private void show(InteractiveToastMessage message) {
        show(message, false);
    }

    private void show(InteractiveToastMessage message, boolean immediately) {

        mShowing = true;
        mContainer.setVisibility(View.VISIBLE);
        mMessage = message;
        mMessageView.setText(message.mMessage);

        if (message.mActionMessage != null) {
            mMessageView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            mButton.setVisibility(View.VISIBLE);
            mButton.setText(message.mActionMessage);
            mButton.setCompoundDrawablesWithIntrinsicBounds(message.mActionIcon, 0, 0, 0);
        } else {
            mMessageView.setGravity(Gravity.CENTER);
            mButton.setVisibility(View.GONE);
        }

        if (immediately) {
            mFadeInAnimation.setDuration(0);
        } else {
            mFadeInAnimation.setDuration(ANIMATION_DURATION);
        }
        mContainer.startAnimation(mFadeInAnimation);
        mHandler.postDelayed(mHideRunnable, HIDE_DELAY);
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_MESSAGE, mMessage);
        return bundle;
    }

    public void setInteractiveToastListener(InteractiveToastListener interactiveToastListener) {
        mInteractiveToastListener = interactiveToastListener;
    }

    public void setOnHideListener(OnHideListener onHideListener) {
        mOnHideListener = onHideListener;
    }

    private final View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mInteractiveToastListener != null && mMessage != null) {
                mInteractiveToastListener.onPressed(mMessage.mToken);
                mMessage = null;
                mHandler.removeCallbacks(mHideRunnable);
                mHideRunnable.run();
            }
        }
    };

    public void clear() {
        mMessage = null;
        mHideRunnable.run();
    }

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            if (mOnHideListener != null && mMessage != null) {
                mOnHideListener.onHide(mMessage.mToken);
            }
            mContainer.startAnimation(mFadeOutAnimation);
        }
    };

    private static class InteractiveToastMessage implements Parcelable {

        final String mMessage;

        final String mActionMessage;

        final int mActionIcon;

        final Parcelable mToken;

        public InteractiveToastMessage(String message, String actionMessage, int actionIcon,
                Parcelable token) {
            mMessage = message;
            mActionMessage = actionMessage;
            mActionIcon = actionIcon;
            mToken = token;
        }

        public InteractiveToastMessage(Parcel parcel) {
            mMessage = parcel.readString();
            mActionMessage = parcel.readString();
            mActionIcon = parcel.readInt();
            mToken = parcel.readParcelable(getClass().getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(mMessage);
            parcel.writeString(mActionMessage);
            parcel.writeInt(mActionIcon);
            parcel.writeParcelable(mToken, 0);
        }

        public static final Parcelable.Creator<InteractiveToastMessage> CREATOR
                = new Parcelable.Creator<InteractiveToastMessage>() {
            public InteractiveToastMessage createFromParcel(Parcel in) {
                return new InteractiveToastMessage(in);
            }

            public InteractiveToastMessage[] newArray(int size) {
                return new InteractiveToastMessage[size];
            }
        };
    }
}
