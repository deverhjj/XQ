package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.NoticeOperationActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.NoticeVO;
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
 * @Title: {公告管理列表}
 * @Description:{描述}
 * @date 2016/1/25
 */
public class NoticeListFragment extends BaseFragment {

    private String TAG="NoticeListFragment";


    private LSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private static final int REQUEST_NOTICE=1;

    public static final String RESULT_NOTICE="result_notice";


    public static final String NOTICE_ID="notice_id";


    public static final String KEY_CIRCLE_ID = "circle_id";


    private String mCircleId="";

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
            mCircleId=args.getString(KEY_CIRCLE_ID);
        }
    }

    public static NoticeListFragment newInstance(String circleId){
        Bundle args=new Bundle();
        args.putString(KEY_CIRCLE_ID,circleId);
        NoticeListFragment noticeListFragment=new NoticeListFragment();
        noticeListFragment.setArguments(args);
        return noticeListFragment;
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
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter=new BaseAdapter(getActivity()) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, final int viewType) {

                BaseViewHolder baseViewHolder = new BaseViewHolder(getActivity().getLayoutInflater()
                        .inflate(R.layout.item_notice, parent, false),
                        new BaseViewHolder.Callbacks2() {

                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                NoticeVO bean = (NoticeVO) data;
                                holder.setText(R.id.name,bean.getTitle());
                                holder.setText(R.id.content,bean.getContent());

                            }

                            @Override
                            public void onItemClick(final BaseViewHolder holder,final View view, final int position) {

                            }
                        });
//                baseViewHolder.setItemChildViewClickListener();
                return baseViewHolder;


            }

        };
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if( dX>100){
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }

            }

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
                NoticeVO bean = (NoticeVO) adapter.getData(position);
                deleteNotice(bean.getId(),position);
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
            params.put("model", Constant.MODEL_CIRCLE_MANAGE);
            params.put("action",Constant.ACTION_GET_NOTICE_LIST);
            params.put("time",time+"");
            params.put("pageNum",pageNum+"");
            params.put("circle_id", mCircleId);
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
                    JSONArray array =JSONUtil.getJSONArray(result,"announceList");
                    ArrayList<NoticeVO> list =JSONUtil.fromJson(array.toString(),new TypeToken<List<NoticeVO>>(){}.getType());
                    refreshList(tag, list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if(key==3){
                    refreshList(tag, new ArrayList<NoticeVO>());
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

    private void refreshList(int tag,ArrayList<NoticeVO> datas){
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

    private void deleteNotice(String id,final int position){
        showProgress(getClass().getSimpleName());
        JSONObject params = new JSONObject();
        try {
            params.put("model", Constant.MODEL_CIRCLE_MANAGE);
            params.put("action",Constant.ACTION_DELETE_NOTICE);
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



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (curCount >= 3) {
//            OtherUtil.showToast(getActivity(), "最多添加3个");
//            return super.onOptionsItemSelected(item);
//        }
        int i = item.getItemId();
        if (i == R.id.action_add) {
            Intent intent = new Intent(getActivity(), NoticeOperationActivity.class);
            intent.putExtra(NoticeOperationActivity.EXTRA_ACTION_NOTICE,
                    NoticeOperationActivity.ACTION_ADD);
            intent.putExtra(CircleFragment.EXTRA_CIRCLE_ID, mCircleId);
            startActivityForResult(intent, REQUEST_NOTICE);

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Activity.RESULT_OK) {
            switch (requestCode){
                case REQUEST_NOTICE:
                    loadData();
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
