package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {新鲜事bean}
 * @Description:{描述}
 * @date 2016/6/1
 */
public class NewsVO extends BaseModel {
    /**发布人id**/
    private String user_id;
    /**发布人名字**/
    private String username;
    /**发布人头像**/
    private String user_pic;
    /**内容**/
    private String content;
    /**图片(用，好隔开)**/
    private String pic;
    /**发布时间**/
    private String create_time;
    /**是否显示 1显示 2不显示**/
    private String is_display;


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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIs_display() {
        return is_display;
    }

    public void setIs_display(String is_display) {
        this.is_display = is_display;
    }
}
