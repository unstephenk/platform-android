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
package com.ushahidi.android.core.executor;

/**
 * Thread abstraction created to change the execution context from any thread to any other thread.
 * implementation of this interface will change context and update the UI.
 *
 * @author  Ushahidi Team <team@ushahidi.com>
 */
public interface PostExecutionThread {

    /**
     * Causes the {@link Runnable} to be added to the message queue of the Main UI Thread of the
     * application.
     *
     * @param runnable {@link Runnable} to be executed.
     */
    void post(Runnable runnable);
}
