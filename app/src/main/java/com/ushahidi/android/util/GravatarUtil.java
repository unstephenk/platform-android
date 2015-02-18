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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class GravatarUtil {

    private static final StringBuilder sBuilder = new StringBuilder();

    private static final String GRAVATAR_SECURE_ENDPOINT = "https://secure.gravatar.com/avatar/";

    private GravatarUtil() {
    }

    private static String hex(byte[] array) {
        sBuilder.setLength(0);
        for (byte b : array) {
            sBuilder.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
        }
        return sBuilder.toString();
    }

    private static String convertEmailToHash(String email) {
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            return hex(messageDigest.digest(email.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            return email;
        } catch (UnsupportedEncodingException e) {
            return email;
        }
    }

    public static String url(String email) {
        final String hash = convertEmailToHash(email);
        final StringBuilder builder = new StringBuilder(GRAVATAR_SECURE_ENDPOINT.length()
                + hash.length() + 1)
                .append(GRAVATAR_SECURE_ENDPOINT)
                .append(hash)
                .append("&f=mm");
        return builder.toString();
    }
}
