/*
 * Copyright (C) 2015 Huajian Jiang
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
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.binfenjiari.R;
import com.binfenjiari.model.ReporterHome;
import com.github.huajianjiang.baserecyclerview.widget.ArrayAdapter;
import com.github.huajianjiang.baserecyclerview.widget.BaseViewHolder;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/22
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class ReporterHomeAdapter extends ArrayAdapter<BaseViewHolder, ReporterHome> {
    private static final String TAG = ReporterHomeAdapter.class.getSimpleName();

    public ReporterHomeAdapter(Context ctxt) {
        super(ctxt);
    }

    @Override
    public BaseViewHolder onGenerateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(getLayoutInflater().inflate(viewType, parent, false)) {
        };
    }

    @Override
    public void onPopulateViewHolder(BaseViewHolder holder, int position) {
        if (position == 0) {
            RecyclerView worksList = holder.getView(R.id.worksList);
            Boolean hasBind = (Boolean) worksList.getTag(R.id.hasBind);
            if (hasBind == null || !hasBind) {
                WorksAdapter worksAdapter = new WorksAdapter(getContext());
                worksList.setAdapter(worksAdapter);
                worksList.addItemDecoration(worksAdapter.new ItemDecor());
                worksList.setTag(R.id.hasBind, true);
                worksAdapter.clickTargets(R.id.worksItem).listenClickEvent(getClickListener());
            }

            RecyclerView selfList = holder.getView(R.id.selfList);
            hasBind = (Boolean) selfList.getTag(R.id.hasBind);
            if (hasBind == null || !hasBind) {
                SelfAdapter selfAdapter = new SelfAdapter(getContext());
                selfList.setAdapter(selfAdapter);
                selfList.addItemDecoration(selfAdapter.new ItemDecor());
                selfList.setTag(R.id.hasBind, true);
            }

            RecyclerView topicList = holder.getView(R.id.topicList);
            hasBind = (Boolean) topicList.getTag(R.id.hasBind);
            if (hasBind == null || !hasBind) {
                TopicAdapter topAdapter = new TopicAdapter(getContext());
                topicList.setAdapter(topAdapter);
                topicList.setTag(R.id.hasBind, true);
            }
        } else {

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? R.layout.header_repoter : R.layout.item_little_report_news;
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    private class WorksAdapter extends ArrayAdapter {

        public WorksAdapter(Context ctxt) {
            super(ctxt);
        }

        @Override
        public BaseViewHolder onGenerateViewHolder(ViewGroup parent, int viewType) {
            return new BaseViewHolder(getLayoutInflater().inflate(viewType, parent, false)) {
            };
        }

        @Override
        public void onPopulateViewHolder(BaseViewHolder holder, int position) {

        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.widget_fixrecycle_item_young_report;
        }

        @Override
        public int getItemCount() {
            return 8;
        }

        class ItemDecor extends RecyclerView.ItemDecoration {
            private int offsets;

            public ItemDecor() {
                offsets = getContext().getResources().getDimensionPixelSize(R.dimen.item_offset);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                    RecyclerView.State state)
            {
                final int childAdapterPos = parent.getChildAdapterPosition(view);
                final int right = childAdapterPos != getItemCount() - 1 ? 0 : offsets;
                outRect.set(offsets, 0, right, 0);
            }
        }
    }

    private class SelfAdapter extends ArrayAdapter {

        SelfAdapter(Context ctxt) {
            super(ctxt);
        }

        @Override
        public BaseViewHolder onGenerateViewHolder(ViewGroup parent, int viewType) {
            return new BaseViewHolder(getLayoutInflater().inflate(viewType, parent, false)) {
            };
        }

        @Override
        public void onPopulateViewHolder(BaseViewHolder holder, int position) {

        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.widget_fixrecycle_item_young_reporter_graceful;
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        class ItemDecor extends RecyclerView.ItemDecoration {
            private int offsetsSmall;
            private int offsetsBig;

            ItemDecor() {
                offsetsSmall = getContext().getResources()
                                           .getDimensionPixelSize(R.dimen.item_offset_small);
                offsetsBig =
                        getContext().getResources().getDimensionPixelSize(R.dimen.item_offset_big);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                    RecyclerView.State state)
            {
                outRect.set(offsetsSmall, 0, offsetsSmall, 0);
            }
        }
    }

    private class TopicAdapter extends ArrayAdapter {
        TopicAdapter(Context ctxt) {
            super(ctxt);
        }

        @Override
        public BaseViewHolder onGenerateViewHolder(ViewGroup parent, int viewType) {
            return new BaseViewHolder(getLayoutInflater().inflate(viewType, parent, false)) {
            };
        }

        @Override
        public void onPopulateViewHolder(BaseViewHolder holder, int position) {

        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.widget_fixrecycle_item_little_report_topic;
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

}
