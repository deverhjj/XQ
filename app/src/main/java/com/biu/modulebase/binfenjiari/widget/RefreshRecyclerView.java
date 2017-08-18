package com.biu.modulebase.binfenjiari.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.biu.modulebase.binfenjiari.R;


/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/6
 */
public class RefreshRecyclerView extends FrameLayout {

    private SwipeRefreshLayout mSwiperefreshlayout;
    private RecyclerView mRecyclerView;
    private LinearLayout mLl_load_more;

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    private RefreshRecyclerView.Action mLoadmoreAction;

    private int mPageNum = 1;

    /**
     * 正在加载中，正在刷新
     */
    private boolean isRefreshing;

    /**
     * 正在加载中,更多加载
     */
    private boolean isLoadingMore;

    /**
     * 是否需要加载更多
     */
    private boolean loadMoreAble = true;

    /**
     * 是否需要下下一页
     */
    public boolean hasNextPage = true;


    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.base_recyclerview_refresh_layout, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mSwiperefreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
        mSwiperefreshlayout.setColorSchemeColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        mLl_load_more = (LinearLayout) view.findViewById(R.id.ll_load_more);
        mLl_load_more.setVisibility(View.GONE);

//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
//        boolean refreshAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_refresh_able, true);
//        loadMoreAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_load_more_able,true);
//        if(!refreshAble){
//            mSwipeRefreshLayout.setEnabled(false);
//        }

    }

    public SwipeRefreshLayout getSwiperefreshlayout() {
        return mSwiperefreshlayout;
    }


    public void setRefreshAction(final RefreshRecyclerView.Action action) {
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                mPageNum = 1;
                action.onAction(mPageNum);
            }
        };
        mSwiperefreshlayout.setOnRefreshListener(onRefreshListener);
    }

    public void setLoadMoreAction(final RefreshRecyclerView.Action action) {
        mLoadmoreAction = action;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        loadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("test", "onScrolled");
            }
        });
    }

    /**
     * 触发加载更多
     */
    public void loadMore() {
        if (mSwiperefreshlayout.isRefreshing())
            return;

        if (isRefreshing)
            return;

        if (isLoadingMore || !loadMoreAble)
            return;

        if (!hasNextPage) {
            Toast.makeText(getContext(), "已经到底了", Toast.LENGTH_SHORT).show();
            return;
        }

        isLoadingMore = true;
        mLl_load_more.setVisibility(View.VISIBLE);

        if (mLoadmoreAction != null) {
            mSwiperefreshlayout.setEnabled(false);
            mLoadmoreAction.onAction(mPageNum);
        }
    }

    public void setLoadMoreAble(boolean flag) {
        loadMoreAble = flag;
    }

    public void showSwipeRefresh() {
        mSwiperefreshlayout.setRefreshing(true);
        if (onRefreshListener != null)
            onRefreshListener.onRefresh();
    }

    public void dismissSwipeRefresh() {
        mSwiperefreshlayout.setRefreshing(false);
    }

    /**
     * 有下一页
     */
    public void showNextMore() {
        endPage();
        mPageNum++;
        hasNextPage = true;

        //子元素加载  撑满屏幕
        if (mRecyclerView.computeVerticalScrollExtent() < mRecyclerView.getHeight()) {
            loadMore();
        } else {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRecyclerView.computeVerticalScrollExtent() < mRecyclerView.getHeight()) {
                        loadMore();
                    }
                }
            }, 2500);
        }


    }

    /**
     * 有下一页
     */
    public void showNextMore(int curPage) {
        endPage();
        mPageNum = curPage + 1;
        hasNextPage = true;

        //子元素加载  撑满屏幕
        if (mRecyclerView.computeVerticalScrollExtent() < mRecyclerView.getHeight()) {
            loadMore();
        } else {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRecyclerView.computeVerticalScrollExtent() < mRecyclerView.getHeight()) {
                        loadMore();
                    }
                }
            }, 2500);
        }


    }

    /**
     * 没有下一页
     */
    public void showNoMore() {
        endPage();
        hasNextPage = false;

    }

    /**
     * 结束加载状态
     */
    public void endPage() {
        isRefreshing = false;
        isLoadingMore = false;
        dismissSwipeRefresh();
        mSwiperefreshlayout.setEnabled(true);
        mLl_load_more.setVisibility(View.GONE);

    }

    /**
     * 8位16进制数 ARGB
     */
    public void setSwipeRefreshColors(@ColorInt int... colors) {
        mSwiperefreshlayout.setColorSchemeColors(colors);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public static interface Action {
        void onAction(int page);
    }

}
