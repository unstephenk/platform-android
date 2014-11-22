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

package com.ushahidi.android.data.repository;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import com.ushahidi.android.core.entity.Post;
import com.ushahidi.android.core.respository.IPostRepository;
import com.ushahidi.android.data.database.IPostDatabaseHelper;
import com.ushahidi.android.data.database.PostDatabaseHelper;
import com.ushahidi.android.data.entity.PostEntity;
import com.ushahidi.android.data.entity.mapper.PostEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;

import java.util.List;

/**
 * {@link com.ushahidi.android.core.respository.IPostRepository} for retrieving post data
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostDataRepository implements IPostRepository {

    private static PostDataRepository sInstance;

    private final PostEntityMapper mPostEntityMapper;

    private final PostDatabaseHelper mPostDatabaseHelper;


    protected PostDataRepository(PostDatabaseHelper postDatabaseHelper,
            PostEntityMapper entityMapper) {
        if (entityMapper == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        Preconditions.checkNotNull(postDatabaseHelper, "DatabaseHelper cannot be null");
        Preconditions.checkNotNull(entityMapper, "Entity mapper cannot be null");
        mPostEntityMapper = entityMapper;
        mPostDatabaseHelper = postDatabaseHelper;
    }

    public static synchronized PostDataRepository getInstance(PostDatabaseHelper
            postDatabaseHelper, PostEntityMapper entityMapper) {
        if (sInstance == null) {
            sInstance = new PostDataRepository(postDatabaseHelper,
                    entityMapper);
        }
        return sInstance;
    }

    /**
     * {@inheritDoc}
     *
     * @param post         The Post to be saved.
     * @param postCallback A {@link com.ushahidi.android.core.respository.IPostRepository.PostAddCallback}
     *                     used for notifying clients.
     */
    @Override
    public void addPost(Post post,
            final PostAddCallback postCallback) {
        // Check for required fields
        boolean isValid = true;
        if (Strings.isNullOrEmpty(post.getTitle())) {
            isValid = false;
            postCallback.onError(new RepositoryError(
                    new ValidationException("Post URL cannot be null or empty")));
        }

        if (Strings.isNullOrEmpty(post.getContent())) {
            isValid = false;
            postCallback.onError(
                    new RepositoryError(
                            new ValidationException("Post content cannot be empty or null")));
        }

        if (isValid) {
            mPostDatabaseHelper.put(mPostEntityMapper.unmap(post),
                    new IPostDatabaseHelper.IPostEntityPutCallback() {

                        @Override
                        public void onPostEntityPut() {
                            postCallback.onPostAdded();
                        }

                        @Override
                        public void onError(Exception exception) {
                            postCallback.onError(new RepositoryError(exception));
                        }
                    });
        }
    }

    @Override
    public void getPostList(final PostListCallback postListCallback) {
        mPostDatabaseHelper.getPostEntities(
                new IPostDatabaseHelper.IPostEntitiesCallback() {

                    @Override
                    public void onPostEntitiesLoaded(
                            List<PostEntity> postEntities) {
                        final List<Post> posts = mPostEntityMapper
                                .map(postEntities);
                        postListCallback.onPostListLoaded(posts);
                    }

                    @Override
                    public void onError(Exception exception) {
                        postListCallback.onError(new RepositoryError(exception));
                    }
                });
    }

    @Override
    public void getPostById(long postId,
            final PostDetailsCallback postDetailsCallback) {
        mPostDatabaseHelper.get(postId,
                new IPostDatabaseHelper.IPostEntityCallback() {

                    @Override
                    public void onPostEntityLoaded(PostEntity postEntity) {
                        final Post post = mPostEntityMapper.map(postEntity);
                        postDetailsCallback.onPostLoaded(post);
                    }

                    @Override
                    public void onError(Exception exception) {
                        postDetailsCallback.onError(new RepositoryError(exception));
                    }
                });
    }

    /**
     * {@inheritDoc}
     *
     * @param post         The Post to be saved.
     * @param postCallback A {@link com.ushahidi.android.core.respository.IPostRepository.PostUpdateCallback}
     *                     used for notifying clients.
     */
    @Override
    public void updatePost(Post post,
            final PostUpdateCallback postCallback) {
        // Check for required fields
        boolean isValid = true;
        if (Strings.isNullOrEmpty(post.getTitle())) {
            isValid = false;
            postCallback.onError(new RepositoryError(
                    new ValidationException("Post URL cannot be null or empty")));
        }

        if (Strings.isNullOrEmpty(post.getContent())) {
            isValid = false;
            postCallback.onError(
                    new RepositoryError(
                            new ValidationException("Post content cannot be empty or null")));
        }

        if (isValid) {
            mPostDatabaseHelper.put(mPostEntityMapper.unmap(post),
                    new IPostDatabaseHelper.IPostEntityPutCallback() {

                        @Override
                        public void onPostEntityPut() {
                            postCallback.onPostUpdated();
                        }

                        @Override
                        public void onError(Exception exception) {
                            postCallback.onError(new RepositoryError(exception));
                        }
                    });
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param post     The ID of the post to be deleted.
     * @param callback A {@link com.ushahidi.android.core.respository.IPostRepository.PostDeletedCallback}
     *                 used for notifying clients.
     */
    @Override
    public void deletePost(final Post post,
            final PostDeletedCallback callback) {
        mPostDatabaseHelper.delete(mPostEntityMapper.unmap(post),
                new IPostDatabaseHelper.IPostEntityDeletedCallback() {
                    @Override
                    public void onPostEntityDeleted() {
                        callback.onPostDeleted();
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(new RepositoryError(exception));
                    }

                });
    }
}
