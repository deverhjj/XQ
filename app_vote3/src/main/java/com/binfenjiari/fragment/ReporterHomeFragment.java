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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.binfenjiari.R;
import com.binfenjiari.activity.ReporterWorksDetailActivity;
import com.binfenjiari.adapter.ReporterHomeAdapter;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Views;
import com.github.huajianjiang.baserecyclerview.widget.BaseAdapter;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/22
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class ReporterHomeFragment extends AppFragment implements BaseAdapter.OnItemClickListener {
    private RecyclerView mReportList;
    private ReporterHomeAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onGenerateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_recyclerview_swiperefresh, container, false);
    }

    @Override
    public void onInitView(View root) {
        mReportList = Views.find(root, R.id.recyclerView);
        mAdapter = new ReporterHomeAdapter(getContext());
        mReportList.setAdapter(mAdapter);
        mReportList.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mAdapter.clickTargets(R.id.reportItem).listenClickEvent(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_young_report, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_edit) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(RecyclerView parent, View view) {
        switch (view.getId()) {
            case R.id.worksItem:
                Intent worksIntent = new Intent(getContext(), ReporterWorksDetailActivity.class);
                worksIntent.putExtra(Constants.KEY_ID, "1");
                startActivity(worksIntent);
                break;

            default:
                break;
        }
    }
}
