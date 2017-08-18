package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.fragment.CircleFragment;
import com.biu.modulebase.binfenjiari.fragment.RankingAllFragment;
import com.biu.modulebase.binfenjiari.fragment.RankingFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/4/25.
 */
public class RankingActivity extends BaseActivity {
    private static final String TAG = "RankingActivity";
    private static final int RANKING_PAGE_COUNT = 2;
    private static final String[] TABNAMES = {"所有排行", "伙伴排行"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabLayout tabLayout = (TabLayout) setToolBarCustomView(R.layout.layout_tab_ranking);
        FrameLayout contentContainer = (FrameLayout) getFragmentContainer();
        ViewPager viewPager = new ViewPager(this);
        viewPager.setId(R.id.id_vp);
        viewPager.setAdapter(new RankingPagerAdapter(getSupportFragmentManager()));
        contentContainer.addView(viewPager,
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        tabLayout.setupWithViewPager(viewPager);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        return null;
    }

    @Override
    protected String getToolbarTitle() {
        return null;
    }


    private class RankingPagerAdapter extends FragmentPagerAdapter {

        public RankingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Intent intent = getIntent();
            int circleId = intent != null ? intent.getIntExtra(CircleFragment.EXTRA_CIRCLE_ID, -1)
                    : -1;
            switch (position) {
                case 0:
                    return new RankingAllFragment();

                case 1:
                    return new RankingFragment();

                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return RANKING_PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TABNAMES[position];
        }
    }
}
