package com.biu.modulebase.binfenjiari.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/6/1.
 */
public class EventHeaderParentItem extends BaseHeaderParentItem implements Serializable {
    private static final String TAG = "JidiHeaderParentItem";

    private String id;
    /**
     * 图片url
     */
    private String banner_pic;
    /**
     * 活动名字
     */
    private String name;

    /**
     * 基地名称
     */
    private String base_name;

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
     *开始时间
     */
    private String open_time;
    /**
     * 结束时间
     */
    private String end_time;


    /**
     *报名人数
     */
    private String apply_number;
    /**
     * 限制人数 （返回null则是不限制人数）
     */
    private String limit_number;

    /**
     * 金额（0元移动端显示免费；其他就显示具体的价格）
     */
    private double money;

    /**儿童票金额**/
    private double child_money;
    /**团队票价**/
    private double team_money;
    /**团购要求最低人数**/
    private int people_numer;

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

    /**
     * 是否结束 1未结束 2已结束（点击报名如果结束则提示活动结束不跳转页面）
     */
    private int isopen;
    /**本人是否预约 1已预约  2未预约**/
    private String appointment_status;

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

    public String getBase_name() {
        return base_name;
    }

    public void setBase_name(String base_name) {
        this.base_name = base_name;
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

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getChild_money() {
        return child_money;
    }

    public void setChild_money(double child_money) {
        this.child_money = child_money;
    }

    public double getTeam_money() {
        return team_money;
    }

    public void setTeam_money(double team_money) {
        this.team_money = team_money;
    }

    public int getPeople_numer() {
        return people_numer;
    }

    public void setPeople_numer(int people_numer) {
        this.people_numer = people_numer;
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

    public List<EvaluateItem> getCollectList() {
        return collectList;
    }

    public void setCollectList(List<EvaluateItem> collectList) {
        this.collectList = collectList;
    }

    public String getAverage_collect() {
        return average_collect;
    }

    public void setAverage_collect(String average_collect) {
        this.average_collect = average_collect;
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

    public int getIsopen() {
        return isopen;
    }

    public void setIsopen(int isopen) {
        this.isopen = isopen;
    }

    @Override
    public List<?> getChildItemList(int parentPosition) {
        return null;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }

    public String getAppointment_status() {
        return appointment_status;
    }

    public void setAppointment_status(String appointment_status) {
        this.appointment_status = appointment_status;
    }
}
