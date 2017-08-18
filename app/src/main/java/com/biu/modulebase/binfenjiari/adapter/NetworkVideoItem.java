package com.biu.modulebase.binfenjiari.adapter;

import android.support.v7.widget.RecyclerView;

import com.biu.modulebase.binfenjiari.widget.video_player_manager.manager.VideoPlayerManager;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.meta.MetaData;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.ui.VideoPlayerView;

/**
 * Created by jhj_Plus on 2016/5/25.
 */
public class NetworkVideoItem extends BaseVideoItem {
    private static final String TAG = "NetworkVideoItem";

    public NetworkVideoItem(VideoPlayerManager<MetaData> videoPlayerManager) {
        super(videoPlayerManager);
    }

    @Override
    public void update(int position, RecyclerView.ViewHolder viewHolder,
            VideoPlayerManager<MetaData> videoPlayerManager)
    {

    }

    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerView player,
            VideoPlayerManager<MetaData> videoPlayerManager)
    {

    }

    @Override
    public void stopPlayback(VideoPlayerManager videoPlayerManager) {

    }
}
