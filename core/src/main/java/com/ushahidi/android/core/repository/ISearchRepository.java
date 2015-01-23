/*
 * Copyright (c) 2015 Ushahidi.
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

package com.ushahidi.android.core.repository;

import com.ushahidi.android.core.Entity;
import com.ushahidi.android.core.exception.ErrorWrap;

import java.util.List;

/**
 * The post repository
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface ISearchRepository<E extends Entity> {


    /**
     * Search for a {@link com.ushahidi.android.core.Entity}
     *
     * @param query    The entity to be searched for.
     * @param callback A {@link SearchCallback} used for notifying clients about the delete
     *                 status.
     */
    void search(String query, SearchCallback<E> callback);


    /**
     * Callback used for notifying the client when either a post has been searched for or failed to be executed.
     */
    interface SearchCallback<E extends Entity> {

        void onSearchResult(List<E> postList);

        void onError(ErrorWrap error);
    }

}
