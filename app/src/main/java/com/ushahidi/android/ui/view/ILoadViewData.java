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

package com.ushahidi.android.ui.view;

import android.content.Context;

/**
 * Implement this interface to load data into the view
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface ILoadViewData extends IView {

    /**
     * Shows a view with a progress bar indicating a loading process.
     */
    public void showLoading();

    /**
     * Hides a loading view.
     */
    public void hideLoading();

    /**
     * Shows a retry view in case of an error when retrieving data.
     */
    public void showRetry();

    /**
     * Hides a retry view shown if there was an error when retrieving data.
     */
    public void hideRetry();

    /**
     * Shows an error message
     *
     * @param message A string resource representing an error.
     */
    public void showError(String message);

}
