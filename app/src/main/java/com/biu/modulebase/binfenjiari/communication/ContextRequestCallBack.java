package com.biu.modulebase.binfenjiari.communication;

/**
 * @author Lee
 * @Title: {BaseActivity、BaseFragment基类 二次封装网络请求的回调}
 * @Description:{描述}
 * @date 2016/5/30
 */
public interface ContextRequestCallBack {
    /**
     *  key ==1 的情况：1.网络请求成功，如登录、点赞等；
     *                  2.获取数据成功（数据不为空），填充数据。
     * @param jsonString 自己构造成JSONObject or JSONArray
     */
    public abstract void onSuccess(String jsonString);

    /**
     * 请求数据失败key!=1 根据具体界面逻辑及需求选择showTost 及额外逻辑
     */
    public abstract void onCodeError(int key,String message);

    /**
     * 网络请求连接异常统一showTost ，额外逻辑在此回调中实现
     * @param message
     */
    public abstract void onConnectError(String message);
}
