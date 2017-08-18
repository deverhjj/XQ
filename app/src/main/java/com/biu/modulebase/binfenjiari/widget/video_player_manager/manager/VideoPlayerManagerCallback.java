package com.biu.modulebase.binfenjiari.widget.video_player_manager.manager;


import com.biu.modulebase.binfenjiari.widget.video_player_manager.PlayerMessageState;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.meta.MetaData;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.ui.VideoPlayerView;

/**
 * This callback is used by {@link com.volokh.danylo.video_player_manager.player_messages.PlayerMessage}
 * to get and set data it needs
 */
public interface VideoPlayerManagerCallback {

    void setCurrentItem(MetaData currentItemMetaData, VideoPlayerView newPlayerView);

    void setVideoPlayerState(VideoPlayerView videoPlayerView, PlayerMessageState playerMessageState);

    PlayerMessageState getCurrentPlayerState();
}
