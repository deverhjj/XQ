package com.biu.modulebase.binfenjiari.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lee
 * @Title: {圈子首页}
 * @Description:{描述}
 * @date 2016/8/8 */
public class CircleIndexVO extends BaseModel{

    /**推荐圈子**/
    private List<CircleVO> circleList = new ArrayList<>();
    /**我的圈子**/
    private List<CircleVO> myList= new ArrayList<>();

    public List<CircleVO> getCircleList() {
        return circleList;
    }

    public void setCircleList(List<CircleVO> circleList) {
        this.circleList = circleList;
    }

    public List<CircleVO> getMyList() {
        return myList;
    }

    public void setMyList(List<CircleVO> myList) {
        this.myList = myList;
    }
}
