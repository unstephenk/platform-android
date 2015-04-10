/*
 *  Copyright (c) 2015 Ushahidi.
 *
 *   This program is free software: you can redistribute it and/or modify it under
 *   the terms of the GNU Affero General Public License as published by the Free
 *   Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 *   This program is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *   FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program in the file LICENSE-AGPL. If not, see
 *   https://www.gnu.org/licenses/agpl-3.0.html
 *
 */

package com.ushahidi.android.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.deployment.ActivateDeployment;
import com.ushahidi.android.core.usecase.deployment.ListDeployment;
import com.ushahidi.android.exception.ErrorMessageFactory;
import com.ushahidi.android.ui.activity.ActivityLauncher;
import com.ushahidi.android.ui.prefs.Prefs;
import com.ushahidi.android.ui.view.IView;
import com.ushahidi.android.util.Util;

import java.util.List;

import javax.inject.Inject;

/**
 * Presenter for {@link com.ushahidi.android.ui.activity.MainActivity}. The main thing it does
 * here is to figure out if there are any deployments setup, otherwise launch
 * {@link com.ushahidi.android.core.usecase.deployment.AddDeployment} to add a new deployment or
 * make the first deployment the active one in the fetched deployment list then launch
 * {@link com.ushahidi.android.ui.activity.PostActivity}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MainPresenter implements IPresenter {

    private final ListDeployment mListDeployment;

    private List<Deployment> deployments = null;

    private View mView;

    @Inject
    ActivityLauncher launcher;

    @Inject
    Prefs mPrefs;

    private final ActivateDeployment mActivateDeployment;

    private final ListDeployment.Callback mListDeploymentCallback = new ListDeployment.Callback() {

        @Override
        public void onDeploymentListLoaded(List<Deployment> listDeployment) {
            // Deployment List is empty, launch activity for adding a new deployment.
            if (Util.isCollectionEmpty(listDeployment)) {
                // Launch Activity to add a new deployment.
                launcher.launchAddDeployment();
            } else {

                deployments = listDeployment;
                // Get the active deployment from the saved shared Prefs.
                if (!TextUtils.isEmpty(mPrefs.getActiveDeploymentUrl().get())) {
                    // Launch PostActivity
                    launchPostActivity();
                } else {
                    // Make the first deployment an active one.
                    mActivateDeployment.execute(deployments, 0, mActivateDeploymentCallback);
                }
            }

        }

        @Override
        public void onError(ErrorWrap error) {
            showErrorMessage(error);
        }
    };

    private void launchPostActivity() {
        launcher.post();
        mView.finishActivity();
    }

    private final ActivateDeployment.Callback mActivateDeploymentCallback =
        new ActivateDeployment.Callback() {
            @Override
            public void onDeploymentActivated() {
                launchPostActivity();
            }

            @Override
            public void onError(ErrorWrap error) {
                showErrorMessage(error);

            }
        };

    @Inject
    public MainPresenter(@NonNull ListDeployment listDeployment,
                         @NonNull ActivateDeployment activateDeployment) {
        mListDeployment = listDeployment;
        mActivateDeployment = activateDeployment;
    }

    @Override
    public void resume() {
        mListDeployment.execute(mListDeploymentCallback);
    }

    @Override
    public void pause() {
        // Do nothing
    }

    public void setView(@NonNull View view) {
        mView = view;
    }

    private void showErrorMessage(ErrorWrap errorWrap) {
        String errorMessage = ErrorMessageFactory.create(mView.getAppContext(),
            errorWrap.getException());
        mView.showError(errorMessage);
    }

    public interface View extends IView {
        // Use inherited methods
        void finishActivity();
    }
}
