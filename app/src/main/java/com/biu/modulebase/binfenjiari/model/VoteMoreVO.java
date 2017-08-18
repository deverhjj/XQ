package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{副投票列表（is_main=1时有，根据副投票的关联顺序来升序排列）}
 * @date ${2016/9/13}
 */
public class VoteMoreVO extends BaseModel {
    /**1普通投票 2带图片的投票**/
    String type;
    /**标题**/
    String title;

    /**总票数**/
    String all_poll;
    /**是否更多 1是 2没有  注：移动端根据该字段来判断是否显示更多；
     * 后台只需要将总投票项all_number和3进行比较来决定该值，all_number>3,
     * is_more=1,all_number<=3,is_more=2**/
    String is_more;
    /**投票项列表（排名前三个）**/
    List<ShowListVO>projectList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAll_poll() {
        return all_poll;
    }

    public void setAll_poll(String all_poll) {
        this.all_poll = all_poll;
    }

    public String getIs_more() {
        return is_more;
    }

    public void setIs_more(String is_more) {
        this.is_more = is_more;
    }

    public List<ShowListVO> getList() {
        return projectList;
    }

    public void setList(List<ShowListVO> list) {
        this.projectList = list;
    }
}
