package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/8/8
 */
public class CircleDetailVO extends BaseModel {
    /**图片路径**/
    private String intro_img;
    /**加入的人数**/
    private String user_n;
    /**简介内容**/
    private String intro_content;
    /**帖子数量**/
    private String post_n;
    /**圈子名称**/
    private String name;
    /**圈子用户关系：1-普通会员，2-管理员 3创建者 4黑名单 10：已登录 11：未登录**/
    private String type;
    /**圈子背景图片**/
    private String back_img;

    private List<AnnounceVO> announce_list;

    public String getIntro_img() {
        return intro_img;
    }

    public void setIntro_img(String intro_img) {
        this.intro_img = intro_img;
    }

    public String getUser_n() {
        return user_n;
    }

    public void setUser_n(String user_n) {
        this.user_n = user_n;
    }

    public String getIntro_content() {
        return intro_content;
    }

    public void setIntro_content(String intro_content) {
        this.intro_content = intro_content;
    }

    public String getPost_n() {
        return post_n;
    }

    public void setPost_n(String post_n) {
        this.post_n = post_n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AnnounceVO> getAnnounce_list() {
        return announce_list;
    }

    public void setAnnounce_list(List<AnnounceVO> announce_list) {
        this.announce_list = announce_list;
    }

    public String getBack_img() {
        return back_img;
    }

    public void setBack_img(String back_img) {
        this.back_img = back_img;
    }
}
