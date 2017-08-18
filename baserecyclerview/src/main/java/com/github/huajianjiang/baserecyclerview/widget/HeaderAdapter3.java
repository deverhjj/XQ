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
public abstract class HeaderAdapter3<HVH extends BaseViewHolder, BVH extends BaseViewHolder, H, B>
        extends ArrayAdapter<BaseViewHolder, B> implements AdapterWrapper
{
    private static final String TAG = HeaderAdapter3.class.getSimpleName();
    private static final int ITEM_VIEW_TYPE_UNKNOWN = -1;
    private static final int ITEM_VIEW_TYPE_DEFAULT = 2;

    private boolean mAutoNotify = true;

    private ArrayAdapter<BVH, B> mAdapter;

    private List<H> mHeaders;

    public HeaderAdapter3(Context ctxt, ArrayAdapter<BVH, B> adapter)
    {
        this(ctxt, adapter, null);
    }

    public HeaderAdapter3(Context ctxt, ArrayAdapter<BVH, B> adapter, List<H> headers)
    {
        super(ctxt);
        mAdapter = adapter;
        mAdapter.setAutoNotify(false);
        mHeaders = headers == null ? new ArrayList<H>(1) : headers;
    }

    public boolean isAutoNotify() {
        return mAutoNotify;
    }

    public void setAutoNotify(boolean autoNotify) {
        mAutoNotify = autoNotify;
    }

    public abstract HVH onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindHeaderViewHolder(HVH vh, int position);


    @Override
    public BaseViewHolder onGenerateViewHolder(ViewGroup parent, int viewType) {
        final int localViewType = Packager.getLocalViewType(viewType);
        final int clientViewType = Packager.getClientViewType(viewType);
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
            mAdapter.onBindViewHolder((BVH) holder, position - getHeaderCount());
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getWrappedItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            int ht = getHeaderViewType(position);
            int type = Packager.makeItemViewTypeSpec(ht, Packager.ITEM_VIEW_TYPE_HEADER);
            Logger.e(TAG, "Header===type==>" + type + ",ht=" + ht);
            return type;
        } else {
            final int adjPos = position - getHeaderCount();
            int ht = getWrappedItemViewType(adjPos);
            int type = Packager.makeItemViewTypeSpec(ht, Packager.ITEM_VIEW_TYPE_BODY);
            Logger.e(TAG, "Item==type==>" + type + ",ht=" + ht);
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
        return mAdapter != null ? onFailedToRecycleView(holder) :
                super.onFailedToRecycleView(holder);
    }

    public boolean isHeaderView(int position) {
        return getHeaderCount() > 0 && position < getHeaderCount();
    }

    public boolean isItemView(int position) {
        return position >= getHeaderCount() && position < getItemCount();
    }

    private int getWrappedItemViewType(int position) {
        return mAdapter != null ? mAdapter.getItemViewType(position) : ITEM_VIEW_TYPE_UNKNOWN;
    }

    public int getHeaderViewType(int position) {
        return ITEM_VIEW_TYPE_DEFAULT;
    }

    public int getFooterViewType(int position, int adjustPosition) {
        return ITEM_VIEW_TYPE_DEFAULT;
    }

    public boolean containHeader() {
        return !mHeaders.isEmpty();
    }

    public int getHeaderCount() {
        return mHeaders.size();
    }

    private int getWrappedItemCount() {
        return mAdapter != null ? mAdapter.getItemCount() : 0;
    }

    @Override
    public RecyclerView.Adapter getWrappedAdapter() {
        return mAdapter;
    }

    //-----------------wrapper----------------


    @Override
    public void insertAll(List<B> items) {
        super.insertAll(items);
    }

    @Override
    public void insertAll(int position, List<B> items) {
        super.insertAll(position, items);
    }


    //----------------header-----------------

    public List<H> getHeaders() {
        return mHeaders;
    }

    public H getHeader(int position) {
        return mHeaders.get(position);
    }

    public void insertHeader(H header) {
        insertHeader(header, false);
    }

    public void insertHeader(H header, boolean reverse) {
        insertHeader(reverse ? 0 : mHeaders.size(), header);
    }

    public void insertHeader(int position, H header) {
        mHeaders.add(position, header);
        if (mAutoNotify) notifyItemInserted(position);
    }

    public void insertHeaders(List<H> headers) {
        insertHeaders(headers, false);
    }

    public void insertHeaders(List<H> headers, boolean reverse) {
        insertHeaders(reverse ? 0 : mHeaders.size(), headers);
    }

    public void insertHeaders(int position, List<H> headers) {
        mHeaders.addAll(position, headers);
        if (mAutoNotify) notifyItemRangeInserted(position, headers.size());
    }

    public H removeHeader(int position) {
        H removedHeader = mHeaders.remove(position);
        if (mAutoNotify) notifyItemRemoved(position);
        return removedHeader;
    }

    public void removeHeader(H header) {
        final int removedPos = mHeaders.indexOf(header);
        mHeaders.remove(header);
        if (mAutoNotify) notifyItemRemoved(removedPos);
    }

    public void removeHeaders(List<H> headers) {
        final int removedPosStart = mHeaders.indexOf(headers.get(0));
        final int removedCount = headers.size();
        mHeaders.removeAll(headers);
        if (mAutoNotify) notifyItemRangeRemoved(removedPosStart, removedCount);
    }

    public void changeHeader(int position, H newHeader) {
        mHeaders.set(position, newHeader);
        if (mAutoNotify) notifyItemChanged(position);
    }

    public void moveHeader(int fromPosition, int toPosition) {
        H moveHeader = mHeaders.remove(fromPosition);
        mHeaders.add(toPosition, moveHeader);
        if (mAutoNotify) notifyItemMoved(fromPosition, toPosition);
    }
}
