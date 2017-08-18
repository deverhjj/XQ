package com.binfenjiari.base;


import com.binfenjiari.utils.Logger;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;


/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/6
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class Expaser {
    private static final String TAG = Expaser.class.getSimpleName();

    public static AppExp parse(Throwable e) {
        Logger.e(TAG, "Expaser>>>" + e);
        if (e instanceof AppExp) return (AppExp) e;
        int code = AppEcho.ERROR_UNKNOWN;
        String msg = e.getMessage();
        if (e instanceof UnknownHostException) {
            msg = "服务器异常,请尝试重新请求";
        } else if (e instanceof SocketTimeoutException) {
            msg = "网络连接超时,请检查网络设置";
        } else if (e instanceof SocketException) {
            msg = "网络连接异常,请检查网络设置";
        } else if (e instanceof HttpException) {
            HttpException httpExp = (HttpException) e;
            code = httpExp.code();
            msg = httpExp.message();
        }
        return new AppExp(code, msg);
    }

}
