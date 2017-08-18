package com.biu.modulebase.binfenjiari.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.ForgetPwdActivity;
import com.biu.modulebase.binfenjiari.activity.WebViewActivity;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.binfenjiari.fragment.WebViewFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.GuideDialogFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ShareDialogFragment;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jhj_Plus on 2016/5/3.
 */
public class LoginFragment extends BaseFragment {
    private static final String TAG = "UnLoginFragment";
    private ViewSwitcher viewSwitcher;

    /** 注册 密码明文标志位 **/
    private boolean isRegisterVisiable = false;
    /** 登录 密码明文标志位 **/
    private boolean isLoginVisiable = false;

    /** 登录 账号EditText **/
    private EditText loginAccountEditText;
    /** 登录 密码EditText **/
    private EditText loginPwdEditText;
    /** 登录 密码明文切换**/
    private ImageView login_pwd_visiable;
    /** 注册 账号EditText **/
    private EditText registerAccountEditText;
    /** 注册 密码EditText **/
    private EditText registerPwdEditText;
    /** 注册 密码明文切换**/
    private ImageView register_pwd_visiable;
    /** 注册 验证码EditText **/
    private EditText verificationEditText;
    /** 发送验证码按钮 **/
    private Button sendVerfiBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_unlogin,container, false);
    }

    @Override
    protected void initView(View rootView) {
        viewSwitcher= (ViewSwitcher) rootView.findViewById(R.id.view_switcher);
        //注册控件
        registerAccountEditText = (EditText) rootView.findViewById(R.id.register_phone);
        verificationEditText = (EditText) rootView.findViewById(R.id.verification);
        sendVerfiBtn = (Button) rootView.findViewById(R.id.send_verification);
        registerPwdEditText = (EditText) rootView.findViewById(R.id.register_pwd);
        register_pwd_visiable = (ImageView) rootView.findViewById(R.id.register_pwd_visiable);
        //登录控件
        loginAccountEditText = (EditText) rootView.findViewById(R.id.login_phone);
        loginPwdEditText = (EditText) rootView.findViewById(R.id.login_pwd);
        login_pwd_visiable = (ImageView) rootView.findViewById(R.id.login_pwd_visiable);
        //listener
        rootView.findViewById(R.id.close).setOnClickListener(this);
        rootView.findViewById(R.id.register_switch).setOnClickListener(this);
        rootView.findViewById(R.id.login_switch).setOnClickListener(this);
        rootView.findViewById(R.id.register_btn).setOnClickListener(this);
        rootView.findViewById(R.id.login_btn).setOnClickListener(this);
        rootView.findViewById(R.id.forgetPwd).setOnClickListener(this);
        rootView.findViewById(R.id.guide).setOnClickListener(this);
        rootView.findViewById(R.id.policy).setOnClickListener(this);
        register_pwd_visiable.setOnClickListener(this);
        login_pwd_visiable.setOnClickListener(this);
        sendVerfiBtn.setOnClickListener(this);

    }

    @Override
    public void loadData() {
    }

    private void sendVerification(String phone){
        JSONObject params = new JSONObject();
        try {
            params.put("mobile",phone);
            params.put("type","10");
            params.put("model",Constant.MODEL_REG);
            params.put("action",Constant.ACTION_VERIFICATION_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                getBaseActivity().showTost("验证码发送成功，请注意查收~",1);
                TimeCount time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
                time.start();
            }

            @Override
            public void onCodeError(int key, String message) {
                showTost(message,1);
            }

            @Override
            public void onConnectError(String message) {

            }
        });
    }

    private void doRegister(final String registerAccount, String registerVerification,final String pwd){
        showProgress(getClass().getSimpleName());
        JSONObject params = new JSONObject();
        try {
            params.put("mobile",registerAccount);
            params.put("mobile_verify",registerVerification);
            params.put("password",pwd);
            params.put("model",Constant.MODEL_REG);
            params.put("action",Constant.ACTION_REGISTER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                LogUtil.LogD(jsonString);
                doLogin(registerAccount,pwd);
                dismissProgress();
            }

            @Override
            public void onCodeError(int key, String message) {
                showTost(message,1);
                dismissProgress();
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
            }
        });
    }

    private void doLogin(String account,String pwd){
        showProgress(getClass().getSimpleName());
        JSONObject params = new JSONObject();
        try {
            params.put("mobile",account);
            params.put("password",pwd);
            params.put("device_id", Utils.getDeviceId(getActivity()));
            params.put("device_type","2");//1-ios,2-android
            params.put("model",Constant.MODEL_REG);
            params.put("action",Constant.ACTION_LOGIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                LogUtil.LogD(jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String token  = JSONUtil.getString(object,"token");
                    getPersonalInfo(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                switch (key){
                    case 1007:
                        showTost(message,1);
                        break;
                    default:
                        showTost(message,1);
                        break;
                }
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
            }
        });
    }

    private void getPersonalInfo(final String token){
        JSONObject params = new JSONObject();
        try {
            params.put("token",token);
            params.put("model",Constant.MODEL_REG);
            params.put("action",Constant.ACTION_GET_USERINFO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                LogUtil.LogD(jsonString);
                MyApplication.userInfo = JSONUtil.fromJson(jsonString, UserInfoBean.class);
                PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_TOKEN, token);
                PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_USER_INFO, jsonString);
                getActivity().finish();
                dismissProgress();
            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_TOKEN, token);
                PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_USER_INFO, null);
                getActivity().finish();
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
            }
        });
    }

    /**
     * 登录界面手机号验证
     *
     * @return
     */
    private boolean loginCheckPhone(String phone) {
        if (Utils.isEmpty(phone)) {
            getBaseActivity().showTost("手机号不能为空!",1);
            loginAccountEditText.requestFocus();
            return false;
        } else if (!Utils.isMobileNO(phone)) {
            getBaseActivity().showTost("请输入正确的手机号!",1);
            loginAccountEditText.requestFocus();
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
            getBaseActivity().showTost("手机号不能为空!",1);
            registerAccountEditText.requestFocus();
            return false;
        } else if (!Utils.isMobileNO(phone)) {
            getBaseActivity().showTost("请输入正确的手机号!",1);
            registerAccountEditText.requestFocus();
            return false;
        }
        return true;
    }

    private boolean registerCheckEmpty(String phone,String verification,String pwd) {
        if(!registerCheckPhone(phone)){
            return false;
        }else if (Utils.isEmpty(verification)) {
            getBaseActivity().showTost("验证码不能为空!",1);
            verificationEditText.requestFocus();
            return false;
        } else if (Utils.isEmpty(pwd)) {
            getBaseActivity().showTost("请输入密码!",1);
            registerPwdEditText.requestFocus();
            return false;
        }
        return true;
    }

    private boolean loginCheckParams(String phone,String pwd){
        if(!loginCheckPhone(phone)){
            return false;
        }else if (Utils.isEmpty(pwd)) {
            getBaseActivity().showTost("请输入密码!",1);
            loginPwdEditText.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.close) {
            getActivity().onBackPressed();

        } else if (i == R.id.register_switch) {
            viewSwitcher.showNext();

        } else if (i == R.id.login_switch) {
            viewSwitcher.showPrevious();

        } else if (i == R.id.guide) {
            GuideDialogFragment shareDialog = GuideDialogFragment.newInstance(ShareDialogFragment.STYLE_NO_TITLE, R.layout.pop_guide);
            shareDialog.show(getActivity().getSupportFragmentManager(), "guide");

        } else if (i == R.id.send_verification) {
            String phone = registerAccountEditText.getText().toString();
            if (registerCheckPhone(phone)) {
                sendVerification(phone);
            }

        } else if (i == R.id.register_pwd_visiable) {
            if (!isRegisterVisiable) {
                // 设置EditText文本为可见的
                register_pwd_visiable.setImageResource(R.mipmap.login_code_clear);
                registerPwdEditText.setTransformationMethod(HideReturnsTransformationMethod
                        .getInstance());
            } else {
                register_pwd_visiable.setImageResource(R.mipmap.login_code_dark);
                registerPwdEditText
                        .setTransformationMethod(PasswordTransformationMethod
                                .getInstance());
            }
            isRegisterVisiable = !isRegisterVisiable;
            registerPwdEditText.postInvalidate();

        } else if (i == R.id.login_pwd_visiable) {
            if (!isLoginVisiable) {
                // 设置EditText文本为可见的
                login_pwd_visiable.setImageResource(R.mipmap.login_code_clear);
                loginPwdEditText.setTransformationMethod(HideReturnsTransformationMethod
                        .getInstance());
            } else {
                login_pwd_visiable.setImageResource(R.mipmap.login_code_dark);
                loginPwdEditText
                        .setTransformationMethod(PasswordTransformationMethod
                                .getInstance());
            }
            isLoginVisiable = !isLoginVisiable;
            loginPwdEditText.postInvalidate();

        } else if (i == R.id.register_btn) {
            String registerAccount = registerAccountEditText.getText().toString();
            String registerVerification = verificationEditText.getText().toString();
            String registerPwd = registerPwdEditText.getText().toString();
            if (registerCheckEmpty(registerAccount, registerVerification, registerPwd)) {
                doRegister(registerAccount, registerVerification, registerPwd);
            }

        } else if (i == R.id.login_btn) {
            String loginAccount = loginAccountEditText.getText().toString();
            String loginPwd = loginPwdEditText.getText().toString();
            if (loginCheckParams(loginAccount, loginPwd)) {
                doLogin(loginAccount, loginPwd);
            }

        } else if (i == R.id.forgetPwd) {
            startActivity(new Intent(getActivity(), ForgetPwdActivity.class));

        } else if (i == R.id.policy) {
            Intent policyIntent = new Intent(getActivity(), WebViewActivity.class);
            policyIntent.putExtra("loadType", WebViewFragment.LOAD_HTML_URL);
            policyIntent.putExtra("title", "用户协议");
            policyIntent.putExtra(Constant.KEY_TYPE, Constant.LOAD_POLICY_HTML);
            startActivity(policyIntent);

        } else {
        }
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
    public void onDestroy() {
        Communications.cancelRequest(getClass().getSimpleName());
        super.onDestroy();
    }
}
