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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ushahidi.android.core.task.ThreadExecutor;
import com.ushahidi.android.data.BuildConfig;
import com.ushahidi.android.data.entity.PostEntity;
import com.ushahidi.android.data.entity.PostTagEntity;
import com.ushahidi.android.data.entity.TagEntity;
import com.ushahidi.android.data.exception.PostNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Helper class for interacting with the app's database.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostDatabaseHelper  extends BaseDatabseHelper
        implements IPostDatabaseHelper, ISearchDatabaseHelper<PostEntity> {

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
                puts(postEntity, callback);
            }
        });

    }

    private void puts(final PostEntity postEntity, final IPostEntityPutCallback callback) {
        SQLiteDatabase db = getReadableDatabase();
        try {
            db.beginTransaction();
                //Delete before insertion
                cupboard().withDatabase(db).delete(postEntity);
                Long rows = cupboard().withDatabase(db).put(postEntity);
                if ((rows > 0) && (postEntity.getPostTagEntityList() != null) && (
                        postEntity.getPostTagEntityList().size() > 0)) {

                    for (PostTagEntity postTagEntity : postEntity.getPostTagEntityList()) {
                            postTagEntity.setPostId(postEntity.getId());
                            cupboard().withDatabase(db).put(postTagEntity);

                    }
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

    @Override
    public synchronized void get(final long id, final IPostEntityCallback callback) {

        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                final PostEntity postEntity = get(id);
                if (postEntity != null) {
                    List<TagEntity> tags = getTagEntity(postEntity);
                    postEntity.setTags(tags);
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
                final List<PostEntity> postEntityList = new ArrayList<>();
                if (postEntities != null) {
                    for (PostEntity postEntity : postEntities) {
                        List<TagEntity> tags = getTagEntity(postEntity);
                        postEntity.setTags(tags);
                        postEntityList.add(postEntity);
                    }
                    callback.onPostEntitiesLoaded(postEntityList);
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

    private List<TagEntity> getTagEntity(PostEntity postEntity) {
        List<TagEntity> tagEntityList = new ArrayList<>();

        // fetch posttag entity
        List<PostTagEntity> postTagEntityList = cupboard().withDatabase(getReadableDatabase())
                .query(PostTagEntity.class)
                .withSelection("mPostId = ?", String.valueOf(postEntity.getId())).list();

        for (PostTagEntity postTagEntity : postTagEntityList) {
            TagEntity tagEntity = cupboard().withDatabase(getReadableDatabase())
                .get(TagEntity.class, postTagEntity.getTagId());

            tagEntityList.add(tagEntity);
        }

        return tagEntityList;
    }

    @Override
    public synchronized void put(final List<PostEntity> postEntities,
            final IPostEntityPutCallback callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                // Delete existing posttag entities
                // Lame way to avoid duplicates
                cupboard().withDatabase(getWritableDatabase()).delete(PostTagEntity.class,null);
                for (PostEntity postEntity : postEntities) {
                    puts(postEntity, callback);
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

    @Override
    public void search(final String query, final SearchCallback<PostEntity> callback) {
        this.asyncRun(new Runnable() {
            @Override
            public void run() {
                if(!isClosed()) {
                    try {
                        final List<PostEntity> postEntityList = search(query);
                        callback.onSearchResult(postEntityList);
                    }catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }
        });
    }

    public List<PostEntity> search(final String query) {
        String selection = " mTitle like ? OR mContent like ?";
        String args [] = {query+"%", query+"%"};
        // Post title holds the search term
        return cupboard().withDatabase(getReadableDatabase()).query(
                PostEntity.class).withSelection(selection, args).list();
    }
}
