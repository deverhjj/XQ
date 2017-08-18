package com.binfenjiari.widget.media;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;


import com.binfenjiari.R;
import com.binfenjiari.utils.Views;
import com.github.huajianjiang.baserecyclerview.widget.BaseViewHolder;


/**
 * Created by jhj_Plus on 2016/6/14.
 */
public class MediaHelper {
    private static final String TAG = "MediaHelper";

    /**
     * 停止视频列表里正在播放的视频
     * 使用场景：1.刷新视频列表，2.离开视频列表界面
     *
     * @param recyclerView 显示视频列表的 RecyclerView
     */
    public static void stopPlayingVideo(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        final int onScreenChildCount = recyclerView.getChildCount();
        for (int i = 0; i < onScreenChildCount; i++) {
            View childView = recyclerView.getChildAt(i);
            if (childView == null) continue;
            VideoView playingVideoView = (VideoView) childView.findViewById(R.id.videoView);
            stopPlayingVideo(playingVideoView);
        }
    }

    /**
     * 停止正在播放的视频
     * 使用场景：1.视频播放列表切换播放视频，停止除了 exceptWho 的视频
     *
     * @param isVideoList  当前界面是否在视频列表界面，如果不是直接返回不处理，因为详情界面只有一个视频可播放
     * @param recyclerView
     * @param exceptWho    停止播放操作所忽略的视频
     */
    public static void stopPlayingVideo(boolean isVideoList, RecyclerView recyclerView,
            VideoView exceptWho)
    {
        if (recyclerView == null || !isVideoList) return;
        final int onScreenChildCount = recyclerView.getChildCount();
        for (int i = 0; i < onScreenChildCount; i++) {
            View childView = recyclerView.getChildAt(i);
            if (childView == null) continue;
            VideoView playingVideoView = (VideoView) childView.findViewById(R.id.videoView);
            if (playingVideoView == exceptWho) continue;
            stopPlayingVideo(playingVideoView);
        }
    }

    /**
     * 停止视频列表指定的视频
     *
     * @param videoView 需要立刻停止播放的 VideoView
     */
    public static void stopPlayingVideo(VideoView videoView) {
        if (videoView == null) return;
        videoView.stopPlayBack();
        MediaController mediaController = videoView.getMediaController();
        if (mediaController != null) {
            //重置界面
            mediaController.reset();
        }
    }

    public static VideoView findPlayingVideo(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return null;
        }
        final int onScreenChildCount = recyclerView.getChildCount();
        for (int i = 0; i < onScreenChildCount; i++) {
            View view = recyclerView.getChildAt(i);
            if (view == null) {
                continue;
            }
            BaseViewHolder viewHolder = (BaseViewHolder) recyclerView.getChildViewHolder(view);
            if (viewHolder == null) {
                continue;
            }
            VideoView videoView = viewHolder.getView(R.id.videoView);
            if (videoView != null && videoView.isPlaying()) {
                return videoView;
            }
        }
        return null;
    }

    /**
     * 设置 VideoItem MediaController
     * @param url
     * @param videoPlayCallback
     */
    public static MediaController setupVideoView(View controlView, String url,
            @Nullable MediaController.VideoPlayCallback videoPlayCallback)
    {
        VideoView videoView = Views.find(controlView, R.id.videoView);
        MediaController controller = new MediaController(url, controlView);
        controller.setVideoPlayCallback(videoPlayCallback);
        controller.setPauseView((ImageButton) Views.find(controlView, R.id.media_pause_start));
        controller.setControl(videoView);
        controller.setCoverLayout((ViewGroup) Views.find(controlView, R.id.layout_cover));
        controller.setProgressBar((ProgressBar) Views.find(controlView, R.id.progress));
        videoView.setMediaController(controller);
        videoView.setScaleType(VideoView.ScaleType.CENTER_CROP);
        videoView.setTag(controller);
        return controller;
    }
}
