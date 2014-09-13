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

package com.ushahidi.android.ui.adapter;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.model.DeploymentModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Manages list of deployments
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentAdapter extends BaseListAdapter<DeploymentModel> {

    public DeploymentAdapter(Context context, List<DeploymentModel> deploymentModelList) {
        this(context);
        setItems(deploymentModelList);
    }
    public DeploymentAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
