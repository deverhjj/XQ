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
import android.view.View;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.fragment.BasejidiListFragment;
import com.binfenjiari.fragment.MineCollectMovementFragment;
import com.binfenjiari.fragment.ProjectListFragment;
import com.binfenjiari.fragment.NaviHomeSearchAvFragment;
import com.biu.modulebase.binfenjiari.activity.SearchActivity;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.common.base.ContentActivity;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{首页搜索 全局搜索}
 * @date 2017/6/15
 */
public class NaviHomeSearchActivity extends ContentActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView searchText;

    @Override
    protected int getContentView() {
        setBackNavigationIcon();
        return R.layout.activity_tablayout_viewpage;
    }

    @Override
    protected String getToolbarTitle() {
        return "";
    }

    @Override
    protected void setViewData() {
//        setHasOptionsMenu(true);
        searchText = (TextView) setToolBarCustomView(R.layout.item_search_title_view);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });


        tabLayout = (TabLayout) findViewById(R.id.tablayout_title);
        viewPager = (ViewPager) findViewById(R.id.viewpager_content);

        ViewPagerAdapter mIcanPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mIcanPagerAdapter);
//        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
//        viewPager.setCurrentItem(posi);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        String[] names = {"活动实践", "成长基地", "校外课程", "视听库"};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return MineCollectMovementFragment.newInstance();
            } else if (position == 1) {
                return BasejidiListFragment.newInstance();
            } else if (position == 2) {
                return ProjectListFragment.newInstance();
            } else {
                return NaviHomeSearchAvFragment.newInstance();
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
        getMenuInflater().inflate(R.menu.search_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            search();
        }
        return super.onOptionsItemSelected(item);
    }

    private void search() {
        SearchActivity.beginActivity(this, Constant.SEARCH_TAG);
    }

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, NaviHomeSearchActivity.class);
        context.startActivity(intent);

    }

}
