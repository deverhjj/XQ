package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.binfenjiari.R;
import com.binfenjiari.fragment.BasejidiListFragment;
import com.binfenjiari.fragment.MineCollectMovementFragment;
import com.binfenjiari.fragment.MineCollectVideoFragment;
import com.binfenjiari.fragment.NaviHomeVideoAudioFragment;
import com.biu.modulebase.common.base.ContentActivity;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/15
 */
public class NaviHomeVideoAudioActivity extends ContentActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected int getContentView() {
        setBackNavigationIcon();
        return R.layout.activity_tablayout_viewpage;
    }

    @Override
    protected String getToolbarTitle() {
        return "视听";
    }

    @Override
    protected void setViewData() {

        tabLayout = (TabLayout) findViewById(R.id.tablayout_title);
        viewPager = (ViewPager) findViewById(R.id.viewpager_content);

        ViewPagerAdapter mIcanPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mIcanPagerAdapter);
        //        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        //        viewPager.setCurrentItem(posi);
    }

    //
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        String[] names = {"视频", "音频", "图文"};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return NaviHomeVideoAudioFragment.newInstance();
            } else if (position == 1) {
                return NaviHomeVideoAudioFragment.newInstance();
            } else {
                return NaviHomeVideoAudioFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return names[position];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_av, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_rank) {
            BaseRankingListActivity.beginActivity(this);

        } else if (i == R.id.action_map) {
            Intent post = new Intent(this, AvPostActivity.class);
            startActivity(post);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, NaviHomeVideoAudioActivity.class);
        context.startActivity(intent);

    }

}
