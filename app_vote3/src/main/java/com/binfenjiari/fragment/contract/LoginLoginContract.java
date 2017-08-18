package com.binfenjiari.fragment.contract;

import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.contract.BaseIContract;
import com.biu.modulebase.rxjava.MCallBack;

/**
 * @Title: {标题}
 * @Description:{指定view 和presenter之间的契约关系}
 * @date 2017/1/18
 */
public interface LoginLoginContract {

    interface View extends BaseIContract.IView<Presenter> {

        void setLoginSuccess(UserInfoBean userInfoVO);

        void setLoginError();
    }

    interface Presenter extends BaseIContract.IPresenter<View> {

        void requestLogin(String account, String pwd, String deviceId,MCallBack callBack);

        String getLoginToken();
    }

}
