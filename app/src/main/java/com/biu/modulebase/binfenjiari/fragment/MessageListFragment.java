package com.biu.modulebase.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ClearMessageAlertDialogFragment;
import com.biu.modulebase.binfenjiari.model.MessageVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @Title: {消息列表}
 * @Description:{描述}
 * @date 2016/1/25
 */
public class MessageListFragment extends BaseFragment {

    private String TAG="NoticeListFragment";


    private LSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    public static final String KEY_MESSAGE_TYPE="message_type";

    /**（1系统推送 2圈子推送 3帖子推送）**/
    public static final String MESSAGE_TYPE_SYSTEM ="1";
    public static final String MESSAGE_TYPE_CIRCLE="2";
    public static final String MESSAGE_TYPE_POST="3";

    private String messageType="";

    private int edit_position=-1;

    private long time;

    private int pageNum =1;
    private int allPageNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args=getArguments();
        if (args!=null) {
            messageType=args.getString(KEY_MESSAGE_TYPE);
        }
    }

    public static MessageListFragment newInstance(String messageType){
        Bundle args=new Bundle();
        args.putString(KEY_MESSAGE_TYPE,messageType );
        MessageListFragment messageListFragment=new MessageListFragment();
        messageListFragment.setArguments(args);
        return messageListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh, container,
                false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        visibleLoading();
    }

    protected void initView(View rootView) {
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.LogE(TAG,"onRefresh******************");
                loadData();
            }

            @Override
            public void onLoadMore() {
                LogUtil.LogE(TAG,"onLoadMore******************");
                pageNum++;
                getList(Constant.LIST_LOAD_MORE);
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter=new BaseAdapter(getActivity()) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, final int viewType) {

                BaseViewHolder baseViewHolder = new BaseViewHolder(getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_message, parent, false),
                        new BaseViewHolder.Callbacks2() {

                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                MessageVO messageVO = (MessageVO) data;
                                TextView content =holder.getView(R.id.content);
                                holder.setText(R.id.content,messageVO.getMessage_content());
                                if(messageVO.getMessage_state().equals("1")){
                                    content.setTextColor(getResources().getColor(R.color.black));
                                }else{
                                    content.setTextColor(getResources().getColor(R.color.graytext_100));
                                }

                            }

                            @Override
                            public void onItemClick(final BaseViewHolder holder,final View view, final int position) {
                                MessageVO bean = (MessageVO) getData(position);
                                if(bean.getMessage_state().equals("1")){
                                    seeMessage(bean.getMessage_id(),position);
                                }


                            }
                        });
//                baseViewHolder.setItemChildViewClickListener();
                return baseViewHolder;


            }

        };
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // callback for drag-n-drop, false to skip this feature
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // callback for swipe to dismiss, removing item from data and adapter
                int position =viewHolder.getAdapterPosition();
                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                MessageVO bean = (MessageVO) adapter.getData(position);
                deleteMessage(bean.getMessage_id(),position);
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                outRect.set(0,0,0,getResources().getDimensionPixelSize(R.dimen.item_divider_height_8dp));
//            }
//        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        mRefreshLayout.startLoad();
                    }
                }
            }
        });
    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {
        time = new Date().getTime()/1000;
        pageNum =1;
        getList(Constant.LIST_REFRESH);
    }


    private void getList(final int tag){
        JSONObject params = new JSONObject();
        try {
            params.put("model", Constant.MODEL_MESSAGE);
            params.put("action",Constant.ACTION_GET_MESSAGE_LIST);
            params.put("time",time+"");
            params.put("pageNum",pageNum+"");
            params.put("message_type", messageType);
            params.put("token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result =new JSONObject(jsonString);
                    time = JSONUtil.getLong(result,"time");
                    allPageNumber =JSONUtil.getInt(result,"allPageNumber");
                    JSONArray array =JSONUtil.getJSONArray(result,"messageList");
                    ArrayList<MessageVO> list =JSONUtil.fromJson(array.toString(),new TypeToken<List<MessageVO>>(){}.getType());
                    refreshList(tag, list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if(key==3){
                    visibleNoData();
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

    private void refreshList(int tag,ArrayList<MessageVO> datas){
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag){
            case Constant.LIST_REFRESH:
                if(datas.size()==0) {
                    visibleNoData();
                    return;
                }
                adapter.setData(datas);
                mRefreshLayout.setRefreshing(false);
                break;
            case Constant.LIST_LOAD_MORE:
                adapter.addItems(datas);
                mRefreshLayout.setLoading(false);
                break;
        }
        if(pageNum<allPageNumber){
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
        }else{
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
        }

    }

    private void seeMessage(String id,final int position){
        showProgress(getClass().getSimpleName());
        JSONObject params = new JSONObject();
        try {
            params.put("model", Constant.MODEL_MESSAGE);
            params.put("action",Constant.ACTION_SEE_MESSAGE);
            params.put("id", id);
            params.put("token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                MessageVO bean = (MessageVO) adapter.getData(position);
                bean.setMessage_state("2");
                adapter.changeData(position,bean);
            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                showTost(message,1);
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
            }
        });
    }

    private void deleteMessage(String id,final int position){
        showProgress(getClass().getSimpleName());
        JSONObject params = new JSONObject();
        try {
            params.put("model", Constant.MODEL_MESSAGE);
            params.put("action",Constant.ACTION_DELETE_MESSAGE);
            params.put("id", id);
            params.put("token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                showTost("删除成功",0);
                dismissProgress();
                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                adapter.removeData(position);
            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                showTost(message,1);
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
            }
        });
    }

    public void deleteAllMessage(){
        showProgress(getClass().getSimpleName());
        JSONObject params = new JSONObject();
        try {
            params.put("model", Constant.MODEL_MESSAGE);
            params.put("action",Constant.ACTION_DELETE_ALL_MESSAGE);
            params.put("message_type", messageType);
            params.put("token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                showTost("消息清空成功",0);
                dismissProgress();
                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                adapter.removeAllData();
            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                showTost(message,1);
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
            }
        });
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.clear, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_clear) {
            ClearMessageAlertDialogFragment newFragment = ClearMessageAlertDialogFragment.newInstance(R.style.AppCompatAlertDialogStyle);
            newFragment.show(getFragmentManager(), "dialog");

        }
        return super.onOptionsItemSelected(item);
    }


}
