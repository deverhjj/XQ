package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/8/12
 */
public class MessageTypeVO extends BaseModel {
    /**数量**/
    private String number;
    /**消息类型 1系统推送 2圈子推送 3帖子推送**/
    private String message_type;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }
}
