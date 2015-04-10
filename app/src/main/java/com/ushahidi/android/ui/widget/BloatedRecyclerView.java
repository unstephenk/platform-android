/*
 *  Copyright (c) 2015 Ushahidi.
 *
 *   This program is free software: you can redistribute it and/or modify it under
 *   the terms of the GNU Affero General Public License as published by the Free
 *   Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 *   This program is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *   FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program in the file LICENSE-AGPL. If not, see
 *   https://www.gnu.org/licenses/agpl-3.0.html
 *
 */

package com.ushahidi.android.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ushahidi.android.R;
import com.ushahidi.android.ui.adapter.BaseRecyclerViewAdapter;
import com.ushahidi.android.ui.listener.ObservableScrollState;
import com.ushahidi.android.ui.listener.ObservableScrollViewListener;
import com.ushahidi.android.ui.listener.SwipeToDismissTouchListener;

import timber.log.Timber;

/**
 * This extends the base {@link android.support.v7.widget.RecyclerView} to encapsulate
 * swipe to dismiss, endless scroll, pluggable animations, item decorators, FAB and Parallax effect
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class BloatedRecyclerView extends FrameLayout {
    public RecyclerView recyclerView;
    public int showLoadMoreItemNum = 3;
    protected MovableFab floatingActionButton;
    protected RecyclerView.OnScrollListener mOnScrollListener;
    protected LAYOUT_MANAGER_TYPE layoutManagerType;
    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected boolean mClipToPadding;
    protected ViewStub mEmpty;
    protected View mEmptyView;
    protected int mEmptyId;
    protected ViewStub mFloatingButtonViewStub;
    protected View mFloatingButtonView;
    protected int mFloatingButtonId;
    private VerticalSwipeRefreshLayout mSwipeRefreshLayout;
    private OnLoadMoreListener onLoadMoreListener;
    private int lastVisibleItemPosition;
    private boolean isLoadingMore = false;
    private int currentScrollState = 0;
    private BaseRecyclerViewAdapter mAdapter;
    private ObservableScrollState mObservableScrollState;
    private ObservableScrollViewListener mCallbacks;
    private SparseIntArray mChildrenHeights = new SparseIntArray();
    private int mPrevFirstVisiblePosition;
    private int mPrevFirstVisibleChildHeight = -1;
    private int mPrevScrolledChildrenHeight;
    private int mPrevScrollY;
    private int mScrollY;
    private boolean mFirstScroll;
    private boolean mDragging;
    private static final int SCROLLBARS_NONE = 0;
    private static final int SCROLLBARS_VERTICAL = 1;
    private static final int SCROLLBARS_HORIZONTAL = 2;
    private int mScrollbarsStyle;

    public BloatedRecyclerView(Context context) {
        super(context);
        initViews();
    }

    public BloatedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initViews();
    }

    public BloatedRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initViews();
    }

    protected void initViews() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bloated_recyclerview, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.bloated_recycleview_list);
        mSwipeRefreshLayout = (VerticalSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        setScrollbars();
        mSwipeRefreshLayout.setEnabled(false);

        if (recyclerView != null) {

            recyclerView.setClipToPadding(mClipToPadding);
            if (mPadding != -1.1f) {
                recyclerView.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                recyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
        }

        floatingActionButton = (MovableFab) view.findViewById(R.id.bloated_recycleview_fab);
        setDefaultScrollListener();

        mEmpty = (ViewStub) view.findViewById(R.id.bloated_recycleview_empty_view);
        mFloatingButtonViewStub = (ViewStub) view.findViewById(R.id.bloated_recycleview_view_stub);

        mEmpty.setLayoutResource(mEmptyId);

        mFloatingButtonViewStub.setLayoutResource(mFloatingButtonId);

        if (mEmptyId != 0) {
            mEmptyView = mEmpty.inflate();
        }
        mEmpty.setVisibility(View.GONE);

        if (mFloatingButtonId != 0) {
            mFloatingButtonView = mFloatingButtonViewStub.inflate();
            mFloatingButtonView.setVisibility(View.VISIBLE);
        }
    }

    private void setScrollbars() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (mScrollbarsStyle) {
            case SCROLLBARS_VERTICAL:
                mSwipeRefreshLayout.removeView(recyclerView);
                View verticalView = inflater.inflate(R.layout.bloated_recyclerview_vertical_scroll, mSwipeRefreshLayout, true);
                recyclerView = (RecyclerView) verticalView.findViewById(R.id.bloated_recycleview_list);
                break;
            case SCROLLBARS_HORIZONTAL:
                mSwipeRefreshLayout.removeView(recyclerView);
                View horizontalView = inflater.inflate(R.layout.bloated_recyclerview_horizontal_scroll, mSwipeRefreshLayout, true);
                recyclerView = (RecyclerView) horizontalView.findViewById(R.id.bloated_recycleview_list);
                break;
            default:
                break;
        }
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BloatedRecyclerView);

        try {
            mPadding = (int) typedArray.getDimension(R.styleable.BloatedRecyclerView_recyclerviewPadding, -1.1f);
            mPaddingTop = (int) typedArray.getDimension(R.styleable.BloatedRecyclerView_recyclerviewPaddingTop, 0.0f);
            mPaddingBottom = (int) typedArray.getDimension(R.styleable.BloatedRecyclerView_recyclerviewPaddingBottom, 0.0f);
            mPaddingLeft = (int) typedArray.getDimension(R.styleable.BloatedRecyclerView_recyclerviewPaddingLeft, 0.0f);
            mPaddingRight = (int) typedArray.getDimension(R.styleable.BloatedRecyclerView_recyclerviewPaddingRight, 0.0f);
            mClipToPadding = typedArray.getBoolean(R.styleable.BloatedRecyclerView_recyclerviewClipToPadding, false);
            mEmptyId = typedArray.getResourceId(R.styleable.BloatedRecyclerView_recyclerviewEmptyView, 0);
            mFloatingButtonId = typedArray.getResourceId(R.styleable.BloatedRecyclerView_recyclerviewFloatingActionView, 0);
            mScrollbarsStyle = typedArray.getInt(R.styleable.BloatedRecyclerView_recyclerviewScrollbars, SCROLLBARS_NONE);
        } finally {

            typedArray.recycle();
        }
    }

    /**
     * Set a swipe-to-dismiss OnItemTouchListener for RecyclerView
     *
     * @param dismissCallbacks
     */
    public void setSwipeToDismissCallback(SwipeToDismissTouchListener.DismissCallbacks dismissCallbacks) {
        int[] notToDismiss = null;
        if (mAdapter.getCustomHeaderView() != null) {
            notToDismiss = new int[]{
                0
            };
        }

        recyclerView.addOnItemTouchListener(new SwipeToDismissTouchListener(recyclerView, dismissCallbacks));
    }

    void setDefaultScrollListener() {
        mOnScrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mHeader != null) {
                    mTotalYScrolled += dy;
                    translateHeader(mTotalYScrolled);
                }
                enableShoworHideToolbarAndFloatingButton(recyclerView);
            }
        };
        recyclerView.setOnScrollListener(mOnScrollListener);
    }

    /**
     * Enable loading more of the recyclerview
     */
    public void enableInfiniteScroll() {
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            private int[] lastPositions;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mHeader != null) {
                    mTotalYScrolled += dy;
                    translateHeader(mTotalYScrolled);
                }
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                if (layoutManagerType == null) {
                    if (layoutManager instanceof LinearLayoutManager) {
                        layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
                    } else if (layoutManager instanceof GridLayoutManager) {
                        layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
                    } else {
                        throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                    }
                }

                switch (layoutManagerType) {
                    case LINEAR:
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        break;
                    case GRID:
                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                        break;
                    case STAGGERED_GRID:
                        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                        if (lastPositions == null)
                            lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];

                        staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                        lastVisibleItemPosition = findMax(lastPositions);
                        break;
                }
                enableShoworHideToolbarAndFloatingButton(recyclerView);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                currentScrollState = newState;
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE &&
                    (lastVisibleItemPosition) >= totalItemCount - 1) && !isLoadingMore) {
                    isLoadingMore = true;
                    if (onLoadMoreListener != null) {
                        isLoadingMore = false;
                        onLoadMoreListener.loadMore(BloatedRecyclerView.this.recyclerView.getAdapter().getItemCount(), lastVisibleItemPosition);
                    }
                }

            }
        };
        recyclerView.setOnScrollListener(mOnScrollListener);
        if (mAdapter.getInfiniteScrollView() == null)
            mAdapter.setInfiniteScrollView(LayoutInflater.from(getContext())
                .inflate(R.layout.bloated_recyclerview_bottom_progressbar, null));
    }

    public void disableInfiniteScroll() {
        setDefaultScrollListener();
        mAdapter.setInfiniteScrollView(LayoutInflater.from(getContext())
            .inflate(R.layout.bloated_recyclerview_empty_progressbar, null));
    }

    public View getmEmptyView() {
        return mEmptyView;
    }

    public ViewStub getEmptyViewSub() {
        return mEmpty;
    }

    protected void enableShoworHideToolbarAndFloatingButton(RecyclerView recyclerView) {
        if (mCallbacks != null) {
            if (getChildCount() > 0) {
                int firstVisiblePosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0));
                int lastVisiblePosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(getChildCount() - 1));
                for (int i = firstVisiblePosition, j = 0; i <= lastVisiblePosition; i++, j++) {
                    if (mChildrenHeights.indexOfKey(i) < 0 || recyclerView.getChildAt(j).getHeight() != mChildrenHeights.get(i)) {
                        mChildrenHeights.put(i, recyclerView.getChildAt(j).getHeight());
                    }
                }

                View firstVisibleChild = recyclerView.getChildAt(0);
                if (firstVisibleChild != null) {
                    if (mPrevFirstVisiblePosition < firstVisiblePosition) {
                        // scroll down
                        int skippedChildrenHeight = 0;
                        if (firstVisiblePosition - mPrevFirstVisiblePosition != 1) {
                            for (int i = firstVisiblePosition - 1; i > mPrevFirstVisiblePosition; i--) {
                                if (0 < mChildrenHeights.indexOfKey(i)) {
                                    skippedChildrenHeight += mChildrenHeights.get(i);
                                } else {
                                    // Approximate each item's height to the first visible child.
                                    // It may be incorrect, but without this, scrollY will be broken
                                    // when scrolling from the bottom.
                                    skippedChildrenHeight += firstVisibleChild.getHeight();
                                }
                            }
                        }
                        mPrevScrolledChildrenHeight += mPrevFirstVisibleChildHeight + skippedChildrenHeight;
                        mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                    } else if (firstVisiblePosition < mPrevFirstVisiblePosition) {
                        // scroll up
                        int skippedChildrenHeight = 0;
                        if (mPrevFirstVisiblePosition - firstVisiblePosition != 1) {
                            for (int i = mPrevFirstVisiblePosition - 1; i > firstVisiblePosition; i--) {
                                if (0 < mChildrenHeights.indexOfKey(i)) {
                                    skippedChildrenHeight += mChildrenHeights.get(i);
                                } else {
                                    // Approximate each item's height to the first visible child.
                                    // It may be incorrect, but without this, scrollY will be broken
                                    // when scrolling from the bottom.
                                    skippedChildrenHeight += firstVisibleChild.getHeight();
                                }
                            }
                        }
                        mPrevScrolledChildrenHeight -= firstVisibleChild.getHeight() + skippedChildrenHeight;
                        mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                    } else if (firstVisiblePosition == 0) {
                        mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                    }
                    if (mPrevFirstVisibleChildHeight < 0) {
                        mPrevFirstVisibleChildHeight = 0;
                    }
                    mScrollY = mPrevScrolledChildrenHeight - firstVisibleChild.getTop();
                    mPrevFirstVisiblePosition = firstVisiblePosition;

                    mCallbacks.onScrollChanged(mScrollY, mFirstScroll, mDragging);
                    if (mFirstScroll) {
                        mFirstScroll = false;
                    }

                    if (mPrevScrollY < mScrollY) {
                        //down
                        mObservableScrollState = ObservableScrollState.UP;
                    } else if (mScrollY < mPrevScrollY) {
                        //up
                        mObservableScrollState = ObservableScrollState.DOWN;
                    } else {
                        mObservableScrollState = ObservableScrollState.STOP;
                    }
                    mPrevScrollY = mScrollY;
                }
            }

        }
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener customOnScrollListener) {
        recyclerView.setOnScrollListener(customOnScrollListener);
    }

    public void addItemDividerDecoration(Context context) {
        RecyclerView.ItemDecoration itemDecoration =
            new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * Add an {@link RecyclerView.ItemDecoration} to this RecyclerView. Item decorations can affect both measurement and drawing of individual item views.
     * <p>Item decorations are ordered. Decorations placed earlier in the list will be run/queried/drawn first for their effects on item views. Padding added to views will be nested; a padding added by an earlier decoration will mean further item decorations in the list will be asked to draw/pad within the previous decoration's given area.</p>
     *
     * @param itemDecoration Decoration to add
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * Add an {@link RecyclerView.ItemDecoration} to this RecyclerView. Item decorations can affect both measurement and drawing of individual item views.
     * <p>Item decorations are ordered. Decorations placed earlier in the list will be run/queried/drawn first for their effects on item views. Padding added to views will be nested; a padding added by an earlier decoration will mean further item decorations in the list will be asked to draw/pad within the previous decoration's given area.</p>
     *
     * @param itemDecoration Decoration to add
     * @param index          Position in the decoration chain to insert this decoration at. If this value is negative the decoration will be added at the end.
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        recyclerView.addItemDecoration(itemDecoration, index);
    }

    /**
     * Sets the {@link RecyclerView.ItemAnimator} that will handle animations involving changes
     * to the items in this RecyclerView. By default, RecyclerView instantiates and
     * uses an instance of {@link android.support.v7.widget.DefaultItemAnimator}. Whether item animations are
     * enabled for the RecyclerView depends on the ItemAnimator and whether
     * the LayoutManager {@link android.support.v7.widget.RecyclerView.LayoutManager#supportsPredictiveItemAnimations()
     * supports item animations}.
     *
     * @param animator The ItemAnimator being set. If null, no animations will occur
     *                 when changes occur to the items in this RecyclerView.
     */
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        recyclerView.setItemAnimator(animator);
    }

    /**
     * Gets the current ItemAnimator for this RecyclerView. A null return value
     * indicates that there is no animator and that item changes will happen without
     * any animations. By default, RecyclerView instantiates and
     * uses an instance of {@link android.support.v7.widget.DefaultItemAnimator}.
     *
     * @return ItemAnimator The current ItemAnimator. If null, no animations will occur
     * when changes occur to the items in this RecyclerView.
     */
    public RecyclerView.ItemAnimator getItemAnimator() {
        return recyclerView.getItemAnimator();
    }

    /**
     * Set the listener when refresh is triggered and enable the SwipeRefreshLayout
     *
     * @param listener
     */
    public void setDefaultOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {

        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_start,
            R.color.refresh_progress_center,
            R.color.refresh_progress_end);

        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }

    /**
     * Set the load more listener of recyclerview
     *
     * @param onLoadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    /**
     * Set the layout manager to the recycler
     *
     * @param manager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        recyclerView.setLayoutManager(manager);
    }

    /**
     * Get the adapter of BloatedRecyclerView
     *
     * @return
     */
    public RecyclerView.Adapter getAdapter() {
        return recyclerView.getAdapter();
    }

    /**
     * Set a BaseRecyclerViewAdapter or the subclass of BaseRecyclerViewAdapter to the recyclerview
     *
     * @param adapter
     */
    public void setAdapter(BaseRecyclerViewAdapter adapter) {
        mAdapter = adapter;
        recyclerView.setAdapter(mAdapter);
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                update();
            }

            @Override
            public void onChanged() {
                super.onChanged();
                update();
            }

            private void update() {
                isLoadingMore = false;
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                if (recyclerView.getAdapter().getItemCount() == 0 && mEmptyId != 0) {
                    mEmpty.setVisibility(View.VISIBLE);
                } else if (mEmptyId != 0) {
                    mEmpty.setVisibility(View.GONE);
                }
                if (mAdapter.getItemCount() >= showLoadMoreItemNum && mAdapter.getInfiniteScrollView() != null
                    && mAdapter.getInfiniteScrollView().getVisibility() == View.GONE) {
                    mAdapter.getInfiniteScrollView().setVisibility(View.VISIBLE);
                }
                if (mAdapter.getItemCount() < showLoadMoreItemNum && mAdapter.getInfiniteScrollView() != null) {
                    mAdapter.getInfiniteScrollView().setVisibility(View.GONE);
                }
            }

        });
        if ((adapter == null || adapter.getItemCount() == 0) && mEmptyId != 0) {
            mEmpty.setVisibility(View.VISIBLE);
        }

    }

    public void setHasFixedSize(boolean hasFixedSize) {
        recyclerView.setHasFixedSize(hasFixedSize);
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when refresh is triggered by a swipe gesture.
     *
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(refreshing);
    }


    /**
     * Enable or disable the SwipeRefreshLayout.
     * Default is false
     *
     * @param isSwipeRefresh
     */
    public void enableDefaultSwipeRefresh(boolean isSwipeRefresh) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(isSwipeRefresh);
        }
    }


    public interface OnLoadMoreListener {
        void loadMore(int itemsCount, int maxLastVisiblePosition);
    }

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    private int findMax(int[] lastPositions) {
        int max = Integer.MIN_VALUE;
        for (int value : lastPositions) {
            if (value > max)
                max = value;
        }
        return max;
    }

    private CustomRelativeWrapper mHeader;
    private int mTotalYScrolled;
    private final float SCROLL_MULTIPLIER = 0.5f;
    private OnParallaxScroll mParallaxScroll;

    /**
     * Set the parallax header of the recyclerview
     *
     * @param header
     */
    public void setParallaxHeader(View header) {
        mHeader = new CustomRelativeWrapper(header.getContext());
        mHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeader.addView(header, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mAdapter.setCustomHeaderView(mHeader);
    }

    /**
     * Set the on scroll method of parallax header
     *
     * @param parallaxScroll
     */
    public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
        mParallaxScroll = parallaxScroll;
        mParallaxScroll.onParallaxScroll(0, 0, mHeader);
    }

    private void translateHeader(float of) {
        float ofCalculated = of * SCROLL_MULTIPLIER;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mHeader.setTranslationY(ofCalculated);
        } else {
            TranslateAnimation anim = new TranslateAnimation(0, 0, ofCalculated, ofCalculated);
            anim.setFillAfter(true);
            anim.setDuration(0);
            mHeader.startAnimation(anim);
        }
        mHeader.setClipY(Math.round(ofCalculated));
        if (mParallaxScroll != null) {
            float left = Math.min(1, ((ofCalculated) / (mHeader.getHeight() * SCROLL_MULTIPLIER)));
            mParallaxScroll.onParallaxScroll(left, of, mHeader);
        }
    }

    public interface OnParallaxScroll {
        void onParallaxScroll(float percentage, float offset, View parallax);
    }

    /**
     * Custom layout for the Parallax Header.
     */
    public static class CustomRelativeWrapper extends RelativeLayout {

        private int mOffset;

        public CustomRelativeWrapper(Context context) {
            super(context);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
            super.dispatchDraw(canvas);
        }

        public void setClipY(int offset) {
            mOffset = offset;
            invalidate();
        }
    }

    public void setScrollViewCallbacks(ObservableScrollViewListener listener) {
        mCallbacks = listener;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        mPrevFirstVisiblePosition = ss.prevFirstVisiblePosition;
        mPrevFirstVisibleChildHeight = ss.prevFirstVisibleChildHeight;
        mPrevScrolledChildrenHeight = ss.prevScrolledChildrenHeight;
        mPrevScrollY = ss.prevScrollY;
        mScrollY = ss.scrollY;
        mChildrenHeights = ss.childrenHeights;
        super.onRestoreInstanceState(ss.getSuperState());
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.prevFirstVisiblePosition = mPrevFirstVisiblePosition;
        ss.prevFirstVisibleChildHeight = mPrevFirstVisibleChildHeight;
        ss.prevScrolledChildrenHeight = mPrevScrolledChildrenHeight;
        ss.prevScrollY = mPrevScrollY;
        ss.scrollY = mScrollY;
        ss.childrenHeights = mChildrenHeights;
        return ss;
    }

    /**
     * This saved state class is a Parcelable and should not extend
     * {@link android.view.View.BaseSavedState} nor {@link android.view.AbsSavedState}
     * because its super class AbsSavedState's constructor
     * {@link android.view.AbsSavedState#AbsSavedState(android.os.Parcel)} currently passes null
     * as a class loader to read its superstate from Parcelable.
     * This causes {@link android.os.BadParcelableException} when restoring saved states.
     * <p/>
     * The super class "RecyclerView" is a part of the support library,
     * and restoring its saved state requires the class loader that loaded the RecyclerView.
     * It seems that the class loader is not required when restoring from RecyclerView itself,
     * but it is required when restoring from RecyclerView's subclasses.
     */
    static class SavedState implements Parcelable {
        public static final SavedState EMPTY_STATE = new SavedState() {
        };

        int prevFirstVisiblePosition;
        int prevFirstVisibleChildHeight = -1;
        int prevScrolledChildrenHeight;
        int prevScrollY;
        int scrollY;
        SparseIntArray childrenHeights;

        // This keeps the parent(RecyclerView)'s state
        Parcelable superState;

        /**
         * Called by EMPTY_STATE instantiation.
         */
        private SavedState() {
            superState = null;
        }

        /**
         * Called by onSaveInstanceState.
         */
        SavedState(Parcelable superState) {
            this.superState = superState != EMPTY_STATE ? superState : null;
        }

        /**
         * Called by CREATOR.
         */
        private SavedState(Parcel in) {
            // Parcel 'in' has its parent(RecyclerView)'s saved state.
            // To restore it, class loader that loaded RecyclerView is required.
            Parcelable superState = in.readParcelable(RecyclerView.class.getClassLoader());
            this.superState = superState != null ? superState : EMPTY_STATE;

            prevFirstVisiblePosition = in.readInt();
            prevFirstVisibleChildHeight = in.readInt();
            prevScrolledChildrenHeight = in.readInt();
            prevScrollY = in.readInt();
            scrollY = in.readInt();
            childrenHeights = new SparseIntArray();
            final int numOfChildren = in.readInt();
            if (0 < numOfChildren) {
                for (int i = 0; i < numOfChildren; i++) {
                    final int key = in.readInt();
                    final int value = in.readInt();
                    childrenHeights.put(key, value);
                }
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeParcelable(superState, flags);

            out.writeInt(prevFirstVisiblePosition);
            out.writeInt(prevFirstVisibleChildHeight);
            out.writeInt(prevScrolledChildrenHeight);
            out.writeInt(prevScrollY);
            out.writeInt(scrollY);
            final int numOfChildren = childrenHeights == null ? 0 : childrenHeights.size();
            out.writeInt(numOfChildren);
            if (0 < numOfChildren) {
                for (int i = 0; i < numOfChildren; i++) {
                    out.writeInt(childrenHeights.keyAt(i));
                    out.writeInt(childrenHeights.valueAt(i));
                }
            }
        }

        public Parcelable getSuperState() {
            return superState;
        }

        public static final Parcelable.Creator<SavedState> CREATOR
            = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (mCallbacks != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mFirstScroll = mDragging = true;
                    mCallbacks.onDownMotionEvent();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    //mIntercepted = false;
                    mDragging = false;
                    mCallbacks.onUpOrCancelMotionEvent(mObservableScrollState);
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Timber.d("mCallbacks   " + (mCallbacks == null));
        if (mCallbacks != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    //mIntercepted = false;
                    mDragging = false;
                    mCallbacks.onUpOrCancelMotionEvent(mObservableScrollState);
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    public boolean toolbarIsShown(Toolbar mToolbar) {
        return mToolbar.getTranslationY() == 0;
    }

    public boolean toolbarIsHidden(Toolbar mToolbar) {
        return mToolbar.getTranslationY() == -mToolbar.getHeight();
    }

    public void showToolbar(Toolbar mToolbar, BloatedRecyclerView recyclerView, int screenHeight) {
        moveToolbar(mToolbar, recyclerView, screenHeight, 0);
    }

    public void hideToolbar(Toolbar mToolbar, BloatedRecyclerView recyclerView, int screenHeight) {
        moveToolbar(mToolbar, recyclerView, screenHeight, -mToolbar.getHeight());
    }

    protected void moveToolbar(final Toolbar mToolbar, final BloatedRecyclerView recyclerView, final int screenHeight, float toTranslationY) {
        if (mToolbar.getTranslationY() == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(mToolbar.getTranslationY(), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                mToolbar.setTranslationY(translationY);

                MarginLayoutParams layoutParams = (MarginLayoutParams) ((View) recyclerView).getLayoutParams();
                layoutParams.height = (int) (-translationY + screenHeight - layoutParams.topMargin);
                ((View) recyclerView).requestLayout();
            }
        });
        animator.start();
    }


    public MovableFab getDefaultFloatingActionButton() {
        return floatingActionButton;
    }

    public void setDefaultFloatingActionButton(MovableFab floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }

    public void showFloatingActionButton() {
        if (mFloatingButtonView != null)
            ((MovableFab) mFloatingButtonView).hide(false);
    }

    public void hideFloatingActionButton() {
        if (mFloatingButtonView != null) ((MovableFab) mFloatingButtonView).hide(true);
    }

    public void showDefaultFloatingActionButton() {
        floatingActionButton.hide(false);
    }

    public void hideDefaultFloatingActionButton() {
        floatingActionButton.hide(true);
    }

    /**
     * Displays or hides a custom FAB view
     *
     * @param visibilityState
     */
    public void displayCustomFloatingActionView(boolean visibilityState) {
        if (mFloatingButtonView != null)
            mFloatingButtonView.setVisibility(visibilityState ? VISIBLE : INVISIBLE);
    }

    /**
     * Displays or hides a default FAB
     *
     * @param visibilityState
     */
    public void displayDefaultFloatingActionButton(boolean visibilityState) {
        floatingActionButton.setVisibility(visibilityState ? VISIBLE : INVISIBLE);
    }
}
