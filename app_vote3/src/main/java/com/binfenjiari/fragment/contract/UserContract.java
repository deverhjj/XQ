package com.binfenjiari.fragment.contract;


import android.os.Bundle;

import com.binfenjiari.base.BaseContract;
import com.binfenjiari.base.PostIView;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public interface UserContract {

    interface UserView extends BaseContract.BaseIView<UserPresenter>, PostIView {

    }

    interface UserPresenter extends BaseContract.BaseIPresenter<UserView> {
        void login(Bundle args);

        void register(Bundle args);
    }

}
