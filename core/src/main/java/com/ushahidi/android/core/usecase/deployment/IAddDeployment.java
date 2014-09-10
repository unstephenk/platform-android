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
package com.ushahidi.android.core.usecase.deployment;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.IInteractor;

/**
 * AddDeployment Implements Add Deployment Use Case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface IAddDeployment extends IInteractor {

    /**
     * Executes this use case.
     *
     * @param deployment The deployment to be added.
     * @param callback   A {@link com.ushahidi.android.core.usecase.deployment.IAddDeployment.Callback}
     *                   used for notify the client.
     * @author Ushahidi Team <team@ushahidi.com>
     */
    public void execute(Deployment deployment, Callback callback);

    /**
     * Callback used to be notified when either a user has been loaded or an error happened.
     */
    interface Callback {

        void onDeploymentAdded();

        void onError(ErrorWrap error);
    }
}
