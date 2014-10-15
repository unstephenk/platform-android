package com.ushahidi.android.test;

import android.test.AndroidTestCase;

/**
 * A workaround for this bug https://code.google.com/p/dexmaker/issues/detail?id=2
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
// TODO: Follow up on bug to see if it has been fixed and update accordingly.
public class CustomAndroidTestCase extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().toString());
    }
}