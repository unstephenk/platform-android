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

package com.ushahidi.android.data.database;

import com.ushahidi.android.core.executor.ThreadExecutor;
import com.ushahidi.android.data.BuildConfig;
import com.ushahidi.android.data.entity.DeploymentEntity;
import com.ushahidi.android.data.exception.DeploymentNotFoundException;
import com.ushahidi.android.data.exception.ListDeploymentNotFoundException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Collection;
import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentDatabaseHelper extends BaseDatabseHelper<DeploymentEntity>
        implements IDeploymentDatabaseHelper {

    private static DeploymentDatabaseHelper mSDeploymentDatabaseHelper;

    private static String TAG = DeploymentDatabaseHelper.class.getSimpleName();

    static {
        // Register our deployment entity
        cupboard().register(DeploymentEntity.class);
    }

    private final ThreadExecutor mThreadExecutor;

    public DeploymentDatabaseHelper(Context context, ThreadExecutor threadExecutor) {
        super(context);

        if (threadExecutor == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }

        mThreadExecutor = threadExecutor;
    }

    public static synchronized DeploymentDatabaseHelper getInstance(Context context,
            ThreadExecutor threadExecutor) {

        if (mSDeploymentDatabaseHelper == null) {
            mSDeploymentDatabaseHelper = new DeploymentDatabaseHelper(context, threadExecutor);
        }
        return mSDeploymentDatabaseHelper;
    }

    /**
     * Executes a {@link Runnable} in another Thread.
     *
     * @param runnable {@link Runnable} to execute
     */
    private void asyncRun(Runnable runnable) {
        mThreadExecutor.execute(runnable);
    }

    @Override
    public synchronized void put(final DeploymentEntity deploymentEntity) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        cupboard().withDatabase(getWritableDatabase()).put(deploymentEntity);
                    } catch (Exception e) {
                        // TODO: catch exception
                    }
                }
            }
        });
    }

    @Override
    public synchronized void get(int id, IDeploymentEntityCallback callback) {
        DeploymentEntity deploymentEntity = get(id);

        if (deploymentEntity != null) {
            callback.onDeploymentEntityLoaded(deploymentEntity);
        } else {
            callback.onError(new DeploymentNotFoundException());
        }

    }

    @Override
    public synchronized void getDeploymentEntities(IDeploymentDeploymentEntitiesCallback callback) {
        List<DeploymentEntity> deploymentEntities = this.getDeployments();
        if (deploymentEntities != null) {
            callback.onDeploymentEntitiesLoaded(deploymentEntities);
        } else {
            callback.onError(new ListDeploymentNotFoundException());
        }
    }

    private List<DeploymentEntity> getDeployments() {
        return cupboard().withDatabase(getReadableDatabase()).query(DeploymentEntity.class).list();
    }

    private DeploymentEntity get(int id) {
        return cupboard().withDatabase(getReadableDatabase()).query(DeploymentEntity.class)
                .byId(Long.valueOf(id)).get();
    }


    @Override
    public synchronized void put(final Collection<DeploymentEntity> deploymentEntities) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = null;

                try {
                    db = getWritableDatabase();
                    db.beginTransaction();
                    for (DeploymentEntity deploymentEntity : deploymentEntities) {
                        cupboard().withDatabase(db).put(deploymentEntity);
                    }
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    // TODO: Handle error
                } finally {
                    if (db != null) {
                        db.endTransaction();
                    }
                }
            }
        });
    }

    @Override
    public synchronized void deleteAll() {
        deleteAll(getWritableDatabase());
    }

    public void deleteAll(SQLiteDatabase db) {
        if (!isClosed()) {
            try {
                final int numDeleted = cupboard().withDatabase(db)
                        .delete(DeploymentEntity.class, null);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "delete all deployment entities. Deleted " + numDeleted + " rows.");
                }
            } catch (Exception e) {
                // TODO: Handle exception message here
            }
        }
    }

    @Override
    public synchronized void delete(DeploymentEntity deploymentEntity) {
        if (!isClosed()) {
            try {
                cupboard().withDatabase(getWritableDatabase()).delete(deploymentEntity);
            } catch (Exception e) {
                // TODO: Handle exception message here
            }
        }
    }


}
