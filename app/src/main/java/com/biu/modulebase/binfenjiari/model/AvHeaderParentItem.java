package com.biu.modulebase.binfenjiari.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/6/12.
 */
public class AvHeaderParentItem extends BaseHeaderParentItem implements Serializable{
    private static final String TAG = "AvHeaderParentItem";

    /**
     * 视听类型 1视频 2图文 3语音
     */
    private int type;

    /**
     * 视频、语音时长（type=1、3）
     */
    private String vedio_time;

    /**
     * 视频、语音url（type=1、3）
     */
    private String url;

    /**
     * 图片url
     */
    private String banner_pic;
    /**
     * 标题
     */
    private String name;

    /**
     * 内容
     */
    private String content;

    /**
     * 图文对象列表
     */
    private List<ImageItem> picList;

    /**
     * 点赞人数
     */
    private String like_number;
    /**
     * 本人是否点赞  1点赞 2未点赞
     */
    private String like_status;

    /**
     * 评论数量
     */
    private String comment_number;

    /**
     * 本人是否收藏  1收藏 2未收藏
     */
    private String collect_status;

    /**
     * 发布时间
     */
    private long create_time;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ImageItem> getPicList() {
        return picList;
    }

    public void setPicList(List<ImageItem> picList) {
        this.picList = picList;
    }

    public String getLike_number() {
        return like_number;
    }

    public void setLike_number(String like_number) {
        this.like_number = like_number;
    }

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    public String getComment_number() {
        return comment_number;
    }

    public void setComment_number(String comment_number) {
        this.comment_number = comment_number;
    }

    public String getCollect_status() {
        return collect_status;
    }

    public void setCollect_status(String collect_status) {
        this.collect_status = collect_status;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    @Override
    public List<?> getChildItemList(int parentPosition) {
        return null;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
