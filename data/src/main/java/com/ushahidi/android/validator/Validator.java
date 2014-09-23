package com.ushahidi.android.validator;

/**
 * Validators need to implement this.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface Validator {

    /**
     * Checks for valid strings
     *
     * @param text The character sequence to be validated
     * @return Boolean
     */
    public boolean isValid(CharSequence text);
}
