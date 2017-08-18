package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * @author Lee
 * @Title: {投票}
 * @Description:{描述}
 * @date 2016/6/12
 */
public class VoteVO extends BaseModel {
    /**1:普通投票 2：带图片的投票**/
    private String type;
    /**发布人id**/
    private String user_id;
    /**发布人昵称**/
    private String username;
    /**发布人头像**/
    private String user_pic;
    /**发布时间**/
    private String create_time;
    /**标题**/
    private String title;
    /**总投票项**/
    private String all_number;
    /**截止时间**/
    private String end_time;
    /**总票数**/
    private int all_poll;
    /**是否结束 1：未结束 2：已结束**/
    private String isopen;
    //---------------------投票列表项特有属性---------------------
    /**投票项列表**/
    private List<VoteProjectVO> project;
    //---------------------投票详情特有属性---------------------
    /**一次可投票数 注：移动端用于区分单选还是多选；>1 多选  ==1 单选**/
    private String check_number;
    /**是否可以投票 1 可以 2：不可以**/
    private String isVote;
    /**剩余可投票数 0就是没有了 1就是还能投一票，其他的依次 注：单选的话就直接返回能否实际可投票数，投过就是0，未投过就是1；如果是多选的话，后台需要他已经投的票数和可以投的票数进行计算返回该字段**/
    private int surplus_number;
    /**天数限制 1本次投票只能投一回  2每天都能投**/
    private int day_type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    public int getAll_poll() {
        return all_poll;
    }

    public void setAll_poll(int all_poll) {
        this.all_poll = all_poll;
    }

    public List<VoteProjectVO> getProject() {
        return project;
    }

    public void setProject(List<VoteProjectVO> project) {
        this.project = project;
    }

    public String getIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }


    public String getIsVote() {
        return isVote;
    }

    public void setIsVote(String isVote) {
        this.isVote = isVote;
    }

    public String getCheck_number() {
        return check_number;
    }

    public void setCheck_number(String check_number) {
        this.check_number = check_number;
    }

    public int getSurplus_number() {
        return surplus_number;
    }

    public void setSurplus_number(int surplus_number) {
        this.surplus_number = surplus_number;
    }

    public int getDay_type() {
        return day_type;
    }

    public void setDay_type(int day_type) {
        this.day_type = day_type;
    }
}
