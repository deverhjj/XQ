package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/3/24.
 */
public class Comment {
    private static final String TAG = "Comment";
    /**
     * 主评论列表
     */
    private List<CommentItem> commentList;

    private long time;

    private int allPageNumber;


    public List<CommentItem> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentItem> commentList) {
        this.commentList = commentList;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getAllPageNumber() {
        return allPageNumber;
    }

    public void setAllPageNumber(int allPageNumber) {
        this.allPageNumber = allPageNumber;
    }
}
