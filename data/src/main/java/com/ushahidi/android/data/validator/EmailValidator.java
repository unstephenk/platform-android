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

package com.ushahidi.android.data.validator;

import com.google.common.base.Strings;

import android.os.Build;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Checks for validity of an email address.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class EmailValidator implements Validator {

    @Override
    public boolean isValid(CharSequence text) {
        return validate(text.toString());
    }

    private boolean validate(String email) {

        if (Strings.isNullOrEmpty(email)) {
            return false;
        }

        final Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();

    }
}
