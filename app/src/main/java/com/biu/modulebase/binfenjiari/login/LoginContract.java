package com.biu.modulebase.binfenjiari.login;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/7/27
 */
public interface LoginContract {

    interface  View <Presenter>{

    }

    interface  Presenter {

        void doLogin();

        void loginSuccess();

        void loginError();


    }
}
