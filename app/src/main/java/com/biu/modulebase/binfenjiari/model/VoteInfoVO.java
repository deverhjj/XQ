package com.biu.modulebase.binfenjiari.model;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{title : "测试活动规则"}
 * @date ${2016/9/13}
 */
public class VoteInfoVO extends BaseModel {

    /**
     *  : "测试活动规则"
     */
    private String title;

    private int status;

    private String delete_time;

    /**
     * : 1490687522
     */
    private String create_time ;

    private int type;

    private String vote_id;

    private String href;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVote_id() {
        return vote_id;
    }

    public void setVote_id(String vote_id) {
        this.vote_id = vote_id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
