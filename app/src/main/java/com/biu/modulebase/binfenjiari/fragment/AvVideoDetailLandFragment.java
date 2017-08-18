package com.biu.modulebase.binfenjiari.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.widget.media.MediaController;
import com.biu.modulebase.binfenjiari.widget.media.VideoView;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * Created by jhj_Plus on 2016/4/27.
 */
public class AvVideoDetailLandFragment extends BaseFragment implements MediaController
        .VideoPlayCallback {
    private static final String TAG = "VideoDetailLandFragment";

    private VideoView mVideoView;
    private MediaController mMediaController;

    private String mUrl=null;

    /**
     * 视频播放预先设定的播放位置，返回到该界面播放视频时可指定 VideoView 开始播放的位置
     */
    private int mPreSeekToPosition=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle args=getArguments();
        if (args!=null) {
            mUrl=args.getString(Constant.KEY_VIDEO_URL);
            mPreSeekToPosition=args.getInt(Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION);
        }

    }

    public static AvVideoDetailLandFragment newInstance(String url,int preSeekToPosition) {
        Bundle args = new Bundle();
        args.putString(Constant.KEY_VIDEO_URL,url);
        args.putInt(Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION,preSeekToPosition);
        AvVideoDetailLandFragment fragment = new AvVideoDetailLandFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_av_video_detail, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {

        mVideoView= (VideoView) rootView.findViewById(R.id.videoView);

        mMediaController = AvVideoFragment.setupVideoItem(false,AvVideoDetailLandFragment.this,
                new BaseViewHolder(rootView.findViewById(R.id.layout_video)), null, mUrl,
                AvVideoDetailLandFragment.this);
    }

    @Override
    public void loadData() {

    }




    @Override
    public void onResume() {
        super.onResume();
        if (mMediaController!=null) {
            //界面恢复时直接开始播放视频
            mMediaController.playVideo(mPreSeekToPosition);
        }
    }

    @Override
    public void onPause() {
        if (mMediaController != null) {
            //这里保存视频播放的进度供返回播放时恢复到该位置播放
            mPreSeekToPosition = mMediaController.getCurrentPlayPosition();
            Log.e(TAG, "onPause()===>" + mPreSeekToPosition);
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mVideoView!=null) {
            mVideoView.stopPlayBack();
        }
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG,"onDestroyView()===>"+mPreSeekToPosition);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestroy()===>"+mPreSeekToPosition);
        mPreSeekToPosition = 0;
        super.onDestroy();
    }

    @Override
    public boolean playBefore(MediaController mediaController) {
        return MyApplication.isInMobileConnectPlayVideo || OtherUtil.checkNetwork(getActivity(),
                mediaController);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        LogUtil.LogE(TAG,"onConfigurationChanged");

        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                LogUtil.LogE(TAG, "ORIENTATION_LANDSCAPE");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                LogUtil.LogE(TAG, "ORIENTATION_PORTRAIT");
                break;
        }

    }

}
