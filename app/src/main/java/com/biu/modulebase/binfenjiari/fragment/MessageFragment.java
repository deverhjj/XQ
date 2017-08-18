package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.ChatActivity;
import com.biu.modulebase.binfenjiari.activity.MessageListActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.MessageTypeVO;
import com.biu.modulebase.binfenjiari.model.NotificationVO;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lee
 * @Title: {消息}
 * @Description:{通知-消息}
 * @date 2016/5/3
 */
public class MessageFragment extends BaseFragment {

    private static final String TAG = "MessageFragment";
    private RecyclerView mRecyclerView;

    private LSwipeRefreshLayout mRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh,
                container,false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        visibleLoading();
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.LogE(TAG, "onRefresh******************");
                getMessageIndex();
            }

            @Override
            public void onLoadMore() {
                LogUtil.LogE(TAG,"onLoadMore******************");
                //loadCards();
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter=new BaseAdapter(getActivity()) {
            private static final int TYPE_HEADER=0X1;
            private static final int TYPE_ITEM=0X2;

            @Override
            public int getItemViewType(int position) {
                return position == 0 ? TYPE_HEADER : TYPE_ITEM;
            }

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = null;
                if(viewType ==TYPE_HEADER){
                    holder=new BaseViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_message_header, parent, false), new BaseViewHolder
                            .Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            NotificationVO bean = (NotificationVO) data;
                            List<MessageTypeVO> messageList =bean.getList();
                            TextView circle_num = holder.getView(R.id.circle_num);
                            TextView discuss_num = holder.getView(R.id.discuss_num);
                            TextView system_num = holder.getView(R.id.system_num);
                            system_num.setVisibility(View.GONE);
                            circle_num.setVisibility(View.GONE);
                            discuss_num.setVisibility(View.GONE);
                            for(int i=0;i<messageList.size();i++){
                                MessageTypeVO messageVO =messageList.get(i);
                                if(messageVO.getMessage_type().equals("2")){

                                    circle_num.setText(messageVO.getNumber());
                                    circle_num.setVisibility(View.VISIBLE);
                                }else if(messageVO.getMessage_type().equals("3")){

                                    discuss_num.setText(messageVO.getNumber());
                                    discuss_num.setVisibility(View.VISIBLE);
                                }else if(messageVO.getMessage_type().equals("1")){

                                    system_num.setText(messageVO.getNumber());
                                    system_num.setVisibility(View.VISIBLE);
                                }
                            }

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {
                            Intent intent = new Intent(getActivity(), MessageListActivity.class);
                            int i = view.getId();
                            if (i == R.id.circle_message) {
                                intent.putExtra(MessageListFragment.KEY_MESSAGE_TYPE, MessageListFragment.MESSAGE_TYPE_CIRCLE);

                            } else if (i == R.id.post_message) {
                                intent.putExtra(MessageListFragment.KEY_MESSAGE_TYPE, MessageListFragment.MESSAGE_TYPE_POST);

                            } else if (i == R.id.system_message) {
                                intent.putExtra(MessageListFragment.KEY_MESSAGE_TYPE, MessageListFragment.MESSAGE_TYPE_SYSTEM);

                            }
                            startActivity(intent);

//                            Intent intent=new Intent(getActivity(), ChatActivity.class);
//                            startActivity(intent);

                        }
                    });
                    holder.setItemChildViewClickListener(R.id.circle_message,R.id.post_message,R.id.system_message);
                }else{
                     holder=new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_notification, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {

                            Intent intent=new Intent(getActivity(), ChatActivity.class);
                            startActivity(intent);

                        }
                });
                }


                return holder;
            }
        };

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {
//        getMessageIndex();
    }


    private void getMessageIndex(){
        JSONObject params = new JSONObject();
        try {
            params.put("token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
            params.put("model", Constant.MODEL_MESSAGE);
            params.put("action",Constant.ACTION_MESSAGE_INDEX);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                ArrayList<MessageTypeVO> list = JSONUtil.fromJson(jsonString,new TypeToken<List<MessageTypeVO>>(){}.getType());
                NotificationVO bean = new NotificationVO();
                bean.setList(list);
                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                adapter.removeAllData();
                adapter.addData(BaseAdapter.AddType.FIRST, Collections.singletonList(bean));
                inVisibleLoading();
                mRefreshLayout.setRefreshing(false);

                int totlaNum =0;
                for(int i=0;i<list.size();i++){
                    totlaNum += Utils.isInteger(list.get(i).getNumber());
                }
                UserInfoBean userInfoVO = MyApplication.getUserInfo(getActivity());
                userInfoVO.setHasMessage(totlaNum>0?"1":"2");
                PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_USER_INFO,JSONUtil.toJson(userInfoVO).toString());
            }

            @Override
            public void onCodeError(int key, String message) {
                if(key==3){
                    UserInfoBean userInfoVO = MyApplication.getUserInfo(getActivity());
                    userInfoVO.setHasMessage("2");
                    PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_USER_INFO,JSONUtil.toJson(userInfoVO).toString());
                    NotificationVO bean = new NotificationVO();
                    bean.setList(new ArrayList<MessageTypeVO>());
                    BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                    adapter.removeAllData();
                    adapter.addData(BaseAdapter.AddType.FIRST, Collections.singletonList(bean));
                    inVisibleLoading();
                }else{
                    showTost(message,1);
                }
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });

    }

    private <T> void refreshList(int tag,List<T> datas){
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag){
            case Constant.LIST_REFRESH:
                mRefreshLayout.setRefreshing(false);
                if(datas.size()==0) {
                    showTost("没有最新数据...",1);
                    return;
                }
                adapter.addData(BaseAdapter.AddType.LASE, datas);
                break;
            case Constant.LIST_LOAD_MORE:
                adapter.addItems(datas);
                mRefreshLayout.setLoading(false);
                break;
        }
//        if(pageNum<allPageNumber){
//            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
//        }else{
//            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
//        }
    }

    @Override
    public void onResume() {
        getMessageIndex();
        super.onResume();
    }
}
