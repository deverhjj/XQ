package com.biu.modulebase.binfenjiari.model;

/**
 * Created by jhj_Plus on 2016/5/31.
 */
public class JidiItem extends BaseModel {

    private static final String TAG = "JidiItem";
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
     * 点赞人数
     */
    private String like_number;
    /**
     * 本人是否点赞  1点赞 2未点赞
     */
    private String like_status;

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
}
