package com.binfenjiari.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.binfenjiari.utils.Views;


/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/3/22
 * <br>Email: developer.huajianjiang@gmail.com
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    /**
     * 是否第一次页面加载
     */
    private boolean mFirstVisible = true;

    private View mContentView;
    private View mLoadingView;
    private View mEmptyView;

    @SuppressWarnings("unused")
    public View onGenerateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState)
    {
        return null;
    }

    @SuppressWarnings("unused")
    public void onInitView(View root) {
    }

    public void setLoadingView(int resId) {
        setLoadingView(LayoutInflater.from(getContext()).inflate(resId, null));
    }

    public void setEmptyView(int resId) {
        setEmptyView(LayoutInflater.from(getContext()).inflate(resId, null));
    }

    public View getContentView() {
        return mContentView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public BaseActivity getAssociateActivity() {
        return (BaseActivity) getActivity();
    }

    /**
     * 插入一些全局 UI 界面, eg: loading dialog, data loading error, net error view 等等
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 一个包含插入了全局UI界面的内容视图层级 root UserView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        mContentView = onGenerateView(inflater, container, savedInstanceState);
        View result = mContentView;
        if (mContentView != null) {
            if (mEmptyView != null || mLoadingView != null) {
                if (mContentView instanceof ScrollView ||
                    mContentView instanceof HorizontalScrollView ||
                    mContentView instanceof NestedScrollView ||
                    !(mContentView instanceof FrameLayout))
                {
                    result = new FrameLayout(getContext());
                    result.setLayoutParams(
                            new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                                         FrameLayout.LayoutParams.MATCH_PARENT));
                    ((FrameLayout) result).addView(mContentView,
                                                   FrameLayout.LayoutParams.MATCH_PARENT,
                                                   FrameLayout.LayoutParams.MATCH_PARENT);// 插入内容 UserView
                }
                FrameLayout rootView = (FrameLayout) result;
                if (mEmptyView != null) {
                    mEmptyView.setVisibility(View.GONE);
                    rootView.addView(mEmptyView, FrameLayout.LayoutParams.MATCH_PARENT,
                                     FrameLayout.LayoutParams.MATCH_PARENT);// 插入空 UserView
                }
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.GONE);
                    rootView.addView(mLoadingView, ViewGroup.LayoutParams.MATCH_PARENT,
                                     ViewGroup.LayoutParams.MATCH_PARENT);// 插入加载 UserView
                }
            }
        }
        return result;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) onInitView(view);
    }

    protected void onFirstShow() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mFirstVisible && getUserVisibleHint()) {
            onFirstShow();
            mFirstVisible = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mFirstVisible && getUserVisibleHint() && isVisible()) {
            onFirstShow();
            mFirstVisible = false;
        }
    }

    protected void hideAll() {
        hideEmptyView();
        hideLoadingView();
    }

    protected void hideEmptyView() {
        if (Views.isVisible(mEmptyView)) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    protected void hideLoadingView() {
        if (Views.isVisible(mLoadingView)) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    protected void showLoadingView() {
        hideEmptyView();
        if (mLoadingView != null && Views.isGone(mLoadingView)) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    protected void showEmptyView() {
        hideLoadingView();
        if (mEmptyView != null && Views.isGone(mEmptyView)) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }



}
