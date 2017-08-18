package com.biu.modulebase.binfenjiari.adapter;

import android.content.res.AssetFileDescriptor;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.manager.VideoPlayerManager;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.meta.MetaData;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.ui.VideoPlayerView;
import com.biu.modulebase.common.adapter.BaseViewHolder;

/**
 * Created by jhj_Plus on 2016/4/26.
 */
public class AssetsVideoItem  extends BaseVideoItem {
    private static final String TAG = "AssetsVideoItem";

    private AssetFileDescriptor mAssetFileDescriptor;

    //本地测试 Cover
    private int mImageRes;

    public AssetsVideoItem(VideoPlayerManager<MetaData> videoPlayerManager,
            AssetFileDescriptor assetFileDescriptor,int imageRes)
    {
        super(videoPlayerManager);
        mAssetFileDescriptor = assetFileDescriptor;
        mImageRes=imageRes;
    }

    public AssetFileDescriptor getAssetFileDescriptor() {
        return mAssetFileDescriptor;
    }

    @Override
    public void update(int position, RecyclerView.ViewHolder viewHolder,
            VideoPlayerManager<MetaData> videoPlayerManager)
    {
        BaseViewHolder holder = (BaseViewHolder) viewHolder;
        holder.getView(R.id.iv_cover).setVisibility(View.VISIBLE);
        holder.setImageResource(R.id.iv_cover, mImageRes);
    }

    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerView player,
            VideoPlayerManager<MetaData> videoPlayerManager)
    {

        LogUtil.LogE(TAG, "playNewVideo===>"+currentItemMetaData);
        videoPlayerManager.playNewVideo(currentItemMetaData, player, mAssetFileDescriptor);
    }

    @Override
    public void stopPlayback(VideoPlayerManager videoPlayerManager) {
        LogUtil.LogE(TAG,"stopPlayback");
        videoPlayerManager.stopAnyPlayback();
    }
}
