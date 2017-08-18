/*
 * Copyright (c) 2017 HuaJian Jiang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.huajianjiang.baserecyclerview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.github.huajianjiang.baserecyclerview.R;
import com.github.huajianjiang.baserecyclerview.util.Predications;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/10/11.
 */
public class BaseRecyclerView extends RecyclerView {
    private static final String TAG = "BaseRecyclerView";

    private List<FixedHeaderInfo> mHeaders;
    private List<FixedFooterInfo> mFooters;

    private boolean mHeaderFooterFullSpan = true;

    private int mOrientation;

    private EmptyDataObserver mObserver;

    private Adapter mAdapter;

    private View mEmptyView;

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.BaseRecyclerView);
        boolean nestedScrollingEnabled = ta
                .getBoolean(R.styleable.BaseRecyclerView_nestedScrollingEnabled, true);
        boolean hasFixedSize = ta.getBoolean(R.styleable.BaseRecyclerView_hasFixedSize, false);
        mHeaderFooterFullSpan = ta
                .getBoolean(R.styleable.BaseRecyclerView_header_footer_fullSpan, true);
//        mOrientation =
//                ta.getInt(R.styleable.BaseRecyclerView_orientation, OrientationHelper.VERTICAL);
        ta.recycle();

        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int currOrientation = linearLayoutManager.getOrientation();
            if (currOrientation != mOrientation) {
                linearLayoutManager.setOrientation(mOrientation);
            }
        }

        setHasFixedSize(hasFixedSize);
        setNestedScrollingEnabled(nestedScrollingEnabled);
        setFocusable(nestedScrollingEnabled);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (mHeaderFooterFullSpan && layout instanceof GridLayoutManager) {
            GridLayoutManager gridLayout = (GridLayoutManager) layout;
            Adapter adapter = getAdapter();
            if (adapter instanceof InnerHeaderAdapter) {
                InnerHeaderAdapter innerHeaderAdapter = (InnerHeaderAdapter) adapter;
                gridLayout.setSpanSizeLookup(
                        new HeaderSpanSizeLookup(innerHeaderAdapter, gridLayout.getSpanCount()));
            }
        }
        super.setLayoutManager(layout);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterAdapterDataObserver(mObserver);
        }
        mAdapter = adapter;
        if (!Predications.isNullOrEmpty(mHeaders) || !Predications.isNullOrEmpty(mFooters)) {
            mAdapter = new InnerHeaderAdapter(adapter);
            ((InnerHeaderAdapter) mAdapter).invalidate(mHeaders, mFooters, false);
        }

        if (mAdapter != null) {
            if (mEmptyView != null) {
                mAdapter.registerAdapterDataObserver(getObserver());
            }
        }

        super.setAdapter(mAdapter);


        if (mHeaderFooterFullSpan && adapter instanceof InnerHeaderAdapter) {
            InnerHeaderAdapter innerHeaderAdapter = (InnerHeaderAdapter) adapter;
            LayoutManager layout = getLayoutManager();
            if (layout instanceof GridLayoutManager) {
                GridLayoutManager gridLayout = (GridLayoutManager) layout;
                gridLayout.setSpanSizeLookup(
                        new HeaderSpanSizeLookup(innerHeaderAdapter, gridLayout.getSpanCount()));
            }
        }
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        if (mEmptyView != null && mAdapter != null && mObserver == null) {
            mAdapter.registerAdapterDataObserver(getObserver());
        }
        updateEmptyStatus(shouldShowEmptyView());
    }


    private EmptyDataObserver getObserver() {
        if (mObserver == null) {
            mObserver = new EmptyDataObserver();
        }
        return mObserver;
    }

    /**
     *
     */
    private class EmptyDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            updateEmptyStatus(shouldShowEmptyView());
        }
    }

    private boolean shouldShowEmptyView() {
        return mAdapter == null || mAdapter.getItemCount() == 0;
    }

    private void updateEmptyStatus(boolean empty) {
        if (empty) {
            if (mEmptyView == null) {
                setVisibility(View.VISIBLE);
            } else {
                setVisibility(View.GONE);
                mEmptyView.setVisibility(VISIBLE);
            }
        } else {
            if (mEmptyView != null) mEmptyView.setVisibility(GONE);
            setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterAdapterDataObserver(mObserver);
        }
    }

    private List<FixedHeaderInfo> getHeaders() {
        if (mHeaders == null) {
            mHeaders = new ArrayList<>();
        }
        return mHeaders;
    }

    private List<FixedFooterInfo> getFooters() {
        if (mFooters == null) {
            mFooters = new ArrayList<>();
        }
        return mFooters;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T addHeaderView(int layoutResId) {
        View headerView = LayoutInflater.from(getContext()).inflate(layoutResId, this, false);
        FixedHeaderInfo headerInfo = new FixedHeaderInfo();
        headerInfo.layoutResId = layoutResId;
        getHeaders().add(headerInfo);
        if (mAdapter != null) {
            if (!(mAdapter instanceof InnerHeaderAdapter)) {
                mAdapter = new InnerHeaderAdapter(mAdapter);
                ((InnerHeaderAdapter) mAdapter).addHeader(headerInfo, false);
                swapAdapter(mAdapter, false);
            } else {
                ((InnerHeaderAdapter) mAdapter).addHeader(headerInfo, true);
            }
        }
        return (T) headerView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T addFooterView(int layoutResId) {
        View footerView = LayoutInflater.from(getContext()).inflate(layoutResId, this, false);
        FixedFooterInfo footerInfo = new FixedFooterInfo();
        footerInfo.layoutResId = layoutResId;
        if (mAdapter != null) {
            if (!(mAdapter instanceof InnerHeaderAdapter)) {
                mAdapter = new InnerHeaderAdapter(mAdapter);
            }
        }
        return (T) footerView;
    }

    static class FixedHeaderInfo {
        int layoutResId;
    }

    static class FixedFooterInfo {
        int layoutResId;
    }







    private ContextMenu.ContextMenuInfo mContextMenuInfo = null;

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return mContextMenuInfo;
    }


    @Override
    public boolean showContextMenuForChild(View originalView) {
        final int longPressPosition = getChildAdapterPosition(originalView);
        if (longPressPosition >= 0) {
            final long longPressId = getAdapter().getItemId(longPressPosition);
            mContextMenuInfo = createContextMenuInfo(longPressPosition, longPressId);
            return super.showContextMenuForChild(originalView);
        }
        return false;
    }

    ContextMenu.ContextMenuInfo createContextMenuInfo(int position, long id) {
        return new RecyclerContextMenuInfo(position, id);
    }

    /**
     * Extra menu information provided to the
     * {@link android.view.View.OnCreateContextMenuListener#onCreateContextMenu(android.view.ContextMenu, View, ContextMenuInfo) }
     * callback when a context menu is brought up for this AdapterView.
     */
    public static class RecyclerContextMenuInfo implements ContextMenu.ContextMenuInfo {

        public RecyclerContextMenuInfo(int position, long id) {
            this.position = position;
            this.id = id;
        }

        /**
         * The position in the adapter for which the context menu is being
         * displayed.
         */
        public int position;

        /**
         * The row id of the item for which the context menu is being displayed.
         */
        public long id;
    }


}
