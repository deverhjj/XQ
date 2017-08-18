package com.biu.modulebase.binfenjiari.model;

import com.biu.modulebase.binfenjiari.widget.video_player_manager.manager.VideoItem;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.manager.VideoPlayerManager;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.meta.MetaData;
import com.biu.modulebase.binfenjiari.widget.video_player_manager.ui.VideoPlayerView;

/**
 * Created by jhj_Plus on 2016/6/7.
 */
public class AvItem extends BaseModel implements VideoItem {
    private static final String TAG = "AvItem";

    /**
     * 视听类型 1视频 2图文 3语音
     */
    private int type;

    /**
     * 图片url
     */
    private String banner_pic;

    /**
     * 标题
     */
    private String name;

    /**
     * 视频、语音时长（type=1、3）
     */
    private String vedio_time;

    /**
     * 视频、语音url（type=1、3）
     */
    private String url;

    /**
     * 点赞人数
     */
    private String like_number;

    /**
     * 本人是否点赞  1点赞 2未点赞
     */
    private int like_status;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBanner_pic() {
        return banner_pic;
    }

    public void setBanner_pic(String banner_pic) {
        this.banner_pic = banner_pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVedio_time() {
        return vedio_time;
    }

    public void setVedio_time(String vedio_time) {
        this.vedio_time = vedio_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLike_number() {
        return like_number;
    }

    public void setLike_number(String like_number) {
        this.like_number = like_number;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
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
