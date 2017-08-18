package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/6/1
 */
public class PicTextVO extends BaseModel {

    /**类型 1文字 2图片 3图文混合**/
    private String type;
    /** 文字描述**/
    private String description;
    /**url+宽高比用，隔开**/
    private String img;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
