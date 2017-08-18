package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/8/12
 */
public class NotificationVO extends BaseModel {

    private List<MessageTypeVO> list;

    public List<MessageTypeVO> getList() {
        return list;
    }

    public void setList(List<MessageTypeVO> list) {
        this.list = list;
    }
}
