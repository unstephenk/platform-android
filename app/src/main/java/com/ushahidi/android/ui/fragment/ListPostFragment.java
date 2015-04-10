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
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;
import com.ushahidi.android.R;
import com.ushahidi.android.model.PostModel;
import com.ushahidi.android.presenter.ListPostPresenter;
import com.ushahidi.android.ui.adapter.PostAdapter;
import com.ushahidi.android.ui.animators.FadeInAnimator;
import com.ushahidi.android.ui.listener.ObservableScrollState;
import com.ushahidi.android.ui.listener.ObservableScrollViewListener;
import com.ushahidi.android.ui.listener.RecyclerViewItemTouchListenerAdapter;
import com.ushahidi.android.ui.widget.MovableFab;

import java.util.ArrayList;
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
    ListPostPresenter.View, RecyclerViewItemTouchListenerAdapter.RecyclerViewOnItemClickListener {

    @Inject
    ListPostPresenter mPostListPresenter;

    TextView mEmptyView;

    @InjectView(R.id.list_post_progress_bar)
    ProgressBar mProgressBar;

    MovableFab mPostFab;

    private static final String IS_REFERESHING = "refreshing";

    private boolean isRefreshing = false;

    private PostListListener mPostListListener;

    private static ListPostFragment mListPostFragment;

    private RecyclerViewItemTouchListenerAdapter mRecyclerViewItemTouchListenerAdapter;

    private LinearLayoutManager mLinearLayoutManager;

    public ListPostFragment() {
        super(PostAdapter.class, R.layout.list_post, R.menu.list_post,
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
        mPostListPresenter.init();
        initRecyclerView();
        if (savedInstanceState != null) {
            isRefreshing = savedInstanceState.getBoolean(IS_REFERESHING, false);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_REFERESHING, isRefreshing);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPostListPresenter.isRefreshing = isRefreshing;
        mPostListPresenter.resume();
        if (isRefreshing) {
            refreshLists();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_refresh_post) {

            refreshWithSwipeToRefresh();
            return true;
        } else if (id == R.id.menu_sort_by_title) {
            sortByTitle();

            return true;
        } else if (id == R.id.menu_sort_by_date) {

            mListPostFragment.sortByDate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fetches post via the API with SwipeRefresh enabled.
     */
    private void refreshWithSwipeToRefresh() {
        mBloatedRecyclerView.setRefreshing(true);
        isRefreshing = true;
        refreshLists();
    }

    private void setEmptyView() {
        mEmptyView = (TextView) mBloatedRecyclerView.getmEmptyView().findViewById(android.R.id.empty);
    }

    private void initRecyclerView() {
        mRecyclerViewItemTouchListenerAdapter = new RecyclerViewItemTouchListenerAdapter(
            mBloatedRecyclerView.recyclerView, this);

        mPostFab = mBloatedRecyclerView.getDefaultFloatingActionButton();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mBloatedRecyclerView.setAdapter(mRecyclerViewAdapter);
        mBloatedRecyclerView.setItemAnimator(new FadeInAnimator());
        mBloatedRecyclerView.addItemDividerDecoration(getActivity());
        mBloatedRecyclerView.displayDefaultFloatingActionButton(true);
        mBloatedRecyclerView.recyclerView.addOnItemTouchListener(mRecyclerViewItemTouchListenerAdapter);

        // Upon  successful refresh, disable swipe to refresh
        mBloatedRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLists();
                mLinearLayoutManager.scrollToPosition(0);
            }
        });

        // Show or hide Toolbar and FAB upon scrolling
        mBloatedRecyclerView.setScrollViewCallbacks(new ObservableScrollViewListener() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                // Do nothing
            }

            @Override
            public void onDownMotionEvent() {
                // Do nothing
            }

            @Override
            public void onUpOrCancelMotionEvent(ObservableScrollState observableScrollState) {
                if (observableScrollState == ObservableScrollState.DOWN) {
                    mBloatedRecyclerView.hideDefaultFloatingActionButton();
                } else if (observableScrollState == ObservableScrollState.UP) {
                    mBloatedRecyclerView.showDefaultFloatingActionButton();
                }
            }
        });
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
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(true);
    }

    @Override
    public void hideLoading() {
        mBloatedRecyclerView.setRefreshing(false);
        isRefreshing = false;
        mLinearLayoutManager.scrollToPosition(0);
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.setIndeterminate(false);
    }

    @Override
    public void showRetry(String message) {
        SnackbarManager.show(Snackbar.with(getActivity())
            .type(SnackbarType.MULTI_LINE)
            .text(message)
            .actionLabel(getActivity().getString(R.string.retry))
            .actionColorResource(R.color.undo_text_color)
            .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
            .actionListener(new ActionClickListener() {
                @Override
                public void onActionClicked(Snackbar snackbar) {
                    refreshWithSwipeToRefresh();
                }
            })
            .eventListener(new EventListener() {
                @Override
                public void onShow(Snackbar snackbar) {
                    mPostFab.moveUp(snackbar.getHeight());
                }

                @Override
                public void onShowByReplace(Snackbar snackbar) {
                    // Do nothing
                }

                @Override
                public void onShown(Snackbar snackbar) {
                    // Do nothing
                }

                @Override
                public void onDismiss(Snackbar snackbar) {
                    mPostFab.moveDown(0);
                }

                @Override
                public void onDismissByReplace(Snackbar snackbar) {
                    // Do nothing
                }

                @Override
                public void onDismissed(Snackbar snackbar) {

                }
            }));
    }

    public boolean canCollectionViewScrollUp() {
        return ViewCompat.canScrollVertically(mBloatedRecyclerView, -1);
    }

    public void sortByDate() {
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.sortByDate();
        }
    }

    public void sortByTitle() {
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.sortByTitle();
        }
    }

    @Override
    public void showError(String message) {
        SnackbarManager.show(Snackbar.with(getActivity())
            .type(SnackbarType.MULTI_LINE)
            .text(message)
            .actionLabel(getActivity().getString(R.string.retry))
            .actionColorResource(R.color.undo_text_color)
            .attachToRecyclerView(mBloatedRecyclerView.recyclerView)
            .eventListener(new EventListener() {
                @Override
                public void onShow(Snackbar snackbar) {
                    mPostFab.moveUp(snackbar.getHeight());
                }

                @Override
                public void onShowByReplace(Snackbar snackbar) {

                }

                @Override
                public void onShown(Snackbar snackbar) {

                }

                @Override
                public void onDismiss(Snackbar snackbar) {
                    mPostFab.moveDown(0);
                }

                @Override
                public void onDismissByReplace(Snackbar snackbar) {
                }

                @Override
                public void onDismissed(Snackbar snackbar) {
                    // Do nothing
                }
            }));
    }

    private void onItemClick(int position) {
        if (mRecyclerViewAdapter.getItemCount() > 0) {
            PostModel postModel = mRecyclerViewAdapter.getItem(position);
            viewPost(postModel);
        }
    }

    @Override
    public void onItemClick(RecyclerView parent, View clickedView, int position) {
        onItemClick(position);
    }

    @Override
    public void onItemLongClick(RecyclerView parent, View clickedView, int position) {
        //Do nothing
    }

    /**
     * Listens for post list events
     */
    public interface PostListListener {
        void onPostClicked(final PostModel postModel);
    }

}
