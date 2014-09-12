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

package com.ushahidi.android.exception;

import com.ushahidi.android.R;
import com.ushahidi.android.data.exception.AddDeploymentException;
import com.ushahidi.android.data.exception.DeploymentNotFoundException;
import com.ushahidi.android.data.exception.ListDeploymentNotFoundException;

import android.content.Context;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ErrorMessageFactory {

    private ErrorMessageFactory() {}

    /**
     * Creates a String representing an error message.
     *
     * @param context Context needed to retrieve string resources.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return {@link String} an error message.
     */
    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);

        if (exception instanceof ListDeploymentNotFoundException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (exception instanceof DeploymentNotFoundException) {
            message = context.getString(R.string.exception_message_deployment_not_found);
        } else if (exception instanceof AddDeploymentException) {
            message = context.getString(R.string.exception_message_deployment_not_added);
        }

        return message;
    }
}
