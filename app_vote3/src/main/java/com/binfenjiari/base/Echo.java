package com.binfenjiari.base;

import com.google.gson.annotations.SerializedName;

/**
 * 服务器返回的数据在本地所解析代表的一个通用数据模型类.
 * <p>
 *     <b>此类的定义需要与服务器返回的数据结构达成契约关系
 * </p>
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/2/24
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class Echo<T> {
    /**
     * 状态码，和 HTTP 响应码一致
     */
    private int statCode;
    /**
     * 状态码或者响应的文本信息的简要描述
     */
    private String stat;
    /**
     * 服务器定义的各种状态码，需要达成契约
     */
    @SerializedName(value = "status", alternate = {"key"})
    private int code;
    /**
     * 服务器定义的响应消息文本描述信息
     */
    @SerializedName(value = "info", alternate = {"msg", "message"})
    private String msg;
    /**
     * 服务器返回的真正所使用的数据所解析代表的本地 java 实体类类型
     */
    @SerializedName(value = "result", alternate = {"jsonObj", "data"})
    private T data;

    /**
     * 返回服务器响应的 HTTP 状态码
     * @return HTTP 状态码
     */
    public int statCode() {
        return statCode;
    }

    /**
     * 返回服务器响应的 HTTP 状态码对应的简要文本描述信息
     * @return HTTP 状态码对应的简要文本描述信息
     */
    public String stat() {
        return stat;
    }

    /**
     * 返回服务器自定义的响应状态码
     * @return 服务器自定义的响应状态码
     */
    public int code() {
        return code;
    }

    /**
     * 返回服务器自定义的响应状态码对应的简要文本描述信息
     * @return 服务器自定义的响应状态码对应的简要文本描述信息
     */
    public String msg() {
        return msg;
    }

    /**
     * 返回服务器返回的数据解析后所代表的 java 数据实体对象
     * @return 数据实体对象
     */
    public T data() {
        return data;
    }

}
