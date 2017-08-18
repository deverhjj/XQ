package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {帖子}
 * @Description:{描述}
 * @date 2016/8/8
 */
public class CardVO extends  BaseModel{
    /**标题**/
    private String title ;
    /**发布人id**/
    private String user_id;
    /**发布人名字**/
    private String username;
    /**发布人头像**/
    private String user_pic;
    /**内容**/
    private String content;
    /**图片用，好隔开**/
    private String pic;
    /**1-推荐,2-不推荐**/
    private String is_commend;
    /**1-精华,2-不精华**/
    private String is_essence;
    /**发布时间**/
    private String create_time;
    /**1显示 2不显示**/
    private String status;
    /**1是 2不是**/
    private String is_admin;
    /**圈子id**/
    private String circle_id;
    /**圈子名称**/
    private String circle_name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCircle_name() {
        return circle_name;
    }

    public void setCircle_name(String circle_name) {
        this.circle_name = circle_name;
    }
}
