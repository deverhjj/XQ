package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.AvItDetailActivity;
import com.biu.modulebase.binfenjiari.activity.AvVideoDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack2;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.Av;
import com.biu.modulebase.binfenjiari.model.AvItem;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.media.MediaController;
import com.biu.modulebase.binfenjiari.widget.media.MediaHelper;
import com.biu.modulebase.binfenjiari.widget.media.VideoView;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/6/15.
 */
public class AvAllFragment extends BaseFragment implements MediaController.VideoPlayCallback,
        AvFragment.Callbacks {
    private static final String TAG = "AvAllFragment";

    private final static int AV_ALL_LIKE_REQUEST =112;

    private static final int TYPE_VIDEO=1;
    private static final int TYPE_IT=2;
    private static final int TYPE_AUDIO=3;

    private RecyclerView mRecyclerView;
    private LSwipeRefreshLayout mRefreshLayout;

    private int mPageNum=1;
    private boolean mRefreshData=true;

    private Av mAv;

    /**
     * 搜索参数
     */
    private HashMap<String,Object> mArgs;

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mArgs = (HashMap<String, Object>) args.getSerializable(Constant.KEY_SEARCH_ARGS);
        }
    }

    /**
     *  提供搜索时的调用
     * @param args 搜索的参数
     * @return
     */
    public static AvAllFragment newInstance(HashMap<String, Object> args) {
        Bundle a = new Bundle();
        a.putSerializable(Constant.KEY_SEARCH_ARGS, args);
        AvAllFragment avAllFragment = new AvAllFragment();
        avAllFragment.setArguments(a);
        return avAllFragment;
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
                        .inflate(viewType == TYPE_VIDEO ? R.layout.item_av_video
                                : viewType == TYPE_IT ? R.layout.item_av_image_text
                                        : R.layout.item_av_video, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {

                                if (data==null||!(data instanceof AvItem)) return;

                                AvItem av= (AvItem) data;

                                bindData4Type(holder,av);

                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {

                                if (handleViewClick(view,position))return;

                                if (position == RecyclerView.NO_POSITION) return;
                                AvItem av = (AvItem) getData(position);
                                handleItemClick4Type(holder,av);

                            }
                        });
                holder.setItemChildViewClickListener(R.id.share);
                holder.setItemChildViewClickListener(R.id.like);
                return holder;
            }

            @Override
            public int getItemViewType(int position) {
                AvItem avItem= (AvItem) getData(position);
                final int type = avItem != null ? avItem.getType() : 1;
                return type == 1 ? TYPE_VIDEO : type == 2 ? TYPE_IT : TYPE_AUDIO;
            }
        };

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

        AvVideoFragment.handleVideoItemDetach(mRecyclerView);

    }


    private void bindData4Type(BaseViewHolder holder, AvItem av) {
        final int type = av.getType();
        switch (type) {
            case TYPE_AUDIO:
            case TYPE_VIDEO:

                ViewGroup coverLayout = holder.getView(R.id.layout_cover);
                coverLayout.setVisibility(View.VISIBLE);

                holder.setText(R.id.tv_time_total, OtherUtil.getVideoTime(av.getVedio_time()));

                AvVideoFragment.setupVideoItem(true, AvAllFragment.this, holder, mRecyclerView,
                        av.getUrl(), AvAllFragment.this);

                holder.setNetImage(Constant.IMG_COMPRESS,R.id.iv_cover_mp3,av
                        .getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);

                break;
            case TYPE_IT:
                break;

        }

        //共同部分
        holder.setNetImage(Constant.IMG_COMPRESS,R.id.iv_cover,av
                .getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);

        holder.setText(R.id.tv_title,av.getName());
        holder.setText(R.id.like,av.getLike_number());
        CheckBox likeBox=holder.getView(R.id.like);
        likeBox.setChecked(av.getLike_status() == 1);
    }

    private void handleItemClick4Type(BaseViewHolder holder,AvItem av) {
        if (av==null) return;

        final int type = av.getType();

        Intent intent=new Intent();
        intent.putExtra(Constant.KEY_ID, av.getId());

        switch (type) {
            case TYPE_AUDIO:
            case TYPE_VIDEO:
                VideoView videoView = holder.getView(R.id.videoView);
                MediaController mediaController = videoView.getMediaController();
                intent.setClass(getActivity(), AvVideoDetailActivity.class);
                intent.putExtra(Constant.KEY_POSITION, holder.getAdapterPosition());
                intent.putExtra(Constant.KEY_VIDEO_URL, av.getUrl());
                intent.putExtra(Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION,
                        mediaController != null ? mediaController.getCurrentPlayPosition() : 0);
                //停止播放视频
                MediaHelper.stopPlayingVideo(holder);
                break;
            case TYPE_IT:
                intent.setClass(getActivity(), AvItDetailActivity.class);
                intent.putExtra(Constant.KEY_POSITION, holder.getAdapterPosition());
                break;
        }

        startActivityForResult(intent,AV_ALL_LIKE_REQUEST);
    }

    private boolean handleViewClick(View view,int position) {

        if (position==RecyclerView.NO_POSITION || mAv==null) return true;

        int id = view.getId();
        final AvItem item = mAv.getVedioList().get(position);

        if (id == R.id.share) {
            OtherUtil.showShareFragment(AvAllFragment.this, item.getId(),item.getName(),item.getName(),
                    item.getType() != TYPE_IT ? Constant.SHARE_AUDIO : Constant.SHARE_PIC_AND_TEXT);
            return true;
        } else if (id == R.id.like) {

            if (item != null) {
                JSONObject params = OtherUtil.getJSONObject(getActivity(), Constant.MODEL_AV,
                        Constant.ACTION_AV_LIKE, true);
                JSONUtil.put(params, "id", item.getId());
                OtherUtil.like(AvAllFragment.this, (CheckBox) view, params,
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

    @SuppressWarnings("unchecked")
    private void loadAvAllData() {

        JSONObject params= OtherUtil.getJSONObject(getActivity(), Constant.MODEL_AV,Constant
                .ACTION_AV_LIST, false);

        long time = mAv == null || mPageNum == 1 ? OtherUtil.getTimeSecs() : mAv.getTime();

        JSONUtil.put(params,"time",time);

        JSONUtil.put(params,"pageNum",mPageNum);

        JSONUtil.put(params,"type",0);

        if (mArgs != null) {
            String title = (String) mArgs.get(Constant.KEY_SEARCH_ARGS_TITLE);
            if (!TextUtils.isEmpty(title)) {
                JSONUtil.put(params, "title", title);
            }
        }

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

                if(mPageNum==1 && avs.size()==0){
                    if(mArgs.get("SearchResultAudioActivity")!=null){
                        showTost("未搜索相关结果！",1);
                        getActivity().finish();
                    }
                }

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
                if(mPageNum==1){
                    if(mArgs.get("SearchResultAudioActivity")!=null){
                        showTost("未搜索相关结果！",1);
                        getActivity().finish();
                    }
                }

                if (key == RequestCallBack2.KEY_FAIL) {
                    visibleNoNetWork();
                } else if (key == 3 && mRefreshData) {
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
        LogUtil.LogI(TAG,"onActivityResult");
//        if (requestCode!=AvVideoFragment.REQUEST_VIDEO_STATUS) return;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case AvVideoFragment.REQUEST_VIDEO_STATUS:
                    int resumePlayPos = data.getIntExtra(Constant.KEY_POSITION, 0);
                    int preSeekToPos = data.getIntExtra(Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION, 0);
                    BaseViewHolder holder = (BaseViewHolder) mRecyclerView.findViewHolderForAdapterPosition(
                            resumePlayPos);
                    if (holder==null) return;
                    VideoView videoView = holder.getView(R.id.videoView);
                    videoView.getMediaController().playVideo(preSeekToPos);
                    break;
                case AV_ALL_LIKE_REQUEST:
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
    }



    @Override
    public void onStop() {
        MediaHelper.stopPlayingVideo(mRecyclerView);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        reset();
        cancelRequest(TAG);
        ImageDisplayUtil.stopTask();
        super.onDestroyView();
    }

    @Override
    public boolean playBefore(MediaController mediaController) {
        return MyApplication.isInMobileConnectPlayVideo || OtherUtil.checkNetwork(getActivity(),
                mediaController);
    }

    @Override
    public void onChannelChanged(boolean inVideoChannel,AvFragment.Callbacks selectedChannel) {
        if (!inVideoChannel || !(selectedChannel instanceof AvAllFragment)) {
            LogUtil.LogI(TAG, "onChannelChanged==>stopAnyPlayback");
            MediaHelper.stopPlayingVideo(mRecyclerView);
        }
    }

}
