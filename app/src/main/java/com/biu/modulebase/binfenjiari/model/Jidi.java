package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/5/31.
 */
public class Jidi {
    private static final String TAG = "Jidi";
    /**
     * 基地列表
     */
    private List<JidiItem> baseList;

    private long time;

    private int allPageNumber;

    public List<JidiItem> getBaseList() {
        return baseList;
    }

    public void setBaseList(List<JidiItem> baseList) {
        this.baseList = baseList;
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
