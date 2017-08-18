package com.binfenjiari.fragment.contract;

import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.contract.BaseIContract;
import com.biu.modulebase.rxjava.MCallBack;

/**
 * @Title: {标题}
 * @Description:{指定view 和presenter之间的契约关系}
 * @date 2017/1/18
 */
public interface LoginRegisterContract {

    interface View extends BaseIContract.IView<Presenter> {
        void showVerification();

        void registerPhoneSuccess(String phone, String pwd);

    }

    interface Presenter extends BaseIContract.IPresenter<View> {

        void doRegister(String registerAccount, String registerVerification, String pwd);

        void sendVerification(String phone);

    }

}
