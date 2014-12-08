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
import com.ushahidi.android.data.entity.TagEntity;
import com.ushahidi.android.data.exception.TagNotFoundException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Tags database helper
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class TagDatabaseHelper extends BaseDatabseHelper implements ITagDatabaseHelper {

    private static TagDatabaseHelper sInstance;

    private static String TAG = TagDatabaseHelper.class.getSimpleName();

    private final ThreadExecutor mThreadExecutor;

    private TagDatabaseHelper(Context context, ThreadExecutor threadExecutor) {
        super(context);

        if (threadExecutor == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }

        mThreadExecutor = threadExecutor;
    }

    public static synchronized TagDatabaseHelper getInstance(Context context,
            ThreadExecutor threadExecutor) {

        if (sInstance == null) {
            sInstance = new TagDatabaseHelper(context, threadExecutor);
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
    public synchronized void put(final TagEntity tagEntity, final ITagEntityPutCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        Log.e(TAG, "Repo " + tagEntity.toString());
                        cupboard().withDatabase(getWritableDatabase()).put(tagEntity);
                        callback.onTagEntityPut();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }

    @Override
    public synchronized void get(final long id, final ITagEntityCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                final TagEntity tagEntity = get(id);
                if (tagEntity != null) {
                    callback.onTagEntityLoaded(tagEntity);
                } else {
                    callback.onError(new TagNotFoundException());
                }
            }
        });
    }

    private TagEntity get(long id) {
        return cupboard().withDatabase(getReadableDatabase()).query(TagEntity.class)
                .byId(id).get();
    }

    private List<TagEntity> getTags() {
        return cupboard().withDatabase(getReadableDatabase()).query(TagEntity.class).list();
    }

    @Override
    public synchronized void getTagEntities(final ITagEntitiesCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                final List<TagEntity> tagEntities = getTags();
                if (tagEntities != null) {
                    callback.onTagEntitiesLoaded(tagEntities);
                } else {
                    callback.onError(new TagNotFoundException());
                }
            }
        });
    }

    @Override
    public synchronized void put(final List<TagEntity> tagEntities,
            final ITagEntityPutCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = null;

                try {
                    db = getWritableDatabase();
                    db.beginTransaction();
                    for (TagEntity tagEntity : tagEntities) {
                        cupboard().withDatabase(db).put(tagEntity);
                    }
                    db.setTransactionSuccessful();
                    callback.onTagEntityPut();
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
    public synchronized void deleteAll(final ITagEntityDeletedCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        final int numRows = cupboard().withDatabase(getWritableDatabase())
                                .delete(TagEntity.class, null);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "delete all tag entities. Deleted " + numRows
                                    + " rows.");
                        }
                        callback.onTagEntityDeleted();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }

    @Override
    public synchronized void delete(final TagEntity tagEntity,
            final ITagEntityDeletedCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if (!isClosed()) {
                    try {
                        cupboard().withDatabase(getWritableDatabase()).delete(tagEntity);
                        callback.onTagEntityDeleted();
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }
}
