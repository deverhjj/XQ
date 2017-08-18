package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/6/13
 */
public class HomeHotVO extends BaseModel {

    /**类型：1：活动 2：普通投票 3：带图投票 4：视频、语音视听 5：图文视听**/
    private String type;
    /**图片url（type=1、4、5）**/
    private String banner_pic;
    /**名字、标题**/
    private String name;
    /**活动地址（type=1）**/
    private String address;
    /**开始时间（type=1）**/
    private String open_time;
    /**结束时间（type=1、2、3）**/
    private String end_time;
    /**基地名字（type=1）**/
    private String base_name;
    /**基地经度（type=1）**/
    private String longitude;
    /**基地纬度（type=1）**/
    private String latitude;
    /**报名人数（type=1）**/
    private String apply_number;
    /**限制人数（type=1）**/
    private String limit_number;
    /**点赞人数（type=1、4、5）**/
    private String like_number;
    /**本人是否点赞 （type=1、4、5） 1点赞 2未点赞**/
    private String like_status;
    /**总的投票项（type=2、3）**/
    private String all_number;
    /**投票项list（type=2、3）**/
    private List<VoteProjectVO> projectList;
    /**总票数（type=2、3）**/
    private String all_poll;
    /**活动、投票是否结束（type=1、2、3） 1未结束  2已结束**/
    private String isopen;
    /**视频、语音时长（type=4）**/
    private String vedio_time;

    private String vedio_url;

    public String getVedio_url() {
        return vedio_url;
    }

    public void setVedio_url(String vedio_url) {
        this.vedio_url = vedio_url;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getBase_name() {
        return base_name;
    }

    public void setBase_name(String base_name) {
        this.base_name = base_name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getApply_number() {
        return apply_number;
    }

    public void setApply_number(String apply_number) {
        this.apply_number = apply_number;
    }

    public String getLimit_number() {
        return limit_number;
    }

    public void setLimit_number(String limit_number) {
        this.limit_number = limit_number;
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

    public String getAll_number() {
        return all_number;
    }

    public void setAll_number(String all_number) {
        this.all_number = all_number;
    }

    public List<VoteProjectVO> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<VoteProjectVO> projectList) {
        this.projectList = projectList;
    }

    public String getAll_poll() {
        return all_poll;
    }

    public void setAll_poll(String all_poll) {
        this.all_poll = all_poll;
    }

    public String getIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }

    public String getVedio_time() {
        return vedio_time;
    }

    public void setVedio_time(String vedio_time) {
        this.vedio_time = vedio_time;
    }
}
