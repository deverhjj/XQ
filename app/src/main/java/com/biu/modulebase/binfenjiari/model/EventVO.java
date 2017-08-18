package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * @author Lee
 * @Title: {活动bean}
 * @Description:{描述}
 * @date 2016/5/31
 */
public class EventVO extends  BaseModel {
    /**图片url**/
    private String banner_pic;
    /**名字**/
    private String name;
    /**活动地址**/
    private String address;
    /**开始时间**/
    private String open_time;
    /**结束时间**/
    private String end_time;
    /**基地名字**/
    private String base_name;
    /**基地经度**/
    private String longitude;
    /**基地纬度**/
    private String latitude;
    /**报名人数**/
    private String apply_number;
    /**限制人数  返回null则是不限制人数**/
    private String limit_number;
    /**点赞人数**/
    private String like_number;
    /** 本人是否点赞  1点赞 2未点赞**/
    private String like_status;
    /**是否结束 1未结束 2已结束**/
    private String isopen;
    /**金额
     * （活动金额是null则移动端显示见详情，因为活动存在（个人1.4米以下儿童免票入园；
     * 团体中、小学生60元/人；团体大、中专院校学生80元/人）的情况；
     * 0元移动端显示免费；其他就显示具体的价格）
     * **/
    private String money;
    /** 备注**/
    private String remark;
    /** 综合评价**/
    private String average_collect;
    /** 评价项列表**/
    private List<EvaluationVO> collectList;
    /** 评论数量**/
    private String comment_number;
    /** 本人是否收藏   1收藏 2未收藏**/
    private String collect_status;

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

    public String getIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAverage_collect() {
        return average_collect;
    }

    public void setAverage_collect(String average_collect) {
        this.average_collect = average_collect;
    }

    public List<EvaluationVO> getCollectList() {
        return collectList;
    }

    public void setCollectList(List<EvaluationVO> collectList) {
        this.collectList = collectList;
    }

    public String getComment_number() {
        return comment_number;
    }

    public void setComment_number(String comment_number) {
        this.comment_number = comment_number;
    }

    public String getCollect_status() {
        return collect_status;
    }

    public void setCollect_status(String collect_status) {
        this.collect_status = collect_status;
    }
}
