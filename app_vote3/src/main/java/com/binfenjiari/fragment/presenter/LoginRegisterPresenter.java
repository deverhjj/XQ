package com.binfenjiari.fragment.presenter;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.binfenjiari.fragment.contract.LoginLoginContract;
import com.binfenjiari.fragment.contract.LoginRegisterContract;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.LoginBean;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.contract.BasePresenter;
import com.biu.modulebase.retrofit.APIService;
import com.biu.modulebase.retrofit.ServiceUtil;
import com.biu.modulebase.retrofit.architecture.ApiResponseBody;
import com.biu.modulebase.rxjava.MCallBack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/9
 */
public class LoginRegisterPresenter extends BasePresenter<LoginRegisterContract.View> implements LoginRegisterContract.Presenter {


    @Override
    public void doRegister(final String registerAccount, String registerVerification, final String pwd) {

        mViewFragment.showViewProgress();

        Map<String, String> maps = new HashMap<>();
        maps.put("model", Constant.MODEL_REG);
        maps.put("action", Constant.ACTION_REGISTER);
        maps.put("mobile", registerAccount);
        maps.put("mobile_verify", registerVerification);
        maps.put("device_id", Utils.getDeviceId(getActivity()));
        maps.put("password", Base64.encodeToString(pwd.getBytes(), Base64.DEFAULT));
        maps.put("signature", Base64.encodeToString(Utils.encodeM((Constant.s + Utils.getDeviceId(getActivity()) + registerAccount)).getBytes(), Base64.DEFAULT));
        RequestBody parmas = ServiceUtil.getRequestBody(maps);
        Call<ApiResponseBody> call = ServiceUtil.createService(APIService.class).app_register(parmas);
        call.enqueue(new Callback<ApiResponseBody>() {
            @Override
            public void onResponse(Call<ApiResponseBody> call, Response<ApiResponseBody> response) {
                if (!response.isSuccessful()) {
                    mViewFragment.showToast(response.message());
                    mViewFragment.dismissViewProgress();
                    return;
                }

                if (response.body().getKey() == 1) {
                    mViewFragment.registerPhoneSuccess(registerAccount, pwd);
                }
            }

            @Override
            public void onFailure(Call<ApiResponseBody> call, Throwable t) {
//                Log.d("Throwable", t.toString());
//                mViewFragment.setLoginError();
                mViewFragment.dismissViewProgress();
                mViewFragment.showToast(t.toString());
            }
        });

    }

    @Override
    public void sendVerification(String phone) {
        mViewFragment.showViewProgress();

        Map<String, String> maps = new HashMap<>();
        maps.put("mobile", phone);
        maps.put("type", "10");
        maps.put("device_id", Utils.getDeviceId(getActivity()));
        maps.put("signature", Base64.encodeToString(Utils.encodeM(Utils.getDeviceId(getActivity()) + Constant.s + phone).getBytes(), Base64.DEFAULT));
        maps.put("model", Constant.MODEL_REG);
        maps.put("action", Constant.ACTION_VERIFICATION_CODE);
        RequestBody parmas = ServiceUtil.getRequestBody(maps);
        Call<ApiResponseBody> call = ServiceUtil.createService(APIService.class).app_sendmobile(parmas);
        call.enqueue(new Callback<ApiResponseBody>() {
            @Override
            public void onResponse(Call<ApiResponseBody> call, Response<ApiResponseBody> response) {
                if (!response.isSuccessful()) {
                    mViewFragment.showToast(response.message());
                    mViewFragment.dismissViewProgress();
                    return;
                }

                if (response.body().getKey() == 1) {
                    mViewFragment.showVerification();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseBody> call, Throwable t) {
//                Log.d("Throwable", t.toString());
//                mViewFragment.setLoginError();
                mViewFragment.dismissViewProgress();
                mViewFragment.showToast(t.toString());
            }
        });

    }
}
