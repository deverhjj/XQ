package com.binfenjiari.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.binfenjiari.R;
import com.binfenjiari.fragment.NaviCommunityFragment;
import com.binfenjiari.fragment.NaviHomePageFragment;
import com.binfenjiari.fragment.NaviMineCenterFragment;
import com.biu.modulebase.common.base.BaseActivity2;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/7
 */
public class NavigationActivity extends BaseActivity2 {

    private ViewPager mViewPager;

    private RadioGroup mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        initView();
        MyApplication.enablePush();

    }

    private void initView() {

        mViewPager = (ViewPager) findViewById(com.biu.modulebase.binfenjiari.R.id.viewPager);
        mViewPager.setOffscreenPageLimit(2);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mGroup = (RadioGroup) findViewById(com.biu.modulebase.binfenjiari.R.id.group);
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int position = group.indexOfChild(group.findViewById(checkedId));
                switch (position) {
                    case 0:
//                        setToolBarTitle("享去");
                        break;
                    case 1:
//                        setToolBarTitle("基地");
                        break;
                    case 2:
//                        setToolBarTitle("视听");
                        break;
                }
                mViewPager.setCurrentItem(position, false);

            }
        });
        mGroup.check(R.id.rb_home);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    return NaviHomePageFragment.newInstance();
                case 1:
                    return NaviCommunityFragment.newInstance();
                case 2:
                    return NaviMineCenterFragment.newInstance();
            }
            return NaviHomePageFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 4;
        }

    }
}
