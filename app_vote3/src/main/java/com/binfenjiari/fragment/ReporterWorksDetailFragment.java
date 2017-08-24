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

package com.binfenjiari.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binfenjiari.R;
import com.binfenjiari.adapter.ReporterCommentAdapter;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.fragment.contract.AbsPaginationContract;
import com.binfenjiari.fragment.contract.ReporterWorksDetailContract;
import com.binfenjiari.fragment.presenter.ReporterWorksDetailPresenter;
import com.binfenjiari.model.Comment;
import com.binfenjiari.model.WorksDetail;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Datas;
import com.binfenjiari.utils.Logger;
import com.binfenjiari.utils.Views;
import com.github.huajianjiang.baserecyclerview.widget.BaseAdapter;
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter;
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableRecyclerView;

import java.util.List;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/23
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class ReporterWorksDetailFragment extends AppFragment<ReporterWorksDetailContract.IPresenter>
        implements ReporterWorksDetailContract.IView ,BaseAdapter.OnItemClickListener,
        ExpandableAdapter.OnParentClickListener
{
    private static final String TAG = ReporterWorksDetailFragment.class.getSimpleName();
    private ExpandableRecyclerView mCommentList;
    private ReporterCommentAdapter mAdapter;

    @Override
    public View onGenerateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_reporter_works_detail, container, false);
    }

    @Override
    public void onInitView(View root) {
        mCommentList = Views.find(root, R.id.commentList);
        mAdapter = new ReporterCommentAdapter(getContext());
        mCommentList.setAdapter(mAdapter);
        mAdapter.setInnerItemClickListener(this);
//        mAdapter.parentClickTargets(R.id.)
    }

    @Override
    protected void onFirstShow() {
        presenter.loadDetail(getArguments());
    }

    @Override
    protected void onRetry() {
        presenter.loadDetail(getArguments());
    }

    @Override
    public void showDetail(WorksDetail detail) {
        mAdapter.insertHeader(detail);
    }

    @Override
    public void showItems(AbsPaginationContract.UpdateType type, List<Comment> items) {

    }

    @Override
    public void onEndOfPage() {

    }

    @Override
    public void onItemClick(RecyclerView parent, View view) {
        Logger.e(TAG,"onInnerItemClick==>");
    }

    @Override
    public void onParentClick(RecyclerView parent, View view) {

    }
}
