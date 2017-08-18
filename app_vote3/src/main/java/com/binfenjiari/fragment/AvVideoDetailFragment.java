package com.binfenjiari.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binfenjiari.R;
import com.binfenjiari.adapter.AvVideoDetailPagerAdapter;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.utils.Res;
import com.binfenjiari.utils.Views;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AvVideoDetailFragment extends AppFragment {
    private TabLayout mTabGroup;
    private ViewPager mDetailVp;

    @Override
    public View onGenerateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_av_video_detail, container, false);
    }

    @Override
    public void onInitView(View root) {
        mDetailVp = Views.find(root, R.id.vp);
        AvVideoDetailPagerAdapter pagerAdapter =
                new AvVideoDetailPagerAdapter(getChildFragmentManager());
        mDetailVp.setAdapter(pagerAdapter);
        mDetailVp.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
        mTabGroup = Views.find(root, R.id.tabGroup);
        mTabGroup.setupWithViewPager(mDetailVp);

        View naviLayout = Views.find(root, R.id.naviLayout);
        ViewGroup.MarginLayoutParams lp =
                (ViewGroup.MarginLayoutParams) naviLayout.getLayoutParams();
        lp.topMargin = Res.getStatusBarHeight(getContext());
        naviLayout.setLayoutParams(lp);
    }

}
