package com.biu.modulebase.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * @author Lee
 * @Title: {社区Tab}
 * @Description:{主界面社区tab页}
 * @date 2016/4/13
 */
public class MainCommunityFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main_community, container, false);
        return layout;
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        setHasOptionsMenu(true);
    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_community, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_chat) {
        }
        return super.onOptionsItemSelected(item);
    }
}
