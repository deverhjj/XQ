package com.biu.modulebase.binfenjiari.model;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{描述}
 * @date ${2016/9/13}
 */
public class InfoMVO extends BaseModel {
    int type;
    String title;
    int status;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
