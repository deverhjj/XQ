package com.binfenjiari.base;

import android.content.Context;
import android.text.TextUtils;

import com.binfenjiari.model.UserInfo;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Prefs;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.github.huajianjiang.expandablerecyclerview.util.Preconditions;


/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/4/23
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AppManager {
    private static AppManager INSTANCE;
    private XqService mApi;
    // 用户信息相关
    private  UserInfo mUserInfo;
    private String mToken;

    private AppManager() {
    }

    public static AppManager get() {
        synchronized (AppManager.class) {
            if (INSTANCE == null) {
                INSTANCE = new AppManager();
            }
            return INSTANCE;
        }
    }

    public XqService getService() {
        if (mApi == null) {
            mApi = NetManager.get().createService(XqService.class);
        }
        return mApi;
    }

    /**
     * 是否登录
     ** @return
     */
    public boolean hasLogin() {
        return getUserInfo() != null;
    }

    public String getToken() {
        if (TextUtils.isEmpty(mToken)) {
            mToken = Prefs.get(MyApplication.getInstance()).pullString(Constants.KEY_TOKEN);
        }
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
        Prefs.get(MyApplication.getInstance()).pushString(Constants.KEY_TOKEN, mToken).done();
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        Prefs.get(MyApplication.getInstance())
             .pushString(Constants.KEY_USERINFO, Gsons.get().toJson(userInfo))
             .done();
    }

    /**
     * 获取用户信息
     ** @return
     */
    public UserInfo getUserInfo() {
        if (mUserInfo == null) {
            String json = Prefs.get(MyApplication.getInstance()).pullString(Constants.KEY_USERINFO);
            if (!TextUtils.isEmpty(json)) {
                mUserInfo = Gsons.get().fromJson(json, UserInfo.class);
            }
        }
        return mUserInfo;
    }

}
