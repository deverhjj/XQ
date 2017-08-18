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
import android.widget.LinearLayout;

import com.binfenjiari.R;
import com.binfenjiari.fragment.contract.LoginRegisterContract;
import com.binfenjiari.fragment.presenter.LoginForgetPwdPresenter;
import com.binfenjiari.fragment.presenter.LoginRegisterPresenter;
import com.biu.modulebase.binfenjiari.activity.WebViewActivity;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.UnLoginFragment;
import com.biu.modulebase.binfenjiari.fragment.WebViewFragment;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.common.base.MvpFragment;

/**
 * @author tangjin
 * @Title: {注册}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class LoginRegisterFragment extends MvpFragment<LoginRegisterContract.Presenter> implements LoginRegisterContract.View {

    private LinearLayout ll_register_doing, ll_register_success;

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


    public static LoginRegisterFragment newInstance() {
        LoginRegisterFragment fragment = new LoginRegisterFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_login_register, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initMvpBinder(View rootView) {
        bindPresenter(new LoginRegisterPresenter());
    }

    @Override
    protected void initView(View rootView) {
        getBaseActivity().setBackNavigationIcon();
        Button btn_submit = (Button) rootView.findViewById(R.id.register_btn);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().setToolBarTitle("注册完成");
                ll_register_doing.setVisibility(View.GONE);
                ll_register_success.setVisibility(View.VISIBLE);
            }
        });

        Button btn_submit_success = (Button) rootView.findViewById(R.id.btn_submit_success);
        btn_submit_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        ll_register_doing = (LinearLayout) rootView.findViewById(R.id.ll_register_doing);
        ll_register_success = (LinearLayout) rootView.findViewById(R.id.ll_register_success);
        ll_register_doing.setVisibility(View.VISIBLE);
        ll_register_success.setVisibility(View.GONE);

        rootView.findViewById(R.id.ll_user_rule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent policyIntent = new Intent(getActivity(), WebViewActivity.class);
                policyIntent.putExtra("loadType", WebViewFragment.LOAD_HTML_URL);
                policyIntent.putExtra("title", "用户协议");
                policyIntent.putExtra(Constant.KEY_TYPE, Constant.LOAD_POLICY_HTML);
                startActivity(policyIntent);
            }
        });


        //注册控件
        registerAccountEditText = (EditText) rootView.findViewById(com.biu.modulebase.binfenjiari.R.id.register_phone);
        verificationEditText = (EditText) rootView.findViewById(com.biu.modulebase.binfenjiari.R.id.verification);
        sendVerfiBtn = (Button) rootView.findViewById(com.biu.modulebase.binfenjiari.R.id.send_verification);
        registerPwdEditText = (EditText) rootView.findViewById(com.biu.modulebase.binfenjiari.R.id.register_pwd);
        sendVerfiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证码
                String phone = registerAccountEditText.getText().toString();
                if (registerCheckPhone(phone)) {
                    mPresenter.sendVerification(phone);
                }
            }
        });
        rootView.findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始注册
                String registerAccount = registerAccountEditText.getText().toString();
                String registerVerification = verificationEditText.getText().toString();
                String registerPwd = registerPwdEditText.getText().toString();
                if (registerCheckEmpty(registerAccount, registerVerification, registerPwd)) {
                    mPresenter.doRegister(registerAccount, registerVerification, registerPwd);
                }
            }
        });

    }

    @Override
    public void loadData() {

    }

    @Override
    public void registerPhoneSuccess(String phone, String pwd) {
        getBaseActivity().showTost("注册成功", 1);
        Intent intent = new Intent();
        intent.putExtra("phone", phone);
        intent.putExtra("pwd", pwd);
        getActivity().setResult(Activity.RESULT_OK, intent);
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

    @Override
    public void showVerification() {
        getBaseActivity().showTost("验证码发送成功，请注意查收~", 1);
        TimeCount time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        time.start();
    }
}
