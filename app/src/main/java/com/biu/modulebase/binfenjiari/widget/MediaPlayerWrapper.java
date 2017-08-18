package com.biu.modulebase.binfenjiari.widget;

import android.media.MediaPlayer;

/**
 * Created by jhj_Plus on 2016/5/16.
 */
public class MediaPlayerWrapper {

    private static final String TAG = "MediaPlayerWrapper";

    private MediaPlayer mMediaPlayer;

    private State mCurrentState;

    private State mTargetState;

    private int mBuffedPercent;

    public enum State {
        IDLE,
        INITIALIZED,
        PREPARING,
        PREPARED,
        STARTED,
        PAUSED,
        STOPPED,
        PLAYBACK_COMPLETED,
        END,
        ERROR
    }

    public void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mCurrentState = State.IDLE;
        mTargetState = State.IDLE;

        mMediaPlayer.setOnPreparedListener(mPreparedListener);
        mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
        mMediaPlayer.setOnInfoListener(mInfoListener);
        mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
        mMediaPlayer.setOnCompletionListener(mCompletionListener);
        mMediaPlayer.setOnErrorListener(mErrorListener);
        mMediaPlayer.setOnVideoSizeChangedListener(mVideoSizeChangedListener);
    }

    private MediaPlayer.OnPreparedListener mPreparedListener =
            new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                }
            };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new MediaPlayer.OnBufferingUpdateListener() {


                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {

                }
            };

    private MediaPlayer.OnInfoListener mInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };

    private MediaPlayer.OnSeekCompleteListener mSeekCompleteListener =
            new MediaPlayer.OnSeekCompleteListener() {


                @Override
                public void onSeekComplete(MediaPlayer mp) {

                }
            };

    private MediaPlayer.OnCompletionListener mCompletionListener =
            new MediaPlayer.OnCompletionListener() {


                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            };

    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener mVideoSizeChangedListener =
            new MediaPlayer.OnVideoSizeChangedListener() {

                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                }
            };

}

