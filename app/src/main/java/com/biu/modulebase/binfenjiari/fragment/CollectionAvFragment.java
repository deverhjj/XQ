package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.AvItDetailActivity;
import com.biu.modulebase.binfenjiari.activity.AvVideoDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack2;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.Collection;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONObject;

import java.util.List;

public class CollectionAvFragment extends BaseFragment {

    private static final String TAG = "CollectionAvFragment";

    private static final int TYPE_VIDEO=1;
    private static final int TYPE_IT=2;
    private static final int TYPE_AUDIO=3;

    private RecyclerView mRecyclerView;
    private LSwipeRefreshLayout mRefreshLayout;

    private int mPageNum=1;
    private boolean mRefreshData=true;

    private Collection mCollection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh,
                container,false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
         visibleLoading();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.LogE(TAG,"onRefresh******************");
                refreshData();
            }

            @Override
            public void onLoadMore() {
                LogUtil.LogE(TAG,"onLoadMore******************");
                loadMoreData();
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        final BaseAdapter adapter=new BaseAdapter(getActivity()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                        .inflate(R.layout.item_collection_av, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {

                                if (data==null||!(data instanceof Collection.CollectionItem)) return;

                                Collection.CollectionItem collection= (Collection.CollectionItem) data;

                                bindData4Type(holder,collection);

                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {

                                Collection.CollectionItem collection = (Collection.CollectionItem) getData(position);

                                handleItemClick4Type(collection);

                            }
                        });
                return holder;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                    RecyclerView.State state)
            {
                outRect.set(0,0, 0,
                        0);
            }

        };

//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,RecyclerView.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
//                        mRefreshLayout.startLoad();
//                    }
//                }
//            }
//        });

    }


    private void bindData4Type(BaseViewHolder holder, Collection.CollectionItem collection) {

        final int type = collection.getVedio_type();

        TextView tv_time_total=holder.getView(R.id.tv_time_total);
        tv_time_total.setVisibility(type==TYPE_IT?View.GONE:View.VISIBLE);

        if (tv_time_total.getVisibility() == View.VISIBLE) {
            tv_time_total.setText(OtherUtil.getVideoTime(collection.getVedio_time()));
        }

        //共同部分
        holder.setNetImage(Constant.IMG_COMPRESS,R.id.iv_image,collection
                .getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
        holder.setText(R.id.tv_title,collection.getName());
    }

    private void handleItemClick4Type(Collection.CollectionItem collection) {
        if (collection==null) return;

        final int type = collection.getVedio_type();

        Intent intent=new Intent();
        intent.putExtra(Constant.KEY_ID, collection.getId());

        switch (type) {
            case TYPE_AUDIO:
            case TYPE_VIDEO:
                intent.setClass(getActivity(), AvVideoDetailActivity.class);
                intent.putExtra(Constant.KEY_VIDEO_URL,collection.getUrl());
                break;
            case TYPE_IT:
                intent.setClass(getActivity(), AvItDetailActivity.class);
                break;
        }

        startActivity(intent);
    }


    private void reset() {
        mRefreshData=true;
        mCollection = null;
        mPageNum=1;
    }

    private void refreshData() {
        reset();
        loadAvAllData();
    }


    private void loadMoreData() {
        if (mRefreshData) {
            mRefreshData = false;
        }
        mPageNum++;
        loadAvAllData();
    }


    @Override
    public void loadData() {
        loadAvAllData();
    }

    private void loadAvAllData() {

        JSONObject params= OtherUtil.getJSONObject(getActivity(), Constant.MODEL_COLLECTION,Constant
                .ACTION_GET_MY_COLLECTION, true);

        JSONUtil.put(params,"pageNum",mPageNum);

        JSONUtil.put(params,"type",3);

        dataRequest(false, params, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {
            }

            @Override
            public void onSuccess(String mainJsonString, JSONObject mainJsonObject,
                    JSONObject rootJsonObject)
            {
                mCollection = JSONUtil.fromJson(mainJsonString, Collection.class);

                if (mCollection==null) return;

                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();

                if (mRefreshData) {
                    adapter.removeAllData();
                }

                List<Collection.CollectionItem> avs = mCollection.getList();

                adapter.addData(BaseAdapter.AddType.LASE, avs);

                if (mRefreshLayout != null && mCollection != null) {
                    //判断是否下次还可以上拉加载更多
                    int allPageNumber = mCollection.getAllPageNumber();
                    if (mPageNum >= allPageNumber) {
                        LogUtil.LogE(TAG, "stop load more");
                        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
                    } else {
                        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
                    }
                }

            }

            @Override
            public void onFail(int key, String message) {
                if (key == RequestCallBack2.KEY_FAIL) {
                    visibleNoNetWork();
                } else if (key == 3) {
                    visibleNoData();
                }else{
                    OtherUtil.showToast(getActivity(),"没有更多内容了");
                }
                //如果上拉时出现错误还原请求时页号
                if (!mRefreshData) {
                    mPageNum--;
                }
            }

            @Override
            public void requestAfter() {
                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setLoading(false);
                inVisibleLoading();
            }
        });
    }


    @Override
    public void onDestroyView() {
        reset();
        cancelRequest(TAG);
        ImageDisplayUtil.stopTask();
        super.onDestroyView();
    }
}
