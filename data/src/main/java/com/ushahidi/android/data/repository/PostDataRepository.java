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
import com.ushahidi.android.data.entity.PostEntity;
import com.ushahidi.android.data.entity.mapper.PostEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.data.repository.datasource.post.PostDataSource;
import com.ushahidi.android.data.repository.datasource.post.PostDataSourceFactory;

import java.util.List;

/**
 * {@link com.ushahidi.android.core.respository.IPostRepository} for retrieving post data
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostDataRepository implements IPostRepository {

    private static PostDataRepository sInstance;

    private final PostEntityMapper mPostEntityMapper;

    private final PostDataSourceFactory mPostDataSourceFactory;


    protected PostDataRepository(PostDataSourceFactory postDataSourceFactory,
            PostEntityMapper entityMapper) {

        mPostDataSourceFactory = Preconditions
                .checkNotNull(postDataSourceFactory, "PostDataSourceFactory cannot be null.");
        mPostEntityMapper = Preconditions
                .checkNotNull(entityMapper, "Entity mapper cannot be null");
    }

    public static synchronized PostDataRepository getInstance(PostDataSourceFactory
            postDataSourceFactory, PostEntityMapper entityMapper) {
        if (sInstance == null) {
            sInstance = new PostDataRepository(postDataSourceFactory,
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
    public void putPost(Post post,
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
            final PostDataSource postDataSource = mPostDataSourceFactory
                    .createPostDatabaseDataSource();
            postDataSource.putPostEntity(mPostEntityMapper.unmap(post),
                    new PostDataSource.PostEntityAddCallback() {
                        @Override
                        public void onPostEntityAdded() {
                            postCallback.onPostAdded();
                        }

                        @Override
                        public void onError(Exception e) {
                            postCallback.onError(new RepositoryError(e));
                        }
                    });

        }
    }

    @Override
    public void getPostList(final PostListCallback postListCallback) {
        final PostDataSource postDataSource = mPostDataSourceFactory.createPostDatabaseDataSource();
        postDataSource.getPostEntityList(new PostDataSource.PostEntityListCallback() {
            @Override
            public void onPostEntityListLoaded(List<PostEntity> postEntityList) {
                final List<Post> posts = mPostEntityMapper
                        .map(postEntityList);
                postListCallback.onPostListLoaded(posts);
            }

            @Override
            public void onError(Exception e) {
                postListCallback.onError(new RepositoryError(e));
            }
        });

    }

    /**
     * Fetches a list of {@link com.ushahidi.android.core.entity.Post} via the API
     *
     * @param postListCallback A {@link PostListCallback} for notifying clients about post fetch
     *                         status.
     */
    @Override
    public void getPostListViaApi(final PostListCallback postListCallback) {
        final PostDataSource postDataSource = mPostDataSourceFactory.createPostApiDataSource();
        postDataSource.getPostEntityList(new PostDataSource.PostEntityListCallback() {
            @Override
            public void onPostEntityListLoaded(List<PostEntity> postEntityList) {

                final List<Post> posts = mPostEntityMapper
                        .map(postEntityList);
                postListCallback.onPostListLoaded(posts);
            }

            @Override
            public void onError(Exception e) {
                postListCallback.onError(new RepositoryError(e));
            }
        });
    }

    @Override
    public void getPostById(long postId,
            final PostDetailsCallback postDetailsCallback) {
        final PostDataSource postDataSource = mPostDataSourceFactory.createPostDatabaseDataSource();
        postDataSource.getPostEntityById(postId, new PostDataSource.PostEntityDetailsCallback() {
            @Override
            public void onPostEntityLoaded(PostEntity postEntity) {
                final Post post = mPostEntityMapper.map(postEntity);
                postDetailsCallback.onPostLoaded(post);
            }

            @Override
            public void onError(Exception e) {
                postDetailsCallback.onError(new RepositoryError(e));
            }
        });
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
        final PostDataSource postDataSource = mPostDataSourceFactory.createPostDatabaseDataSource();
        postDataSource.deletePostEntity(mPostEntityMapper.unmap(post),
                new PostDataSource.PostEntityDeletedCallback() {
                    @Override
                    public void onPostEntityDeleted() {
                        callback.onPostDeleted();
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(new RepositoryError(e));
                    }
                });
    }
}
