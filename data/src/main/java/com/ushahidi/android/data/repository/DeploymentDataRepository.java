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

package com.ushahidi.android.data.repository;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.respository.IDeploymentRepository;
import com.ushahidi.android.data.entity.mapper.DeploymentEntityMapper;

/**
 * {@link com.ushahidi.android.core.respository.IDeploymentRepository} for retrieving deployment
 * data
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentDataRepository implements IDeploymentRepository {

    private static DeploymentDataRepository sDeploymentDataRepository;

    private final DeploymentEntityMapper mDeploymentEntityMapper;


    public static synchronized  DeploymentDataRepository getInstance(DeploymentEntityMapper entityMapper){
        if (sDeploymentDataRepository == null) {
            sDeploymentDataRepository = new DeploymentDataRepository(entityMapper);
        }
        return sDeploymentDataRepository;
    }

    public DeploymentDataRepository(DeploymentEntityMapper entityMapper) {
        if ( entityMapper == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        mDeploymentEntityMapper = entityMapper;
    }

    @Override
    public void addDeployment(Deployment deployment, DeploymentAddCallback deploymentCallback) {

    }
}
