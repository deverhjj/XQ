package com.biu.modulebase.binfenjiari.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * @author Lee
 * @Title: {视听搜索结果}
 * @Description:{描述}
 * @date 2016/6/5
 */
public class SearchResultAudioFragment extends BaseFragment {

    /**搜索参数**/
    private String title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh,
                container,false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }
    @Override
    protected void getIntentData() {
        title =getActivity().getIntent().getStringExtra("title");
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    public void loadData() {

    }
}
