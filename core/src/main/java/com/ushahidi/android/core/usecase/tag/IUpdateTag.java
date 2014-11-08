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

package com.ushahidi.android.core.usecase.tag;

import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.IInteractor;

/**
 * Update tag use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface IUpdateTag extends IInteractor {

    /**
     * Executes this use case
     *
     * @param tag The tag to be added.
     * @param callback   A {@link IUpdateTag.Callback}
     *                   used for notifying the client about the execute method call.
     */
    void execute(Tag tag, Callback callback);

    /**
     * Notifies client when a tag is successfully updated or error occurs
     */
    interface Callback {

        void onTagUpdated();

        void onError(ErrorWrap error);
    }
}
