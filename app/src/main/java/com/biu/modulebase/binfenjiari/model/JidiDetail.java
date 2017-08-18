package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/6/1.
 */
public class JidiDetail {
    private static final String TAG = "JidiDetail";
    /**
     * 主评论列表
     */
    private List<CardDetailParentItem> commentList;

    private long time;

    private int allPageNumber;


    public List<CardDetailParentItem> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CardDetailParentItem> commentList) {
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
