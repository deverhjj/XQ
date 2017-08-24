/*
 * Copyright (C) 2017 Huajian Jiang
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
 */

package com.binfenjiari.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.github.huajianjiang.baserecyclerview.widget.ArrayAdapter;
import com.github.huajianjiang.baserecyclerview.widget.BaseViewHolder;

import java.util.List;

/**
 * Created by Huajian Jiang on 2017/8/23.
 * developer.huajianjiang@gmail.com
 */
public abstract class HeaderAdapter<HVH extends BaseViewHolder, BVH extends BaseViewHolder, B>
        extends ArrayAdapter<BaseViewHolder, B>
{
    private static final String TAG = HeaderAdapter.class.getSimpleName();

    public HeaderAdapter(Context ctxt) {
        super(ctxt);
    }

    public HeaderAdapter(Context ctxt, List<B> items) {
        super(ctxt, items);
    }

    public abstract HVH onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindHeaderViewHolder(HVH vh);

    public abstract BVH onCreateBodyViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindBodyViewHolder(BVH vh, int position, int adjustPosition);

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return getHeaderViewType();
        }
        return getBodyViewType(position-1);
    }

    public int getHeaderViewType() {
        return 0;
    }

    public int getBodyViewType(int position) {
        return 0;
    }

    @Override
    public BaseViewHolder onGenerateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == getHeaderViewType()) {
            return onCreateHeaderViewHolder(parent, viewType);
        } else {
            return onCreateBodyViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onPopulateViewHolder(BaseViewHolder holder, int position) {
        if (position == 0) {
            onBindHeaderViewHolder((HVH) holder);
        } else {
            onBindBodyViewHolder((BVH) holder, position, position - 1);
        }
    }
}
