package com.binfenjiari.widget.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.binfenjiari.R;
import com.binfenjiari.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by jhj_Plus on 2016/4/27.
 */
public class CustomMediaController extends MediaController {
    private static final String TAG = "CustomMediaControl";

    private MediaPlayerControl mPlayer;

    private ControllerHandler mHandler;

    private WeakReference<CustomMediaController> mMediaController = new WeakReference<>(this);

    private static final int STATE_ERROR = -1;
    private static final int STATE_PREPARED= 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSED = 3;
    private static final int STATE_PLAYBACK_COMPLETED = 4;

    private int mState;
    private  boolean mDragging;

    private View mAnchor;
    private TextView mCurTime;
    private TextView mEndTime;
    private ImageButton mPauseView;
    private SeekBar mSb;


    public CustomMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        //initControlView();
    }

    public CustomMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public CustomMediaController(Context context) {
        this(context, true);

    }

    private void initControlView() {
        mCurTime = (TextView) findViewById(R.id.tv_time_curr);
        mEndTime = (TextView) findViewById(R.id.tv_time_end);
        mSb = (SeekBar) findViewById(R.id.sb);
        mSb.setMax(100);
        mSb.setOnSeekBarChangeListener(l);

        mHandler=new ControllerHandler(mMediaController);
    }

    public void setPauseView(ImageButton pauseView) {
        mPauseView = pauseView;
        if (pauseView!=null) {
           mPauseView.setOnClickListener(mPauseListener);
        }
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        super.setMediaPlayer(player);
        mPlayer = player;
        if (player != null && player instanceof VideoView) {
            VideoView videoView = (VideoView) player;
            videoView.setOnCompletionListener(mCompletionListener);
            videoView.setOnErrorListener(mErrorListener);
            videoView.setOnPreparedListener(mPreparedListener);
        }
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        mAnchor=view;

        removeAllViews();

        final View controllerView = makeControllerView();

        addView(controllerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        initControlView();
    }

    private View makeControllerView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_controller_view, this,
                                                         false);
    }

    @Override
    public void show(int timeout) {

        if (timeout!=0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendEmptyMessageDelayed(FADE_OUT,timeout);
        }

        super.show(timeout);

        if (isShowing() && mAnchor != null && !mDragging) {
            setProgress();
        }

        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        togglePauseStartView(VISIBLE,false);
    }


    @Override
    public void hide() {
        if (mAnchor == null) {
            return;
        }
        if (isShowing()) {
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        if (!mDragging) {
            togglePauseStartView(INVISIBLE,false);
            super.hide();
        }
    }

    private void setProgress() {
        if (mPlayer == null) {
            return;
        }

        final int duration = mPlayer.getDuration();
        final int curPos = mPlayer.getCurrentPosition();

        Logger.e(TAG, "setProgress_curPos=" + curPos + ",duration=" + duration);

        if (duration > 0) {
            if (mSb != null) {
                int progress = (int) (curPos / (float) duration * mSb.getMax());
                if (mState == STATE_PLAYBACK_COMPLETED) {
                    progress = mSb.getMax();
                }
                mSb.setProgress(progress);
            }
        }
        if (mCurTime != null) {
            String time = time2String(curPos);
            if (mState == STATE_PLAYBACK_COMPLETED) {
                time = time2String(duration);
            }
            mCurTime.setText(time);
        }

        if (mEndTime != null) {
            mEndTime.setText(time2String(duration));
        }
    }

    private String time2String(int timMs) {
        int totalSeconds=timMs/1000;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds / 60) % 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%1$d:%2$02d:%3$02d", hours, minutes, seconds);
        } else {
            return String.format("%1$02d:%2$02d", minutes, seconds);
        }
    }

    private OnClickListener mPauseListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPauseView!=null) {
                togglePlayState();
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener l=new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
             mDragging=true;

             show(3600000);

             mHandler.removeMessages(SHOW_PROGRESS);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mDragging=false;

            final float percent = seekBar.getProgress() / (float) seekBar.getMax();

            if (percent < 1) {

                final int duration = mPlayer.getDuration();

                mPlayer.seekTo((int) (duration * percent));

                mPlayer.start();

                mState = STATE_PLAYING;
            }

            show(3000);

            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };


    private static final int SHOW_PROGRESS = 1;

    private static final int FADE_OUT=2;

    private static class ControllerHandler extends Handler {

        private WeakReference<CustomMediaController> mMediaController;

        public ControllerHandler(WeakReference<CustomMediaController> mediaController) {
            mMediaController = mediaController;
        }

        @Override
        public void handleMessage(Message msg) {
            CustomMediaController controller = mMediaController.get();
            switch (msg.what) {
                case SHOW_PROGRESS:
                    if (controller.isShowing() && !controller.mDragging &&
                            controller.mPlayer.isPlaying())
                    {
                        Logger.e(TAG,"handleMessage_SHOW_PROGRESS");
                        controller.setProgress();
                        sendEmptyMessageDelayed(SHOW_PROGRESS, 300);
                    }
                    break;
                case FADE_OUT:
                    controller.hide();
                    break;
                default:
                    break;
            }
        }
    }


    public void start() {
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    public void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    public void togglePlayState() {
        if (mPlayer == null) {
            return;
        }
        if (!mPlayer.isPlaying()) {
            mPlayer.start();
            mState = STATE_PLAYING;
        } else {
            mPlayer.pause();
            mState = STATE_PAUSED;
        }

        togglePauseStartView(VISIBLE, false);

        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        show(3000);
    }

    private void togglePauseStartView(int visibility,boolean forcePlay) {

        if (mPauseView == null || mPlayer == null) {
            return;
        }

        mPauseView.setVisibility(visibility);
        if (mPauseView.isShown()) {
            mPauseView.setImageResource(mPlayer.isPlaying() || forcePlay ? R.drawable.audio_pause
                                                                         : R.drawable.audio_start);
        }
    }

    private MediaPlayer.OnPreparedListener mPreparedListener =
            new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mState = STATE_PREPARED;
                    show(3000);
                    mHandler.sendEmptyMessage(SHOW_PROGRESS);
                    togglePauseStartView(VISIBLE, true);
                }
            };

    private MediaPlayer.OnCompletionListener mCompletionListener =new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            mState=STATE_PLAYBACK_COMPLETED;
            show(3000);
        }
    };

    private MediaPlayer.OnErrorListener mErrorListener=new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            mState=STATE_ERROR;
            return false;
        }
    };

}
