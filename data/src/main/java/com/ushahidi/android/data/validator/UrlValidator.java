package com.ushahidi.android.data.validator;

import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks if a URL is valid
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UrlValidator implements Validator {

    private static final String URL_PATTERN
            = "\\b(https?|ftp|file)://[-a-zA-Z0-9+\\$&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    @Override
    public boolean isValid(CharSequence text) {
        return valid(text.toString());
    }

    private boolean valid(String url) {

        if (Strings.isNullOrEmpty(url)) {
            return false;
        }

        final Pattern pattern = Pattern.compile(URL_PATTERN);
        final Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
