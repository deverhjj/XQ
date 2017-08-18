package com.biu.modulebase.binfenjiari.model;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{按添加时间降序显示前3)}
 * @date ${2016/9/13}
 */
public class InfoVO extends BaseModel {
    /**资讯类型 1公告 2活动介绍**/
    int type;
    /**标题**/
    String title;

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
}
