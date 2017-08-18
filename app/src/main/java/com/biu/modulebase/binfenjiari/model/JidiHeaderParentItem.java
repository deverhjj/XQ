package com.biu.modulebase.binfenjiari.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/6/1.
 */
public class JidiHeaderParentItem extends BaseHeaderParentItem implements Serializable {
    private static final String TAG = "JidiHeaderParentItem";

    private String id;
    /**
     * 图片url
     */
    private String banner_pic;
    /**
     * 名字
     */
    private String name;
    /**
     * 基地地址
     */
    private String address;
    /**
     * 距离（单位KM）如果移动端传过来的是0，则返回0；否则后台根据两个经纬度计算距离
     */
    private double distance;
    /**
     *经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 数量
     */
    private String activity_number;
    /**
     * 备注
     */
    private String remark;
    /**
     * 标签(多个之间 , 隔开的)
     */
    private String label;

    /**
     * 图文对象列表
     */
    private List<ImageItem> picList;

    /**
     * 评价项列表
     */
    private List<EvaluateItem> collectList;

    /**
     * 综合评分（后台返回两位小数，移动端四舍五入处理下）
     */
    private String average_collect;

    /**
     * 点赞人数
     */
    private String like_number;
    /**
     * 本人是否点赞  1点赞 2未点赞
     */
    private String like_status;

    /**
     * 评论数量
     */
    private String comment_number;

    /**
     * 本人是否收藏  1收藏 2未收藏
     */
    private String collect_status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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

    public String getActivity_number() {
        return activity_number;
    }

    public void setActivity_number(String activity_number) {
        this.activity_number = activity_number;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ImageItem> getPicList() {
        return picList;
    }

    public void setPicList(List<ImageItem> picList) {
        this.picList = picList;
    }

    public String getAverage_collect() {
        return average_collect;
    }

    public void setAverage_collect(String average_collect) {
        this.average_collect = average_collect;
    }

    public List<EvaluateItem> getCollectList() {
        return collectList;
    }

    public void setCollectList(List<EvaluateItem> collectList) {
        this.collectList = collectList;
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

    @Override
    public List<?> getChildItemList(int parentPosition) {
        return null;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}
