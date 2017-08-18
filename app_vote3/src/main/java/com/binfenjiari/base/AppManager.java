package com.binfenjiari.base;

import android.content.Context;
import android.text.TextUtils;

import com.binfenjiari.model.UserInfo;
import com.binfenjiari.utils.Prefs;


/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/4/23
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AppManager {
    private static AppManager INSTANCE;
    private XqService mApi;
    private static UserInfo USERINFO;

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
     *
     * @param context
     * @return
     */
    public static boolean hasLogin(Context context) {
        return getUserInfo(context) != null;
    }

    /**
     * 获取用户信息
     *
     * @param context
     * @return
     */
    public static UserInfo getUserInfo(Context context) {
        if (USERINFO == null) {
            String json = Prefs.get(context).pullString("UserInfo");
            if (!TextUtils.isEmpty(json)) {
                USERINFO = Gsons.get().fromJson(json, UserInfo.class);
            }
        }
        return USERINFO;
    }

}
