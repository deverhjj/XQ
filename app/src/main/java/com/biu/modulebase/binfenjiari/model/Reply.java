package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/6/4.
 */
public class Reply extends BaseModel {
    private static final String TAG = "Reply";

    private List<ReplyDetailItem> list;

    private long time;

    private int allPageNumber;

    public List<ReplyDetailItem> getList() {
        return list;
    }

    public void setList(List<ReplyDetailItem> list) {
        this.list = list;
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
