package com.biu.modulebase.binfenjiari.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
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
public class CircleBlackListFragment extends BaseFragment {

    private static final String TAG = "MessageFragment";

    /**成员操作类型  1转让圈子 2设置管理员 3取消管理员 4移除成员 5拉黑 6取消拉黑**/
    private String operateType;

    private ExpandableListView eListView;
    private AssortView assortView;
    private CircleMemeberPinyinAdapter adapter = null;

    private PopupWindow popupWindow;

    private List<CircleMemeberVO> userList =new ArrayList<>();

    private String mCircleId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_circle_blacklist, container, false);
        return super.onCreateView(inflater,rootView,savedInstanceState);
    }

    @Override
    protected void getIntentData() {
        mCircleId =getActivity().getIntent().getStringExtra("circle_id");
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        visibleLoading();
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
                CircleMemeberVO memeberVO = (CircleMemeberVO) adapter.getChild(groupPosition, childPosition);
                List<String> userIds = new ArrayList<String>();
                userIds.add(memeberVO.getId());
                showPop(groupPosition,childPosition,userIds);
                return true;
            }
        });

        showList(userList);
    }

    private void showList(List<CircleMemeberVO> datas) {
        if (datas != null && datas.size() != 0) {
            adapter = new CircleMemeberPinyinAdapter(this, datas,operateType,mCircleId);
            eListView.setAdapter(adapter);
            // 展开所有
            for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
                eListView.expandGroup(i);
            }
        } else {
            adapter = new CircleMemeberPinyinAdapter(this, null,operateType,mCircleId);
            eListView.setAdapter(adapter);
        }

    }

    private void showPop(final int group,final int child,final List<String> userIds) {
        DialogFactory.showDialog(getActivity(), R.layout.pop_cancle_pull_black,
                R.style.WheelDialog, R.style.popwin_anim_style, Gravity.BOTTOM, 0.9f, 0,
                new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {
                        v.findViewById(R.id.cancle_black).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        OtherUtil.memeberOperate(CircleBlackListFragment.this, mCircleId, userIds, Constant.MEMEBER_OPERATE_CANCLE_PULL_BLACK, new OtherUtil.MemeberOperateCallback() {
                                            @Override
                                            public void onsuccess(String type) {
                                                adapter.removeChild(group,child);
                                                if(adapter.getGroupCount() ==0)
                                                    visibleNoData();
                                            }
                                        });
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
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {
        getMemeberList();
    }

    private void getMemeberList(){
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"id", mCircleId);
        JSONUtil.put(params,"model", Constant.MODEL_CIRCLE_MANAGE);
        JSONUtil.put(params,"action",Constant.ACTION_GET_CIRCLE_BLACK_LIST);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {

                try {
                    JSONObject result = new JSONObject(jsonString);
                    userList =JSONUtil.fromJson(JSONUtil.getJSONArray(result,"userList").toString(),new TypeToken<List<CircleMemeberVO>>(){}.getType());
                    showList(userList);
                    dismissProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onCodeError(int key, String message) {
                switch (key){
                    case 3:
                       visibleNoData();
                        break;
                    default:

                        showTost(message,0);
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
        switch (v.getId()){

        }
    }

}
