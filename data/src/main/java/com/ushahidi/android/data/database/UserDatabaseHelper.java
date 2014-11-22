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

import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.data.BuildConfig;
import com.ushahidi.android.data.entity.UserEntity;
import com.ushahidi.android.data.exception.UserNotFoundException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Users database helper
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserDatabaseHelper extends BaseDatabseHelper implements IUserDatabaseHelper {

    private static UserDatabaseHelper sInstance;

    private static String TAG = UserDatabaseHelper.class.getSimpleName();

    static {
        // Register our deployment entity
        cupboard().register(UserEntity.class);
    }

    private final ThreadExecutor mThreadExecutor;

    private UserDatabaseHelper(Context context, ThreadExecutor threadExecutor) {
        super(context);

        if (threadExecutor == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }

        mThreadExecutor = threadExecutor;
    }

    public static synchronized UserDatabaseHelper getInstance(Context context,
            ThreadExecutor threadExecutor) {

        if (sInstance == null) {
            sInstance = new UserDatabaseHelper(context, threadExecutor);
        }
        return sInstance;
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
    public synchronized void put(final UserEntity userEntity,
            final IUserEntityPutCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        Log.e(TAG, "Repo " + userEntity.toString());
                        cupboard().withDatabase(getWritableDatabase()).put(userEntity);
                        callback.onUserEntityPut();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }

    @Override
    public synchronized void get(final long id, final IUserEntityCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                final UserEntity userEntity = get(id);
                if (userEntity != null) {
                    callback.onUserEntityLoaded(userEntity);
                } else {
                    callback.onError(new UserNotFoundException());
                }
            }
        });
    }

    private UserEntity get(long id) {
        return cupboard().withDatabase(getReadableDatabase()).query(UserEntity.class)
                .byId(id).get();
    }

    private List<UserEntity> getUsers() {
        return cupboard().withDatabase(getReadableDatabase()).query(UserEntity.class).list();
    }

    @Override
    public synchronized void getUserEntities(final IUserEntitiesCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                final List<UserEntity> userEntities = getUsers();
                if (userEntities != null) {
                    callback.onUserEntitiesLoaded(userEntities);
                } else {
                    callback.onError(new UserNotFoundException());
                }
            }
        });
    }

    @Override
    public synchronized void put(final List<UserEntity> userEntities,
            final IUserEntityPutCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = null;

                try {
                    db = getWritableDatabase();
                    db.beginTransaction();
                    for (UserEntity userEntity : userEntities) {
                        cupboard().withDatabase(db).put(userEntity);
                    }
                    db.setTransactionSuccessful();
                    callback.onUserEntityPut();
                } catch (Exception e) {
                    callback.onError(e);
                } finally {
                    if (db != null) {
                        db.endTransaction();
                    }
                }
            }
        });
    }

    @Override
    public synchronized void deleteAll(final IUserEntityDeletedCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        final int numRows = cupboard().withDatabase(getWritableDatabase())
                                .delete(UserEntity.class, null);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "delete all user entities. Deleted " + numRows
                                    + " rows.");
                        }
                        callback.onUserEntityDeleted();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }

    @Override
    public synchronized void delete(final UserEntity userEntity,
            final IUserEntityDeletedCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        cupboard().withDatabase(getWritableDatabase()).delete(userEntity);
                        callback.onUserEntityDeleted();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }
}
