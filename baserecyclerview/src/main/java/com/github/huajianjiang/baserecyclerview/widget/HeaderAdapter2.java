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
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.github.huajianjiang.baserecyclerview.util.Packager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/10/10.
 */
public abstract class HeaderAdapter2<HVH extends BaseViewHolder, BVH extends BaseViewHolder, H, B>
        extends ArrayAdapter<BaseViewHolder, B>
{
    private static final String TAG = HeaderAdapter2.class.getSimpleName();
    private static final int ITEM_VIEW_TYPE_DEFAULT = -1;

    private List<H> mHeaders;

    public HeaderAdapter2(Context ctxt)
    {
        super(ctxt);
        setAutoNotify(false);
        mHeaders = new ArrayList<>(1);
    }

    public abstract HVH onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindHeaderViewHolder(HVH vh, int position);

    public abstract BVH onCreateBodyViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindBodyViewHolder(BVH vh, int position, int adjustPosition);

    @Override
    public BaseViewHolder onGenerateViewHolder(ViewGroup parent, int viewType) {
        final int localViewType = Packager.getLocalViewType(viewType);
        final int clientViewType = Packager.getClientViewType(viewType);
        if (localViewType == Packager.ITEM_VIEW_TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent, clientViewType);
        } else {
            return onCreateBodyViewHolder(parent, clientViewType);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onPopulateViewHolder(BaseViewHolder holder, int position) {
        if (isHeaderView(position)) {
            onBindHeaderViewHolder((HVH) holder, position);
        } else {
            onBindBodyViewHolder((BVH) holder, position, getBodyAdjustPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return Packager.makeItemViewTypeSpec(getHeaderViewType(position),
                    Packager.ITEM_VIEW_TYPE_HEADER);
        } else {
            return Packager.makeItemViewTypeSpec(
                    getBodyViewType(position, getBodyAdjustPosition(position)),
                    Packager.ITEM_VIEW_TYPE_BODY);
        }
    }

    public int getBodyAdjustPosition(int position) {
        return position - getHeaderCount();
    }

    public boolean isHeaderView(int position) {
        return getHeaderCount() > 0 && position < getHeaderCount();
    }

    public boolean isItemView(int position) {
        return position >= getHeaderCount() && position < getItemCount();
    }

    public int getHeaderViewType(int position) {
        return ITEM_VIEW_TYPE_DEFAULT;
    }

    public int getBodyViewType(int position,int adjustPosition) {
        return ITEM_VIEW_TYPE_DEFAULT;
    }

    public boolean containHeader() {
        return !mHeaders.isEmpty();
    }

    public int getHeaderCount() {
        return mHeaders.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && getHeaderCount() <= 0;
    }

    @Override
    public int getPosition(@Nullable B item) {
        return getHeaderCount() + super.getPosition(item);
    }

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
        notifyItemInserted(position);
    }

    public void changeHeader(int position, H newHeader) {
        mHeaders.set(position, newHeader);
        notifyItemChanged(position);
    }

    //--------------------------

//    @Override
//    public void invalidate(List<B> newItems) {
//        if (!getItems().isEmpty()) removeAll(getItems());
//        insertAll(newItems);
//        notifyItemRangeInserted(1, newItems.size());
//    }

    @Override
    public void insertAll(List<B> items) {
        insertAll(0, items);
    }

    @Override
    public void insertAll(int position, List<B> items) {
        super.insertAll(position, items);
        notifyItemRangeInserted(getHeaderCount() + position, items.size());
    }
}
