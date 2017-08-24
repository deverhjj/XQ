package com.binfenjiari.model;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class UserInfo implements BaseModel {
    public static final String TAG = UserInfo.class.getSimpleName();
    /**
     * 用户id
     */
    public int id;
    /**
     *认证状态
     */
    public int auth_status;
    /**
     *积分
     */
    public String rank;
    /**
     *昵称
     */
    public String username;
    /**
     *学校
     */
    public String schoolName;
    /**
     *爱好
     */
    public String hobby;
    /**
     *媒体名称
     */
    public String media_name;
    /**
     *性别 1男 2女
     */
    public String gender;
    /**
     *年级（后台需要拼装好年级和班级
     */
    public String className;
    /**
     *头像
     */
    public String user_pic;
    /**
     *是否为小记者
     */
    public int is_reporter_auth;
}
