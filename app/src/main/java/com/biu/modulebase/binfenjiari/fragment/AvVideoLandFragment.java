package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.AvVideoLandActivity;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.widget.media.MediaController;
import com.biu.modulebase.binfenjiari.widget.media.VideoView;
import com.biu.modulebase.common.base.BaseFragment;

import java.io.IOException;

/**
 * Created by jhj_Plus on 2016/6/21.
 */
public class AvVideoLandFragment extends BaseFragment implements MediaController.VideoPlayCallback,AvVideoLandActivity.Callback
{

    private static final String TAG = "AvVideoLandFragment";

    private MediaController mMediaController;

    private int mVideoPositionInList;
    private String mUrl = null;
    private int mPreSeekToPos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        if (args!=null) {
            mVideoPositionInList=args.getInt(Constant.KEY_POSITION);
            mUrl=args.getString(Constant.KEY_VIDEO_URL);
            mPreSeekToPos=args.getInt(Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION);
        }
    }

    public static AvVideoLandFragment newInstance(int videoPositionInList, String url,
            int preSeekToPosition)
    {
        Bundle args = new Bundle();
        args.putInt(Constant.KEY_POSITION, videoPositionInList);
        args.putString(Constant.KEY_VIDEO_URL, url);
        args.putInt(Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION, preSeekToPosition);
        AvVideoLandFragment fragment = new AvVideoLandFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_av_video_land,container,false);
    }

    @Override
    protected void initView(View rootView) {

        VideoView videoView = (VideoView) rootView.findViewById(R.id.videoView);

        if (videoView != null) {
            videoView.setScaleType(VideoView.ScaleType.FIT_CENTER);
        }

        rootView.setTag(mVideoPositionInList);
        BaseViewHolder holder=new BaseViewHolder(rootView);

        mMediaController= AvVideoFragment.setupVideoItem(true, this, holder, null, mUrl, this);
        mMediaController.getFullScreen().setImageResource(R.mipmap.ic_quit_full_screen);
        mMediaController.getFullScreen().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟返回键功能
//                Instrumentation inst = new Instrumentation();
//                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean playBefore(MediaController mediaController) {
        return true;
    }


    @Override
    public void onResume() {
        if (mMediaController!=null) {
            mMediaController.playVideo(mPreSeekToPos);
        }
        super.onResume();
    }


    @Override
    public void onPause() {
        if (mMediaController != null) {
            //这里保存视频播放的进度供返回播放时恢复到该位置播放
            mPreSeekToPos = mMediaController.getCurrentPlayPosition();
            Log.e(TAG, "onPause()===>" + mPreSeekToPos);
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtil.LogI(TAG,"onStop");
        if (mMediaController!=null) {
            mMediaController.stopPlayingVideo();
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra(Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION,
                mMediaController != null ? mMediaController.getCurrentPlayPosition() : 0);
        final int resumePlayPos = mVideoPositionInList;
        result.putExtra(Constant.KEY_POSITION, resumePlayPos);
        getActivity().setResult(Activity.RESULT_OK, result);
    }
}
