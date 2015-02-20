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

package com.ushahidi.android.util;

import java.util.Collection;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Generic Utilities for the application
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class Util {

    public static boolean isCollectionEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static int collectionSize(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }

    public static String capitalizeFirstLetter(String text) {
        if (text.length() == 0) {
            return text;
        }

        return text.substring(0, 1).toUpperCase(Locale.getDefault())
                + text.substring(1).toLowerCase(Locale.getDefault());

    }

    public static boolean validateHexColor(String hexColor) {
        
        final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        return Pattern.compile(HEX_PATTERN).matcher(hexColor).matches();
    }
}
