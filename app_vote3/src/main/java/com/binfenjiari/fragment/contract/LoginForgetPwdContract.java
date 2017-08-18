package com.binfenjiari.fragment.contract;

import com.biu.modulebase.contract.BaseIContract;

/**
 * @Title: {标题}
 * @Description:{指定view 和presenter之间的契约关系}
 * @date 2017/1/18
 */
public interface LoginForgetPwdContract {

    interface View extends BaseIContract.IView<Presenter> {
        void showVerification();

        void updatePwdSuccess(String phone, String pwd);
    }

    interface Presenter extends BaseIContract.IPresenter<View> {
        void doForgetPwd(String registerAccount, String registerVerification, String pwd);

        void sendVerification(String phone);
    }

}
