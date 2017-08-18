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
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.huajianjiang.baserecyclerview.util.Logger;
import com.github.huajianjiang.baserecyclerview.util.Packager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/10/10.
 */
public abstract class HeaderAdapter<HVH extends BaseViewHolder, H>
        extends BaseAdapter<BaseViewHolder> implements AdapterWrapper
{
    private static final String TAG = HeaderAdapter.class.getSimpleName();
    private static final int ITEM_VIEW_TYPE_UNKNOWN = -1;
    private static final int ITEM_VIEW_TYPE_DEFAULT = 4;

    private boolean mApplyData = true;
    private boolean mAutoNotify = true;

    private BaseAdapter mAdapter;

    private List<H> mHeaders;

    public HeaderAdapter(Context ctxt, BaseAdapter adapter)
    {
        this(ctxt, adapter, null);
    }

    public HeaderAdapter(Context ctxt, BaseAdapter adapter, List<H> headers)
    {
        super(ctxt);
        mAdapter = adapter;
        mHeaders = headers == null ? new ArrayList<H>(1) : headers;

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
               // Logger.e(TAG, "onChanged");
                HeaderAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                //Logger.e(TAG, "onItemRangeChanged");
                HeaderAdapter.this.notifyItemRangeChanged(1 + positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                //Logger.e(TAG, "onItemRangeInserted");
                HeaderAdapter.this.notifyItemRangeInserted(1 + positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                //Logger.e(TAG, "onItemRangeRemoved");
                HeaderAdapter.this.notifyItemRangeRemoved(1 + positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                Logger.e(TAG, "onItemRangeMoved");
                HeaderAdapter.this.notifyItemMoved(1 + fromPosition, 1 + toPosition);
            }
        });

    }

    public boolean isAutoNotify() {
        return mAutoNotify;
    }

    public void setAutoNotify(boolean autoNotify) {
        mAutoNotify = autoNotify;
    }

    public boolean isApplyData() {
        return mApplyData;
    }

    public void setApplyData(boolean applyData) {
        mApplyData = applyData;
    }

    public abstract HVH onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindHeaderViewHolder(HVH vh, int position);


    @Override
    public BaseViewHolder onGenerateViewHolder(ViewGroup parent, int viewType) {
        final int localViewType = Packager.getLocalViewType(viewType);
        final int clientViewType = Packager.getClientViewType(viewType);
//        Logger.e(TAG,
//                "onBuildViewHolder==>" + viewType + "," + localViewType + "," + clientViewType);
        if (localViewType == Packager.ITEM_VIEW_TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent, clientViewType);
        } else {
            return mAdapter.onCreateViewHolder(parent, clientViewType);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onPopulateViewHolder(BaseViewHolder holder, int position) {
        if (isHeaderView(position)) {
            onBindHeaderViewHolder((HVH) holder, position);
        } else {
            mAdapter.onBindViewHolder(holder, position - getHeaderCount());
        }
    }

    @Override
    public int getItemCount() {
        int count = getHeaderCount() + getWrappedItemCount();
        //Logger.e(TAG, "count=======>" + count);
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            int ht = getHeaderViewType(position);
            int type = Packager.makeItemViewTypeSpec(ht, Packager.ITEM_VIEW_TYPE_HEADER);
            //Logger.e(TAG, "Header===type==>" + type + ",ht=" + ht);
            return type;
        } else {
            final int adjPos = position - getHeaderCount();
            int ht = getWrappedItemViewType(adjPos);
            int type = Packager.makeItemViewTypeSpec(ht, Packager.ITEM_VIEW_TYPE_BODY);
            //Logger.e(TAG, "Item==type==>" + type + ",ht=" + ht);
            return type;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mAdapter != null) mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (mAdapter != null) mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(BaseViewHolder holder) {
        return mAdapter != null ? onFailedToRecycleView(holder)
                                : super.onFailedToRecycleView(holder);
    }

    public boolean isHeaderView(int position) {
        return getHeaderCount() > 0 && position < getHeaderCount();
    }

    public boolean isItemView(int position) {
        return position >= getHeaderCount() && position < getItemCount();
    }

//    public boolean isFooterView(int position) {
//        return getFooterCount() > 0 && position >= getHeaderCount() + getWrappedItemCount() &&
//               position < getItemCount();
//    }

   //------------------type-------------------
    private int getWrappedItemViewType(int position) {
        return mAdapter != null ? mAdapter.getItemViewType(position) : ITEM_VIEW_TYPE_UNKNOWN;
    }

    public int getHeaderViewType(int position) {
        return ITEM_VIEW_TYPE_DEFAULT;
    }

//    public int getFooterViewType(int position, int adjustPosition) {
//        return ITEM_VIEW_TYPE_DEFAULT;
//    }

    public boolean containHeader() {
        return !mHeaders.isEmpty();
    }

    public int getHeaderCount() {
        return mHeaders.size();
    }

//    public int getFooterCount() {
//        return mF.size();
//    }

    private int getWrappedItemCount() {
        return mAdapter != null ? mAdapter.getItemCount() : 0;
    }

    @Override
    public RecyclerView.Adapter getWrappedAdapter() {
        return mAdapter;
    }


    //--------------header action--------------

    public List<H> getHeaders() {
        return mHeaders;
    }

    public H getHeader(int position) {
        return mHeaders.get(position);
    }

    public void insertHeader(H header) {
        insertHeader(0, header);
    }

    public void insertHeader(int position, H header) {
        if (mApplyData) mHeaders.add(position, header);
        if (mAutoNotify) notifyItemInserted(position);
    }

    public H removeHeader(int position) {
        if (mApplyData) {
            return mHeaders.remove(position);
        }
        if (mAutoNotify) notifyItemRemoved(position);
        return null;
    }

    public void removeHeader(H header) {
        final int removedPos = mHeaders.indexOf(header);
        removeHeader(removedPos);
    }

    public void changeHeader(H newHeader) {
        changeHeader(0, newHeader);
    }

    public void changeHeader(int position, H newHeader) {
        if (mApplyData) mHeaders.set(position, newHeader);
        if (mAutoNotify) notifyItemChanged(position);
    }

}
