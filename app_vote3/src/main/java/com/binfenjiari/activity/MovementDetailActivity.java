package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.binfenjiari.R;
import com.binfenjiari.fragment.CommentParentSubFragment;
import com.binfenjiari.fragment.MovementDetailSubFragment;
import com.binfenjiari.widget.HeadViewMovementDetail;
import com.biu.modulebase.binfenjiari.fragment.CircleFragment;
import com.biu.modulebase.common.base.BaseActivity;


/**
 * @Title: {活动详情}
 * @Description:{}
 * @date 2016/4/13
 */
public class MovementDetailActivity extends BaseActivity {
    private static final String TAG = "MovementDetailActivity";
    private static final int CIRCLE_PAGE_COUNT = 2;
    private static final String[] TABNAMES = {"活动详情", "评价(5)"};

    private int currPosition = 0;

    private LinearLayout ll_head_content;
    private TabLayout tab_layout;
    private ViewPager viewpager_content;
    private CommentParentSubFragment mCommentFragment;

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, MovementDetailActivity.class);
//        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);

    }
        //
    @Override
    protected void setActView() {
        setContentView(R.layout.activity_movement_detail_tablayout);

        setTitle(null);
        layout_app_bar = (AppBarLayout) findViewById(R.id.layout_app_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("活动详情");

        ll_head_content = (LinearLayout) findViewById(R.id.ll_head_content);
        ll_head_content.removeAllViews();
        loadData();

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        viewpager_content = (ViewPager) findViewById(R.id.viewpager_content);

        viewpager_content.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tab_layout.setupWithViewPager(viewpager_content);
        setBackNavigationIcon();


    }

    public void loadData(){
        HeadViewMovementDetail headView = new HeadViewMovementDetail(this);
        headView.setGroupView(ll_head_content);
    }

    @Override
    protected Fragment getFragment() {
        return null;
    }

    @Override
    protected String getToolbarTitle() {
        return null;
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            currPosition = position;
            Intent intent = getIntent();
            String circleId = intent != null ? intent.getStringExtra(CircleFragment.EXTRA_CIRCLE_ID)
                    : "";
            if (position == 1) {
                return mCommentFragment = CommentParentSubFragment.newInstance();
            } else {
                return MovementDetailSubFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return CIRCLE_PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TABNAMES[position];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_more) {
//            OtherUtil.showMoreOperate(EventDetailFragment.this, mId, eventHeader.getName(), eventHeader.getRemark(), null, Constant.SHARE_ACTIVITY,
//                    Constant.REPORT_ACTIVITY, null, false, null, -1, null);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        getSupportFragmentManager().getFragments().get(1).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


}
