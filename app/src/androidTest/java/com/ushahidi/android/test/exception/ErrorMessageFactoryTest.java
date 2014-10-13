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

package com.ushahidi.android.test.exception;

import com.ushahidi.android.R;
import com.ushahidi.android.data.exception.DeploymentNotFoundException;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.exception.ErrorMessageFactory;

import android.test.AndroidTestCase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests {@link com.ushahidi.android.exception.ErrorMessageFactory}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ErrorMessageFactoryTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testDeploymentNotFoundException() {
        final String expectedMessage = getContext()
                .getString(R.string.exception_message_deployment_not_found);
        final String actualMessage = ErrorMessageFactory
                .create(getContext(), new DeploymentNotFoundException());

        assertThat(actualMessage, is(equalTo(expectedMessage)));
    }

    public void testDeploymentValidationException() {
        final String expectedMessage = getContext().getString(R.string.error_domain_not_valid);
        final String actualMessage = ErrorMessageFactory
                .create(getContext(), new ValidationException(
                        getContext().getString(R.string.error_domain_not_valid)));
        assertThat(actualMessage, is(equalTo(expectedMessage)));
    }
}
