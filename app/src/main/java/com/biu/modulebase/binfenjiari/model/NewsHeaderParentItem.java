package com.biu.modulebase.binfenjiari.model;

import java.io.Serializable;
import java.util.List;

/**
 * 新鲜事详情bean
 */
public class NewsHeaderParentItem extends BaseHeaderParentItem implements Serializable {
    /***主键id*/
    private String id;
    /***发布人id*/
    private String user_id;
    /***发布人名字*/
    private String username;
    /***发布人头像*/
    private String user_pic;
    /***内容*/
    private String content;
    /***图片用，好隔开*/
    private String pic;
    /***发布时间*/
    private String create_time;
    /***点赞人数*/
    private String like_number;
    /***本人是否点赞  1点赞 2未点赞*/
    private String like_status;
    /***评论数量*/
    private String comment_number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    @Override
    public List<?> getChildItemList(int parentPosition) {
        return null;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}
