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
import com.ushahidi.android.data.entity.PostEntity;
import com.ushahidi.android.data.exception.PostNotFoundException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Helper class for interacting with the app's database.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostDatabaseHelper  extends BaseDatabseHelper
        implements IPostDatabaseHelper {

    private static PostDatabaseHelper sInstance;

    private static String TAG = PostDatabaseHelper.class.getSimpleName();

    private final ThreadExecutor mThreadExecutor;

    private PostDatabaseHelper(Context context, ThreadExecutor threadExecutor) {
        super(context);

        if (threadExecutor == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }

        mThreadExecutor = threadExecutor;
    }

    public static synchronized PostDatabaseHelper getInstance(Context context,
            ThreadExecutor threadExecutor) {

        if (sInstance == null) {
            sInstance = new PostDatabaseHelper(context, threadExecutor);
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
    public synchronized void put(final PostEntity postEntity,
            final IPostEntityPutCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        Log.e("DATA", "Repo " + postEntity.toString());
                        cupboard().withDatabase(getWritableDatabase()).put(postEntity);
                        callback.onPostEntityPut();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });

    }

    @Override
    public synchronized void get(final long id, final IPostEntityCallback callback) {

        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                final PostEntity postEntity = get(id);
                if (postEntity != null) {
                    callback.onPostEntityLoaded(postEntity);
                } else {
                    callback.onError(new PostNotFoundException());
                }
            }
        });

    }

    @Override
    public synchronized void getPostEntities(final IPostEntitiesCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                final List<PostEntity> postEntities = getPosts();
                if (postEntities != null) {
                    callback.onPostEntitiesLoaded(postEntities);
                } else {
                    callback.onError(new PostNotFoundException());
                }
            }
        });
    }

    private List<PostEntity> getPosts() {
        return cupboard().withDatabase(getReadableDatabase()).query(PostEntity.class).list();
    }

    private PostEntity get(long id) {
        return cupboard().withDatabase(getReadableDatabase()).query(PostEntity.class)
                .byId(id).get();
    }


    @Override
    public synchronized void put(final List<PostEntity> postEntities,
            final IPostEntityPutCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = null;

                try {
                    db = getWritableDatabase();
                    db.beginTransaction();
                    for (PostEntity postEntity : postEntities) {
                        cupboard().withDatabase(db).put(postEntity);
                    }
                    db.setTransactionSuccessful();
                    callback.onPostEntityPut();
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
    public synchronized void deleteAll(final IPostEntityDeletedCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        final int numRows = cupboard().withDatabase(getWritableDatabase())
                                .delete(PostEntity.class, null);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "delete all post entities. Deleted " + numRows
                                    + " rows.");
                        }
                        callback.onPostEntityDeleted();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }

    @Override
    public synchronized void delete(final PostEntity postEntity,
            final IPostEntityDeletedCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        cupboard().withDatabase(getWritableDatabase()).delete(postEntity);
                        callback.onPostEntityDeleted();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }



}
