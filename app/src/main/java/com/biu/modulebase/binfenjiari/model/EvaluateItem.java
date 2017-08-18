package com.biu.modulebase.binfenjiari.model;

/**
 * Created by jhj_Plus on 2016/6/1.
 */
public class EvaluateItem extends BaseModel {
    private static final String TAG = "EvaluateItem";

    private String description;

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
