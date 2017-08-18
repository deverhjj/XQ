package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.fragment.BasejidiListFragment;
import com.biu.modulebase.binfenjiari.activity.SearchActivity;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.common.base.ContentActivity;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{活动基地}
 * @date 2017/6/15
 */
public class NaviHomeBaseListActivity extends ContentActivity {
    //    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView searchText;
    private LinearLayout ll_pop;

    public int dateCheckedPos = -1;
    public String dateArray[] = {"综合排序", "时间从近到远", "时间从远到近"};

    @Override
    protected int getContentView() {
        setBackNavigationIcon();
        return R.layout.activity_home_base_list;
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


//        tabLayout = (TabLayout) findViewById(R.id.tablayout_title);
        viewPager = (ViewPager) findViewById(R.id.viewpager_content);

        ViewPagerAdapter mIcanPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mIcanPagerAdapter);
//        viewPager.setOffscreenPageLimit(3);
//        tabLayout.setupWithViewPager(viewPager);
//        viewPager.setCurrentItem(posi);

        findViewById(R.id.fl_base_current_hot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //近期热点
                BaseJidiListActivity.beginActivity(NaviHomeBaseListActivity.this);
            }
        });

        findViewById(R.id.fl_base_general_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //常态项目
                BaseJidiListActivity.beginActivity(NaviHomeBaseListActivity.this);
            }
        });

        ll_pop = (LinearLayout) findViewById(R.id.ll_pop);

        findViewById(R.id.rl_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortFilter();
            }
        });

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        String[] names = {"成长基地"};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return BasejidiListFragment.newInstance();
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
        getMenuInflater().inflate(R.menu.menu_base_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_rank) {
            BaseRankingListActivity.beginActivity(this);

        }
        return super.onOptionsItemSelected(item);
    }

    private void search() {
        SearchActivity.beginActivity(this, Constant.SEARCH_TAG);
    }

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, NaviHomeBaseListActivity.class);
        context.startActivity(intent);

    }

    private void showSortFilter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout
                .item_menu2, R.id.tv, dateArray);
        OtherUtil.showPopupWin(this, ll_pop, adapter, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                dateCheckedPos, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        tv_date.setText(dateArray[position]);
                        dateCheckedPos = position;
//                        if (position == 0) {
//                            timeOrder = "";
//                        } else {
//                            timeOrder = orderArray[position - 1];
//                        }
//                        if (!v.isSelected()) {
//                            v.setSelected(true);
//                        }
                    }
                });
    }

}
