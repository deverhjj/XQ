package com.biu.modulebase.binfenjiari.model;

/**
 * Created by jhj_Plus on 2016/3/21.
 */
public class ReplyItem extends BaseModel {
    private static final String TAG = "ReplyItem";

    private String comment_id;

    /**
     * 回复人id
     */
    private String user_id;
    /**
     * 被回复对象姓名
     */
    private String to_name;
    /**
     * 被回复对象id（根据此字段判断是否需要显示回复某某）
     */
    private String to_user_id;
    /**
     * 回复内容
     */
    private String content;
    /**
     * 回复人姓名
     */
    private String username;
    /**
     * 回复时间
     */
    private long create_time;

    private String to_id;
    /**
     * 是否显示 1显示 2不显示
     */
    private int status;
    /**
     * 评论人头像
     */
    private String user_pic;

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;

    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ReplyItem{" +
                "user_id=" + user_id +
                ", to_name='" + to_name + '\'' +
                ", to_user_id=" + to_user_id +
                ", comment='" + content + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
