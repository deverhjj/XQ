package com.biu.modulebase.binfenjiari.fragment;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CircleCreatedMyActivity;
import com.biu.modulebase.binfenjiari.activity.CollectionActivity;
import com.biu.modulebase.binfenjiari.activity.MyExerciseActivity;
import com.biu.modulebase.binfenjiari.activity.MyQRcodeActivity;
import com.biu.modulebase.binfenjiari.activity.MyShareActivity;
import com.biu.modulebase.binfenjiari.activity.PersonalInfoActivity;
import com.biu.modulebase.binfenjiari.activity.SettingActivity;
import com.biu.modulebase.common.base.BaseFragment;


public class AppMainFragment extends BaseFragment {

    private static final String TAG = "AppMainFragment";
    private  ViewPager mViewPager;

    private FrameLayout nav_main;
    private LinearLayout nav;

//    private FrameLayout info ;
//    private FrameLayout huodong ;
//    private FrameLayout jidi ;
//    private FrameLayout shoucang ;
//    private FrameLayout circle;
//    private FrameLayout erweima;
//    private FrameLayout share;
//    private FrameLayout shezhi;

    private AnimatorSet setShow1;
    private AnimatorSet setShow2;
    private AnimatorSet setShow3;
    private AnimatorSet setShow4;
    private AnimatorSet setShow5;
    private AnimatorSet setShow6;
    private AnimatorSet setShow7;
    private AnimatorSet setShow8;

    private AnimatorSet sethide1;
    private AnimatorSet sethide2;
    private AnimatorSet sethide3;
    private AnimatorSet sethide4;
    private AnimatorSet sethide5;
    private AnimatorSet sethide6;
    private AnimatorSet sethide7;
    private AnimatorSet sethide8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_app_main, container, false);
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
//        android.support.v7.widget.Toolbar toolbar =getBaseActivity().getToolbar();
//        toolbar.setNavigationIcon(R.mipmap.hd_grzx);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getBaseActivity().showNav();
//            }
//        });
//        DrawerLayout drawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
////        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        getBaseActivity().getSupportActionBar().setHomeButtonEnabled(true);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                getActivity(), drawer, getBaseActivity().getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        toggle.syncState();
//        drawer.addDrawerListener(toggle);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        RadioGroup group = (RadioGroup) rootView.findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position =group.indexOfChild(group.findViewById(checkedId));
                mViewPager.setCurrentItem(position, false);
            }
        });

//        getBaseActivity().initNav();

    }




    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.nav_main) {//                getBaseActivity().hideNav();

        } else if (i == R.id.info) {
            startActivity(new Intent(getActivity(), PersonalInfoActivity.class));

        } else if (i == R.id.huodong) {
            startActivity(new Intent(getActivity(), MyExerciseActivity.class));

        } else if (i == R.id.shangcheng) {
            getBaseActivity().showTost("改成积分商城了...", 1);
//                startActivity(new Intent(getActivity(),ReservationJidiActivity.class));

        } else if (i == R.id.shoucang) {
            startActivity(new Intent(getActivity(), CollectionActivity.class));

        } else if (i == R.id.circle) {
            startActivity(new Intent(getActivity(), CircleCreatedMyActivity.class));

        } else if (i == R.id.shezhi) {
            startActivity(new Intent(getActivity(), SettingActivity.class));

        } else if (i == R.id.erweima) {
            startActivity(new Intent(getActivity(), MyQRcodeActivity.class));

        } else if (i == R.id.share) {
            startActivity(new Intent(getActivity(), MyShareActivity.class));

        }
    }

//    @Override
//    public void onBackPressed() {
////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////        if (drawer.isDrawerOpen(GravityCompat.START)) {
////            drawer.closeDrawer(GravityCompat.START);
////        } else {
//            super.onBackPressed();
////        }
//    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position){
                case 0:
                    return new HomeFragment();
                case 1:
                    return new JidiFragment();
                case 2:
                    return new AvFragment();
                case 3:
                    return new CircleFragment();
            }
            return  new HomeFragment();
        }

        @Override
        public int getCount() {
            return 4;
        }

    }


}
