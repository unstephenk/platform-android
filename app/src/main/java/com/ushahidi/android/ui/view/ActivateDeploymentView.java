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

package com.ushahidi.android.ui.view;


import com.ushahidi.android.presenter.DeploymentNavPresenter;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ActivateDeploymentView implements IActivateDeploymentView {

    private Context mContext;

    private DeploymentNavPresenter mDeploymentNavPresenter;

    public ActivateDeploymentView(Context context) {
        mContext = context;
    }

    @Override
    public void showError(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setDeploymentNavPresenter(DeploymentNavPresenter deploymentNavPresenter) {
        mDeploymentNavPresenter = deploymentNavPresenter;
    }

    @Override
    public void markStatus() {
        if (mDeploymentNavPresenter != null) {
            mDeploymentNavPresenter.resume();
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
