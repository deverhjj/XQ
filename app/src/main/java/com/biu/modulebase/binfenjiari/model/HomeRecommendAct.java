package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/19
 */
public class HomeRecommendAct extends BaseModel {

    private List<EventVO> datas;

    public List<EventVO> getDatas() {
        return datas;
    }

    public void setDatas(List<EventVO> datas) {
        this.datas = datas;
    }
}
