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
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.biu.modulebase.binfenjiari.R;
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

import java.util.List;

/**
 * Created by jhj_Plus on 2016/4/26.
 */
public class AvVideoFragment extends BaseFragment
        implements AvFragment.Callbacks ,MediaController.VideoPlayCallback {
    private static final String TAG = "AvVideoFragment";

    private final static int AV_VIDEO_LIKE_REQUEST =115;
    public static final int REQUEST_VIDEO_STATUS = 1;

    private RecyclerView mRecyclerView;

    private LSwipeRefreshLayout mRefreshLayout;

    private Av mAv;

    private int mPageNum = 1;

    private boolean mRefreshData=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.LogI(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        LogUtil.LogI(TAG,"onCreateView");
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }


    @Override
    protected void initView(View rootView) {
        LogUtil.LogE(TAG,"initView");


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
        //提高性能
        mRecyclerView.setHasFixedSize(true);

        final BaseAdapter adapter=new BaseAdapter(getActivity()) {

            @Override
            public BaseViewHolder getViewHolder(final ViewGroup parent, final int viewType) {
                final BaseViewHolder holder=new BaseViewHolder(LayoutInflater.from(getActivity())
                        .inflate(R.layout.item_av_video, parent, false), new BaseViewHolder
                        .Callbacks2() {
                    @Override
                    public void bind(BaseViewHolder holder, Object data) {

                        if (data==null||!(data instanceof AvItem)) return;

                        AvItem videoItem = (AvItem) data;

                        ViewGroup coverLayout = holder.getView(R.id.layout_cover);
                        coverLayout.setVisibility(View.VISIBLE);

                        holder.setNetImage(Constant.IMG_COMPRESS,R.id.iv_cover, videoItem.getBanner_pic(),
                                ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                        holder.setNetImage(Constant.IMG_COMPRESS,R.id.iv_cover_mp3, videoItem.getBanner_pic(),
                                ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                        holder.setText(R.id.tv_title,videoItem.getName());
                        holder.setText(R.id.tv_time_total,
                                OtherUtil.getVideoTime(videoItem.getVedio_time()));

                        holder.setText(R.id.like,videoItem.getLike_number());
                        CheckBox likeBox=holder.getView(R.id.like);
                        likeBox.setChecked(videoItem.getLike_status() == 1);

                        setupVideoItem(true, AvVideoFragment.this, holder, mRecyclerView,
                                videoItem.getUrl(), AvVideoFragment.this);
                    }

                    @Override
                    public void onItemClick(BaseViewHolder holder, View view, int position) {

                        if (handleViewClick(view,position)) {
                           return;
                        }

                        VideoView videoView=holder.getView(R.id.videoView);
                        MediaController mediaController=videoView.getMediaController();

                        AvItem item = (AvItem) getData(position);
                        Intent intent=new Intent(getActivity(), AvVideoDetailActivity.class);
                        intent.putExtra(Constant.KEY_ID, item.getId());
                        intent.putExtra(Constant.KEY_POSITION, position);
                        intent.putExtra(Constant.KEY_VIDEO_URL,item.getUrl());
                        intent.putExtra(Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION,
                                mediaController!=null?mediaController.getCurrentPlayPosition():0);

                        //停止播放视频
                        MediaHelper.stopPlayingVideo(holder);

                        startActivityForResult(intent,AV_VIDEO_LIKE_REQUEST);
                    }
                });

                holder.setItemChildViewClickListener(R.id.like,R.id.share);

                return holder;
            }
        };

        final LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setLayoutManager(layoutManager);

        handleVideoItemDetach(mRecyclerView);
    }

    public static void handleVideoItemDetach(final RecyclerView recyclerView) {
        if (recyclerView==null) return;
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                int childAdapterPos=recyclerView.getChildAdapterPosition(view);
               LogUtil.LogE(TAG,"onChildViewAttachedToWindow==>"+childAdapterPos);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

                int childAdapterPos = recyclerView.getChildAdapterPosition(view);

                LogUtil.LogE(TAG, "onChildViewDetachedFromWindow==>" + childAdapterPos);

                BaseViewHolder viewHolder= (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition
                        (childAdapterPos);

                MediaHelper.stopPlayingVideo(viewHolder);
            }
        });
    }

    /**
     *  设置 VideoItem MediaController
     * @param holder
     * @param recyclerView
     * @param url
     * @param videoPlayCallback
     */
    public static MediaController setupVideoItem(boolean isInVideoList,BaseFragment baseFragment,
            BaseViewHolder holder,
            RecyclerView
            recyclerView,String url,
            MediaController.VideoPlayCallback videoPlayCallback)
    {
        VideoView videoView = holder.getView(R.id.videoView);
        MediaController controller = new MediaController(isInVideoList, baseFragment, recyclerView,
                holder, url);
        controller.setVideoPlayCallback(videoPlayCallback);
        controller.setPauseView((ImageButton) holder.getView(R.id.media_pause_start));
        controller.setControl(videoView);
        controller.setCoverLayout((ViewGroup) holder.getView(R.id.layout_cover));
        controller.setProgressBar((ProgressBar) holder.getView(R.id.progress));
        videoView.setMediaController(controller);
        videoView.setTag(controller);
        return controller;
    }


    private void reset() {
        mRefreshData=true;
        mAv = null;
        mPageNum=1;
    }

    private void refreshData() {
        reset();
        loadVideoData();
    }

    private void loadMoreData() {
        if (mRefreshData) {
            mRefreshData = false;
        }
        mPageNum++;
        loadVideoData();
    }

    @Override
    public void loadData() {
        loadVideoData();
    }

    private void loadVideoData() {
        JSONObject params= OtherUtil.getJSONObject(getActivity(), Constant.MODEL_AV,Constant
                .ACTION_AV_LIST, false);

        long time = mAv == null || mPageNum == 1 ? OtherUtil.getTimeSecs() : mAv.getTime();

        JSONUtil.put(params,"time",time);

        JSONUtil.put(params,"pageNum",mPageNum);

        //视频类型请求
        JSONUtil.put(params,"type",1);

        dataRequest(false, params, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {

            }

            @Override
            public void onSuccess(String mainJsonString, JSONObject mainJsonObject,
                    JSONObject rootJsonObject)
            {
                mAv=JSONUtil.fromJson(mainJsonString,Av.class);

                if (mAv==null) return;

                BaseAdapter adapter= (BaseAdapter) mRecyclerView.getAdapter();

                if (mRefreshData) {
                    //下拉刷新时停止当前播放的视频
                    MediaHelper.stopPlayingVideo(mRecyclerView);
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
                } else if (key == 3 && mRefreshData) {
                    visibleNoData();
                }else{
                    OtherUtil.showToast(getActivity(),"没有更多内容了");
                }

                LogUtil.LogE(TAG,"onFail*");

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


    private boolean handleViewClick(View view,int position) {
        if (position==RecyclerView.NO_POSITION || mAv==null) return true;
        int id = view.getId();
        final AvItem item = mAv.getVedioList().get(position);
        if (id == R.id.share) {
            OtherUtil.showShareFragment(AvVideoFragment.this,item.getId(),item.getName(),item.getName(),Constant.SHARE_AUDIO);
            return true;
        } else if (id == R.id.like) {
            if (item != null) {
                JSONObject params = OtherUtil.getJSONObject(getActivity(), Constant.MODEL_AV,
                        Constant.ACTION_AV_LIKE, true);
                JSONUtil.put(params, "id", item.getId());
                OtherUtil.like(AvVideoFragment.this, (CheckBox) view, params,
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.LogI(TAG,"onActivityResult********************");
//        if (requestCode != REQUEST_VIDEO_STATUS) return;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case REQUEST_VIDEO_STATUS:
                    int resumePlayPos = data.getIntExtra(Constant.KEY_POSITION, 0);
                    int preSeekToPos = data.getIntExtra(Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION, 0);
                    LogUtil.LogI(TAG,"onActivityResult********************"+"resumePlayPos" +
                            "="+resumePlayPos+",preSeekToPos==>"+preSeekToPos);
                    BaseViewHolder holder = (BaseViewHolder) mRecyclerView.findViewHolderForAdapterPosition(
                            resumePlayPos);
                    if (holder==null) return;
                    VideoView videoView = holder.getView(R.id.videoView);
                    videoView.getMediaController().playVideo(preSeekToPos);
                    break;
                case AV_VIDEO_LIKE_REQUEST:
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
    public void onResume() {
        LogUtil.LogI(TAG,"onResume");
        super.onResume();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtil.LogI(TAG,"onActivityCreated");
        visibleLoading();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        LogUtil.LogI(TAG,"onPause");
        super.onPause();
    }


    @Override
    public void onStop() {
        LogUtil.LogI(TAG,"onStop");
        MediaHelper.stopPlayingVideo(mRecyclerView);
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        LogUtil.LogI(TAG,"onDestroyView");
        reset();
        cancelRequest(TAG);
        ImageDisplayUtil.stopTask();
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        LogUtil.LogI(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtil.LogI(TAG,"onDetach");
        super.onDetach();
    }

    /**
     * 不在视频频道就停止当前播放的视频
     * @param inVideoChannel
     */
    @Override
    public void onChannelChanged(boolean inVideoChannel,AvFragment.Callbacks selectedChannel) {
        if (!inVideoChannel || !(selectedChannel instanceof AvVideoFragment)) {
            LogUtil.LogI(TAG, "onChannelChanged==>stopAnyPlayback");
            MediaHelper.stopPlayingVideo(mRecyclerView);
        }
    }

    @Override
    public boolean playBefore(MediaController mediaController) {
        return MyApplication.isInMobileConnectPlayVideo || OtherUtil.checkNetwork(getActivity(),
                mediaController);
    }


}
