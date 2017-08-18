package com.biu.modulebase.binfenjiari.widget.video_player_manager.player_messages;

import com.biu.modulebase.binfenjiari.widget.video_player_manager.Config;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.PlayerMessageState;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.manager.VideoPlayerManagerCallback;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.ui.VideoPlayerView;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.utils.Logger;

/**
 * Created by jhj_Plus on 2016/5/25.
 */
public class Resume extends PlayerMessage {
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
    private static final String TAG = Start.class.getSimpleName();

    private PlayerMessageState mResultPlayerMessageState;

    public Resume(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {

        PlayerMessageState currentState = getCurrentState();
        if(SHOW_LOGS) Logger.d(TAG, "currentState " + currentState);

        switch (currentState){
            case SETTING_NEW_PLAYER:
            case IDLE:
            case INITIALIZED:
            case PREPARING:
            case RELEASING:
            case RELEASED:
            case RESETTING:
            case RESET:
            case CLEARING_PLAYER_INSTANCE:
            case PLAYER_INSTANCE_CLEARED:
            case CREATING_PLAYER_INSTANCE:
            case PLAYER_INSTANCE_CREATED:
            case SETTING_DATA_SOURCE:
            case DATA_SOURCE_SET:
            case END:

                throw new RuntimeException("unhandled current state " + currentState);

            case PREPARED:
            case STARTING:
            case PAUSED:
            case PLAYBACK_COMPLETED:
                currentPlayer.start();
                mResultPlayerMessageState = PlayerMessageState.STARTED;
                break;

            case ERROR:
                mResultPlayerMessageState = PlayerMessageState.ERROR;
                break;

            case PAUSING:
            case STOPPING:
            case STOPPED:
                // TODO: probably need to handle this
                throw new RuntimeException("unhandled current state " + currentState);
        }
    }

    @Override
    protected PlayerMessageState stateBefore() {
        PlayerMessageState result = null;

        PlayerMessageState currentState = getCurrentState();
        if(SHOW_LOGS) Logger.d(TAG, "stateBefore, currentState " + currentState);
        switch (currentState) {

            case PREPARED:
                result = PlayerMessageState.STARTING;
                break;

            case SETTING_NEW_PLAYER:
            case IDLE:
            case INITIALIZED:
            case PREPARING:
            case RELEASING:
            case RELEASED:
            case RESETTING:
            case RESET:
            case CLEARING_PLAYER_INSTANCE:
            case PLAYER_INSTANCE_CLEARED:
            case CREATING_PLAYER_INSTANCE:
            case PLAYER_INSTANCE_CREATED:
            case SETTING_DATA_SOURCE:
            case DATA_SOURCE_SET:
            case PLAYBACK_COMPLETED:
            case END:
            case STARTING:
                throw new RuntimeException("unhandled current state " + currentState);

            case ERROR:
                result = PlayerMessageState.ERROR;
                break;
            case STARTED:
            case PAUSING:
            case PAUSED:
            case STOPPING:
            case STOPPED:
                // TODO: probably need to handle this
                throw new RuntimeException("unhandled current state " + currentState);

        }
        return result;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return mResultPlayerMessageState;
    }
}
