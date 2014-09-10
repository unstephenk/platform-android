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

package com.ushahidi.android.data;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.Fs;

/**
 * Custom Roboelectric Test Runner to make use of SDK v18 as
 * Roboelectric doesn't support SDK v18+
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class CustomRobolectricTestRunner extends RobolectricTestRunner {

    // Maximum roboelectric support is SDK 18
    private static final int MAX_SDK_SUPPORTED_BY_ROBOLECTRIC = 18;

    public CustomRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        String manifestProperty = System.getProperty("android.manifest");
        if (config.manifest().equals(Config.DEFAULT) && manifestProperty != null) {
            String resProperty = System.getProperty("android.resources");
            String assetsProperty = System.getProperty("android.assets");
            return new AndroidManifest(Fs.fileFromPath(manifestProperty), Fs.fileFromPath(resProperty)) {
                @Override
                public int getTargetSdkVersion() {
                    return MAX_SDK_SUPPORTED_BY_ROBOLECTRIC;
                }
            };
        }
        AndroidManifest appManifest = super.getAppManifest(config);
        return appManifest;
    }
}
