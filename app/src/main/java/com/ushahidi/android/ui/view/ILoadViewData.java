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

/**
 * Implement this interface to show or hide views as data is being loaded.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface ILoadViewData extends IView {

    /**
     * Shows a view with a progress bar indicating a loading process.
     */
    void showLoading();

    /**
     * Hides a loading view.
     */
    void hideLoading();

    /**
     * Shows a retry view in case of an error when retrieving data.
     */
    void showRetry(String message);

}
