package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/8/10
 */
public class CircleMemeberVO extends BaseModel {
    /**用户名**/
    private String username;
    /**头像路径**/
    private String user_pic;

    public String getUser_name() {
        return username;
    }

    public void setUser_name(String user_name) {
        this.username = user_name;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    /**1-普通会员，2-管理员 3创建者**/
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
