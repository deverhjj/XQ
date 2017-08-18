package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/6/1
 */
public class NewVoteBeanVO extends BaseModel {

    /**标题**/
    private String title;

    /**封面图片**/
    private String banner_pic;

    /**
     * 投票规则
     */
    private String rule;

    /**
     * 截止时间
     */
    private String end_time;

    /**
     * 是否结束 1未结束 2已结束
     */
    private int isopen;

    /**
     * 投票类型 1普通投票 2带图片投票
     */
    private int type;

    /**
     * 最小票数
     */
    private int min_number;

    /**
     * 最多票数
     */
    private int max_number;

    /**
     * 天数限制 1本次投票只能投一回  2每天都能投
     */
    private int day_type;

    /**
     * 投票项展示形式 1一行三个 2一行一个
     */
    private int show_type;

    /**
     * 是否主投票 1是  2不是
     */
    private int is_main;

    /**
     * 是否需要填写身份证信息 1是  2不是（移动端根据此标志位布局提交页面，是否需要填写身份证信息）
     */
    private int is_realname;

    public int getShow_type() {
        return show_type;
    }

    public void setShow_type(int show_type) {
        this.show_type = show_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner_pic() {
        return banner_pic;
    }

    public void setBanner_pic(String banner_pic) {
        this.banner_pic = banner_pic;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getIsopen() {
        return isopen;
    }

    public void setIsopen(int isopen) {
        this.isopen = isopen;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMin_number() {
        return min_number;
    }

    public void setMin_number(int min_number) {
        this.min_number = min_number;
    }

    public int getMax_number() {
        return max_number;
    }

    public void setMax_number(int max_number) {
        this.max_number = max_number;
    }

    public int getDay_type() {
        return day_type;
    }

    public void setDay_type(int day_type) {
        this.day_type = day_type;
    }

    public int getIs_main() {
        return is_main;
    }

    public void setIs_main(int is_main) {
        this.is_main = is_main;
    }

    public int getIs_realname() {
        return is_realname;
    }

    public void setIs_realname(int is_realname) {
        this.is_realname = is_realname;
    }
}
