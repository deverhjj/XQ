package com.binfenjiari.base;

/**
 * 服务器返回的数据在本地所解析代表的一个通用数据模型类.
 * <p>
 * <b>此类的定义需要与服务器返回的数据结构达成契约关系
 * </p>
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/2/24
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AppEcho<T> extends Echo<T> {
    public static final int ERROR_UNKNOWN = -400;

    public boolean isOk() {
        return code() == 1;
    }

    public boolean isTokenExpired() {
        return code() == 1024;
    }

}
