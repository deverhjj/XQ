package com.biu.modulebase.binfenjiari.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author Lee
 * @Title: {ViewPage Adapter}
 * @Description:{描述}
 * @date 2016/5/23
 */
public class ViewPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> mViewList;
    private List<String> list_Title;

    public ViewPageAdapter(FragmentManager fm, List<Fragment> mViewList, List<String> list_Title) {
        super(fm);
        this.mViewList =mViewList;
        this.list_Title =list_Title;
    }


    @Override
    public Fragment getItem(int position) {
        return mViewList.get(position);
    }

    @Override
    public int getCount() {
        return mViewList==null?0:mViewList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position%mViewList.size());
    }
}
