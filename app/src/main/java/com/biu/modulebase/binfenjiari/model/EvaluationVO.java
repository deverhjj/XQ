package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {评价bean}
 * @Description:{描述}
 * @date 2016/6/1
 */
public class EvaluationVO extends  BaseModel {

    /**描述（有趣程度）**/
    private String description;
    /**分数**/
    private String evaluate_number;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvaluate_number() {
        return evaluate_number;
    }

    public void setEvaluate_number(String evaluate_number) {
        this.evaluate_number = evaluate_number;
    }
}
