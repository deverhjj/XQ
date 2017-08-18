package com.biu.modulebase.binfenjiari.communication;

import org.json.JSONObject;

/**
 * Created by jhj_Plus on 2016/6/1.
 */
public interface RequestCallBack2 {
    int KEY_FAIL=-100;
    void requestBefore();
    void onSuccess(String mainJsonString,JSONObject mainJsonObject,JSONObject rootJsonObject);
    void onFail(int key,String message);
    void requestAfter();
}
