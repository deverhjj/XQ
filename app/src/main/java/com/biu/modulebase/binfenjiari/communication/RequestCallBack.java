package com.biu.modulebase.binfenjiari.communication;

import org.json.JSONObject;

/**
 * 普通网络请求回调
 * 
 * @author Lee
 *
 */
public interface RequestCallBack {
	/**
	 * 成功回调
	 * 
	 */
	public abstract void onResponse(JSONObject response);

	/**
	 * 失败回调
	 * 
	 */
	public void onErrorResponse(String errorInfo);

	/***
	 * token 失效
	 */
	public void onUnLogin();
}
