package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/6/7.
 */
public class Av {
    private static final String TAG = "Av";

    /**
     * 视频列表
     */
    private List<AvItem> vedioList;

    private long time;

    private int allPageNumber;

    public List<AvItem> getVedioList() {
        return vedioList;
    }

    public void setVedioList(List<AvItem> vedioList) {
        this.vedioList = vedioList;
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
