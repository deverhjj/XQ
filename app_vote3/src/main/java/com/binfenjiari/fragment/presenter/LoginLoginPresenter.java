package com.binfenjiari.fragment.presenter;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.binfenjiari.fragment.contract.LoginLoginContract;
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
public class LoginLoginPresenter extends BasePresenter<LoginLoginContract.View> implements LoginLoginContract.Presenter {

    private String mtoken="";

    @Override
    public void requestLogin(String account, String pwd, String deviceId,MCallBack callBack) {
//        rxLogin(account,pwd,deviceId,callBack);

        getLoginToken(account,pwd,deviceId);

//        postReport();
    }

    public void getLoginToken(String account, String pwd, String deviceId){
        mViewFragment.showViewProgress();

        Map<String, String> maps = new HashMap<>();
        maps.put("model", Constant.MODEL_REG);
        maps.put("action", Constant.ACTION_LOGIN);
        maps.put("mobile", account);
        maps.put("device_id", deviceId);
        maps.put("device_type", "2");//1-ios,2-android
//        maps.put("password", pwd);
//        maps.put("signature", (account + Constant.s + deviceId));
        maps.put("password", Base64.encodeToString(pwd.getBytes(), Base64.DEFAULT));
        maps.put("signature", Base64.encodeToString(Utils.encodeM((account + Constant.s + deviceId)).getBytes(), Base64.DEFAULT));
        RequestBody parmas = ServiceUtil.getRequestBody(maps);
        Call<ApiResponseBody<LoginBean>> call = ServiceUtil.createService(APIService.class).app_login(parmas);
        call.enqueue(new Callback<ApiResponseBody<LoginBean>>() {
            @Override
            public void onResponse(Call<ApiResponseBody<LoginBean>> call, Response<ApiResponseBody<LoginBean>> response) {
                if (!response.isSuccessful()) {
                    mViewFragment.showToast(response.message());
                    mViewFragment.dismissViewProgress();
                    return;
                }
                mtoken = response.body().getResult().getToken();

                if(TextUtils.isEmpty(mtoken)){
                    mViewFragment.dismissViewProgress();
                    return;
                }

                getUserInfo(mtoken);

            }

            @Override
            public void onFailure(Call<ApiResponseBody<LoginBean>> call, Throwable t) {
//                Log.d("Throwable", t.toString());
                mViewFragment.setLoginError();
                mViewFragment.dismissViewProgress();
                mViewFragment.showToast(t.toString());
            }
        });
    }

    public void getUserInfo(String token){
        Map<String, String> maps = new HashMap<>();
        maps.put("model", Constant.MODEL_REG);
        maps.put("action", Constant.ACTION_GET_USERINFO);
        maps.put("token", token);
        RequestBody parmas = ServiceUtil.getRequestBody(maps);
        Call<ApiResponseBody<UserInfoBean>> call = ServiceUtil.createService(APIService.class).user_userInfo(parmas);
        call.enqueue(new Callback<ApiResponseBody<UserInfoBean>>() {
            @Override
            public void onResponse(Call<ApiResponseBody<UserInfoBean>> call, Response<ApiResponseBody<UserInfoBean>> response) {
                if (!response.isSuccessful()) {
                    mViewFragment.showToast(response.message());
                    mViewFragment.dismissViewProgress();
                    return;
                }
                mViewFragment.setLoginSuccess(response.body().getResult());
//                mtoken = response.body().getResult().getToken();

//                if(TextUtils.isEmpty(mtoken)){
//                    mViewFragment.dismissProgress();
//                    return;
//                }
            }

            @Override
            public void onFailure(Call<ApiResponseBody<UserInfoBean>> call, Throwable t) {
                mViewFragment.dismissViewProgress();
                mViewFragment.setLoginError();
                mViewFragment.showToast(t.toString());
            }
        });
    }

    @Override
    public String getLoginToken() {
        return mtoken;
    }


    private void postReport() {
//        JSONObject jsonParams = new JSONObject();
//        try {
//            jsonParams.put("action", "version_up");
//            jsonParams.put("model", "CircleManage");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());


        Map<String, String> maps = new HashMap<>();
        maps.put("action", "version_up");
        maps.put("model", "CircleManage");
        RequestBody body = ServiceUtil.getRequestBody(maps);
        Call<ResponseBody> call = ServiceUtil.createService(APIService.class).version_up(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    response.message();
                    return;
                }
                try {
                    Log.d("Response", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Throwable", t.toString());

            }
        });

    }

    public void rxLogin(String account, String pwd, String deviceId, final MCallBack callBack){
//        Map<String, String> maps = new HashMap<>();
//        maps.put("model", Constant.MODEL_REG);
//        maps.put("action", Constant.ACTION_LOGIN);
//        maps.put("mobile", account);
//        maps.put("device_id", deviceId);
//        maps.put("device_type", "2");//1-ios,2-android
//        maps.put("password", Base64.encodeToString(pwd.getBytes(), Base64.DEFAULT));
//        maps.put("signature", Base64.encodeToString(Utils.encodeM((account + Constant.s + deviceId)).getBytes(), Base64.DEFAULT));
//        RequestBody parmas = ServiceUtil.getRequestBody(maps);
//
//        ServiceUtil.createService(APIService.class)
//                .rx_app_login(parmas)
//                .onErrorResumeNext(new ApiResponseErrorFunc<ApiResponseBody>())
//                .flatMap(new Func1<ApiResponseBody, Observable<ApiResponseBody>>() {
//                    @Override
//                    public Observable<ApiResponseBody> call(ApiResponseBody responseBody) {
//                        LogUtil.LogD("RxJava", Thread.currentThread().toString() + "call...");
////                        String token = responseBody.getResult().getToken();
//                        JsonObject jsonObj = JSONUtil.getJsonElement(responseBody.getResult().toString()).getAsJsonObject();
//                        mtoken = JSONUtil.getString(jsonObj,"token");
//
//                        Map<String, String> mapss = new HashMap<>();
//                        mapss.put("model", Constant.MODEL_REG);
//                        mapss.put("action", Constant.ACTION_GET_USERINFO);
//                        mapss.put("token", mtoken);
//                        RequestBody parmass = ServiceUtil.getRequestBody(mapss);
//                        return ServiceUtil.createService(APIService.class).rx_user_userInfo(parmass);
//                    }
//                })
//                //io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，使用computation(),否则 I/O 操作的等待时间会浪费 CPU。
//                .subscribeOn(Schedulers.io())//指定订阅的行为（网络请求、IO操作等）发生的线程;
//                .observeOn(AndroidSchedulers.mainThread())//指定订阅回调（影响onNext、onError）发生的线程 ；而onStart不是订阅回调，是在订阅之前的行为。
//                .subscribe(new BaseSubscriber<ApiResponseBody<UserInfoBean>>(callBack) {
//
//                    @Override
//                    public void onNext(ApiResponseBody<UserInfoBean> responseBody) {
//                        LogUtil.LogD("RxJava", Thread.currentThread().toString()+"onNext");
//                        callBack.onSuccess(responseBody);
//                    }
//
//                });
    }
}
