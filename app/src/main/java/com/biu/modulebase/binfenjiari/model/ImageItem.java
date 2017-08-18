package com.biu.modulebase.binfenjiari.model;

/**
 * Created by jhj_Plus on 2016/3/21.
 */
public class ImageItem extends BaseModel {
    private static final String TAG = "ImageItem";

    /**
     * 类型 1文字 2图片 3图文混合
     */
    private int type;

    /**
     * 文字描述
     */
    private String description;

    /**
     * 图片 URL
     */
    private String img;

    public int getType() {
        return type;
    }

    public void setType(int type) {
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
