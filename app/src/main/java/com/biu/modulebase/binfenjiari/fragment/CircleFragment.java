package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CircleAllActivity;
import com.biu.modulebase.binfenjiari.activity.CircleCreatedMyActivity;
import com.biu.modulebase.binfenjiari.activity.CircleDetailActivity;
import com.biu.modulebase.binfenjiari.activity.CommActivity;
import com.biu.modulebase.binfenjiari.activity.NotificationActivity;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ExitCircleDialogFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ShareDialogFragment;
import com.biu.modulebase.binfenjiari.model.CircleIndexVO;
import com.biu.modulebase.binfenjiari.model.CircleVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONObject;

/**
 * @Title: {圈子Tab}
 * @Description:{主界面基地tab页}
 * @date 2016/4/13
 */
public class CircleFragment extends BaseFragment {
    private static final String TAG = "CircleFragment";
    public static final String EXTRA_CIRCLE_ID = "circleId";

    private static final Object TAG_CIRCLE_MY="tag_circle_my";

    private static final Object TAG_CIRCLE_RECO="tag_circle_reco";

    private static final Object TAG_BTN_JOIN="tag_join";

    private LSwipeRefreshLayout mRefreshLayout;

    private CircleIndexVO circleIndexVO =new CircleIndexVO();

    /**退出圈子requestCode**/
    private static final int TARGET_REQUESST_CODE_EXIT_CIRCLE =1;
//    private Circle mCircle;
    /**当前点击的加入按钮**/
    private TextView joinBtn;
    /**当前点击的圈子加入的人数**/
    private TextView joinNumText;

    private LinearLayout layout_circle_my;
    private ViewGroup layout_circle_none;

    private LinearLayout layout_circle_recommend;
    private ViewGroup layout_my_circle;

//    private TextView tv_sun_number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //标记下当前的登录状态，提供后期回到该界面是否需要刷新界面
        MyApplication.sRefreshMap.put(TAG,!OtherUtil.hasLogin(getActivity()));
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle, container,
                false);
        return super.onCreateView(inflater,rootView,savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //显示加载界面
        visibleLoading();
        super.onActivityCreated(savedInstanceState);
    }

    protected void initView(View rootView) {
        if (rootView == null) {
            return;
        }
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.LogE(TAG,"onRefresh******************");
//                loadData();
                getCircleIndex();
            }

            @Override
            public void onLoadMore() {
            }
        });


        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.layout_circle_comm);
        viewGroup.setOnClickListener(this);

        layout_circle_my= (LinearLayout) rootView.findViewById(R.id.layout_circle_my);
        layout_circle_none= (ViewGroup) rootView.findViewById(R.id.layout_circle_none);
        layout_circle_none.setOnClickListener(this);

        layout_my_circle = (ViewGroup) rootView.findViewById(R.id.layout_my_circle);
        layout_my_circle.setOnClickListener(this);

        layout_circle_recommend = (LinearLayout) rootView.findViewById(
                R.id.layout_circle_recommend);

//        Button btn_all_group = (Button)
        rootView.findViewById(R.id.btn_all_group).setOnClickListener(this);
//        btn_all_group

//        tv_sun_number= (TextView) rootView.findViewById(R.id.tv_sun_number);


//        TestCenter.setClickListener(null, rootView.findViewById(R.id.layout_circle_my_1),
//                rootView.findViewById(R.id.layout_circle_my_2),
//                rootView.findViewById(R.id.layout_circle_recommend_1),
//                rootView.findViewById(R.id.layout_circle_recommend_2));
    }

    @Override
    public void loadData() {
//        getCircleIndex();
    }

    private void getCircleIndex(){
        final String token = PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN);
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"token",token);
        JSONUtil.put(params,"model",Constant.MODEL_CIRCLE);
        JSONUtil.put(params,"action",Constant.ACTION_CIRCLE_MAIN);
        jsonRequest(false, params, Constant.SERVERURL, TAG, new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                mRefreshLayout.stopRefresh();
                LogUtil.LogE(TAG,"response--->"+jsonString.toString());
                circleIndexVO =JSONUtil.fromJson(jsonString,CircleIndexVO.class);
                circleIndexVO.getCircleList();
                if(circleIndexVO.getMyList().size() >0){
                    getView().findViewById(R.id.more).setVisibility(View.VISIBLE);
                    layout_my_circle.setOnClickListener(CircleFragment.this);
                }else{
                    getView().findViewById(R.id.more).setVisibility(View.GONE);
                    layout_my_circle.setOnClickListener(null);
                }
                //根据我加入的圈子和推荐的圈子动态创建圈子 Item 视图
                createCircleItemViews(circleIndexVO);
                inVisibleLoading();
            }

            @Override
            public void onCodeError(int key, String message) {
                mRefreshLayout.stopRefresh();
                createCircleItemViews(new CircleIndexVO());
            }

            @Override
            public void onConnectError(String message) {
                mRefreshLayout.stopRefresh();
                createCircleItemViews(new CircleIndexVO());
                visibleNoNetWork();
                showTost(message,1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_all_group) {
            startActivity(new Intent(getActivity(), CircleAllActivity.class));

        } else if (i == R.id.layout_circle_comm) {
            startActivity(new Intent(getActivity(), CommActivity.class));

        } else if (i == R.id.layout_circle_none) {
            startActivity(new Intent(getActivity(), CircleAllActivity.class));

        } else if (i == R.id.layout_my_circle) {
            if (TextUtils.isEmpty(PreferencesUtils.getString(getActivity().getApplicationContext(), PreferencesUtils.KEY_TOKEN))) {
                showUnLoginSnackbar();
                return;
            }
            startActivity(new Intent(getActivity(), CircleCreatedMyActivity.class));

        } else {
        }
    }

    /**
     *
     * @param status  0:未加入
     * @param but_join
     */
    private void changeBtnAppear(int status,TextView but_join) {
        boolean notJoin = status == 3 || status == 2|| status == 1;
        but_join.setBackgroundDrawable(getResources().getDrawable(
                notJoin ? R.drawable.selector_btn_outline_gray
                        : R.drawable.selector_btn_outline_orange));
        but_join.setTextColor(
                getResources().getColor(notJoin ? R.color.colorTextGray : R.color.colorAccent));
        but_join.setText(notJoin ? "已加入" : "加入");
        if(notJoin){
            but_join.setClickable(false);
        }else{
            but_join.setClickable(true);
        }
    }


    private void switchMyCircleView() {
        if (layout_circle_none != null && layout_circle_my != null &&
                layout_circle_my.getChildCount() == 0)
        {
            layout_circle_none.setVisibility(View.VISIBLE);
        } else if (layout_circle_none != null) {
            layout_circle_none.setVisibility(View.GONE);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.circle, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(hasMessage()){
            menu.findItem(R.id.action_notification).setIcon(R.mipmap.message_hint);
        }else{
            menu.findItem(R.id.action_notification).setIcon(R.mipmap.message);
        }
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_notification) {
            if(checkIsLogin()){
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }

        }
        return super.onOptionsItemSelected(item);
    }


    private void createCircleItemViews(CircleIndexVO circleIndexVO) {
        //清空旧View
        layout_circle_my.removeAllViews();
        layout_circle_recommend.removeAllViews();

        int myCirlceLength=circleIndexVO.getMyList().size();
        for(int j=0;j<myCirlceLength;j++){
            CircleVO cirlce =circleIndexVO.getMyList().get(j);
            View rootView = getActivity().getLayoutInflater().inflate(
                    R.layout.item_circle_my, null);
            rootView.setTag(TAG_CIRCLE_MY);
            ImageView circle_img = (ImageView) rootView.findViewById(R.id.circle_img);
            ImageDisplayUtil.displayImage(Constant.IMG_COMPRESS,cirlce.getIntro_img(),circle_img,ImageDisplayUtil.DISPLAY_ROUND_IMAGE);
            TextView name =(TextView) rootView.findViewById(R.id.name);
            name.setText(cirlce.getName());
            TextView content =(TextView) rootView.findViewById(R.id.content);
            content.setText(cirlce.getIntro_content());
            TextView join_num =(TextView) rootView.findViewById(R.id.join_num);
            join_num.setText(String.format(getString(R.string.circle_join_num),cirlce.getUser_n()));
            TextView post_num =(TextView) rootView.findViewById(R.id.post_num);
            post_num.setText(String.format(getString(R.string.circle_post_num),cirlce.getPost_n()));
            rootView.setOnClickListener(new MyCircleItemClickListener(cirlce.getId(),cirlce.getIsEdit()));
            layout_circle_my.addView(rootView);

            if(j<myCirlceLength-1){
                View view = new View(getActivity());
                view.setMinimumHeight(1);
                view.setBackgroundColor(getResources().getColor(R.color.colorDividerLight));
                layout_circle_my.addView(view);
            }
        }
        switchMyCircleView();

        int recom_length =circleIndexVO.getCircleList().size();
        for(int i=0;i<recom_length;i++){
            CircleVO cirlce =circleIndexVO.getCircleList().get(i);
            View rootView=getActivity().getLayoutInflater().inflate(R.layout.item_circle_reco, null);
            rootView.setTag(TAG_CIRCLE_RECO);
            //填充数据
            ImageView circle_img = (ImageView) rootView.findViewById(R.id.circle_img);
            ImageDisplayUtil.displayImage(Constant.IMG_COMPRESS,cirlce.getIntro_img(),circle_img,ImageDisplayUtil.DISPLAY_ROUND_IMAGE);
            TextView name =(TextView) rootView.findViewById(R.id.name);
            name.setText(cirlce.getName());
            TextView content =(TextView) rootView.findViewById(R.id.content);
            content.setText(cirlce.getIntro_content());
            TextView join_num =(TextView) rootView.findViewById(R.id.join_num);
            join_num.setText(String.format(getString(R.string.circle_join_num),cirlce.getUser_n()));
            TextView post_num =(TextView) rootView.findViewById(R.id.post_num);
            post_num.setText(String.format(getString(R.string.circle_post_num),cirlce.getPost_n()));
            TextView btn_join= (TextView) rootView.findViewById(R.id.btn_join);
            btn_join.setTag(TAG_BTN_JOIN);
            btn_join.setOnClickListener(new JoinOnClickListener(i,cirlce,join_num,btn_join));
            rootView.setOnClickListener(new MyCircleItemClickListener(cirlce.getId(),null));
            layout_circle_recommend.addView(rootView);
            if(i<recom_length-1){
                View view = new View(getActivity());
                view.setMinimumHeight(1);
                view.setBackgroundColor(getResources().getColor(R.color.colorDividerLight));
                layout_circle_recommend.addView(view);
            }

        }

    }

    class MyCircleItemClickListener implements View.OnClickListener{

        private String circleId;

        private String isEdit;

        public MyCircleItemClickListener(String circleId,String isEdit){
            this.circleId=circleId;
            this.isEdit =isEdit;
        }

        @Override
        public void onClick(View v) {
            if(!Utils.isEmpty(isEdit)){
                if(isEdit.equals("1")){
                    showTost("审核失败,不能进入圈子,请重新提交审核...",1);
                    return;
                }else if(isEdit.equals("3")){
                    showTost("圈子审核中,请耐心等待...",1);
                    return;
                }else{
                    Intent intent=new Intent(getActivity(), CircleDetailActivity.class);
                    intent.putExtra(CircleFragment.EXTRA_CIRCLE_ID,circleId);
                    startActivity(intent);
                }
            }else{
                Intent intent=new Intent(getActivity(), CircleDetailActivity.class);
                intent.putExtra(CircleFragment.EXTRA_CIRCLE_ID,circleId);
                startActivity(intent);
                return;
            }

        }
    }

    /****
     * 加入/退出按钮点击事件
     */
    class JoinOnClickListener implements View.OnClickListener{

        private CircleVO circleVO;

        private int position;

        private TextView join_num;
        private TextView btn_join;

        public JoinOnClickListener(int position,CircleVO circleVO,TextView join_num,TextView btn_join){
            this.circleVO=circleVO;
            this.position =position;
            this.join_num =join_num;
            this.btn_join =btn_join;

        }

        @Override
        public void onClick(View v) {
            joinNumText =join_num;
            joinBtn = btn_join;
            if(circleVO.getType().equals("1")){
                ExitCircleDialogFragment shareDialog = ExitCircleDialogFragment.newInstance(ShareDialogFragment.STYLE_NO_TITLE,position,circleVO.getId());
                shareDialog.setTargetFragment(CircleFragment.this,TARGET_REQUESST_CODE_EXIT_CIRCLE);
                shareDialog.show(getActivity().getSupportFragmentManager().beginTransaction(), "exit_circle");
            }else{
                OtherUtil.joinCircle(CircleFragment.this, circleVO.getId(), new OtherUtil.JoinCircleCallback() {
                    @Override
                    public void onFinish(int key) {
                        if(key ==1){//加入成功
                            circleVO.setType("1");
                            circleVO.setUser_n(Utils.isInteger(circleVO.getUser_n())+1+"");
                            joinNumText.setText(String.format(getString(R.string.circle_join_num),circleVO.getUser_n()));
                        }
                        changeBtnAppear(key,btn_join);
                    }
                });
            }
        }
    }

//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (MyApplication.sRefreshMap.get(TAG)&& OtherUtil.hasLogin(getActivity())) {
//            loadData();
//            MyApplication.sRefreshMap.put(TAG, false);
//        }
//    }
//
//    private void refreshUi() {
//        if (layout_circle_my != null) {
//            layout_circle_my.removeAllViews();
//        }
//        if (layout_circle_recommend != null) {
//            layout_circle_recommend.removeAllViews();
//        }
//        loadData();
//    }
//


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case TARGET_REQUESST_CODE_EXIT_CIRCLE:
                    int position = data.getIntExtra("position",-1);
                    if(position!=-1){
                        CircleVO circleVO = circleIndexVO.getCircleList().get(position);
                        circleVO.setType("0");
                        circleVO.setUser_n(Utils.isInteger(circleVO.getUser_n())-1+"");
                        joinNumText.setText(String.format(getString(R.string.circle_join_num),circleVO.getUser_n()));
                    }
                    changeBtnAppear(0,joinBtn);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Communications.cancelRequest(TAG);
    }

    @Override
    public void onResume() {
        if(checkIsLogin()&& circleIndexVO.getMyList().size()>0){
            getView().findViewById(R.id.more).setVisibility(View.VISIBLE);
            layout_my_circle.setOnClickListener(this);
        }else{
            getView().findViewById(R.id.more).setVisibility(View.GONE);
            layout_my_circle.setOnClickListener(null);
        }
        getActivity().invalidateOptionsMenu();
        getCircleIndex();
        super.onResume();
    }
}
