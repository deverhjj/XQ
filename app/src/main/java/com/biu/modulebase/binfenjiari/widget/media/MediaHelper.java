package com.biu.modulebase.binfenjiari.widget.media;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.common.adapter.BaseViewHolder;

/**
 * Created by jhj_Plus on 2016/6/14.
 */
public class MediaHelper  {
    private static final String TAG = "MediaHelper";

    /**
     *  停止视频列表里正在播放的视频
     *  使用场景：1.刷新视频列表，2.离开视频列表界面
     * @param recyclerView 显示视频列表的 RecyclerView
     */
    public static void stopPlayingVideo(RecyclerView recyclerView) {
        if (recyclerView==null) return;
       final int onScreenChildCount=recyclerView.getChildCount();
        for (int i = 0; i < onScreenChildCount; i++) {
            View view=recyclerView.getChildAt(i);
            if (view==null) continue;
            BaseViewHolder viewHolder= (BaseViewHolder) recyclerView.getChildViewHolder(view);
            stopPlayingVideo(viewHolder);
        }
    }

    /**
     * 停止正在播放的视频
     * 使用场景：1.视频播放列表切换播放视频，停止除了 exceptWho 的视频
     * @param isVideoList  当前界面是否在视频列表界面，如果不是直接返回不处理，因为详情界面只有一个视频可播放
     * @param recyclerView
     * @param exceptWho 停止播放操作所忽略的视频
     */
    public static void stopPlayingVideo(boolean isVideoList, RecyclerView recyclerView,
            BaseViewHolder exceptWho)
    {
        if (recyclerView == null || !isVideoList) return;
        final int onScreenChildCount=recyclerView.getChildCount();
        for (int i = 0; i < onScreenChildCount; i++) {
            View view=recyclerView.getChildAt(i);
            if (view==null) continue;
            BaseViewHolder viewHolder= (BaseViewHolder) recyclerView.getChildViewHolder(view);
            if (viewHolder==exceptWho) continue;
            stopPlayingVideo(viewHolder);
        }
    }

    /**
     * 停止视频列表指定的视频
     * @param videoHolder 包含 VideoView 的 Holder，用于获取并停止该视频播放
     */
    public static void stopPlayingVideo(BaseViewHolder videoHolder) {
        if (videoHolder==null) return;
        VideoView videoView=videoHolder.getView(R.id.videoView);
        if (videoView==null) return;
        videoView.stopPlayBack();
        MediaController mediaController=videoView.getMediaController();
        if (mediaController!=null) {
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
}
