package com.biu.modulebase.binfenjiari.eventbus;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{投票下一步-同步更新-主投票-副投票}
 * @date 2016/10/9
 */
public class VoteNextEvent {

    public VoteNextEvent() {
    }

    public VoteNextEvent(String ids, int position) {
        this.ids = ids;
        this.position = position;
    }

    /**
     * id （选中的投票项用,隔开；投票id和选中的投票项目用@隔开；主副投票用#隔开；实例：1@1,2,3,4,5#2@3,4,5,6,7）
     */
    public String ids;

    public int position;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
