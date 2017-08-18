package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.fragment.AvFragment;
import com.biu.modulebase.binfenjiari.fragment.CircleFragment;
import com.biu.modulebase.binfenjiari.fragment.HomeFragment;
import com.biu.modulebase.binfenjiari.fragment.JidiFragment;
import com.biu.modulebase.binfenjiari.fragment.PersonalFragment;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.binfenjiari.service.DevTokenService;
import com.biu.modulebase.binfenjiari.service.SensitiveWordsService;
import com.biu.modulebase.binfenjiari.util.CheckUpdate;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.NetworkUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.MyNavigationView;
import com.biu.modulebase.binfenjiari.widget.QuickReturnFooterBehavior;
import com.biu.modulebase.binfenjiari.widget.QuickReturnHeaderBehavior;
import com.biu.modulebase.common.base.BaseActivity2;

import java.util.ArrayList;
import java.util.List;

public class
MainActivity extends BaseActivity2 implements MyNavigationView.NavigationViewCallBack {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawer;

    private CoordinatorLayout mCoordinatorLayout;

    private AppBarLayout mAppBarLayout;

    private Toolbar mToolbar;

    private TextView title;

    private ViewPager mViewPager;

    private RadioGroup mGroup;

    private MyNavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        MyApplication.enablePush();
    }

    private void initView() {
        mToolbar=(Toolbar) findViewById(R.id.toolbar);
        mCoordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mAppBarLayout= (AppBarLayout) findViewById(R.id.layout_app_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(0);
            mAppBarLayout.setOutlineProvider(null);
        }

        navView =(MyNavigationView)findViewById(R.id.nav_view);
        navView.setCallBack(this);
        setSupportActionBar(mToolbar);
//        mToolbar.setNavigationIcon(R.mipmap.hd_grzx);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(Utils.isEmpty(PreferencesUtils.getString(getApplicationContext(), PreferencesUtils.KEY_TOKEN))|| MyApplication.getUserInfo(getApplicationContext())==null) {
//                    showLoginSnackbar();
//                    return;
//                }
                navView.show();
            }
        });

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                adjustBehavior(position);
        }});

        mGroup = (RadioGroup)findViewById(R.id.group);
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int position =group.indexOfChild(group.findViewById(checkedId));
                switch (position){
                    case 0:
                        setToolBarTitle("享去");
                        break;
                    case 1:
                        setToolBarTitle("基地");
                        break;
                    case 2:
                        setToolBarTitle("社区");
                        break;
                    case 3:
                        setToolBarTitle("视听");
                        break;
                    case 4:
                        setToolBarTitle("个人中心");
                        break;
                }
                mViewPager.setCurrentItem(position, false);

            }
        });
        mGroup.check(R.id.huodongBtn);
        adjustNormalBehavior();
    }


    public void quickShowTabLayout() {
        ViewGroup layout_tab = (ViewGroup) findViewById(R.id.layout_tab_main);
        if (layout_tab == null) return;
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) layout_tab.getLayoutParams();
        QuickReturnFooterBehavior behavior = (QuickReturnFooterBehavior) layoutParams.getBehavior();
        behavior.quickShow(layout_tab);
    }


    /**
     * 设置标题
     * @param titleString
     */
    public void setToolBarTitle(String titleString) {
        if (title == null)
            title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(titleString);
    }

    @Override
    public void OnNavItemClick(int currClickPosition) {
        switch (currClickPosition){
            case 0:
                if(Utils.isEmpty(PreferencesUtils.getString(getApplicationContext(), PreferencesUtils.KEY_TOKEN))|| MyApplication.getUserInfo(getApplicationContext())==null) {
                    startActivity(new Intent(MainActivity.this, UnLoginActivity.class));
                    return;
                }
                UserInfoBean userInfoVO =MyApplication.getUserInfo(getApplicationContext());
                Intent intent =new Intent(this,PersonalInfoActivity.class);
                intent.putExtra(Constant.KEY_ID,userInfoVO==null?"":userInfoVO.getId());
                startActivity(intent);
                break;
            case 1:
                if(Utils.isEmpty(PreferencesUtils.getString(getApplicationContext(), PreferencesUtils.KEY_TOKEN))|| MyApplication.getUserInfo(getApplicationContext())==null) {
                    showLoginSnackbar();
                    return;
                }
                startActivity(new Intent(this,MyExerciseActivity.class));
                break;
            case 2:
                showTost("积分商城,敬请期待...",0);
//                startActivity(new Intent(this,ReservationJidiActivity.class));
                break;
            case 3:
                if(Utils.isEmpty(PreferencesUtils.getString(getApplicationContext(), PreferencesUtils.KEY_TOKEN))|| MyApplication.getUserInfo(getApplicationContext())==null) {
                    showLoginSnackbar();
                    return;
                }
                startActivity(new Intent(this,CollectionActivity.class));
                break;
            case 4:
                if(Utils.isEmpty(PreferencesUtils.getString(getApplicationContext(), PreferencesUtils.KEY_TOKEN))|| MyApplication.getUserInfo(getApplicationContext())==null) {
                    showLoginSnackbar();
                    return;
                }
                startActivity(new Intent(this,CircleCreatedMyActivity.class));
                break;
            case 5:
                startActivity(new Intent(this,SettingActivity.class));
                break;
            case 6:
                showTost("我的二维码,敬请期待...",0);
//                startActivity(new Intent(this,MyQRcodeActivity.class));
                break;
            case 7:
                startActivity(new Intent(this,MyShareActivity.class));
                break;
        }
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
                    return new HomeFragment();
                case 1:
                    return new JidiFragment();
                case 2:
                    return new CircleFragment();
                case 3:
                    return new AvFragment();
                case 4:
                    return new PersonalFragment();
            }
            return  new HomeFragment();
        }

        @Override
        public int getCount() {
            return 5;
        }

    }



    public int getTabJiDiLayoutHeight() {
        View tabJidi = findViewById(R.id.layout_tab_jidi);
        return tabJidi != null ? tabJidi.getMeasuredHeight() : 0;
    }


    private void adjustBehavior(int position) {
        View header=null;
        switch (position) {
            case 0:
            case 1:
                adjustNormalBehavior();
//                adjustQuickReturnBehavior();
                break;
            case 2:
                adjustNormalBehavior();
                break;
            case 3:
                //为了迎合布局里的位置
                header=adjustTabLayoutBehavior(position-2);
                break;
            case 4:
                adjustNormalBehavior();
                break;
        }

        for (int i = 0; i < mCallbacks.size(); i++) {
            mCallbacks.get(i).onPageSelected(position, header);
        }

    }

    private void adjustNormalBehavior() {
        CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
//        lp.setBehavior(null);
        ((CoordinatorLayout.LayoutParams) mViewPager.getLayoutParams()).setBehavior(
                new AppBarLayout.ScrollingViewBehavior());

        AppBarLayout.LayoutParams lp2 = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        lp2.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        final int count = mAppBarLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mAppBarLayout.getChildAt(i);
            if (child != null && !(child instanceof Toolbar) && child.isShown()&&
                    child.getId() != R.id.divider) {
                child.setVisibility(View.GONE);
                Log.e(TAG, "GONE-------------N------"+child);
            }
        }
    }

    /**
     * 调整 AppBarLayout 的 Behavior 为 QuickReturnHeaderBehavior
     * 该行为会根据 RecyclerView 的垂直滚动行为自动动画显示隐藏 AppBarLayout
     */
    private void adjustQuickReturnBehavior() {
        final int count = mAppBarLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mAppBarLayout.getChildAt(i);
            if (child != null && !(child instanceof Toolbar) && child.isShown() &&
                    child.getId() != R.id.divider)
            {
                child.setVisibility(View.GONE);
                Log.e(TAG, "GONE-------------Q------"+child);
            }
        }
        //设置快速返回 Behavior
        CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        lp.setBehavior(new QuickReturnHeaderBehavior());

        //重置 ViewPager 为无行为
        ((CoordinatorLayout.LayoutParams) mViewPager.getLayoutParams()).setBehavior(null);
    }

    /**
     * 调整 AppBarlayout 里有 TabLayout 布局 的 Behavior
     * 为了调整为 TabLayout 滚动到顶部时置顶显示
     * 需要调整的地方：
     * 1.如果 AppBarlayout 容器里之前没有添加(第一次切换到拥有 TabLayout 的页面时) TabLayout
     * 那么添加并设置 TabLayout 布局参数为 AppBarlayout 布局属性：可滚动并且滚动或者折叠到设置给 TabLayout 的
     * miniHeight 时为止
     * 2.调整 AppBarlayout 的 Behavior 为 AppBarlayout的默认 Behavior
     * 3.设置 ToolBar 滚动 Flag 为：可滚动并且  ENTER_ALWAYS
     * 4.设置 ViewPager 的 Behavior 为 AppBarlayout.ScrollingViewBehavior
     */
    private View adjustTabLayoutBehavior(final int position) {

        View child=mAppBarLayout.getChildAt(position);

        if (child==null) {
             return null;
        }

        final int count = mAppBarLayout.getChildCount();
        for (int i = 1; i < count; i++) {
            View childView = mAppBarLayout.getChildAt(i);
            if (childView != null && !(childView instanceof Toolbar) &&
                    childView.getId() != R.id.divider &&
                    i != position)
            {
                childView.setVisibility(View.GONE);
                Log.e(TAG, "GONE-------------------"+childView);
            }
        }

        child.setVisibility(View.VISIBLE);

        CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        lp.setBehavior(new AppBarLayout.Behavior());

        AppBarLayout.LayoutParams lp2 = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        lp2.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        ((CoordinatorLayout.LayoutParams) mViewPager.getLayoutParams()).setBehavior(
                new AppBarLayout.ScrollingViewBehavior());


        return child;
//        if (child==null) {
//
//            Log.e(TAG, "add--------TabLayout-----------");
//            final TabLayout tabLayout= (TabLayout) getLayoutInflater()
//                    .inflate( R.layout.header_av,
//                            mAppBarLayout, false);
//            AppBarLayout.LayoutParams tabLp = new AppBarLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            tabLp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
//                    AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
//            //                        tabLayout.setMinimumHeight(100);
//            tabLayout.setLayoutParams(tabLp);
//            mAppBarLayout.addView(tabLayout);
//
//            tabLayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    tabLayout.setMinimumHeight(tabLayout.getMeasuredHeight());
//                }
//            });
//
//            CoordinatorLayout.LayoutParams lp =
//                    (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
//            lp.setBehavior(new AppBarLayout.Behavior());
//
//            AppBarLayout.LayoutParams lp2 = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
//            lp2.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
//                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
//
//            ((CoordinatorLayout.LayoutParams) mViewPager.getLayoutParams()).setBehavior(
//                    new AppBarLayout.ScrollingViewBehavior());
//
//            mCallbackses.get(position-1).onPageSelected(position, tabLayout);
//
//        } else {
//            Log.e(TAG, "add--------VISIBLE-----------");
//            child.setVisibility(View.VISIBLE);
//
//            CoordinatorLayout.LayoutParams lp =
//                    (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
//            lp.setBehavior(new AppBarLayout.Behavior());
//
//            AppBarLayout.LayoutParams lp2 = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
//            lp2.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
//                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
//
//            ((CoordinatorLayout.LayoutParams) mViewPager.getLayoutParams()).setBehavior(
//                    new AppBarLayout.ScrollingViewBehavior());
//
//
//            mCallbackses.get(position-1).onPageSelected(position, (TabLayout) child);
//
//
//        }
    }

    private List<Callbacks> mCallbacks=new ArrayList<>(2);

    public interface Callbacks {
        void onPageSelected(int position , View header);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof Callbacks) {
            Log.e(TAG, "fragment==========>" + fragment);
            mCallbacks.add((Callbacks) fragment);
        }
    }

    public Toolbar getToolbar(){
        if(mToolbar==null)
            mToolbar=(Toolbar) findViewById(R.id.toolbar);
        return mToolbar;

    }

    public AppBarLayout getAppBarLayout(){
        return mAppBarLayout;
    }

    private void showLoginSnackbar(){
        Snackbar snackbar = Snackbar.make(mDrawer, "未登录，请先登录", Snackbar.LENGTH_SHORT).setAction("去登录",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, UnLoginActivity.class));
                    }
                });
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.white));
        snackbar.show();
    }

    private static final int UPDATE = 1;// 可更新
    private static final int CONTINUE = 2;// 无需更新
    private static final int UPDATE_BACKGROUND = 3;// 更新进程在运行
    private CheckUpdate checkUpdate;

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtil.isNetworkConnected(getApplicationContext()) && MyApplication.allow_update) {
            checkUpdate = new CheckUpdate(this, mHandler);
            // 检查更新
            checkUpdate.checkUpdate();
        }
        String tomorrow = PreferencesUtils.getString(getApplicationContext(),PreferencesUtils.KEY_TOMORROW_ZERO_AM);
        if(!Utils.isEmpty(tomorrow)&&Utils.dateComparator2(tomorrow,Utils.getCurrentDate2())){
            PreferencesUtils.putBoolean(getApplicationContext(),  PreferencesUtils.KEY_SENSITIVE, false);
        }
        if (!PreferencesUtils.getBoolean(getApplicationContext(), PreferencesUtils.KEY_SENSITIVE)) {
            Intent service = new Intent(this, SensitiveWordsService.class);
            startService(service);
        }

        if(Utils.isEmpty(PreferencesUtils.getString(getApplicationContext(), PreferencesUtils.KEY_TOKEN)))
            return;
//         //这个版本改成一天上传一次  下个版本改成只上传一次
//        String tomorrow2 = PreferencesUtils.getString(getApplicationContext(),PreferencesUtils.KEY_UMENG_TOKEN_TOMORROW_ZERO_AM);
//        if(!Utils.isEmpty(tomorrow2)&&Utils.dateComparator2(tomorrow2,Utils.getCurrentDate2())){
//            PreferencesUtils.putBoolean(getApplicationContext(),  PreferencesUtils.KEY_UMENG_TOKEN, false);
//        }
//        if (!PreferencesUtils.getBoolean(getApplicationContext(), PreferencesUtils.KEY_UMENG_TOKEN)) {
//            Intent service = new Intent(this, DevTokenService2.class);
//            startService(service);
//        }
         if (!PreferencesUtils.getBoolean(getApplicationContext(), PreferencesUtils.KEY_UMENG_TOKEN)) {
            Intent service = new Intent(this, DevTokenService.class);
            startService(service);
        }
        if(MyApplication.userInfo ==null){
            String userData= PreferencesUtils.getString(getApplicationContext(), PreferencesUtils.KEY_USER_INFO);
            MyApplication.userInfo = JSONUtil.fromJson(userData, UserInfoBean.class);
        }
    }

    // 用于接收线程发送的消息
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    checkUpdate.showNoticeDialog();
                    break;
                case CONTINUE:// 程序继续运行
                    break;
                case UPDATE_BACKGROUND:// 更新程序在后台运行
                    break;
                default:
                    break;
            }
        }
    };

    public CoordinatorLayout getCoordinatorLayout() {
        return mCoordinatorLayout;
    }

    public DrawerLayout getmDrawer(){
        return mDrawer;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        LogUtil.LogE(TAG,"onConfigurationChanged");

        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                LogUtil.LogE(TAG, "ORIENTATION_LANDSCAPE");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                LogUtil.LogE(TAG, "ORIENTATION_PORTRAIT");
                break;
        }

    }

}
