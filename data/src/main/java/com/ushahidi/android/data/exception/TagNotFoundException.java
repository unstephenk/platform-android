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

package com.ushahidi.android.data.exception;

/**
 * Exception thrown when a query for a particular tag does not return any result
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagNotFoundException extends Exception {

    public TagNotFoundException() {
        super();
    }

    public TagNotFoundException(final String message) {
        super(message);
    }

    public TagNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TagNotFoundException(final Throwable cause) {
        super(cause);
    }
}
