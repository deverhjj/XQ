package com.binfenjiari.widget.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import java.io.IOException;

/**
 * Created by jhj_Plus on 2016/5/16.
 */
public class VideoView extends TextureView implements MediaController.Control ,View
        .OnClickListener {
    private static final String TAG = "VideoView";

    private MediaPlayer mMediaPlayer;

    private MediaController mMediaController;

    private MediaPlayer.OnPreparedListener mPreparedListener;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private MediaPlayer.OnErrorListener mErrorListener;
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private MediaPlayer.OnSeekCompleteListener mSeekCompleteListener;
    private MediaPlayer.OnInfoListener mInfoListener;

    private Matrix mTransform = new Matrix();
    private ScaleType mScaleType = ScaleType.CENTER_INSIDE;

    private int mVideoWidth;
    private int mVideoHeight;

    private int mVideoViewWidth;
    private int mVideoViewHeight;

    /**
     * mCurrentState 是当前视频播放的状态
     * mTargetState 是用户期望要到达的视频状态，有些时候因为视频 url 或 SurfaceText unAvailable 时，
     * 在 setupVideo 方法中需要自动恢复之前的准备和播放视频的操作
     */
    private State mCurrentState;
    private State mTargetState;

    private int mBufferState;

    private Uri mUri;

    private int mCurrentBufferPercentage;

    private int mPreSeekToPosition=0;

    public enum State {
        ERROR,
        IDLE,
        INITIALIZING,
        INITIALIZED,
        PREPARING,
        PREPARED,
        STARTING,
        STARTED,
        PAUSING,
        PAUSED,
        STOPPING,
        STOPPED,
        PLAYBACK_COMPLETED,
        END
    }

    public enum ScaleType {
        /**
         * 不做任何视频缩放处理，只是将视频放置在 VideoView 的中间播放
         */
        CENTER,
        /**
         * 按照视频的宽高比例对视频进行缩放，缩放后的视频的宽高小于或者等于 VideoView 的 宽高
         */
        CENTER_INSIDE,
        /**
         * 按照视频的宽高比例对视频进行缩放，缩放后的视频的宽高大于或者等于 VideoView 的 宽高
         */
        CENTER_CROP,
        /**
         * 如果视频的宽或者高大于 VideoView 的宽或者高，则至少缩放视频的一个坐标(x或者y)以 fit VideoView 对应的坐标
         */
        FIT_CENTER
    }

    public VideoView(Context context) {
        super(context);
        initVideoView();
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initVideoView();
    }

    private void initVideoView() {
        setSurfaceTextureListener(new SurfaceTextureListener());
        mCurrentState= State.IDLE;
        mTargetState= State.IDLE;
        mBufferState=MediaPlayer.MEDIA_INFO_BUFFERING_END;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mVideoViewWidth=getMeasuredWidth();
        mVideoViewHeight=getMeasuredHeight();

        Log.e(TAG,"onMeasure");
        updateSurfaceTextSize();
    }

    /**
     * 更改 SurfaceTexture 图片缓存 buffer 的大小
     * 1.如果视频 宽 或/和 高 大于 VideoView 的 宽 或/和 高 ，那么 buffer 宽或/和高 等于 ViewView 的宽或/和高
     * 2.视频 宽或/和高 小于  VideoView 的 宽 或/和 高 ，那么对 buffer 进行缩放到 视频的 宽或/和高比例和大小
     */
    private void updateSurfaceTextSize() {

        if (mVideoWidth==0 || mVideoHeight==0 || mVideoViewWidth==0||mVideoViewHeight==0)
            return;

        Log.e(TAG,"updateSurfaceTextSize/////mVideoWidth==>"+mVideoWidth+"," +
                "mVideoHeight="+mVideoHeight+",mVideoViewWidth="+mVideoViewWidth+"," +
                "mVideoViewHeight="+mVideoViewHeight);

        mTransform.reset();

        final float px = mVideoViewWidth / 2.0f;
        final float py = mVideoViewHeight / 2.0f;

        float sx = 1.0f;
        float sy = 1.0f;

        switch (mScaleType) {
            case CENTER:

                sx = mVideoWidth / (float) mVideoViewWidth;
                sy = mVideoHeight / (float) mVideoViewHeight;

                break;
            case CENTER_CROP:
                if (mVideoViewWidth > mVideoWidth && mVideoViewHeight > mVideoHeight) {
                    //视频宽高都小于VideoView的宽高
                    sx = mVideoWidth / (float) mVideoViewWidth;
                    sy = mVideoHeight / (float) mVideoViewHeight;
                    if (mVideoViewWidth>mVideoViewHeight) {
                        sx = 1.0f;
                        sy = mVideoHeight / (float) mVideoWidth * mVideoViewWidth /
                                (float) mVideoViewHeight;

                    } else {
                        sy=1.0f;
                        sx= mVideoWidth / (float) mVideoHeight * mVideoViewHeight /
                                (float) mVideoViewWidth;
                    }
                } else if (mVideoViewWidth < mVideoWidth && mVideoViewHeight < mVideoHeight) {
                    //视频宽高都大于VideoView的宽高

                    //视频的宽度大于视频的高度
                    if (mVideoWidth > mVideoHeight) {
                        sx = 1.0f;
                        sy = mVideoHeight / (float) mVideoWidth * mVideoViewWidth /
                                (float) mVideoViewHeight;
                    } else {
                        //视频的宽度小于视频的高度
                        sy=1.0f;
                        sx = mVideoWidth / (float) mVideoHeight * mVideoViewHeight /
                                (float) mVideoViewWidth;
                    }


                } else if (mVideoViewHeight < mVideoHeight) {
                    //视频的宽度小于VideoView的宽度但视频的高度大于VideoView的高度
                    sy = 1.0f;
                    sx = mVideoWidth / (float) mVideoHeight * mVideoViewHeight /
                            (float) mVideoViewWidth;

                } else {
                    //视频的宽度大于VideoView的宽度但视频的高度小于VideoView的高度
                    sx = 1.0f;
                    sy = mVideoHeight / (float) mVideoWidth * mVideoViewWidth /
                            (float) mVideoViewHeight;
                }
                break;
            case CENTER_INSIDE:
                if (mVideoViewWidth > mVideoWidth && mVideoViewHeight > mVideoHeight) {
                   //视频宽高都小于VideoView的宽高
                    sx = mVideoWidth / (float) mVideoViewWidth;
                    sy = mVideoHeight / (float) mVideoViewHeight;
                } else if (mVideoViewWidth < mVideoWidth && mVideoViewHeight < mVideoHeight) {
                    //视频宽高都大于VideoView的宽高

                    //视频的宽度大于视频的高度
                    if (mVideoWidth > mVideoHeight) {
                        sx = 1.0f;
                        sy = mVideoHeight / (float) mVideoWidth * mVideoViewWidth /
                                (float) mVideoViewHeight;
                        //如果缩放后的视频高度还是大于 VideoView 的高度，二次缩放视频的高度至 VideoView 的高度
                        //视频宽度等比例缩放
                        if (sy>1.0f) {
                            sx = sx - (sy - 1.0f);
                            sy = 1.0f;
                        }
                    } else {
                        //视频的宽度小于视频的高度
                        sy=1.0f;
                        sx = mVideoWidth / (float) mVideoHeight * mVideoViewHeight /
                                (float) mVideoViewWidth;
                        //如果缩放后的视频宽度还是大于 VideoView 的宽度，二次缩放视频的宽度至 VideoView 的宽度
                        //视频高度等比例缩放
                        if (sx > 1.0f) {
                            sy = sy - (sx - 1.0f);
                            sx = 1.0f;
                        }
                    }

                } else if (mVideoViewHeight < mVideoHeight) {
                    //视频的宽度小于VideoView的宽度但视频的高度大于VideoView的高度
                    sy = 1.0f;
                    sx = mVideoWidth / (float) mVideoHeight * mVideoViewHeight /
                            (float) mVideoViewWidth;

                } else {
                    //视频的宽度大于VideoView的宽度但视频的高度小于VideoView的高度
                    sx = 1.0f;
                    sy = mVideoHeight / (float) mVideoWidth * mVideoViewWidth /
                            (float) mVideoViewHeight;
                }
                break;
            case FIT_CENTER:

                //横屏
                if (mVideoViewWidth > mVideoViewHeight) {
                    //视频的高度放大到 VideoView 的高度，视频的宽度等比例放大
                    sy = 1.0f;
                    sx = mVideoWidth / (float) mVideoHeight * mVideoViewHeight /
                            (float) mVideoViewWidth;
                } else {
                    sx=1.0f;
                    sy = mVideoHeight / (float) mVideoWidth * mVideoViewWidth /
                            (float) mVideoViewHeight;
                }

                break;
            default:
                throw new RuntimeException("must set a Video ScaleType");
        }


        Log.e(TAG, "sx===>" + sx + ",sy====>" + sy+",px===>"+px+",py=====>"+py);

        mTransform.setScale(sx, sy, px, py);
        setTransform(mTransform);

    }


    /**
     * 设置 视频 的缩放类型
     * @param scaleType
     */
    public void setScaleType(ScaleType scaleType) {
        if (scaleType == mScaleType) {
            return;
        }
        mScaleType=scaleType;
        requestLayout();
    }


    private void setUpVideo() {

        release();

        if (mUri == null || !isAvailable()) return ;

        try {

            Log.e(TAG,"setUpVideo");

            MediaListener mediaListener=new MediaListener();
            mMediaPlayer=new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(mediaListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mediaListener);
            mMediaPlayer.setOnSeekCompleteListener(mediaListener);
            mMediaPlayer.setOnBufferingUpdateListener(mediaListener);
            mMediaPlayer.setOnCompletionListener(mediaListener);
            mMediaPlayer.setOnErrorListener(mediaListener);
            mMediaPlayer.setOnInfoListener(mediaListener);


            mCurrentState= State.INITIALIZING;
            mMediaPlayer.setDataSource(getContext(),mUri,null);
            mCurrentState= State.INITIALIZED;
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            final SurfaceTexture surfaceTexture=getSurfaceTexture();
            Surface surface=new Surface(surfaceTexture);
            mMediaPlayer.setSurface(surface);


            //如果之前由于没有成功准备或播放视频时，那么直接准备并在视频准备好后播放
            if (mTargetState == State.PREPARING || mTargetState == State.STARTING ) {
                Log.e(TAG,"setUpVideo//PREPARING");
                mCurrentState= State.PREPARING;
                mMediaPlayer.prepareAsync();
                mTargetState= State.STARTING;
            }

            Log.e(TAG,"setUpVideo///Success");


        } catch (IOException e) {
            Log.e(TAG,"IOException=>"+e.getMessage());
            mCurrentState= State.ERROR;
            release();
            if (mErrorListener != null) {
                mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0xfffffc14);
            }
            e.printStackTrace();

        } catch (Surface.OutOfResourcesException e) {
            Log.e(TAG,"OutOfResourcesException=>"+e.getMessage());
            mCurrentState= State.ERROR;
            release();
            if (mErrorListener != null) {
                mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0xfffffc14);
            }
            e.printStackTrace();

        } catch (IllegalStateException e) {
            Log.e(TAG,"IllegalStateException=>"+e.getMessage());
            release();
            mCurrentState= State.ERROR;
            if (mErrorListener != null) {
                mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0xfffffc14);
            }

        }
    }

    public void setMediaController(MediaController mediaController){
        mMediaController = mediaController;
        setOnClickListener(this);
    }

    public MediaController getMediaController() {
        return mMediaController;
    }

    public void setVideoPath(String path) {
        Log.e(TAG,"setVideoPath===》"+path);
        final State currentState=mCurrentState;
        switch (currentState) {
            case ERROR:
            case INITIALIZING:
            case INITIALIZED:
            case PREPARING:
            case PREPARED:
            case STARTING:
            case STARTED:
            case PAUSING:
            case PAUSED:
            case STOPPING:
            case STOPPED:
            case PLAYBACK_COMPLETED:
//                mCurrentState=State.ERROR;
                throw new IllegalStateException("setVideoPath, called from illegal state "+ currentState);
        }

        mUri = Uri.parse(path);

        Log.e(TAG,"mUri==null>>?"+(mUri==null));

         setUpVideo();

        requestLayout();

    }


    @Override
    public void setDataSource(String path) {
         setVideoPath(path);
    }

    @Override
    public void prepareAsync() {

        Log.e(TAG,"prepareAsync() before");

        if (mMediaPlayer == null) {
            mTargetState = State.PREPARING;
            return;
        }

        final State currentState=mCurrentState;
        switch (currentState) {
            case ERROR:
            case IDLE:
            case INITIALIZING:
            case PREPARING:
            case PREPARED:
            case STARTING:
            case STARTED:
            case PAUSING:
            case PAUSED:
            case STOPPING:
            case PLAYBACK_COMPLETED:
            case END:
                throw new IllegalStateException("prepareAsync, called from illegal state "+ currentState);
        }

        Log.e(TAG,"prepareAsync()");

        mMediaPlayer.prepareAsync();
        mCurrentState= State.PREPARING;
    }

    @Override
    public void start() {

        Log.e(TAG,"start() before");

        if (mMediaPlayer == null) {
            mTargetState = State.STARTING;
            return;
        }

        final State currentState=mCurrentState;
        switch (currentState) {
            case ERROR:
            case IDLE:
            case INITIALIZING:
            case INITIALIZED:
            case STOPPING:
            case STOPPED:
            case END:
                mCurrentState= State.ERROR;
                mTargetState= State.STARTING;
                throw new IllegalStateException("start, called from illegal state "+ currentState);

            case PREPARING:
                Log.e(TAG,"start, but is preparing, waiting");
                mTargetState= State.STARTING;
                return;
        }

        Log.e(TAG,"start()");

        mCurrentState= State.STARTING;
        mMediaPlayer.start();
        mCurrentState= State.STARTED;
    }

    @Override
    public void pause() {

        if (mMediaPlayer==null) return;

        final State currentState=mCurrentState;
        switch (currentState) {
            case ERROR:
            case IDLE:
            case INITIALIZING:
            case INITIALIZED:
            case PREPARING:
            case PREPARED:
            case STOPPING:
            case STOPPED:
            case END:
                mCurrentState= State.ERROR;
                throw new IllegalStateException("pause, called from illegal state "+ currentState);
        }
        mCurrentState= State.PAUSING;
        mMediaPlayer.pause();
        mCurrentState= State.PAUSED;
    }

    @Override
    public void stop() {
        if (mMediaPlayer==null) return;

        final State currentState=mCurrentState;
        switch (currentState) {
            case ERROR:
            case IDLE:
            case INITIALIZING:
            case INITIALIZED:
            case END:
                mCurrentState= State.ERROR;
                throw new IllegalStateException("stop, called from illegal state "+ currentState);
        }
        mCurrentState= State.STOPPING;
        mMediaPlayer.stop();
        mCurrentState= State.STOPPED;
    }

    @Override
    public void reset() {
        if (mMediaPlayer==null) return;
        mMediaPlayer.reset();
        mUri=null;
        mCurrentBufferPercentage=0;
        mPreSeekToPosition=0;
        mCurrentState= State.IDLE;
        mTargetState= State.IDLE;
    }

    @Override
    public void release() {

        if (mMediaPlayer==null) return;

        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
//        mUri=null;
        mCurrentBufferPercentage=0;
        mPreSeekToPosition=0;
        mCurrentState = State.END;
        mTargetState= State.END;
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer==null) return 0;
        final State currentState=mCurrentState;
        switch (currentState) {
            case ERROR:
            case IDLE:
            case INITIALIZING:
            case INITIALIZED:
            case PREPARING:
            case END:
                mCurrentState= State.ERROR;
                throw new IllegalStateException("getDuration, called from illegal state "+ currentState);
        }
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer==null) return 0;
        final State currentState=mCurrentState;
        switch (currentState) {
            case ERROR:
            case END:
            case PREPARING:
                mCurrentState= State.ERROR;
                throw new IllegalStateException("getCurrentPosition, called from illegal state "+ currentState);
        }
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int msec) {

        Log.e(TAG,"seekTo before");

        if (mMediaPlayer == null) {
            mPreSeekToPosition = msec;
            return;
        }

        final State currentState=mCurrentState;

        switch (currentState) {
            case ERROR:
            case IDLE:
            case INITIALIZING:
            case INITIALIZED:
            case STOPPING:
            case STOPPED:
            case END:
                mCurrentState= State.ERROR;
                throw new IllegalStateException("seekTo, called from illegal state "+ currentState);
            case PREPARING:
                //视频正在加载，保存待 seekTo 的 position，当视频 prepared 后再 seekTo
                Log.e(TAG,"seekTo, but is preparing, waiting");
                mPreSeekToPosition=msec;
                return;
        }

        mMediaPlayer.seekTo(msec);

        Log.e(TAG,"seekTo after");
    }

    @Override
    public boolean isPlaying() {
        if (mMediaPlayer==null) return false;
        final State currentState=mCurrentState;
        switch (currentState) {
            case ERROR:
            case END:
                mCurrentState= State.ERROR;
                throw new IllegalStateException("isPlaying, called from illegal state "+ currentState);
        }
        return mMediaPlayer.isPlaying();
    }

    @Override
    public boolean isBuffering() {
        return mMediaPlayer != null && mBufferState == MediaPlayer.MEDIA_INFO_BUFFERING_START;
    }

    @Override
    public int getBufferPercentage() {
        return mCurrentBufferPercentage;
    }

    @Override
    public State getCurrentState() {
        return mCurrentState;
    }

    public void stopPlayBack() {
        if (mMediaPlayer==null) return;
        mMediaPlayer.stop();
        mMediaPlayer.release();
      //  eraseSurfaceTexture();
        mMediaPlayer=null;
        mUri=null;
        mCurrentBufferPercentage=0;
        mPreSeekToPosition=0;
        mCurrentState= State.IDLE;
        mTargetState= State.IDLE;
        mBufferState=MediaPlayer.MEDIA_INFO_BUFFERING_END;
    }


    private void eraseSurfaceTexture() {
        Canvas canvas = lockCanvas();
        canvas.drawColor(0x000000);
        unlockCanvasAndPost(canvas);
    }

    @Override
    public void onClick(View v) {
        toggleMediaControllerVisibility();
    }

    private void toggleMediaControllerVisibility() {
        if (mMediaController==null) return;

        if (mMediaController.isShowing()) {
            mMediaController.hide();
        } else {
            mMediaController.show(3000);
        }
    }

    private class SurfaceTextureListener implements TextureView.SurfaceTextureListener {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.e(TAG,"onSurfaceTextureAvailable=>"+"size=>"+"width="+width+",height="+height);

            //如果目标播放为这些状态就开始初始化并播放视频
            if (mTargetState == State.PREPARING || mTargetState == State.STARTING) {
                setUpVideo();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.e(TAG,"onSurfaceTextureSizeChanged"+"size=>"+"width="+width+",height="+height);
            updateSurfaceTextSize();
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.e(TAG,"onSurfaceTextureDestroyed");
            surface.release();
            stopPlayBack();
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            Log.v(TAG,"onSurfaceTextureUpdated");
        }
    }


    public void setOnPreparedListener(MediaPlayer.OnPreparedListener preparedListener) {
        mPreparedListener = preparedListener;
    }

    public void setOnBufferingUpdateListener(
            MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener)
    {
        mBufferingUpdateListener = bufferingUpdateListener;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener completionListener)
    {
        mCompletionListener = completionListener;
    }

    public void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener seekCompleteListener)
    {
        mSeekCompleteListener = seekCompleteListener;
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener errorListener) {
        mErrorListener = errorListener;
    }

    public void setOnInfoListener(MediaPlayer.OnInfoListener infoListener) {
        mInfoListener = infoListener;
    }

    private class MediaListener
            implements MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnPreparedListener,
            MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener,
            MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
            MediaPlayer.OnInfoListener
    {

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.i(TAG,"onBufferingUpdate=>"+"percent=>"+percent);

            mCurrentBufferPercentage=percent;

            if (percent == 100) {
                mBufferState = MediaPlayer.MEDIA_INFO_BUFFERING_END;
            }

            if (mBufferingUpdateListener!=null) {
               mBufferingUpdateListener.onBufferingUpdate(mp,percent);
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            mCurrentState= State.PLAYBACK_COMPLETED;
            if (mCompletionListener!=null) {
                mCompletionListener.onCompletion(mp);
            }
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            mCurrentState = State.ERROR;
            boolean handled = false;
            if (mErrorListener != null) {
                handled = mErrorListener.onError(mp, what, extra);
            }
            return handled;
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {

            boolean handled = false;

            switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    mBufferState = MediaPlayer.MEDIA_INFO_BUFFERING_START;
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    mBufferState = MediaPlayer.MEDIA_INFO_BUFFERING_END;
                    break;
            }

            if (mInfoListener != null) {
                handled = mInfoListener.onInfo(mp, what, extra);
            }

            return handled;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {

            Log.e(TAG,"onPrepared=>"+"size=>"+"width="+mp.getVideoWidth()+",height="+mp.getVideoHeight());

            mCurrentState= State.PREPARED;

            if (mTargetState == State.STARTING) {
                Log.e(TAG,"onPrepared=>"+"call start() because of mTargetState==State.STARTING");
                start();
            }

            if (mPreSeekToPosition != 0) {
                seekTo(mPreSeekToPosition);
                mPreSeekToPosition = 0;
            }

            if (mPreparedListener!=null) {
               mPreparedListener.onPrepared(mp);
            }


        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {
            if (mSeekCompleteListener!=null) {
               mSeekCompleteListener.onSeekComplete(mp);
            }
        }

        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

            Log.e(TAG,"onVideoSizeChanged=>"+"size=>"+"width="+width+",height="+height);

            mVideoWidth = width;
            mVideoHeight = height;

            requestLayout();

        }
    }

    private SurfaceTexture.OnFrameAvailableListener mFrameAvailableListener=new SurfaceTexture.OnFrameAvailableListener() {

        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {

        }
    };

}
