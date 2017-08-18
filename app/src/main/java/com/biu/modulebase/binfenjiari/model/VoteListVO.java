package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * Created by hasee on 2016/11/3.
 */

public class VoteListVO  extends BaseModel{
    /***1普通投票 2带图片的投票*/
    String type;
    /**封面图片**/
    String banner_pic;
    /**标题**/
    String title;
    /**总投票项**/
    String all_number;
    /**截止时间**/
    String end_time;
    /**总票数**/
    String all_poll;
    /**投票项列表（列表页三个）**/
    List<ProjectVO> project;
    /**是否结束 1未结束 2已结束 注:移动端已结束的显示标志但是仍然能点进去**/
    String isopen;
    /**是否显示 1显示 2不显示**/
    String status;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBanner_pic() {
        return banner_pic;
    }

    public void setBanner_pic(String banner_pic) {
        this.banner_pic = banner_pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAll_number() {
        return all_number;
    }

    public void setAll_number(String all_number) {
        this.all_number = all_number;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAll_poll() {
        return all_poll;
    }

    public void setAll_poll(String all_poll) {
        this.all_poll = all_poll;
    }

    public List<ProjectVO> getProject() {
        return project;
    }

    public void setProject(List<ProjectVO> project) {
        this.project = project;
    }

    public String getIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
