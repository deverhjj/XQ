package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.CardAllFragment;
import com.biu.modulebase.binfenjiari.fragment.CardEssenceFragment;
import com.biu.modulebase.binfenjiari.fragment.CircleFragment;
import com.biu.modulebase.binfenjiari.fragment.CircleItemFragment;
import com.biu.modulebase.binfenjiari.fragment.CircleManageFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ExitCircleDialogFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ShareDialogFragment;
import com.biu.modulebase.binfenjiari.model.AnnounceVO;
import com.biu.modulebase.binfenjiari.model.CircleDetailVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;


/**
 * @Title: {全部/精华帖子列表}
 * @Description:{}
 * @date 2016/4/13
 */
public class CircleDetailActivity extends BaseActivity {
    private static final String TAG = "CircleDetailActivity";
    private static final int CIRCLE_PAGE_COUNT = 2;
    private static final String[] TABNAMES = {"全部", "精华"};

    private int currPosition = 0;

    private LinearLayout ll_head_content;
    private TabLayout tab_layout;
    private ViewPager vp_content;
    private CircleDetailVO mCircleDetailVO;
    private CardAllFragment mCardAllFragment;

    @Override
    protected void setActView() {
        setContentView(R.layout.activity_circle_detail);

        setTitle(null);
        layout_app_bar = (AppBarLayout) findViewById(R.id.layout_app_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("圈子详情");

        ll_head_content = (LinearLayout) findViewById(R.id.ll_head_content);
        ll_head_content.removeAllViews();
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        vp_content = (ViewPager) findViewById(R.id.vp_content);

        vp_content.setAdapter(new CirclePagerAdapter(getSupportFragmentManager()));
        tab_layout.setupWithViewPager(vp_content);
        setBackNavigationIcon();

        loadHeaderData();

    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setBackNavigationIcon();
//        TabLayout tabLayout = (TabLayout) setToolBarCustomView(R.layout.layout_tab);
//        FrameLayout contentContainer = (FrameLayout) getFragmentContainer();
//        ViewPager viewPager = new ViewPager(this);
//        viewPager.setId(R.id.id_vp);
//        viewPager.setAdapter(new CirclePagerAdapter(getSupportFragmentManager()));
//        contentContainer.addView(viewPager,new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        tabLayout.setupWithViewPager(viewPager);
//        setBackNavigationIcon();
//    }


    private void loadHeaderData() {
        JSONObject params = new JSONObject();
        Intent i = getIntent();
        String circleId = i != null ? i.getStringExtra(CircleFragment.EXTRA_CIRCLE_ID)
                : "";
        try {
            params.put("token", PreferencesUtils.getString(this, PreferencesUtils.KEY_TOKEN));
            params.put("circle_id", circleId);
            params.put("model", Constant.MODEL_CIRCLE);
            params.put("action", Constant.ACTION_GET_CIRCLE_DETAIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                CircleDetailVO bean = JSONUtil.fromJson(jsonString, CircleDetailVO.class);
                loadHeaderCircleetail(bean);
//                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
//                adapter.removeAllData();
//                adapter.addData(BaseAdapter.AddType.FIRST, Collections.singletonList(bean));
//                loadCards(Constant.LIST_REFRESH);
//                inVisibleLoading();

            }

            @Override
            public void onCodeError(int key, String message) {
                if (key == 3) {
                } else {
                    showTost(message, 1);
                }
            }

            @Override
            public void onConnectError(String message) {
//                visibleNoNetWork();
            }
        });
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
            currPosition = position;
            Intent intent = getIntent();
            String circleId = intent != null ? intent.getStringExtra(CircleFragment.EXTRA_CIRCLE_ID)
                    : "";
            if (position == 0) {
                return mCardAllFragment = CardAllFragment.newInstance(circleId);
            } else {
                return CardEssenceFragment.newInstance(circleId);
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
        getMenuInflater().inflate(R.menu.circle_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = getIntent();
        String circleId = i != null ? i.getStringExtra(CircleFragment.EXTRA_CIRCLE_ID)
                : "";
        int i1 = item.getItemId();
        if (i1 == R.id.action_post_card) {
            Intent intent = new Intent(this, CardPostActivity.class);
            intent.putExtra(CircleFragment.EXTRA_CIRCLE_ID, circleId);
            intent.putExtra("tag", CommActivity.SEND_CARD);
            startActivityForResult(intent, CardAllFragment.REQUEST_CARD_ADD);

        } else if (i1 == R.id.action_search) {
            Intent searchIntent = new Intent(CircleDetailActivity.this, SearchActivity.class);
            searchIntent.putExtra(Constant.SEARCH_TAG, Constant.SEARCH_POST);
            searchIntent.putExtra(CircleFragment.EXTRA_CIRCLE_ID, circleId);
            startActivity(searchIntent);
            overridePendingTransition(0, R.anim.fade_in);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getSupportFragmentManager().getFragments().get(1).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private TextView btn_join;
    public void loadHeaderCircleetail(CircleDetailVO bean) {
        mCircleDetailVO = bean;
        View view = LayoutInflater.from(this).inflate(R.layout.header_circle_detail, ll_head_content, true);
//        holder.setNetImage(Constant.IMG_COMPRESS,R.id.iv_head_portrait, bean.getIntro_img(), ImageDisplayUtil.DISPLAY_ROUND_IMAGE);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_head_portrait);
        ImageDisplayUtil.displayImage(Constant.IMG_COMPRESS,bean.getIntro_img(),imageView,ImageDisplayUtil.DISPLAY_PERSONAL_HEADER);
        ((TextView) view.findViewById(R.id.tv_name)).setText(bean.getName());
        ((TextView) view.findViewById(R.id.tv_subject)).setText(bean.getIntro_content());
        ((TextView) view.findViewById(R.id.tv_circle_number)).setText(String.format(getString(R.string.circle_join_num), bean.getUser_n()));
        ((TextView) view.findViewById(R.id.tv_card_number)).setText(String.format(getString(R.string.circle_post_num), bean.getPost_n()));
//        holder.setText(R.id.tv_name,bean.getName());
//        holder.setText(R.id.tv_subject,bean.getIntro_content());
//        holder.setText(R.id.tv_circle_number, String.format(getString(R.string.circle_join_num), bean.getUser_n()));
//        holder.setText(R.id.tv_card_number, String.format(getString(R.string.circle_post_num), bean.getPost_n()));
        int idType = Utils.isInteger(bean.getType());
        btn_join = (TextView) view.findViewById(R.id.btn_join);//holder.getView(R.id.btn_join);
        if(idType==4){
            btn_join.setVisibility(View.GONE);
        }else{
            if(idType ==2|| idType ==3){
                btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_orange));
                btn_join.setTextColor(getResources().getColor(R.color.colorAccent));
                btn_join.setText("管理");
            }else if(idType==1){//已加入的普通会员
                btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_gray));
                btn_join.setTextColor(getResources().getColor(R.color.colorTextGray));
                btn_join.setText("退出");
            }else{
                btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_orange));
                btn_join.setTextColor(getResources().getColor(R.color.colorAccent));
                btn_join.setText("加入");
            }
            btn_join.setVisibility(View.VISIBLE);
            btn_join.setOnClickListener(this);
        }
//                                        changeBtnAppear(btn_join,status);

        LinearLayout layout_notice = (LinearLayout) view.findViewById(R.id.layout_notice);//holder.getView(R.id.layout_notice);
        layout_notice.removeAllViews();
        List<AnnounceVO> list = bean.getAnnounce_list();
        if(list!=null &&list.size()>0){
            for(int i =0;i<list.size();i++){
                AnnounceVO beanv =list.get(i);
                View notice =View.inflate(this,R.layout.part_circle_detail_header_notice,null);
                TextView content = (TextView) notice.findViewById(R.id.tv_notice);
                content.setText(OtherUtil.filterSensitives(this,beanv.getTitle()));
                layout_notice.addView(notice);
                notice.setOnClickListener(new CardAllFragment.NoticeClickListener(this,beanv));
            }
        }
    }

    public void updateBtn(CircleDetailVO bean){
        int idType = Utils.isInteger(bean.getType());
        if(idType==4){
            btn_join.setVisibility(View.GONE);
        }else{
            if(idType ==2|| idType ==3){
                btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_orange));
                btn_join.setTextColor(getResources().getColor(R.color.colorAccent));
                btn_join.setText("管理");
            }else if(idType==1){//已加入的普通会员
                btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_gray));
                btn_join.setTextColor(getResources().getColor(R.color.colorTextGray));
                btn_join.setText("退出");
            }else{
                btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_orange));
                btn_join.setTextColor(getResources().getColor(R.color.colorAccent));
                btn_join.setText("加入");
            }
            btn_join.setVisibility(View.VISIBLE);
            btn_join.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_join) {
            if(mCircleDetailVO==null)
                return;
//            vp_content.setCurrentItem(0);

            mCardAllFragment.btnClick(mCircleDetailVO);

        }
    }
}
