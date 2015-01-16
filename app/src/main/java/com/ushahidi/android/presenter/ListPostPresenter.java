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

package com.ushahidi.android.presenter;

import com.google.common.base.Preconditions;

import com.ushahidi.android.Util.ApiServiceUtil;
import com.ushahidi.android.core.entity.Post;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.respository.IPostRepository;
import com.ushahidi.android.core.usecase.ISearch;
import com.ushahidi.android.core.usecase.Search;
import com.ushahidi.android.core.usecase.post.FetchPost;
import com.ushahidi.android.core.usecase.post.ListPost;
import com.ushahidi.android.data.api.service.PostService;
import com.ushahidi.android.data.database.PostDatabaseHelper;
import com.ushahidi.android.data.entity.mapper.PostEntityMapper;
import com.ushahidi.android.data.repository.PostDataRepository;
import com.ushahidi.android.data.repository.datasource.post.PostDataSourceFactory;
import com.ushahidi.android.exception.ErrorMessageFactory;
import com.ushahidi.android.model.PostModel;
import com.ushahidi.android.model.mapper.PostModelDataMapper;
import com.ushahidi.android.ui.prefs.Prefs;
import com.ushahidi.android.ui.view.ILoadViewData;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

/**
 * {@link com.ushahidi.android.presenter.IPresenter} facilitates interactions between post list view
 * and post models.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ListPostPresenter implements IPresenter {

    private final PostEntityMapper mPostEntityMapper;

    private final PostModelDataMapper mPostModelDataMapper;

    private final PostDatabaseHelper mPostDatabaseHelper;

    private final ListPost mListPost;

    private final FetchPost mFetchPost;

    private final Prefs mPrefs;

    private final Search<Post> mSearch;

    private final ApiServiceUtil mApiServiceUtil;

    private final Context mContext;

    private final ListPost.Callback mListCallback = new ListPost.Callback() {

        @Override
        public void onPostListLoaded(List<Post> listPost) {
            showPostsListInView(listPost);
            hideViewLoading();
        }

        @Override
        public void onError(ErrorWrap error) {
            hideViewLoading();
            showErrorMessage(error);
            showViewRetry();
        }
    };

    private final FetchPost.Callback mCallback = new FetchPost.Callback() {

        @Override
        public void onPostFetched(List<Post> listPost) {
            showPostsListInView(listPost);
            hideViewLoading();
        }

        @Override
        public void onError(ErrorWrap error) {
            hideViewLoading();
            showErrorMessage(error);
            showViewRetry();
        }
    };

    private final Search.Callback<Post> mSearchCallback = new Search.Callback<Post>() {

        @Override
        public void onError(ErrorWrap error) {
            mView.showEmptySearchResultsInfo();
            showErrorMessage(error);
        }

        @Override
        public void onEntityFound(List<Post> entityList) {
            if(entityList.size() > 0) {
                showPostsListInView(entityList);
            } else {
                mView.showEmptySearchResultsInfo();
            }
        }
    };

    private View mView;

    @Inject
    public ListPostPresenter(ListPost listPost,
            Search<Post> search,
            FetchPost fetchPost,
            PostModelDataMapper postModelDataMapper, PostEntityMapper postEntityMapper,
            PostDatabaseHelper postDatabaseHelper,
            Prefs prefs,
            ApiServiceUtil apiServiceUtil,
            Context context
    ) {
        mListPost = Preconditions.checkNotNull(listPost, "ListPost cannot be null");
        mSearch = Preconditions.checkNotNull(search, "Search cannot be null");
        mFetchPost = Preconditions.checkNotNull(fetchPost, "Fetch Post listing");
        mPostModelDataMapper = Preconditions
                .checkNotNull(postModelDataMapper, "PostModelDataMapper cannot be null");
        mPostEntityMapper = Preconditions.checkNotNull(postEntityMapper,
                "Post entity mapper cannot be null");
        mPostDatabaseHelper = Preconditions
                .checkNotNull(postDatabaseHelper, "Post database helper cannot be null");
        mPrefs = Preconditions.checkNotNull(prefs, "Preferences cannot be null");
        mContext = context;
        mApiServiceUtil = apiServiceUtil;
        setPostService(createPostService());
    }

    public void setPostService(PostService postService) {
        PostDataSourceFactory postDataSourceFactory = new PostDataSourceFactory(mContext,
                mPostDatabaseHelper, postService);
        IPostRepository postRepository = PostDataRepository
                .getInstance(postDataSourceFactory, mPostEntityMapper);
        mListPost.setPostRepository(postRepository);
        mFetchPost.setPostRepository(postRepository);
        mSearch.setRepository(postRepository);
    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View cannot be null.");
        }
        mView = view;
    }

    public PostService createPostService() {
        return mApiServiceUtil.createService(PostService.class,
                mPrefs.getActiveDeploymentUrl().get(),
                mPrefs.getAccessToken().get());
    }

    @Override
    public void resume() {
        fetchPost();
    }

    @Override
    public void pause() {
        // Do nothing
    }

    public void init() {
        setPostService(createPostService());
        loadList();
    }

    private void loadList() {
        hideViewRetry();
        showViewLoading();
        getPostList();
    }

    public void refreshList() {
        getPostList();
    }

    public void onPostClicked(PostModel postModel) {
        mView.viewPost(postModel);
    }

    private void showViewLoading() {
        mView.showLoading();
    }

    private void hideViewLoading() {
        mView.hideLoading();
    }

    private void showViewRetry() {
        mView.showRetry();
    }

    private void hideViewRetry() {
        mView.hideRetry();
    }

    private void showErrorMessage(ErrorWrap errorWrap) {
        String errorMessage = ErrorMessageFactory.create(mView.getContext(),
                errorWrap.getException());
        mView.showError(errorMessage);
    }

    private void showPostsListInView(List<Post> listPosts) {
        final List<PostModel> postModelsList =
                mPostModelDataMapper.map(listPosts);
        mView.renderPostList(postModelsList);
    }

    private void getPostList() {
        mListPost.execute(mListCallback);
    }

    public void fetchPost() {
        setPostService(createPostService());
        mFetchPost.execute(mCallback);
    }

    public void search(String search) {
        mSearch.execute(search,mSearchCallback);
    }
    public interface View extends ILoadViewData {

        /**
         * Render a post list in the UI.
         *
         * @param postModel The collection of {@link com.ushahidi.android.model.PostModel} that will
         *                  be shown.
         */
        void renderPostList(List<PostModel> postModel);

        /**
         * View {@link com.ushahidi.android.model.PostModel} details
         *
         * @param postModel The post model to view
         */
        void viewPost(PostModel postModel);

        /**
         * Refreshes the existing items in the list view without any visual loaders
         */
        void showEmptySearchResultsInfo();

        void refreshList();
    }
}
