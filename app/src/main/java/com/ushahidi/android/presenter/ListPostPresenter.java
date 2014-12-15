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

import com.ushahidi.android.core.entity.Post;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.post.FetchPost;
import com.ushahidi.android.core.usecase.post.ListPost;
import com.ushahidi.android.exception.ErrorMessageFactory;
import com.ushahidi.android.model.PostModel;
import com.ushahidi.android.model.mapper.PostModelDataMapper;
import com.ushahidi.android.ui.view.ILoadViewData;

import java.util.List;

import javax.inject.Inject;

/**
 * {@link com.ushahidi.android.presenter.IPresenter} facilitates interactions between post list view
 * and post models.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ListPostPresenter implements IPresenter {

    private final PostModelDataMapper mPostModelDataMapper;

    private final ListPost mListPost;

    private final FetchPost mFetchPost;

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

    private View mView;

    @Inject
    public ListPostPresenter(ListPost listPost,
            FetchPost fetchPost,
            PostModelDataMapper postModelDataMapper) {
        mListPost = Preconditions.checkNotNull(listPost, "ListPost cannot be null");
        mFetchPost = Preconditions.checkNotNull(fetchPost, "Fetch Post listing");
        mPostModelDataMapper = Preconditions
                .checkNotNull(postModelDataMapper, "PostModelDataMapper cannot be null");
    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View cannot be null.");
        }
        mView = view;
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
        mFetchPost.execute(mCallback);
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
        void refreshList();
    }
}
