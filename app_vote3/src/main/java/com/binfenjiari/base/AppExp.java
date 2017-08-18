package com.binfenjiari.base;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/5/23
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AppExp extends RuntimeException {
    /**
     * 错误码
     */
    private int code;
    /**
     * 错误码对应的错误消息
     */
    private String msg;

    public AppExp() {
    }

    public AppExp(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }

}
