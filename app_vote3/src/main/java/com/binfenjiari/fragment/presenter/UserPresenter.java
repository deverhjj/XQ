package com.binfenjiari.fragment.presenter;

import android.os.Bundle;
import android.util.Base64;

import com.binfenjiari.base.AppEcho;
import com.binfenjiari.base.NetObserver;
import com.binfenjiari.base.NetPresenter;
import com.binfenjiari.base.PostCallback;
import com.binfenjiari.fragment.contract.UserContract;
import com.binfenjiari.model.LoginResult;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Datas;
import com.binfenjiari.utils.Logger;
import com.binfenjiari.utils.Rxs;

import java.util.Map;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class UserPresenter extends NetPresenter<UserContract.UserView>
        implements UserContract.UserPresenter
{
    public static final String TAG = UserPresenter.class.getSimpleName();

    @Override
    public void start() {
    }

    @Override
    public void login(Bundle args) {
        String account = args.getString(Constants.KEY_ACCOUNT);
        String pwd = args.getString(Constants.KEY_PWD, "");
        pwd = Base64.encodeToString(pwd.getBytes(), Base64.DEFAULT);

        Map<String, String> params = Datas.paramsOf("mobile", account, "password", pwd);

        pushTask(Rxs.applyBase(service.login(params))
                    .subscribeWith(new NetObserver<>(new PostCallback<LoginResult>(view) {
                        @Override
                        public void onEcho(AppEcho<LoginResult> echo) {
                            String token = echo.data().token;
                            Logger.e(TAG, "token = " + token);
                        }
                    })));
    }

    @Override
    public void register(Bundle args) {

    }

}
