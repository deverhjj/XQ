package com.biu.modulebase.binfenjiari.model;


import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model.ParentListItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/3/21.
 */
public class CardDetailParentItem extends ParentListItem implements Serializable {
    private static final String TAG = "CardDetailParentItem";

    private boolean card;

    private boolean hasChildComment;

    private boolean hasMoreChildComment;

    /**
     * 评论编号
     */
    private int id;
    /**
     * 评论人id
     */
    private int user_id;
    /**
     * 评论人头像
     */
    private String user_pic;
    /**
     * 评论人姓名
     */
    private String username;
    /**
     * 评论时间
     */
    private long create_time;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 子评论列表
     */
    private List<ReplyItem> comment_list;

    /**
     * 查看更多  1有更多  2没有（最多显示五条，五条以内包括五条则为2，五条以上则是1）
     */
    private int is_more;


    public boolean isCard() {
        return card;
    }

    public void setCard(boolean card) {
        this.card = card;
    }

    public boolean isHasChildComment() {
        return hasChildComment;
    }

    public void setHasChildComment(boolean hasChildComment) {
        this.hasChildComment = hasChildComment;
    }

    public boolean isHasMoreChildComment() {
        return hasMoreChildComment;
    }

    public void setHasMoreChildComment(boolean hasMoreChildComment) {
        this.hasMoreChildComment = hasMoreChildComment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ReplyItem> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<ReplyItem> comment_list) {
        this.comment_list = comment_list;
    }

    public int getIs_more() {
        return is_more;
    }

    public void setIs_more(int is_more) {
        this.is_more = is_more;
    }

    @Override
    public String toString() {
        return "CommentItem{" +
                "user_id=" + user_id +
                ", user_head='" + user_pic + '\'' +
                ", username='" + username + '\'' +
                ", create_time=" + create_time +
                ", content='" + content + '\'' +
                ", comment_list=" + comment_list +
                '}';
    }


    @Override
    public List<ReplyItem> getChildItemList(int parentPosition) {
        return  comment_list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }


}
