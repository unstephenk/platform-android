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

package com.ushahidi.android.module;

import com.squareup.otto.Bus;
import com.ushahidi.android.state.ApplicationState;
import com.ushahidi.android.state.IDeploymentState;
import com.ushahidi.android.state.IUserState;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Module(
        library = true
)
public class StateModule {

    @Provides
    @Singleton
    public Bus provideEventBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    public ApplicationState provideApplicationState(Bus bus) {
        return new ApplicationState(bus);
    }


    @Provides
    public IUserState provideUserState(ApplicationState state) {
        return state;
    }

    @Provides
    public IDeploymentState provideDeploymentState(ApplicationState state) {
        return state;
    }
}
