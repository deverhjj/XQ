package com.biu.modulebase.binfenjiari.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.fragment.CircleFragment;
import com.biu.modulebase.binfenjiari.fragment.CommFreshThingsFragment;
import com.biu.modulebase.binfenjiari.fragment.CommQAFragment;
import com.biu.modulebase.binfenjiari.fragment.CommVoteFragment2;
import com.biu.modulebase.binfenjiari.widget.DialogFactory;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @Title: {交流}
 * @Description:{}
 * @date 2016/4/13
 */
public class CommActivity extends BaseActivity {
    private static final String TAG = "CommActivity";
    /**发布新鲜事（没有标题）**/
    public static final int SEND_POST =0;
    /**发布问答**/
    public static final int SEND_QUESTION=1;
    /**发布帖子**/
    public static final int SEND_CARD=2;
    private static final int CIRCLE_PAGE_COUNT = 3;
    private static final String[] TABNAMES = {"新鲜事", "投票", "问答"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabLayout tabLayout = (TabLayout) setToolBarCustomView(R.layout.layout_tab_2,new Toolbar
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        FrameLayout contentContainer = (FrameLayout) getFragmentContainer();
        ViewPager viewPager = new ViewPager(this);
        viewPager.setId(R.id.id_vp);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new CirclePagerAdapter(getSupportFragmentManager()));
        contentContainer.addView(viewPager,new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tabLayout.setupWithViewPager(viewPager);
        setBackNavigationIcon();

        viewPager.setCurrentItem(1);
    }

    @Override
    protected Fragment getFragment() {
        return null;
    }

    @Override
    protected String getToolbarTitle() {
        return null;
    }


    private class CirclePagerAdapter extends FragmentPagerAdapter {

        public CirclePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Intent intent = getIntent();
            int circleId = intent != null ? intent.getIntExtra(CircleFragment.EXTRA_CIRCLE_ID, -1)
                    : -1;
            switch (position) {

                case 0:
                   return new CommFreshThingsFragment();
                case 1:
                    return new CommVoteFragment2();
                case 2:
                    return new CommQAFragment();

                default:
                    break;
            }
            return null;
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
        getMenuInflater().inflate(R.menu.common_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_post_card) {
            DialogFactory.showDialog(this, R.layout.dialog_post, R.style.CustomDialog,
                    android.R.style.Animation_Dialog, Gravity.BOTTOM, 1f, 0,
                    new DialogFactory.DialogListener() {
                        @Override
                        public void OnInitViewListener(View v, final Dialog dialog) {
                            View close=v.findViewById(R.id.close);
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            v.findViewById(R.id.post_fresh).setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            post(SEND_POST);
                                            dialog.dismiss();
                                        }
                                    });
                            v.findViewById(R.id.post_qa).setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            post(SEND_QUESTION);
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case SEND_POST:

                    break;
                case SEND_QUESTION:

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void post(int tag) {
        Intent i = getIntent();
        Intent intent=new Intent(CommActivity.this,CardPostActivity.class);
        intent.putExtra("tag",tag);
        startActivityForResult(intent,tag);
    }

}
