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

package com.ushahidi.android.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ushahidi.android.R;
import com.ushahidi.android.core.usecase.post.ListPost;
import com.ushahidi.android.model.PostModel;
import com.ushahidi.android.presenter.ListPostPresenter;
import com.ushahidi.android.ui.adapter.PostAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;

/**
 * Shows list of posts
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ListPostFragment extends BaseRecyclerViewFragment<PostModel, PostAdapter>
    implements
    ListPostPresenter.View, RecyclerView.OnItemTouchListener {

    @Inject
    ListPostPresenter mPostListPresenter;

    @InjectView(android.R.id.empty)
    TextView mEmptyView;

    private PostListListener mPostListListener;

    private GridLayoutManager mLinearLayoutManager;

    private GestureDetectorCompat mGestureDetector;

    private static ListPostFragment mListPostFragment;

    public ListPostFragment() {
        super(PostAdapter.class, R.layout.list_post, 0,
            android.R.id.list);
    }

    public static ListPostFragment newInstance() {
        if (mListPostFragment == null) {
            mListPostFragment = new ListPostFragment();
        }
        return mListPostFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof PostListListener) {
            mPostListListener = (PostListListener) activity;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        mPostListPresenter.init();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Calling this here because when called in the onResume method the activity doesn't
        // attached in time and causing getActivity() to return a null value.
        mPostListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPostListPresenter.pause();
    }

    @Override
    void initPresenter() {
        mPostListPresenter.setView(this);
    }

    private void setEmptyView() {
        if (mRecyclerViewAdapter != null && mRecyclerViewAdapter.getItemCount() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {
        mRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setEmptyView();
            }
        });
        mRecyclerView.addOnItemTouchListener(this);

        // Calculate the number of columns based on screen size.
        // Screen size of 300 - 500 will have 1 column and tablet size
        // will have 2 - 3 columns
        // Get the screen width in dip and divide by 300 (assuming it's the smallest possible screen
        // size for a phone ) to get the number of columns.
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = displayMetrics.widthPixels / density;
        //
        int columns = Math.round(dpWidth / 300);

        mLinearLayoutManager = new GridLayoutManager(getAppContext(), columns);
        mLinearLayoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mGestureDetector = new GestureDetectorCompat(getActivity(),
            new RecyclerViewOnGestureListener());

        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        setEmptyView();
    }

    public void search(String query) {
        mPostListPresenter.search(query);
    }

    @Override
    public void renderPostList(List<PostModel> postModel) {
        if (postModel != null && mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.setItems(postModel);
        }
    }

    @Override
    public void viewPost(PostModel postModel) {
        if (mPostListPresenter != null) {
            mPostListListener.onPostClicked(postModel);
        }
    }

    public void refreshLists() {
        if (mEmptyView != null) {
            mEmptyView.setText(R.string.empty_post_list);
        }

        if (mPostListPresenter != null) {
            mPostListPresenter.refreshList();
        }
    }

    public void refreshFromCache() {
        if (mPostListPresenter != null) {
            mPostListPresenter.loadfromLocalCache();
        }
    }

    @Override
    public void showEmptySearchResultsInfo() {
        if (mEmptyView != null) {
            mEmptyView.setText(R.string.post_not_found);
        }
        // setEmpty post list
        mRecyclerViewAdapter.setItems(new ArrayList<PostModel>());
        setEmptyView();
    }

    @Override
    public Context getAppContext() {
        return getActivity();
    }

    @Override
    public void showLoading() {
        mPostListListener.enableSwipeRefresh();
    }

    @Override
    public void hideLoading() {
        mPostListListener.hideSwipeRefresh();
    }

    @Override
    public void showRetry() {
        //TODO: implement this
    }

    @Override
    public void hideRetry() {
        //TODO: implement this
    }

    public boolean canCollectionViewScrollUp() {
        return ViewCompat.canScrollVertically(mRecyclerView, -1);
    }

    public void sortByDate() {
        if(mRecyclerViewAdapter !=null) {
            mRecyclerViewAdapter.sortByDate();
        }
    }

    public void sortByTitle() {
        if(mRecyclerViewAdapter !=null) {
            mRecyclerViewAdapter.sortByTitle();
        }
    }


    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return mLinearLayoutManager;
    }

    @Override
    public void showError(String message) {
        showToast(message);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    private void onItemClick(int position) {
        if (mRecyclerViewAdapter.getItemCount() > 0) {
            PostModel postModel = mRecyclerViewAdapter.getItem(position);
            viewPost(postModel);
        }
    }

    /**
     * Listens for post list events
     */
    public interface PostListListener {

        void onPostClicked(final PostModel postModel);

        void hideSwipeRefresh();

        void enableSwipeRefresh();
    }

    private class RecyclerViewOnGestureListener extends
        GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            onItemClick(mRecyclerView.getChildPosition(view));
            return super.onSingleTapConfirmed(e);
        }
    }
}
