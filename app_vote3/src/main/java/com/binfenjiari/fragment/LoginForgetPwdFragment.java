package com.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.binfenjiari.R;
import com.binfenjiari.fragment.contract.LoginForgetPwdContract;
import com.binfenjiari.fragment.presenter.LoginForgetPwdPresenter;
import com.binfenjiari.fragment.presenter.LoginLoginPresenter;
import com.biu.modulebase.binfenjiari.fragment.ForgetPwFragment;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.common.base.MvpFragment;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class LoginForgetPwdFragment extends MvpFragment<LoginForgetPwdContract.Presenter> implements LoginForgetPwdContract.View {

    /**
     * 注册 账号EditText
     **/
    private EditText registerAccountEditText;
    /**
     * 注册 密码EditText
     **/
    private EditText registerPwdEditText;
    /**
     * 注册 验证码EditText
     **/
    private EditText verificationEditText;
    /**
     * 发送验证码按钮
     **/
    private Button sendVerfiBtn;

    public static LoginForgetPwdFragment newInstance() {
        LoginForgetPwdFragment fragment = new LoginForgetPwdFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_login_forgetpwd, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initMvpBinder(View rootView) {
        bindPresenter(new LoginForgetPwdPresenter());
    }

    @Override
    protected void initView(View rootView) {
        getBaseActivity().setBackNavigationIcon();
//        mRefreshRecyclerView = (RefreshRecyclerView) rootView.findViewById(R.id.rrv_refresh_recycleview);
        registerAccountEditText = (EditText) rootView.findViewById(com.biu.modulebase.binfenjiari.R.id.register_phone);
        verificationEditText = (EditText) rootView.findViewById(com.biu.modulebase.binfenjiari.R.id.verification);
        sendVerfiBtn = (Button) rootView.findViewById(com.biu.modulebase.binfenjiari.R.id.send_verification);
        registerPwdEditText = (EditText) rootView.findViewById(com.biu.modulebase.binfenjiari.R.id.register_pwd);


        rootView.findViewById(R.id.sureBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String registerAccount = registerAccountEditText.getText().toString();
                String registerVerification = verificationEditText.getText().toString();
                String registerPwd = registerPwdEditText.getText().toString();
                if (registerCheckEmpty(registerAccount, registerVerification, registerPwd)) {
                    mPresenter.doForgetPwd(registerAccount, registerVerification, registerPwd);
                }
            }
        });
        sendVerfiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = registerAccountEditText.getText().toString();
                if (registerCheckPhone(phone)) {
                    mPresenter.sendVerification(phone);
                }
            }
        });


    }

    @Override
    public void loadData() {

    }

    @Override
    public void showVerification() {
        getBaseActivity().showTost("验证码发送成功，请注意查收~", 1);
        TimeCount time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        time.start();
    }

    @Override
    public void updatePwdSuccess(String phone, String pwd) {
        getBaseActivity().showTost("密码修改成功", 1);
        Intent intent = new Intent();
        intent.putExtra("phone", phone);
        intent.putExtra("pwd", pwd);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }


    private boolean registerCheckEmpty(String phone, String verification, String pwd) {
        if (!registerCheckPhone(phone)) {
            return false;
        } else if (Utils.isEmpty(verification)) {
            getBaseActivity().showTost("验证码不能为空!", 1);
            verificationEditText.requestFocus();
            return false;
        } else if (Utils.isEmpty(pwd)) {
            getBaseActivity().showTost("请输入密码!", 1);
            registerPwdEditText.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 注册界面手机号验证
     *
     * @return
     */
    private boolean registerCheckPhone(String phone) {
        if (Utils.isEmpty(phone)) {
            getBaseActivity().showTost("手机号不能为空!", 1);
            registerAccountEditText.requestFocus();
            return false;
        }
//        else if (!Utils.isMobileNO(phone)) {
//            getBaseActivity().showTost("请输入正确的手机号!",1);
//            registerAccountEditText.requestFocus();
//            return false;
//        }
        return true;
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            sendVerfiBtn.setText("重新发送");
            sendVerfiBtn.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            sendVerfiBtn.setClickable(false);
            sendVerfiBtn.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
