package com.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.activity.LoginActivity;
import com.binfenjiari.fragment.contract.LoginLoginContract;
import com.binfenjiari.fragment.presenter.LoginLoginPresenter;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.MvpFragment;
import com.biu.modulebase.retrofit.architecture.ApiException;
import com.biu.modulebase.retrofit.architecture.ApiResponseBody;
import com.biu.modulebase.rxjava.MCallBack;

/**
 * @author tangjin
 * @Title: {登录}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class LoginLoginFragment extends MvpFragment<LoginLoginContract.Presenter> implements LoginLoginContract.View {

    /**
     * 登录 账号EditText
     **/
    private EditText loginAccountEditText;
    /**
     * 登录 密码EditText
     **/
    private EditText loginPwdEditText;


    public static LoginLoginFragment newInstance() {
        LoginLoginFragment fragment = new LoginLoginFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_login_login, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initMvpBinder(View rootView) {
        bindPresenter(new LoginLoginPresenter());
    }

    @Override
    protected void initView(View rootView) {
        getBaseActivity().setBackNavigationIcon();
        TextView mTvRegister = (TextView) rootView.findViewById(R.id.tv_register);

        loginAccountEditText = (EditText) rootView.findViewById(com.biu.modulebase.binfenjiari.R.id.login_phone);
        loginPwdEditText = (EditText) rootView.findViewById(com.biu.modulebase.binfenjiari.R.id.login_pwd);


        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.beginRegister(LoginLoginFragment.this);
            }
        });

        TextView tv_forget_pwd = (TextView) rootView.findViewById(R.id.tv_forget_pwd);
        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.beginForgetPwd(LoginLoginFragment.this);
            }
        });

        TextView btn_submit_login = (TextView) rootView.findViewById(R.id.btn_submit_login);
        btn_submit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginAccount = loginAccountEditText.getText().toString();
                String loginPwd = loginPwdEditText.getText().toString();
                if (loginCheckParams(loginAccount, loginPwd)) {
//                    doLogin(loginAccount, loginPwd);
//                    showProgress(Tag);
                    mPresenter.requestLogin(loginAccount, loginPwd, Utils.getDeviceId(getActivity()), callBackLoginSuccess);
//                    retrofitLogin(loginAccount,loginPwd);
                }


            }
        });


    }

    @Override
    public void loadData() {

    }

    MCallBack<UserInfoBean> callBackLoginSuccess = new MCallBack<UserInfoBean>() {

        @Override
        public void onSuccess(ApiResponseBody<UserInfoBean> responseBody) {
            setLoginSuccess(responseBody.getResult());

        }

        @Override
        public void onError(ApiException e) {
            setLoginError();
            dismissProgress();

        }

        @Override
        public void onTokenInvalid() {

        }

        @Override
        public void onNoNetwork() {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.TYPE_REGISTER) {
            //注册
            if (resultCode == Activity.RESULT_OK) {
//                showTost("登录成功", 0);
//                getActivity().finish();
                Bundle bundle = data.getExtras();
                String loginAccount = bundle.getString("phone");
                String loginPwd = bundle.getString("pwd");
                mPresenter.requestLogin(loginAccount, loginPwd, Utils.getDeviceId(getActivity()), callBackLoginSuccess);
            }

        } else if (requestCode == LoginActivity.TYPE_FORGET_PWD) {
            if (resultCode == Activity.RESULT_OK) {
                //忘记密码
                Bundle bundle = data.getExtras();
                String loginAccount = bundle.getString("phone");
                loginAccountEditText.setText(loginAccount);
            }
        }


    }

    private boolean loginCheckParams(String phone, String pwd) {
        if (!loginCheckPhone(phone)) {
            return false;
        } else if (Utils.isEmpty(pwd)) {
            getBaseActivity().showTost("请输入密码!", 1);
            loginPwdEditText.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 登录界面手机号验证
     *
     * @return
     */
    private boolean loginCheckPhone(String phone) {
        if (Utils.isEmpty(phone)) {
            getBaseActivity().showTost("手机号不能为空!", 1);
            loginAccountEditText.requestFocus();
            return false;
        } else if (!Utils.isMobileNO(phone)) {
            getBaseActivity().showTost("请输入正确的手机号!", 1);
            loginAccountEditText.requestFocus();
            return false;
        }
        return true;
    }

    public void setLoginSuccess(UserInfoBean userInfoVO) {

        String jsonString = JSONUtil.toJson(userInfoVO);
        LogUtil.LogD(jsonString);
        MyApplication.userInfo = userInfoVO;
//            JSONUtil.fromJson(jsonString, UserInfoBean.class);
        PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_TOKEN, mPresenter.getLoginToken());
        PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_USER_INFO, jsonString);

        showTost("登录成功", 0);
        getActivity().finish();
    }

    @Override
    public void setLoginError() {
        PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_TOKEN, mPresenter.getLoginToken());
        PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_USER_INFO, null);
        getActivity().finish();
    }
}
