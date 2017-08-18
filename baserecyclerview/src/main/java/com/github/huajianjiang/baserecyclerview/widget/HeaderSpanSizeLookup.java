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

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by jhj_Plus on 2016/11/4.
 */
public class HeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private static final String TAG = "HeaderSpanSizeLookup";
    private InnerHeaderAdapter mInnerHeaderAdapter;
    private int mSpanCount;

    public HeaderSpanSizeLookup(InnerHeaderAdapter innerHeaderAdapter, int spanCount) {
        setSpanIndexCacheEnabled(true);
        mInnerHeaderAdapter = innerHeaderAdapter;
        mSpanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {
        if (mInnerHeaderAdapter.isHeaderView(position)) {
            return getHeaderSpanSize(position);
        } else if (mInnerHeaderAdapter.isFooterView(position)) {
            return getFooterSpanSize(position);
        }
        return getItemSpanSize(position);
    }

    protected int getHeaderSpanSize(int position) {
        return mSpanCount;
    }

    protected int getItemSpanSize(int position) {
        return 1;
    }

    protected int getFooterSpanSize(int position) {
        return mSpanCount;
    }
}
