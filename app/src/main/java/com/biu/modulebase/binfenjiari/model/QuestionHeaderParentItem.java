package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {问答详情bean}
 * @Description:{描述}
 * @date 2016/6/4
 */
public class QuestionHeaderParentItem extends NewsHeaderParentItem {
    /**标题**/
    private String title;

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
}
