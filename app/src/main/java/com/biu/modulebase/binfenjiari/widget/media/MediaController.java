package com.biu.modulebase.binfenjiari.widget.media;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.AvVideoDetailActivity;
import com.biu.modulebase.binfenjiari.activity.AvVideoLandActivity;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.AvVideoFragment;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;


/**
 * Created by jhj_Plus on 2016/6/7.
 */
public class MediaController {

    private static final String TAG = "MediaController";

    private boolean mIsVideoList;

    private VideoPlayCallback mVideoPlayCallback;

    private MediaHandler mMediaHandler;

    private Control mControl;

    private BaseFragment mBaseFragment;
    private RecyclerView mRecyclerView;
    private BaseViewHolder mHolder;
    private String mPath;

    private VideoView mVideoView;
    private View mControlView;
    private TextView mCurTime;
    private TextView mEndTime;
    private ImageButton mPauseView;
    private ProgressBar mProgressBar;
    private ViewGroup mCoverLayout;
    private ImageView mCoverMp3;
    private SeekBar mSb;
    private ImageButton mFullScreen;

    public MediaController(boolean isVideoList, BaseFragment baseFragment,
            RecyclerView recyclerView, BaseViewHolder holder, String path)
    {
        mIsVideoList = isVideoList;
        mBaseFragment = baseFragment;
        mRecyclerView = recyclerView;
        mHolder = holder;
        mPath = path;
        mMediaHandler = new MediaHandler();
        initControlView();
    }

    private void initControlView() {
        mVideoView=mHolder.getView(R.id.videoView);
        VideoListener videoListener=new VideoListener();
        mVideoView.setOnCompletionListener(videoListener);
        mVideoView.setOnPreparedListener(videoListener);
        mVideoView.setOnErrorListener(videoListener);
        mVideoView.setOnSeekCompleteListener(videoListener);
        mVideoView.setOnBufferingUpdateListener(videoListener);
        mVideoView.setOnInfoListener(videoListener);

        mControlView=mHolder.getView(R.id.media_controller);
        mControlView.setVisibility(View.GONE);
        mPauseView =  mHolder.getView(R.id.media_pause_start);
        mCurTime =  mHolder.getView(R.id.tv_time_curr);
        mEndTime =  mHolder.getView(R.id.tv_time_end);
        mSb =  mHolder.getView(R.id.sb);
        mSb.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mFullScreen=mHolder.getView(R.id.fullScreen);
        mFullScreen.setOnClickListener(mFullScreenListener);

        mCoverMp3 = mHolder.getView(R.id.iv_cover_mp3);
        if(mCoverMp3!=null) {
            if (mPath.endsWith(".mp3") || mPath.endsWith(".MP3")) {
                mCoverMp3.setVisibility(View.VISIBLE);
                mFullScreen.setVisibility(View.GONE);
            } else {
                mCoverMp3.setVisibility(View.GONE);
            }
        }
    }


    public void setVideoPath(String path) {
        if (TextUtils.isEmpty(path) || mPath != null && mPath.equals(path)) return;
        mPath = path;
    }

    public void setVideoPlayCallback(VideoPlayCallback videoPlayCallback)
    {
        mVideoPlayCallback = videoPlayCallback;
    }


    public void setControl(Control control) {
        mControl = control;
    }



    public ViewGroup getCoverLayout() {
        return mCoverLayout;
    }

    public ImageView getCoverMp3(){
        return mCoverMp3;
    }

    public void setCoverLayout(ViewGroup coverLayout) {
        mCoverLayout = coverLayout;
        if (mCoverLayout!=null) {
            mCoverLayout.setOnClickListener(mCoverListener);
        }
    }

    public void setPauseView(ImageButton pauseView) {
        mPauseView = pauseView;
        if (pauseView!=null) {
            mPauseView.setOnClickListener(mPauseListener);
        }
    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    public ImageButton getFullScreen() {
        return mFullScreen;
    }

    public int getCurrentPlayPosition() {
        Log.e(TAG,"canSetProgress===>"+canSetProgress());
        return mControl != null && canSetProgress() ? mControl.getCurrentPosition() : 0;
    }


    public void show(int timeout) {

        if (mMediaHandler==null) return;

        mControlView.setVisibility(View.VISIBLE);
        togglePauseStartView(View.VISIBLE,false);

        if (canSetProgress()) {
            setProgress();
        }

        mMediaHandler.sendEmptyMessage(MediaHandler.MSG_SHOW_PROGRESS);

        if (timeout != 0) {
            mMediaHandler.removeMessages(MediaHandler.MSG_FADE_OUT);
            mMediaHandler.sendEmptyMessageDelayed(MediaHandler.MSG_FADE_OUT, timeout);
        }
    }

    public void hide() {

        if ( mMediaHandler==null) return;

        mControlView.setVisibility(View.GONE);
        mPauseView.setVisibility(View.GONE);

        mMediaHandler.removeMessages(MediaHandler.MSG_SHOW_PROGRESS);
    }


    private boolean canSetProgress() {

        if (mControl==null) return false;

        final VideoView.State currState=mControl.getCurrentState();

        Log.e(TAG,"currState===>"+currState);

        return currState != VideoView.State.ERROR && currState != VideoView.State.IDLE &&
                currState != VideoView.State.INITIALIZING &&
                currState != VideoView.State.INITIALIZED && currState!=
                VideoView.State.PREPARING;
    }

    private boolean canSeek() {
        if (mControl==null) return false;
        final VideoView.State currState = mControl.getCurrentState();
        return currState != VideoView.State.ERROR && currState != VideoView.State.IDLE &&
                currState != VideoView.State.STOPPING && currState != VideoView.State.STOPPED &&
                currState != VideoView.State.INITIALIZING &&
                currState != VideoView.State.INITIALIZED && currState != VideoView.State.PREPARING;
    }

    private void setProgress() {

        if (mControl == null) return;

        final int duration = mControl.getDuration();
        final int curPos = mControl.getCurrentPosition();

        Log.i(TAG, "setProgress_curPos="+curPos+",duration="+duration);

        if (duration > 0) {
            if (mSb != null) {

                int progress = (int) (curPos / (float) duration * mSb.getMax());

                int secondaryProgress =
                        (int) (mSb.getMax() * (mControl.getBufferPercentage() / 100.f));

                //播放完成时MediaPlayer返回的 getCurrentPosition 值可能不是视频的结束位置，这里强制设置满进度状态
                if (mControl.getCurrentState() == VideoView.State.PLAYBACK_COMPLETED) {
                    progress = mSb.getMax();
                    secondaryProgress = mSb.getMax();
                }
                Log.i(TAG, "secondaryProgress="+secondaryProgress+",progress="+progress);
                //缓存进度
                mSb.setSecondaryProgress(secondaryProgress);
                //播放进度
                mSb.setProgress(progress);
            }
        }
        if (mCurTime != null) {
            String time = time2String(curPos);
            //播放完成时MediaPlayer返回的 getCurrentPosition 值可能不是视频的结束位置，这里强制设置满进度状态
            if (mControl.getCurrentState() == VideoView.State.PLAYBACK_COMPLETED) {
                time = time2String(duration);
            }
            mCurTime.setText(time);
        }

        if (mEndTime != null) {
            mEndTime.setText(time2String(duration));
        }
    }

    /**
     * 毫秒时间转视频界面显示时间格式
     * @param timMs 要转换的毫秒时间
     * @return 视频时间显示格式字符串
     */
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


    /**
     *  是否当前 VideoView上 显示媒体控制视图
     * @return
     */
    public boolean isShowing() {
        return mControlView != null && mControlView.isShown();
    }


    public void togglePlay() {

        if (mControl==null) return;

        boolean playNewVideo=false;

        if (mControl.isPlaying()) {

            Log.e(TAG,"togglePlay/pause");

            mControl.pause();

        } else {
            playNewVideo = playVideo(0);
            Log.e(TAG, playNewVideo ? "togglePlay/playNewVideo" : "togglePlay/start");
        }

        if (!playNewVideo) {
            togglePauseStartView(View.VISIBLE, false);
            mMediaHandler.sendEmptyMessage(MediaHandler.MSG_SHOW_PROGRESS);
            show(3000);
        }
    }


    private void togglePauseStartView(int visibility,boolean forcePlay) {

        if (mPauseView == null || mControl == null) {
            return;
        }

        mPauseView.setVisibility(visibility);

        if (mProgressBar.isShown()) {
            mPauseView.setVisibility(View.GONE);
        }

        if (mPauseView.isShown()) {
            mPauseView.setImageResource(
                    mControl.getCurrentState() == VideoView.State.STARTED && !forcePlay
                            ? R.mipmap.audio_pause : R.mipmap.audio_start);
        }
    }

    public interface Control {

        void setDataSource(String path);

        void prepareAsync();

        void start();

        void pause();

        void stop();

        void reset();

        void release();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int msec);

        boolean isPlaying();

        boolean isBuffering();

        int getBufferPercentage();

        VideoView.State getCurrentState();
    }

    private class MediaHandler extends Handler {

       public static final int MSG_SHOW_PROGRESS=0;

       public static final int MSG_FADE_OUT=1;

        public MediaHandler() {
            super(Looper.myLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_PROGRESS:
                    if (mControlView.isShown() && !mIsDragging && mControl != null && mControl
                            .getCurrentState()== VideoView.State.STARTED &&
                            canSetProgress())
                    {
                        LogUtil.LogE(TAG, "handleMessage_SHOW_PROGRESS");
                        setProgress();
                        sendEmptyMessageDelayed(MSG_SHOW_PROGRESS, 300);
                    }
                    break;
                case MSG_FADE_OUT:
                    hide();
                    break;
                default:
                    break;
            }
        }
    }

    public boolean mIsDragging=false;

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener=new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mIsDragging = fromUser;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mIsDragging = true;
            if (mMediaHandler != null) {
                mMediaHandler.removeMessages(MediaHandler.MSG_FADE_OUT);
                mMediaHandler.removeMessages(MediaHandler.MSG_SHOW_PROGRESS);
            }
            show(0);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            mIsDragging=false;

            if (canSeek() && canSetProgress()) {

                float percent = mSb.getProgress() / (float) mSb.getMax();

                Log.e(TAG,"seekToPercent==>"+percent);

                int seekToPos = (int) (mControl.getDuration() * percent);

                if (mProgressBar!=null && !mProgressBar.isShown()) {
                   mProgressBar.setVisibility(View.VISIBLE);
                }
                mControl.seekTo(seekToPos);
            }

            //必须最后 show
            show(3000);

            if (mProgressBar!=null && mProgressBar.isShown()) {
                //seek操作时 显示 progressbar 时 隐藏 播放暂停控件
                togglePauseStartView(View.GONE,false);
            }
        }
    };



    private View.OnClickListener mPauseListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPauseView!=null) {
                togglePlay();
            }
        }
    };

    private View.OnClickListener mCoverListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playVideo(0);
        }
    };


    private View.OnClickListener mFullScreenListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleScreen();
        }
    };

    /**
     * 视频横竖屏切换
     */
    private void toggleScreen() {
        Configuration cfg = mControlView.getContext().getResources().getConfiguration();
        final int currOrientation = cfg.orientation;
        if (currOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mBaseFragment!=null) {
                if (!mIsVideoList) {
                    Activity activity=mBaseFragment.getActivity();
                    activity.setRequestedOrientation(
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    if (activity instanceof AvVideoDetailActivity) {
                       AvVideoDetailActivity avVideoDetailActivity=
                               (AvVideoDetailActivity) activity;
                        avVideoDetailActivity.toggleSystemUi();
                    }
                    mFullScreen.setImageResource(R.mipmap.ic_quit_full_screen);

                } else {

                    Intent intent=new Intent(mBaseFragment.getActivity(), AvVideoLandActivity.class);
                    int pos = mHolder != null ? mHolder.getAdapterPosition() : 0;
                    intent.putExtra(Constant.KEY_POSITION, pos);
                    intent.putExtra(Constant.KEY_VIDEO_URL,mPath);
                    intent.putExtra(Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION,
                            getCurrentPlayPosition());
                    //停止当前播放的视频
                    MediaHelper.stopPlayingVideo(mHolder);

                    mBaseFragment.startActivityForResult(intent, AvVideoFragment.REQUEST_VIDEO_STATUS);
                }
            }

        } else if (currOrientation == Configuration.ORIENTATION_LANDSCAPE) {

            if (mBaseFragment == null) return;

            if (!mIsVideoList) {

                Activity activity = mBaseFragment.getActivity();

                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                if (activity instanceof AvVideoDetailActivity) {
                    AvVideoDetailActivity avVideoDetailActivity = (AvVideoDetailActivity) activity;
                    avVideoDetailActivity.toggleSystemUi();
                }

                mFullScreen.setImageResource(R.mipmap.ic_full_screen);

            } else {
                Intent result=new Intent();
                result.putExtra(Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION,getCurrentPlayPosition());
                int resumePlayPos =
                        mHolder.itemView.getTag() != null ? (int) mHolder.itemView.getTag() : 0;
                result.putExtra(Constant.KEY_POSITION, resumePlayPos);
                mBaseFragment.getActivity().setResult(Activity.RESULT_OK,result);
                mBaseFragment.getActivity().finish();
            }
        }
    }


    private boolean canPlayNewVideo() {
        if (mControl == null) return false;
        final VideoView.State currState = mControl.getCurrentState();
        return currState == VideoView.State.IDLE || currState == VideoView.State.ERROR ||
                currState== VideoView.State.END;
    }

    private boolean canStart() {
        if (mControl == null) return false;
        final VideoView.State currState = mControl.getCurrentState();
        return currState!= VideoView.State.ERROR && currState!=
                VideoView.State.IDLE &&
                currState!= VideoView.State.END && currState!=
                VideoView.State.STOPPED &&
                currState!= VideoView.State.STOPPING && currState!=
                VideoView.State.INITIALIZING &&
                currState!= VideoView.State.INITIALIZED && currState!=
                VideoView.State.PREPARING;
    }

    /**
     *  恢复播放或播放新的视频
     * @param preSeekToPosition 预跳到的视频的播放位置 ，数值大于 0 表明需要 SeekTo
     * @return 是否是播放新的视频
     */
    public boolean playVideo(int preSeekToPosition) {

        if (mControl == null) return false;

        Log.e(TAG,"playVideo");

        if (canStart()) {

            mCoverLayout.setVisibility(View.GONE);

            mControl.start();

            //直接快进
            if (preSeekToPosition>0) {
                mControl.seekTo(preSeekToPosition);
            }

            show(3000);

        } else if (canPlayNewVideo()) {

            playNewVideo(preSeekToPosition);

            return true;
        } else {
            OtherUtil.showToast(mControlView.getContext(),"很抱歉,视频无法播放");
        }

        return false;
    }


    /**
     *  播放新的视频 ，在播放之前会判断处理在数据流量情况下是否播放的问题
     * @param preSeekToPosition 预跳到的视频的播放位置 ，数值大于 0 表明需要 SeekTo
     */
    public void playNewVideo(int preSeekToPosition) {

        if (mControl==null) return;

        Log.e(TAG,"playNewVideo");

        if (mVideoPlayCallback != null) {
            if (!mVideoPlayCallback.playBefore(this)) {
                return;
            }
        }

        reallyPlayNewVideo(preSeekToPosition);
    }

    /**
     *  直接播放新的视频
     * @param preSeekToPosition 预跳到的视频的播放位置 ，数值大于 0 表明需要 SeekTo
     */
    public void reallyPlayNewVideo(int preSeekToPosition) {

        if (mControl==null) return;

        //停止旧的视频
        MediaHelper.stopPlayingVideo(mIsVideoList, mRecyclerView, mHolder);

        if (TextUtils.isEmpty(mPath)) {
            OtherUtil.showToast(mBaseFragment.getActivity(), "视频播放地址错误");
            return;
        }

        Log.e(TAG,"reallyPlayNewVideo");

        mCoverLayout.setVisibility(View.GONE);
        mPauseView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        //开始新的视频播放
        mControl.setDataSource(mPath);
        mControl.prepareAsync();
        mControl.start();

        //直接快进
        if (preSeekToPosition > 0) {
            Log.e(TAG,"reallyPlayNewVideo////preSeekToPosition"+preSeekToPosition);
            mControl.seekTo(preSeekToPosition);
        }

    }

    /**
     * 停止播放与当前 MediaController 关联的视频
     * 注意：之前创建的 MediaController 所传递的 Holder 应包含 VideoView 否则停止失效
     */
    public void stopPlayingVideo() {
        MediaHelper.stopPlayingVideo(mHolder);
    }

    /**
     * 重置播放界面，在停止播放视频后
     * */
    public void reset() {
        if (mMediaHandler != null) {
            mMediaHandler.removeMessages(MediaHandler.MSG_FADE_OUT);
        }
        mProgressBar.setVisibility(View.GONE);
        mCoverLayout.setVisibility(View.VISIBLE);
        togglePauseStartView(View.VISIBLE,true);
        mControlView.setVisibility(View.GONE);
    }

    private class VideoListener
            implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
            MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
            MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnInfoListener
    {

        @Override
        public void onPrepared(MediaPlayer mp) {
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.e(TAG,"onCompletion");
            reset();
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            Log.e(TAG,"onError");

            boolean handled=true;

            reset();

            OtherUtil.showToast(mControlView.getContext(),"视频播放失败");

            switch (what) {
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Log.e(TAG,"onError/what/MEDIA_ERROR_UNKNOWN");
                    break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    Log.e(TAG,"onError/what/MEDIA_ERROR_SERVER_DIED");
                    break;
                default:
                    break;
            }

            switch (extra) {
                case MediaPlayer.MEDIA_ERROR_IO:
                    Log.e(TAG,"onError/extra/MEDIA_ERROR_IO");
                    break;
                case MediaPlayer.MEDIA_ERROR_MALFORMED:
                    Log.e(TAG,"onError/extra/MEDIA_ERROR_MALFORMED");
                    break;
                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                    Log.e(TAG,"onError/extra/MEDIA_ERROR_UNSUPPORTED");
                    break;
                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                    Log.e(TAG,"onError/extra/MEDIA_ERROR_TIMED_OUT");
                    break;
                default:
                    break;
            }

            if (mControl!=null) {
                //TODO must setDataResource to reuse/retry and play video
                mControl.reset();
            }

            return handled;
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {

            Log.e(TAG,"onSeekComplete");

            //播放视频不管之前是否已暂停
            if (mControl != null && canStart()) {
                mControl.start();
            }

            if (mProgressBar != null && mProgressBar.isShown() && mControl != null &&
                    mControl.getCurrentState()== VideoView.State.STARTED &&
                    !mControl.isBuffering())
            {
                Log.e(TAG, "onSeekComplete==>" + "hide loading progress");
                mProgressBar.setVisibility(View.GONE);
            }

        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.v(TAG,"onBufferingUpdate=>"+percent);

            if((mPath.endsWith(".mp3")||mPath.endsWith(".MP3"))&&mp.isPlaying()) {
                if (mProgressBar != null && mProgressBar.isShown()) {
                    Log.i(TAG, "onBufferingUpdate==>" + "hide loading progress");
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.e(TAG,"onInfo");

            switch (what) {
                case MediaPlayer.MEDIA_INFO_UNKNOWN:
                    Log.e(TAG,"onInfo/what/MEDIA_INFO_UNKNOWN");
                    break;
                case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    Log.e(TAG,"onInfo/what/MEDIA_INFO_VIDEO_TRACK_LAGGING");
                    break;
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    Log.e(TAG,"onInfo/what/MEDIA_INFO_VIDEO_RENDERING_START");

                    //视频第一贞图像显示时隐藏 加载视图
                    if (mProgressBar!=null && mProgressBar.isShown()) mProgressBar.setVisibility
                            (View.GONE);

                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Log.e(TAG,"onInfo/what/MEDIA_INFO_BUFFERING_START");

                    //视频开始缓冲时
                    //1.显示加载进度视图，2.隐藏播放暂停视图
                    if (mProgressBar!=null && !mProgressBar.isShown()) mProgressBar.setVisibility
                            (View.VISIBLE);
                    togglePauseStartView(View.GONE,false);

                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    Log.e(TAG,"onInfo/what/MEDIA_INFO_BUFFERING_END");

                    //视频缓冲结束时
                    //隐藏加载进度视图
                    //这里不管 媒体控制视图是否可见，缓冲结束后只隐藏加载进度视图，不显示暂停播放视图
                    if (mProgressBar!=null && mProgressBar.isShown()) mProgressBar.setVisibility
                            (View.GONE);
//                    if (isShowing() && mProgressBar!=null && mProgressBar.isShown()) mProgressBar
//                            .

                    break;
                case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                    Log.e(TAG,"onInfo/what/MEDIA_INFO_BAD_INTERLEAVING");
                    break;
                case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    Log.e(TAG,"onInfo/what/MEDIA_INFO_NOT_SEEKABLE");
                    break;
                case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                    Log.e(TAG,"onInfo/what/MEDIA_INFO_METADATA_UPDATE");
                    break;
                case MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                    Log.e(TAG,"onInfo/what/MEDIA_INFO_UNSUPPORTED_SUBTITLE");
                    break;
                case MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                    Log.e(TAG,"onInfo/what/MEDIA_INFO_SUBTITLE_TIMED_OUT");
                    break;
                default:
                    break;
            }

//            switch (extra) {
//                case MediaPlayer.MEDIA_ERROR_IO:
//                    Log.e(TAG,"onCompletion/extra/MEDIA_ERROR_IO");
//                    break;
//                case MediaPlayer.MEDIA_ERROR_MALFORMED:
//                    Log.e(TAG,"onCompletion/extra/MEDIA_ERROR_MALFORMED");
//                    break;
//                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
//                    Log.e(TAG,"onCompletion/extra/MEDIA_ERROR_UNSUPPORTED");
//                    break;
//                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
//                    Log.e(TAG,"onCompletion/extra/MEDIA_ERROR_TIMED_OUT");
//                    break;
//                default:
//                    break;
//            }

            return true;
        }
    }


    public interface VideoPlayCallback{
        boolean playBefore(MediaController mediaController);
    }

}
