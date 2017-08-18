package com.biu.modulebase.binfenjiari.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.manager.VideoItem;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.manager.VideoPlayerManager;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.meta.MetaData;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.utils.Logger;
import com.biu.modulebase.binfenjiari.widget.visibility_utils.items.ListItem;
import com.biu.modulebase.common.adapter.BaseViewHolder;

/**
 * Created by jhj_Plus on 2016/4/26.
 */
public abstract class BaseVideoItem implements VideoItem, ListItem {
    private static final String TAG = "BaseVideoItem";

    private VideoPlayerManager<MetaData> mVideoPlayerManager;

    private Rect mVisibleRect=new Rect();


    public BaseVideoItem(VideoPlayerManager<MetaData> videoPlayerManager) {
        mVideoPlayerManager = videoPlayerManager;
    }


    public abstract void update(int position,RecyclerView.ViewHolder viewHolder,
            VideoPlayerManager<MetaData> videoPlayerManager);

    @Override
    public int getVisibilityPercents(View view) {

        int percent=100;

        final int height=view.getMeasuredHeight();

        view.getLocalVisibleRect(mVisibleRect);


        Logger.v(TAG, "getVisibilityPercents mCurrentViewRect top " + mVisibleRect.top + ", " +
                "left " + mVisibleRect.left + ", bottom " + mVisibleRect.bottom + ", right " + mVisibleRect.right);

        if (mVisibleRect.top > 0) {
            percent = (height - mVisibleRect.top) * 100 / height;
        } else if (mVisibleRect.bottom > 0 && mVisibleRect.bottom < height) {
            percent = mVisibleRect.bottom * 100 / height;
        }

        return percent;
    }


    @Override
    public void setActive(View newActiveView, int newActiveViewPosition) {
        LogUtil.LogE(TAG,"setActive");
        BaseViewHolder baseViewHolder = (BaseViewHolder) newActiveView.getTag();
//        playNewVideo(new CurrentItemMetaData(newActiveViewPosition, newActiveView),
//                (VideoPlayerView) baseViewHolder.getView(R.id.video_player_view),
//                mVideoPlayerManager);
    }

    @Override
    public void deactivate(View currentView, int position) {
        LogUtil.LogE(TAG,"deactivate");
        stopPlayback(mVideoPlayerManager);
    }

}
