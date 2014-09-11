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

import com.ushahidi.android.data.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class RepositoryErrorTest extends BaseTestCase {

    private RepositoryError mRepositoryError;

    @Mock
    private Exception mMockException;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mRepositoryError = new RepositoryError(mMockException);
    }

    @Test
    public void shouldGetRepositoryExceptionMessage() {
        mRepositoryError.getErrorMessage();

        verify(mMockException).getMessage();
    }

}
