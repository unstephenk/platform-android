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

package com.ushahidi.android.core.usecase.media;

import com.ushahidi.android.core.entity.Media;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.IInteractor;

/**
 * Interface for Update Media use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface IUpdateMedia extends IInteractor {

    /**
     * Executes this use case
     *
     * @param media    The media to be added.
     * @param callback A {@link com.ushahidi.android.core.usecase.media.IUpdateMedia.Callback} used
     *                 for notifying the client about the execute method call.
     */
    void execute(Media media, Callback callback);

    /**
     * Notifies client when a deployment is successfully updated or error occurs
     */
    interface Callback {

        void onMediaUpdated();

        void onError(ErrorWrap error);
    }

}
