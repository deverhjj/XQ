package com.binfenjiari.utils;

import android.support.v7.widget.RecyclerView;

import com.biu.modulebase.binfenjiari.widget.RefreshRecyclerView;
import com.biu.modulebase.common.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/16
 */
public class DataTest {

    /**
     * 测试数据  加载具体的数据
     */
    public static void loadDefiniteData(RefreshRecyclerView mRefreshRecyclerView, int count) {
        RecyclerView mRecyclerView = mRefreshRecyclerView.getRecyclerView();
        mRefreshRecyclerView.endPage();

        List<String> datas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            datas.add("");
        }
        ((BaseAdapter) (mRecyclerView.getAdapter())).addItems(datas);
        mRefreshRecyclerView.showNoMore();
    }


    /**
     * 测试分页  假如共有有10页
     *
     * @param page
     */
    public static void load(RefreshRecyclerView mRefreshRecyclerView, int page) {
        RecyclerView mRecyclerView = mRefreshRecyclerView.getRecyclerView();
        mRefreshRecyclerView.endPage();

        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            datas.add("");
        }

        if (page == 1) {
            ((BaseAdapter) (mRecyclerView.getAdapter())).setData(datas);
            mRefreshRecyclerView.showNextMore(page);
            return;
        }

        if (page <= 5) {

            ((BaseAdapter) (mRecyclerView.getAdapter())).addItems(datas);
            mRefreshRecyclerView.showNextMore(page);
            return;
        }

        if (page > 5) {
            List<String> datas2 = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                datas2.add("");
            }
            ((BaseAdapter) (mRecyclerView.getAdapter())).addItems(datas2);
            mRefreshRecyclerView.showNoMore();
        }
    }
}
