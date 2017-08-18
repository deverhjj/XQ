package com.binfenjiari.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.binfenjiari.fragment.AvVideoEpisodeListFragment;
import com.binfenjiari.fragment.AvVideoIntroFragment;
import com.binfenjiari.fragment.CommentFragment;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AvVideoDetailPagerAdapter extends FragmentPagerAdapter {

    private static final CharSequence[] TITLES = {"剧集", "简介", "评价"};

    public AvVideoDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment content;
        switch (position) {
            case 0:
                content = new AvVideoEpisodeListFragment();
                break;
            case 1:
                content = new AvVideoIntroFragment();
                break;
            case 2:
                content = new CommentFragment();
                break;
            default:
                throw new IllegalStateException("can not init page in position =>" + position);
        }
        return content;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
