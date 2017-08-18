package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/6/17
 */
public class MyEventVO extends BaseModel {
    /**图片url**/
    private String banner_pic;
    /**名字**/
    private String name;
    /**开始时间**/
    private String open_time;
    /**结束时间**/
    private String end_time;
    /**需要支付金额（跳转支付页面时候直接带过去）（type=1）**/
    private double pay_money;
    /**是否结束 1未结束 2已结束**/
    private String isopen;
    /**是否评价 1已评价 2未评价（type=2）**/
    private String isComment;
    /**预约状态 1预约中 4预约失效(type=1)**/
    private String appointment_ok;
    /**订单id**/
    private String order_id;

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

    public double getPay_money() {
        return pay_money;
    }

    public void setPay_money(double pay_money) {
        this.pay_money = pay_money;
    }

    public String getIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }

    public String getIsComment() {
        return isComment;
    }

    public void setIsComment(String isComment) {
        this.isComment = isComment;
    }

    public String getAppointment_ok() {
        return appointment_ok;
    }

    public void setAppointment_ok(String appointment_ok) {
        this.appointment_ok = appointment_ok;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
