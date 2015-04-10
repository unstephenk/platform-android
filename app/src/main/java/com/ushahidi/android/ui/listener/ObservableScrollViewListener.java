/*
 *  Copyright (c) 2015 Ushahidi.
 *
 *   This program is free software: you can redistribute it and/or modify it under
 *   the terms of the GNU Affero General Public License as published by the Free
 *   Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 *   This program is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *   FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program in the file LICENSE-AGPL. If not, see
 *   https://www.gnu.org/licenses/agpl-3.0.html
 *
 */

package com.ushahidi.android.ui.listener;

/**
 * ObservableScrollView Listener
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface ObservableScrollViewListener {

    /**
     * Called when the scroll change events occurred.
     * This won't be called just after the view is laid out, so if you'd like to
     * initialize the position of your views with this method, you should call this manually
     * or invoke scroll as appropriate.
     *
     * @param scrollY     scroll position in Y axis
     * @param firstScroll true when this is called for the first time in the consecutive motion events
     * @param dragging    true when the view is dragged and false when the view is scrolled in the inertia
     */
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging);

    /**
     * Called when the down motion event occurred.
     */
    public void onDownMotionEvent();

    /**
     * Called when the dragging ended or canceled.
     *
     * @param observableScrollState state to indicate the scroll direction
     */
    public void onUpOrCancelMotionEvent(ObservableScrollState observableScrollState);
}
