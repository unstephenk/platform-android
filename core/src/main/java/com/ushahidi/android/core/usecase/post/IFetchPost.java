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

package com.ushahidi.android.core.usecase.post;

import com.ushahidi.android.core.entity.Post;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.IInteractor;

import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface IFetchPost extends IInteractor {

    /**
     * Executes this use case.
     *
     * @param callback     A {@link IListPost.Callback} used to notify the client.
     * @param deploymentId A {@link com.ushahidi.android.core.entity.Deployment} ID
     */
    void execute(long deploymentId, Callback callback);

    /**
     * Notify client when a list of posts are successfully loaded or an error occurred in the
     * process.
     */
    interface Callback {

        void onPostFetched(List<Post> listPost);

        void onError(ErrorWrap error);
    }
}
