package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.AvItDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack2;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.Av;
import com.biu.modulebase.binfenjiari.model.AvItem;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/4/27.
 */
public class AvImageTextFragment extends BaseFragment {
    private static final String TAG = "AvImageTextFragment";

    private final static int AV_IT_LIKE_REQUEST =114;
    private RecyclerView mRecyclerView;
    private LSwipeRefreshLayout mRefreshLayout;

    private int mPageNum=1;
    private boolean mRefreshData=true;

    private Av mAv;

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
                        .inflate(R.layout.item_av_image_text, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                if (data==null||!(data instanceof AvItem)) return;

                                AvItem av= (AvItem) data;

                                holder.setNetImage(Constant.IMG_COMPRESS,R.id.iv_cover,av
                                        .getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
//                                holder.setNetImage(Constant.IMG_COMPRESS,R.id.iv_cover_mp3, av.getBanner_pic(),
//                                        ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                                holder.setText(R.id.tv_title,av.getName());

                                holder.setText(R.id.like,av.getLike_number());
                                CheckBox likeBox=holder.getView(R.id.like);
                                likeBox.setChecked(av.getLike_status() == 1);

                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {
                                if (handleViewClick(view,position)) {
                                    return;
                                }
                                AvItem item = (AvItem) getData(position);
                                Intent intent=new Intent(getActivity(), AvItDetailActivity.class);
                                intent.putExtra(Constant.KEY_POSITION, position);
                                intent.putExtra(Constant.KEY_ID, item.getId());
                                startActivityForResult(intent,AV_IT_LIKE_REQUEST);
                            }
                        });
                holder.setItemChildViewClickListener(R.id.share);
                holder.setItemChildViewClickListener(R.id.like);
                return holder;
            }

        };

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private boolean handleViewClick(View view,int position) {
        if (position==RecyclerView.NO_POSITION || mAv==null) return true;

        int id = view.getId();
        final AvItem item = mAv.getVedioList().get(position);

        if (id == R.id.share) {
            OtherUtil.showShareFragment(AvImageTextFragment.this,item.getId(),item.getName(),item.getName(),Constant.SHARE_PIC_AND_TEXT);
            return true;
        } else if (id == R.id.like) {

            if (item != null) {
                JSONObject params = OtherUtil.getJSONObject(getActivity(), Constant.MODEL_AV,
                        Constant.ACTION_AV_LIKE, true);
                JSONUtil.put(params, "id", item.getId());
                OtherUtil.like(AvImageTextFragment.this, (CheckBox) view, params,
                        new OtherUtil.LikeCallback() {
                            @Override
                            public void onFinished(int backKey) {
                                if (backKey != -1) {
                                    item.setLike_status(backKey);
                                    if(backKey ==1){
                                        item.setLike_number(Utils.isInteger(item.getLike_number())+1+"");
                                    }else if(backKey ==2){
                                        item.setLike_number(Utils.isInteger(item.getLike_number())-1+"");
                                    }
                                }
                            }
                        });
            }
            return true;
        }
        return false;
    }

    private void reset() {
        mRefreshData=true;
        mAv = null;
        mPageNum=1;
    }

    private void refreshData() {
        reset();
        loadAvItData();
    }


    private void loadMoreData() {
        if (mRefreshData) {
            mRefreshData = false;
        }
        mPageNum++;
        loadAvItData();
    }


    @Override
    public void loadData() {
        loadAvItData();
    }

    private void loadAvItData() {

        JSONObject params= OtherUtil.getJSONObject(getActivity(), Constant.MODEL_AV,Constant
                .ACTION_AV_LIST, false);

        long time = mAv == null || mPageNum == 1 ? OtherUtil.getTimeSecs() : mAv.getTime();

        JSONUtil.put(params,"time",time);

        JSONUtil.put(params,"pageNum",mPageNum);

        JSONUtil.put(params,"type",2);

        dataRequest(false, params, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {
            }

            @Override
            public void onSuccess(String mainJsonString, JSONObject mainJsonObject,
                    JSONObject rootJsonObject)
            {
                mAv = JSONUtil.fromJson(mainJsonString, Av.class);

                if (mAv==null)return;

                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();

                if (mRefreshData) {
                    adapter.removeAllData();
                }

                List<AvItem> avs = mAv.getVedioList();

                adapter.addData(BaseAdapter.AddType.LASE, avs);

                if (mRefreshLayout != null && mAv != null) {
                    //判断是否下次还可以上拉加载更多
                    int allPageNumber = mAv.getAllPageNumber();
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
                }else if (key == 3 && mRefreshData ) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case AV_IT_LIKE_REQUEST:
                    BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                    int position = data.getIntExtra("position",-1);
                    int backKey = data.getIntExtra("backKey",-1);
                    AvItem bean = (AvItem) adapter.getData(position);
                    bean.setLike_status(backKey==1?1:2);
                    int likeNum = Utils.isInteger(bean.getLike_number());
                    bean.setLike_number(backKey==1?++likeNum +"":--likeNum+"" );
                    adapter.changeData(position,bean);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        reset();
        cancelRequest(TAG);
        ImageDisplayUtil.stopTask();
        super.onDestroyView();
    }

}
