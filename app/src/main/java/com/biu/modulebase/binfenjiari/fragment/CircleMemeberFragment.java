package com.biu.modulebase.binfenjiari.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CircleBlackListActivity;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.CircleMemeberVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.DialogFactory;
import com.biu.modulebase.binfenjiari.widget.expandablelistview.AssortView;
import com.biu.modulebase.binfenjiari.widget.expandablelistview.CircleMemeberPinyinAdapter;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lee
 * @Title: {圈子成员}
 * @Description:{描述}
 * @date 2016/5/4
 */
public class CircleMemeberFragment extends BaseFragment {

    private static final String TAG = "MessageFragment";

//    private SearchEditText searchView;
    private ExpandableListView eListView;
    private AssortView assortView;
    private CircleMemeberPinyinAdapter adapter = null;

    private PopupWindow popupWindow;

    private List<CircleMemeberVO> adminList = new ArrayList<>();
    private List<CircleMemeberVO> userList = new ArrayList<>();

    /**
     * 我的身份类型 ：2-管理员 3创建者
     **/
    private String myAdminType;
    /**
     * 操作类型
     **/
    private String operateType = "";
    /**
     * checkbox 最多可选人数
     * 转让圈子：1
     * 设置管理员：最多两个管理员 2-(adminList.size()-1)>0?2-(adminList.size()-1):0
     * 删除圈子成员 ：无限制   且 管理员只能操作普通成员
     * 不允许再次加入：无限制  且 管理员只能操作普通成员
     * -10:  无限大
     **/
    private int checkNum = 1;

    private String mCircleId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_memeber, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void getIntentData() {
        mCircleId = getActivity().getIntent().getStringExtra("circle_id");
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        visibleLoading();
//        searchView =(SearchEditText)rootView.findViewById(R.id.searchView);
//        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if(actionId == EditorInfo.IME_ACTION_SEARCH){
//                    String key =v.getText().toString();
//                    hideSoftKeyboard();
//                }
//                return false;
//            }
//        });
        eListView = (ExpandableListView) getView().findViewById(R.id.expandListView);
        assortView = (AssortView) getView().findViewById(R.id.assort);
        eListView.setGroupIndicator(null);
        eListView.setChildIndicator(null);
        eListView.setDivider(null);
        // 字母按键回调
        assortView.setOnTouchAssortListener(new AssortView.OnTouchAssortListener() {
            View layoutView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.alert_dialog_menu_layout, null);
            TextView text = (TextView) layoutView.findViewById(R.id.content);

            public void onTouchAssortListener(String str) {
                int index = -1;
                if (adapter != null)
                    index = adapter.getAssort().getHashList().indexOfKey(str);
                if (index != -1) {
                    eListView.setSelectedGroup(index);
                }
                if (popupWindow != null) {
                    text.setText(str);
                } else {
                    popupWindow = new PopupWindow(layoutView, Utils.getScreenWidth(getActivity()) / 6, Utils.getScreenWidth(getActivity()) / 6, false);
                    // 显示在Activity的根视图中心
                    popupWindow.showAtLocation(getActivity().getWindow().getDecorView(),
                            Gravity.CENTER, 0, 0);
                }
                text.setText(str);
            }

            public void onTouchAssortUP() {
                if (popupWindow != null)
                    popupWindow.dismiss();
                popupWindow = null;
            }
        });
        eListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CircleMemeberVO friendVO = adapter.getChild(groupPosition, childPosition);
                return true;
            }
        });

//        showList(userList);
    }

    private void showList(List<CircleMemeberVO> datas) {
        if (datas != null && datas.size() != 0) {
            adapter = new CircleMemeberPinyinAdapter(this, datas, operateType, mCircleId);
            eListView.setAdapter(adapter);
            // 展开所有
            for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
                eListView.expandGroup(i);
            }
        } else {
            adapter = new CircleMemeberPinyinAdapter(this, null, operateType, mCircleId);
            eListView.setAdapter(adapter);
        }
        updateHeaderView();

    }

    private void updateHeaderView() {
        // The exp list-view is having a header view remove the header View
        if (eListView.getHeaderViewsCount() != 0) {
            eListView.removeHeaderView(headerView);
        }
        addHeaderView();
    }

    private View headerView;

    private void addHeaderView() {
        headerView = View.inflate(getActivity(), R.layout.header_list_circle_memeber, null);
        LinearLayout admin_layout = (LinearLayout) headerView.findViewById(R.id.admin_layout);
        TextView num = (TextView) headerView.findViewById(R.id.num);
        num.setText("(" + adminList.size() + "人)");
        admin_layout.removeAllViews();
        for (int i = 0; i < adminList.size(); i++) {
            final CircleMemeberVO bean = adminList.get(i);
            View view = View.inflate(getActivity(), R.layout.item_memeber_header, null);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
            ImageView delete = (ImageView) view.findViewById(R.id.delete);
            if ((operateType.equals(Constant.MEMEBER_OPERATE_CANCLE_MANAGER) || operateType.equals(Constant.MEMEBER_OPERATE_TRANSFER_CIRCLE)) && bean.getType().equals("2") && myAdminType.equals("3")) {//创建者才能取消管理员
                checkBox.setChecked(false);
                checkBox.setVisibility(View.VISIBLE);
                delete.setVisibility(View.GONE);
                checkBox.setOnCheckedChangeListener(new HeaderCheckedListener(i));
            }
            if (operateType.equals(Constant.MEMEBER_OPERATE_MOVE_OUT)) {
                checkBox.setVisibility(View.GONE);
                if (bean.getType().equals("2") && myAdminType.equals("3")) {//创建者  才能删除管理员成员
                    delete.setVisibility(View.VISIBLE);
                    delete.setOnClickListener(new HeaderDeleteClickListener(i));
                }
            }
            if(operateType.equals(Constant.MEMEBER_OPERATE_PULL_BLACK) && bean.getType().equals("2")){//创建者  才能拉黑管理员成员  不能拉黑自己
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setOnCheckedChangeListener(new HeaderCheckedListener(i));
                delete.setVisibility(View.GONE);
            }
            ImageView header = (ImageView) view.findViewById(R.id.header);
            ImageDisplayUtil.displayImage(Constant.IMG_COMPRESS, bean.getUser_pic(), header, ImageDisplayUtil.DISPLAY_HEADER);
            TextView admin_type = (TextView) view.findViewById(R.id.admin_type);
            if (bean.getType().equals("3")) {//3:创建者
                admin_type.setBackgroundResource(R.drawable.rr_red_5);
                admin_type.setText("创建者");
            } else {//管理员
                admin_type.setBackgroundResource(R.drawable.selector_rr_orange_5_rest);
                admin_type.setText("管理员");
            }
            TextView child_name = (TextView) view.findViewById(R.id.child_name);
            child_name.setText(bean.getUser_name());
            TextView me = (TextView) view.findViewById(R.id.me);
            if (bean.getId().equals(MyApplication.getUserInfo(getActivity()).getId())) {
//                myAdminType =bean.getType();
                me.setVisibility(View.VISIBLE);
            } else {
                me.setVisibility(View.GONE);
            }
            admin_layout.addView(view);
        }
        eListView.addHeaderView(headerView);
    }

    class HeaderCheckedListener implements CompoundButton.OnCheckedChangeListener {
        private int position;

        public HeaderCheckedListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CircleMemeberVO checkedBean = adminList.get(position);
            if (isChecked) {
                adapter.addCheckList(checkedBean, buttonView);
//                headCheckedList.add(checkedBean);
                if (operateType.equals(Constant.MEMEBER_OPERATE_CANCLE_MANAGER)) {

                }
                if (!operateType.equals(Constant.MEMEBER_OPERATE_CANCLE_MANAGER)) {
                    //取消listview item 选中项
//                    adapter.setOperateType("");
                }


            } else {
                adapter.removeCheckListById(checkedBean);
//                for(int i=0;i<headCheckedList.size();i++){
//                    if(headCheckedList.get(i).getId().equals(checkedBean.getId())){
//                        headCheckedList.remove(i);
//                        break;
//                    }
//                }
            }
        }
    }

    class HeaderDeleteClickListener implements View.OnClickListener {

        private int position;

        public HeaderDeleteClickListener(int position) {
            this.position = position;


        }

        @Override
        public void onClick(View v) {
            CircleMemeberVO bean =adminList.get(position);
            List<String> userIds = new ArrayList<>();
            userIds.add( bean.getId());
            OtherUtil.memeberOperate(CircleMemeberFragment.this, mCircleId, userIds, operateType, new OtherUtil.MemeberOperateCallback() {
                @Override
                public void onsuccess(String typenim) {
                    showTost("移除成功",0);
                    adminList.remove(position);
                    updateHeaderView();
                }
            });
        }
    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {
        getMemeberList();
    }

    private void getMemeberList() {
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params, "id", mCircleId);
        JSONUtil.put(params, "model", Constant.MODEL_CIRCLE_MANAGE);
        JSONUtil.put(params, "action", Constant.ACTION_GET_MEMEBER_LIST);

        JSONUtil.put(params, "search_type", "1");// 1正常展示 2搜索
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result = new JSONObject(jsonString);
                    myAdminType = JSONUtil.getString(result, "type");
                    adminList = JSONUtil.fromJson(JSONUtil.getJSONArray(result, "adminList").toString(), new TypeToken<List<CircleMemeberVO>>() {
                    }.getType());
                    userList = JSONUtil.fromJson(JSONUtil.getJSONArray(result, "userList").toString(), new TypeToken<List<CircleMemeberVO>>() {
                    }.getType());
                    showList(userList);
                    setHasOptionsMenu(true);
                    dismissProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCodeError(int key, String message) {
                switch (key) {
                    case 3:
                        visibleNoData();
                        break;
                    default:

                        showTost(message, 0);
                }
                dismissProgress();
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }

    private void updateMeun() {
        getActivity().invalidateOptionsMenu();
    }

    private void updateAdapterType() {
        editStatus = true;
        updateMeun();
        adapter.setCheckNum(checkNum);
        adapter.setOperateType(operateType);
    }

    /**
     * 创建者pop
     */
    private void showCreaterPop() {
        DialogFactory.showDialog(getActivity(), R.layout.pop_circle_memeber_operate_creater,
                R.style.WheelDialog, R.style.popwin_anim_style, Gravity.BOTTOM, 0.9f, 0,
                new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {
                        v.findViewById(R.id.transfer_circle).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        operateType = Constant.MEMEBER_OPERATE_TRANSFER_CIRCLE;
                                        checkNum = 1;
                                        updateHeaderView();
                                        updateAdapterType();
                                        dialog.dismiss();
                                    }
                                });
                        v.findViewById(R.id.set_manage).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        operateType = Constant.MEMEBER_OPERATE_SET_MANAGER;
                                        checkNum = 2 - (adminList.size() - 1) >= 0 ? 2 - (adminList.size() - 1) : 0;
                                        updateAdapterType();
                                        dialog.dismiss();
                                    }
                                });
                        v.findViewById(R.id.cancle_manage).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        operateType = Constant.MEMEBER_OPERATE_CANCLE_MANAGER;
                                        //取消管理员只需更改管理员布局 无需修改LisstView item布局
                                        updateHeaderView();
                                        editStatus = true;
                                        checkNum = adminList.size() - 1;
                                        updateAdapterType();
                                        dialog.dismiss();
                                    }
                                });
                        v.findViewById(R.id.delete_memeber).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        operateType = Constant.MEMEBER_OPERATE_MOVE_OUT;
                                        updateHeaderView();
                                        checkNum = -10;
                                        updateAdapterType();
                                        dialog.dismiss();
                                    }
                                });
                        v.findViewById(R.id.not_allow_join).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        operateType = Constant.MEMEBER_OPERATE_PULL_BLACK;
                                        updateHeaderView();
                                        checkNum = -10;
                                        updateAdapterType();
                                        dialog.dismiss();
                                    }
                                });

                        v.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
                });
    }

    /**
     * 管理员pop
     */
    private void showManagerPop() {
        DialogFactory.showDialog(getActivity(), R.layout.pop_circle_memeber_operate_manager,
                R.style.WheelDialog, R.style.popwin_anim_style, Gravity.BOTTOM, 0.9f, 0,
                new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {
                        v.findViewById(R.id.cancle_manager).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        operateType = Constant.MEMEBER_OPERATE_CANCLE_MANAGER;
                                        //...TODO 直接调接口取消自己管理员身份
                                        List<String> userIds =new ArrayList<>();
                                        final String userId= MyApplication.getUserInfo(getActivity()).getId();
                                        userIds.add(userId);
                                        OtherUtil.memeberOperate(CircleMemeberFragment.this, mCircleId, userIds, operateType, new OtherUtil.MemeberOperateCallback() {
                                            @Override
                                            public void onsuccess(String typenim) {
                                                showTost("取消自己管理员成功",0);
                                                //去除adminList里的数据
                                                for (int i = 0; i < adminList.size(); i++) {
                                                    CircleMemeberVO bean = adminList.get(i);
                                                    if (userId.equals(bean.getId())) {
                                                        adminList.remove(i);
                                                        userList.add(bean);
                                                        break;
                                                    }
                                                }
                                                showList(userList);
                                                updateHeaderView();
                                            }
                                        });
//                                        updateAdapterType();
                                        dialog.dismiss();
                                    }
                                });
                        v.findViewById(R.id.delete_memeber).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        operateType = Constant.MEMEBER_OPERATE_MOVE_OUT;
                                        checkNum = -10;
                                        updateAdapterType();
                                        dialog.dismiss();
                                    }
                                });
                        v.findViewById(R.id.not_allow_join).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        operateType = Constant.MEMEBER_OPERATE_PULL_BLACK;
                                        checkNum = -10;
                                        updateAdapterType();
                                        dialog.dismiss();
                                    }
                                });

                        v.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
                });
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.circle_memeber, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean editStatus = false;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_black).setVisible(!editStatus);
        menu.findItem(R.id.action_more).setVisible(!editStatus);
        menu.findItem(R.id.action_complete).setVisible(editStatus);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i1 = item.getItemId();
        if (i1 == R.id.action_black) {
            Intent intent = new Intent(getActivity(), CircleBlackListActivity.class);
            intent.putExtra("circle_id", mCircleId);
            startActivity(intent);

        } else if (i1 == R.id.action_more) {
            if (myAdminType.equals("2")) {//管理员
                showManagerPop();
            } else if (myAdminType.equals("3")) {//创建者
                showCreaterPop();
            }

        } else if (i1 == R.id.action_complete) {
            final List<CircleMemeberVO> checkedList = adapter.getCheckedList();
            if (checkedList.size() == 0) {
                exitEdit();
                return true;
            }
            List<String> userIdList = new ArrayList<>();
            for (int i = 0; i < checkedList.size(); i++) {
                userIdList.add(checkedList.get(i).getId());
            }

//                }
            OtherUtil.memeberOperate(CircleMemeberFragment.this, mCircleId, userIdList, operateType, new OtherUtil.MemeberOperateCallback() {
                @Override
                public void onsuccess(String typenim) {
                    if (operateType.equals(Constant.MEMEBER_OPERATE_TRANSFER_CIRCLE)) {
                        showTost("转让成功，你已经成为普通成员", 1);
                        getActivity().finish();
                    }
                    if (operateType.equals(Constant.MEMEBER_OPERATE_PULL_BLACK)) {//拉黑
                        showTost("拉黑成功", 0);
                        //去除adminList里的数据
                        for (int i = 0; i < checkedList.size(); i++) {
                            CircleMemeberVO bean = checkedList.get(i);
                            for (int j = 0; j < adminList.size(); j++) {
                                if (adminList.get(j).getId().equals(bean.getId())) {
                                    adminList.remove(j);
                                    break;
                                }
                            }
                        }
                        updateHeaderView();
                        adapter.removeChilds(checkedList);
                    } else if (operateType.equals(Constant.MEMEBER_OPERATE_SET_MANAGER)) {//设置管理员
                        adminList.addAll(checkedList);
                        //去除userList里的数据
                        for (int i = 0; i < checkedList.size(); i++) {
                            CircleMemeberVO bean = checkedList.get(i);
                            bean.setType("2");//设置成管理员类型
                            for (int j = 0; j < userList.size(); j++) {
                                if (userList.get(j).getId().equals(bean.getId())) {
                                    userList.remove(j);
                                    break;
                                }
                            }
                        }
                        showList(userList);
                    } else if (operateType.equals(Constant.MEMEBER_OPERATE_CANCLE_MANAGER)) {//取消管理员
                        showTost("取消管理员成功", 0);
                        userList.addAll(checkedList);
                        //去除adminList里的数据
                        for (int i = 0; i < checkedList.size(); i++) {
                            CircleMemeberVO bean = checkedList.get(i);
                            for (int j = 0; j < adminList.size(); j++) {
                                if (adminList.get(j).getId().equals(bean.getId())) {
                                    adminList.remove(j);
                                    break;
                                }
                            }
                        }
                        showList(userList);
                    }
                    exitEdit();
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 退出编辑状态
     */
    private void exitEdit() {
        operateType = "";
        editStatus = false;
        updateHeaderView();
        updateMeun();
        //初始化checkNum和operateType
        checkNum = 1;
        adapter.setOperateType(operateType);
    }
}
