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

import com.ushahidi.android.model.PostModel;

import java.util.List;

/**
 * Renders post list views
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface IPostListView extends ILoadViewData {

    /**
     * Render a post list in the UI.
     *
     * @param postModel The collection of {@link com.ushahidi.android.model.PostModel} that will be
     *                  shown.
     */
    void renderPostList(List<PostModel> postModel);

    /**
     * View {@link com.ushahidi.android.model.PostModel} details
     *
     * @param postModel The post model to view
     */
    void viewPost(PostModel postModel);

    /**
     * Refreshes the existing items in the list view without any visual loaders
     */
    void refreshList();
}
