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

package com.ushahidi.android.ui.form.validator;

import com.ushahidi.android.ui.form.FormWidget;

/**
 * Base class that all validated has to be derived from
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class Validator {

    protected String mErrorMessage;

    public Validator(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    /**
     * Determines if the validation passes.
     *
     * @param formWidget The form widget to validate its value.
     * @return The status of the validation.
     */
    public abstract boolean isValid(FormWidget formWidget);

    /**
     * Determines if there is an error message.
     *
     * @return True if there is an error message. Otherwise False.
     */
    public boolean hasErrorMessage() {
        return mErrorMessage != null;
    }

    /**
     * Gets the error message.
     *
     * @return The error message.
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }
}
