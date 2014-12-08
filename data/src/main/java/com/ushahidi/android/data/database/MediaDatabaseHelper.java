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
import com.ushahidi.android.data.entity.MediaEntity;
import com.ushahidi.android.data.exception.MediaNotFoundException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Media database helper
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MediaDatabaseHelper extends BaseDatabseHelper implements IMediaDatabaseHelper {

    private static MediaDatabaseHelper sInstance;

    private static String TAG = MediaDatabaseHelper.class.getSimpleName();

    private final ThreadExecutor mThreadExecutor;

    private MediaDatabaseHelper(Context context, ThreadExecutor threadExecutor) {
        super(context);

        if (threadExecutor == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }

        mThreadExecutor = threadExecutor;
    }

    public static synchronized MediaDatabaseHelper getInstance(Context context,
            ThreadExecutor threadExecutor) {

        if (sInstance == null) {
            sInstance = new MediaDatabaseHelper(context, threadExecutor);
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
    public synchronized void put(final MediaEntity mediaEntity,
            final IMediaEntityPutCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        Log.e(TAG, "Repo " + mediaEntity.toString());
                        cupboard().withDatabase(getWritableDatabase()).put(mediaEntity);
                        callback.onMediaEntityPut();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }

    @Override
    public synchronized void get(final long id, final IMediaEntityCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                final MediaEntity mediaEntity = get(id);
                if (mediaEntity != null) {
                    callback.onMediaEntityLoaded(mediaEntity);
                } else {
                    callback.onError(new MediaNotFoundException());
                }
            }
        });
    }

    private MediaEntity get(long id) {
        return cupboard().withDatabase(getReadableDatabase()).query(MediaEntity.class)
                .byId(id).get();
    }

    private List<MediaEntity> getMedias() {
        return cupboard().withDatabase(getReadableDatabase()).query(MediaEntity.class).list();
    }

    @Override
    public synchronized void getMediaEntities(final IMediaEntitiesCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                final List<MediaEntity> mediaEntities = getMedias();
                if (mediaEntities != null) {
                    callback.onMediaEntitiesLoaded(mediaEntities);
                } else {
                    callback.onError(new MediaNotFoundException());
                }
            }
        });
    }

    @Override
    public synchronized void put(final List<MediaEntity> mediaEntities,
            final IMediaEntityPutCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = null;

                try {
                    db = getWritableDatabase();
                    db.beginTransaction();
                    for (MediaEntity mediaEntity : mediaEntities) {
                        cupboard().withDatabase(db).put(mediaEntity);
                    }
                    db.setTransactionSuccessful();
                    callback.onMediaEntityPut();
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
    public synchronized void deleteAll(final IMediaEntityDeletedCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        final int numRows = cupboard().withDatabase(getWritableDatabase())
                                .delete(MediaEntity.class, null);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "delete all media entities. Deleted " + numRows
                                    + " rows.");
                        }
                        callback.onMediaEntityDeleted();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }

    @Override
    public synchronized void delete(final MediaEntity mediaEntity,
            final IMediaEntityDeletedCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        cupboard().withDatabase(getWritableDatabase()).delete(mediaEntity);
                        callback.onMediaEntityDeleted();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }
}
