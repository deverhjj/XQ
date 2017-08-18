package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/8/8
 */
public class CircleVO extends BaseModel {
    /**图片路径**/
    private String intro_img;
    /**加入的人数**/
    private String user_n;
    /**简介内容**/
    private String intro_content;
    /**帖子数量**/
    private String post_n;
    /**圈子名称**/
    private String name;
    /**一级大类id**/
    private String type_id;

    /**圈子背景图片**/
    private String back_img;

    private List<CircleMemeberVO> user_list;

    //我的圈子
    /**是否有更多 1是 2否     没啥用处。。。。。。。。**/
    private int is_more;
    /**1-普通会员，2-管理员 3创建者 4黑名单 10：已登录 11：未登录**/
    private String type="11";

    /**
     * isEdit 1可修改（不能进入详情） ；2可修改（能进入详情） 3不能修改（不能进入详情） 4不能修改（可以进入详情）
     *圈子表中一、申请中，返回3；
     *        二、审核失败，返回1；
     *        三、申请成功，查询圈子再次申请表中最近的一条可用的记录 ①申请中，返回4；②审核成功，返回2；③申请失败，返回2；④没有记录，返回2
     *
     *        isEdit 1：不能进入圈子 ，提示"审核失败,不能进入圈子,请重新提交审核.."  ;圈子管理显示“创建圈子审核失败，请重新提交审核”；
     *        isEdit 3：不能进入圈子,提示“创建圈子审核中,请耐心等待...”;圈子管理显示"创建圈子审核中,自提交日起三天内完成..."
     *        isEdit 4：可进入圈子；圈子管理显示"信息修改审核中，自提交日起三天内完成..."，不能编辑数据；
     *        isEdit 2：圈子管理显示"信息一经修改，三天内进行审核"，可修改提交审核 ；可进入圈子
     **/
    private String isEdit;

    public String getIntro_img() {
        return intro_img;
    }

    public void setIntro_img(String intro_img) {
        this.intro_img = intro_img;
    }

    public String getUser_n() {
        return user_n;
    }

    public void setUser_n(String user_n) {
        this.user_n = user_n;
    }

    public String getIntro_content() {
        return intro_content;
    }

    public void setIntro_content(String intro_content) {
        this.intro_content = intro_content;
    }

    public String getPost_n() {
        return post_n;
    }

    public void setPost_n(String post_n) {
        this.post_n = post_n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_more() {
        return is_more;
    }

    public void setIs_more(int is_more) {
        this.is_more = is_more;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBack_img() {
        return back_img;
    }

    public void setBack_img(String back_img) {
        this.back_img = back_img;
    }

    public List<CircleMemeberVO> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<CircleMemeberVO> user_list) {
        this.user_list = user_list;
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
}
