package com.biu.modulebase.binfenjiari.communication;

import org.json.JSONObject;

/**
 * Created by jhj_Plus on 2016/6/1.
 */
public interface RequestCallback3 {
    void requestBefore();
    void onResponse(JSONObject response);
    void onErrorResponse(String errorInfo);
    void onLogout();
}
