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

package com.ushahidi.android.ui;

import com.ushahidi.android.core.task.PostExecutionThread;

import android.os.Handler;
import android.os.Looper;

/**
 * MainThread (UI Thread) implementation based on a Handler instantiated with the main application
 * Looper.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UiThread implements PostExecutionThread {

    private final Handler handler;

    private UiThread() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    public static UiThread getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * Causes the Runnable r to be added to the message queue. The runnable will be run on the main
     * thread.
     *
     * @param runnable {@link Runnable} to be executed.
     */
    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }

    private static class LazyHolder {

        private static final UiThread INSTANCE = new UiThread();
    }

}
