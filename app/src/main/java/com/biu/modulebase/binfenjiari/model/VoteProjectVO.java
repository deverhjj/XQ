package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {投票项}
 * @Description:{描述}
 * @date 2016/6/12
 */
public class VoteProjectVO extends BaseModel {
    /**投票项标题**/
    private String title;
    private String  small_title;
    /**投票项图片（type=2 带图片投票才有）**/
    private String pic;
    /**投票项获得的票数**/
    private int number;
    String create_number ;
    private int sex;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCreate_number() {
        return create_number;
    }

    public void setCreate_number(String create_number) {
        this.create_number = create_number;
    }
    //    private

    private boolean checked =false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSmall_title() {
        return small_title;
    }

    public void setSmall_title(String small_title) {
        this.small_title = small_title;
    }
}
