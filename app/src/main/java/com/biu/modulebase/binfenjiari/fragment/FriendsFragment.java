package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
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
import com.biu.modulebase.binfenjiari.activity.NewFriendActivity;
import com.biu.modulebase.binfenjiari.model.CircleMemeberVO;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.expandablelistview.AssortView;
import com.biu.modulebase.binfenjiari.widget.expandablelistview.FriendsPinyinAdapter;
import com.biu.modulebase.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lee
 * @Title: {好友}
 * @Description:{通知-好友}
 * @date 2016/5/3
 */
public class FriendsFragment extends BaseFragment {

    private static final String TAG = "MessageFragment";

    private ExpandableListView eListView;
    private AssortView assortView;
    private FriendsPinyinAdapter adapter = null;

    private PopupWindow popupWindow;

    private List<CircleMemeberVO> datas =new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_friends, container, false);
        return layout;
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.newFriends).setOnClickListener(this);
        eListView = (ExpandableListView) getView().findViewById(R.id.winners_listview);
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

        CircleMemeberVO friend = new CircleMemeberVO();
        friend.setUser_name("豆豆妈妈");
        datas.add(friend);
        CircleMemeberVO friend1 = new CircleMemeberVO();
        friend1.setUser_name("张老师");
        datas.add(friend1);
        CircleMemeberVO friend2 = new CircleMemeberVO();
        friend2.setUser_name("张老师2");
        datas.add(friend2);
        showList(datas);
    }

    private void showList(List<CircleMemeberVO> datas) {
        if (datas != null && datas.size() != 0) {
            adapter = new FriendsPinyinAdapter(getActivity(), datas);
            eListView.setAdapter(adapter);
            // 展开所有
            for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
                eListView.expandGroup(i);
            }
        } else {
            adapter = new FriendsPinyinAdapter(getActivity(), null);
            eListView.setAdapter(adapter);
        }

    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.newFriends) {
            startActivity(new Intent(getActivity(), NewFriendActivity.class));

        }
    }
}
