package com.binfenjiari.fragment.presenter;

import android.os.Bundle;
import android.util.Base64;

import com.binfenjiari.R;
import com.binfenjiari.base.AppEcho;
import com.binfenjiari.base.AppExp;
import com.binfenjiari.base.AppManager;
import com.binfenjiari.base.NetObserver;
import com.binfenjiari.base.NetPresenter;
import com.binfenjiari.base.PostCallback;
import com.binfenjiari.base.PostIView;
import com.binfenjiari.fragment.contract.UserContract;
import com.binfenjiari.model.LoginResult;
import com.binfenjiari.model.UserInfo;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Datas;
import com.binfenjiari.utils.Logger;
import com.binfenjiari.utils.Rxs;

import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

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

        Map<String, String> params =
                Datas.paramsOf("mobile", account, "password", pwd, "methodName", "app_login");

        pushTask(Rxs.applyBase(service.login(params))
                    .flatMap(
                            new Function<AppEcho<LoginResult>, ObservableSource<AppEcho<UserInfo>>>() {
                                @Override
                                public ObservableSource<AppEcho<UserInfo>> apply(
                                        @NonNull AppEcho<LoginResult> echo) throws Exception
                                {
                                    final String token = echo.data().token;
                                    AppManager.get().setToken(token);
                                    Map<String, String> params =
                                            Datas.paramsOf("token", token, "methodName",
                                                           "user_userInfo");
                                    return Rxs.applyBase(service.userInfo(params));
                                }
                            })
                    .subscribeWith(new NetObserver<>(new PostCallback<UserInfo>(view) {
                        @Override
                        public void onEcho(AppEcho<UserInfo> echo) {
                            view.onLoginSuccess();
                            Logger.e(TAG,"onEcho");
                            AppManager.get().setUserInfo(echo.data());
                        }

                        @Override
                        public void onFailure(AppExp e) {
                            super.onFailure(e);
                            Logger.e(TAG,"onFailure");
                            view.onLoginFailed(e);
                        }
                    })));
    }

    @Override
    public void register(Bundle args) {
        String account = args.getString(Constants.KEY_ACCOUNT);
        String pwd = args.getString(Constants.KEY_PWD, "");
        pwd = Base64.encodeToString(pwd.getBytes(), Base64.DEFAULT);
        String vCode = args.getString(Constants.KEY_VCODE);

        Map<String, String> params =
                Datas.paramsOf("mobile", account, "password", pwd, "mobile_verify", vCode,
                               "methodName", "app_register");

        pushTask(Rxs.applyBase(service.register(params))
                    .subscribeWith(new NetObserver<>(new PostCallback<Void>(view) {
                        @Override
                        public void onEcho(AppEcho<Void> echo) {
                            view.onRegisterSuccess();
                        }

                        @Override
                        public void onFailure(AppExp e) {
                            super.onFailure(e);
                            view.onRegisterFailed(e);
                        }
                    })));
    }

    @Override
    public void findPwd(Bundle args) {
        String account = args.getString(Constants.KEY_ACCOUNT);
        String pwd = args.getString(Constants.KEY_PWD, "");
        pwd = Base64.encodeToString(pwd.getBytes(), Base64.DEFAULT);
        String vCode = args.getString(Constants.KEY_VCODE);

        Map<String, String> params =
                Datas.paramsOf("mobile", account, "password", pwd, "mobile_verify", vCode,
                               "methodName", "app_findPassword");

        pushTask(Rxs.applyBase(service.verifyCode(params))
                    .subscribeWith(new NetObserver<>(new PostCallback<Void>(view) {
                        @Override
                        public void onEcho(AppEcho<Void> echo) {
                            view.onFindPwdSuccess();
                        }

                        @Override
                        public void onFailure(AppExp e) {
                            super.onFailure(e);
                            view.onFindPwdFailed(e);
                        }
                    })));
    }

    @Override
    public void logout(Bundle args) {

    }

    @Override
    public void getCode(Bundle args) {
        String account = args.getString(Constants.KEY_ACCOUNT);
        String type = args.getString(Constants.KEY_TYPE);

        Map<String, String> params =
                Datas.paramsOf("phone", account, "type", type, "methodName", "app_sendmobile");

        pushTask(Rxs.applyBase(service.verifyCode(params))
                    .subscribeWith(new NetObserver<>(new PostCallback<Void>(view) {
                        @Override
                        public void onEcho(AppEcho<Void> echo) {
                            view.onCodeSuccess();
                        }

                        @Override
                        public void onFailure(AppExp e) {
                            super.onFailure(e);
                            view.onCodeFailed(e);
                        }
                    })));
    }

}
