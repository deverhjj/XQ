package com.biu.modulebase.binfenjiari.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/5/30
 */
public class ForgetPwFragment extends BaseFragment {
    /** 密码明文标志位 **/
    private boolean isVisiable = false;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        return super.onCreateView(inflater,
                (ViewGroup) inflater.inflate(R.layout.fragment_forget_pwd,container,
                        false),
                savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        setHasOptionsMenu(true);
        registerAccountEditText = (EditText) rootView.findViewById(R.id.register_phone);
        verificationEditText = (EditText) rootView.findViewById(R.id.verification);
        sendVerfiBtn = (Button) rootView.findViewById(R.id.send_verification);
        registerPwdEditText = (EditText) rootView.findViewById(R.id.register_pwd);
        register_pwd_visiable = (ImageView) rootView.findViewById(R.id.register_pwd_visiable);

        rootView.findViewById(R.id.close).setOnClickListener(this);
        rootView.findViewById(R.id.sureBtn).setOnClickListener(this);
        register_pwd_visiable.setOnClickListener(this);
        sendVerfiBtn.setOnClickListener(this);
    }

    @Override
    public void loadData() {

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

    private void sendVerification(String phone){
        JSONObject params = new JSONObject();
        try {
            params.put("type","20");
            params.put("model", Constant.MODEL_REG);
            params.put("action",Constant.ACTION_VERIFICATION_CODE);
            params.put("mobile",phone);
            params.put("device_id",Utils.getDeviceId(getActivity()));
            params.put("signature", Base64.encodeToString(Utils.encodeM(Utils.getDeviceId(getActivity())+ Constant.s +phone).getBytes(),Base64.DEFAULT));
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
                getBaseActivity().showTost(message, 1);
            }

            @Override
            public void onConnectError(String message) {

            }
        });
    }

    private void forgetPwd(final String registerAccount,final String registerVerification,String pwd){
        JSONObject params = new JSONObject();
        try {
            params.put("model",Constant.MODEL_REG);
            params.put("action",Constant.ACTION_FORGET_PWD);
            params.put("mobile",registerAccount);
            params.put("device_id",Utils.getDeviceId(getActivity()));
            params.put("mobile_verify",registerVerification);
            params.put("password",Base64.encodeToString(pwd.getBytes(),Base64.DEFAULT));
            params.put("signature",Base64.encodeToString(Utils.encodeM((Constant.s +Utils.getDeviceId(getActivity())+ registerAccount)).getBytes(),Base64.DEFAULT));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                LogUtil.LogD(jsonString);
                getBaseActivity().finish();
            }

            @Override
            public void onCodeError(int key, String message) {
            }

            @Override
            public void onConnectError(String message) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.close) {
            getActivity().onBackPressed();

        } else if (i == R.id.send_verification) {
            String phone = registerAccountEditText.getText().toString();
            if (registerCheckPhone(phone)) {
                sendVerification(phone);
            }

        } else if (i == R.id.sureBtn) {
            String registerAccount = registerAccountEditText.getText().toString();
            String registerVerification = verificationEditText.getText().toString();
            String registerPwd = registerPwdEditText.getText().toString();
            if (registerCheckEmpty(registerAccount, registerVerification, registerPwd)) {
                forgetPwd(registerAccount, registerVerification, registerPwd);
            }

        } else if (i == R.id.register_pwd_visiable) {
            if (!isVisiable) {
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
            isVisiable = !isVisiable;
            registerPwdEditText.postInvalidate();

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
}
