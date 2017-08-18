package com.biu.modulebase.binfenjiari.model;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{参赛人数  累计投票  参与人数}
 * @date ${2016/9/13}
 */
public class VoteArrVO extends BaseModel {

    /**
     * 参与人数
     */
    int votePeson;

    /**
     * 参赛人数
     */
    int joiners;

    /**
     * 累计投票
     */
    int number;

    public int getVotePeson() {
        return votePeson;
    }

    public void setVotePeson(int votePeson) {
        this.votePeson = votePeson;
    }

    public int getJoiners() {
        return joiners;
    }

    public void setJoiners(int joiners) {
        this.joiners = joiners;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
