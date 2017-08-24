package com.binfenjiari.fragment.contract;


import android.os.Bundle;

import com.binfenjiari.base.AppExp;
import com.binfenjiari.base.BaseContract;
import com.binfenjiari.base.PostIView;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public interface UserContract {

    interface UserView extends BaseContract.BaseIView<UserPresenter>, PostIView {

        void onLoginSuccess();

        void onLoginFailed(AppExp exp);

        void onCodeFailed(AppExp exp);

        void onCodeSuccess();

        void onRegisterSuccess();

        void onRegisterFailed(AppExp exp);

        void onFindPwdSuccess();

        void onFindPwdFailed(AppExp exp);
    }

    interface UserPresenter extends BaseContract.BaseIPresenter<UserView> {
        void login(Bundle args);

        void register(Bundle args);

        void findPwd(Bundle args);

        void logout(Bundle args);

        void getCode(Bundle args);

    }

}
