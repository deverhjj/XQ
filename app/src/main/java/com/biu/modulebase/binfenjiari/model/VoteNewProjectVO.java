package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {投票项}
 * @Description:{描述}
 * @date 2016/6/12
 */
public class VoteNewProjectVO extends BaseModel {
    private String create_number;
    private String title;
    private String small_title;
    private String pic;
    private int number;
    private int sex;
    private int is_able;

    public int getIs_able() {
        return is_able;
    }

    public void setIs_able(int is_able) {
        this.is_able = is_able;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    private boolean checked =false;

    public String getCreate_number() {
        return create_number;
    }

    public void setCreate_number(String create_number) {
        this.create_number = create_number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSmall_title() {
        return small_title;
    }

    public void setSmall_title(String small_title) {
        this.small_title = small_title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
