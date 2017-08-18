package com.biu.modulebase.binfenjiari.model;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{投票项列表（排名前三个）,副投票}
 * @date ${2016/9/13}
 */
public class ShowListVO extends BaseModel {
    /**投票项标题(后台返回长标题)**/
    /**投票项图片（type=2带图片投票才有）**/
    String pic;
    /**投票项获得的票数**/
    String number;
    int create_number;
    String title;
    String small_title;
    int sex;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCreate_number() {
        return create_number;
    }

    public void setCreate_number(int create_number) {
        this.create_number = create_number;
    }
}
