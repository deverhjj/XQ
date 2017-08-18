package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {问答详情bean}
 * @Description:{描述}
 * @date 2016/6/4
 */
public class PostHeaderParentItem extends NewsHeaderParentItem {
    /**标题**/
    private String title;
    /**1-推荐,2-不推荐**/
    private String is_commend;
    /**1-精华,2-不精华**/
    private String is_essence;
    /**是否管理员 1是 2不是**/
    private String is_admin;
    /**圈子id**/
    private String circle_id;
    /**圈子name**/
    private String circle_name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String admin_name;

    private String admin_id;

    private String admin_pic;

    private String admin_content;

    private String admin_time;

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_pic() {
        return admin_pic;
    }

    public void setAdmin_pic(String admin_pic) {
        this.admin_pic = admin_pic;
    }

    public String getAdmin_content() {
        return admin_content;
    }

    public void setAdmin_content(String admin_content) {
        this.admin_content = admin_content;
    }

    public String getAdmin_time() {
        return admin_time;
    }

    public void setAdmin_time(String admin_time) {
        this.admin_time = admin_time;
    }

    public String getCircle_name() {
        return circle_name;
    }

    public void setCircle_name(String circle_name) {
        this.circle_name = circle_name;
    }

    public String getIs_commend() {
        return is_commend;
    }

    public void setIs_commend(String is_commend) {
        this.is_commend = is_commend;
    }

    public String getIs_essence() {
        return is_essence;
    }

    public void setIs_essence(String is_essence) {
        this.is_essence = is_essence;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    public String getCircle_id() {
        return circle_id;
    }

    public void setCircle_id(String circle_id) {
        this.circle_id = circle_id;
    }
}
