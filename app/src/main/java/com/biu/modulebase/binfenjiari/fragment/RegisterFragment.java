package com.biu.modulebase.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jhj_Plus on 2016/1/26.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RegisterFragment";

    private EditText mAccount;
    private EditText mPwd;
    private EditText mVerifiCode;

    private Button mGetVerifiCode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

    //    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
//            @Nullable Bundle savedInstanceState)
//    {
//        View rootView=inflater.inflate(R.layout.fragment_register,container,false);
//        mGetVerifiCode= (Button)rootView. findViewById(R.id.btn_getVerifiCode);
//        mGetVerifiCode.setOnClickListener(this);
//        Button btn_register= (Button) rootView.findViewById(R.id.btn_register);
//        btn_register.setOnClickListener(this);
//        mAccount = (EditText) rootView.findViewById(R.id.et_account);
//        mPwd = (EditText) rootView.findViewById(R.id.et_pwd);
//        mVerifiCode = (EditText) rootView.findViewById(R.id.et_verifiCode);
//
//        return rootView;
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_getVerifiCode:
//                OtherUtil.sendVerifiCodeMsg(getContext(), mAccount.getText().toString(),"10",
//                        mGetVerifiCode,60, TAG);
//                break;
//            case R.id.btn_register:
//                register(mAccount.getText().toString(), mVerifiCode.getText().toString(),
//                        mPwd.getText().toString());
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void register(String phone, String verifiCode, String pwd) {
//        boolean isPhoneCorrect = !TextUtils.isEmpty(phone) && Utils.isMobileNO(phone);
//        boolean isPwdCorrect = !TextUtils.isEmpty(pwd) && Utils.isPasswordCorrect(pwd);
//        boolean isVerificationCodeCorrect=!TextUtils.isEmpty(verifiCode) &&Utils.isVerificationCodeCorrect(verifiCode);
//        if (!isPhoneCorrect || !isVerificationCodeCorrect || !isPwdCorrect) {
//            OtherUtil.showToast(getActivity(), !isPhoneCorrect ? "手机号格式不正确"
//                    : !isVerificationCodeCorrect ? "验证码格式错误" : "密码格式错误");
//            return;
//        }
//
//        Map<String, Object> params = new HashMap<>();
//
//        params.put("mobile", phone);
//        params.put("mobile_verify", verifiCode);
//        params.put("password", pwd);
//        Communications.stringRequestData(false,false,null,params, Constant.URL_REGISTER, Request.Method
//                .POST, TAG,
//                new RequestCallBack() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e(TAG, "json text------>" + response.toString());
//                        try {
//                            OtherUtil.showToast(getActivity(),getRegisterStatus(
//                                    response.getInt("key")));
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onErrorResponse(String errorInfo) {
//                        Log.e(TAG, "onErrorResponse------>" + errorInfo);
//                        OtherUtil.showToast(getActivity(),errorInfo);
//                    }
//
//                    @Override
//                    public void onUnLogin() {
//                        //Log.e(TAG, "onErrorResponse------>" +errorInfo);
//                    }
//                });
//    }
//
//    private String getRegisterStatus(int code) {
//        String status = "";
//        switch (code) {
//            case 1002:
//                status = "手机号、密码或验证码格式有误";
//                break;
//            case 1003:
//                status = "手机号码已经注册过";
//                break;
//            case 1006:
//                status = "验证码过期了";
//                break;
//            case 1007:
//                status = "校验失败,验证码错误";
//                break;
//            case 1008:
//                status = "注册成功";
//                getActivity().finish();
//                break;
//            default:
//                break;
//        }
//        return status;
//    }
}
