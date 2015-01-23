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

package com.ushahidi.android.state;

import com.ushahidi.android.core.entity.UserAccount;
import com.ushahidi.android.model.DeploymentModel;
import com.ushahidi.android.model.UserAccountModel;
import com.ushahidi.android.model.UserModel;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface Istate {

    public UserAccountModel getActiveUserAccount();

    public UserModel getUserModel();

    public DeploymentModel getActiveDeployment();

    public void registerEvent(Object receiver);

    public void unregisterEvent(Object receiver);
}
