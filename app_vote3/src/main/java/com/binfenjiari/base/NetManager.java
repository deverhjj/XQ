package com.binfenjiari.base;

import android.support.annotation.NonNull;


import com.binfenjiari.BuildConfig;
import com.binfenjiari.retrofit.MyGsonConverterFactory;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Logger;
import com.binfenjiari.utils.MsgDigests;
import com.binfenjiari.utils.Utils;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/2/23
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class NetManager {
    private static NetManager INSTANCE;
    private Retrofit mRetrofit;
    private ParamInterceptor mBodyInterceptor;

    private NetManager() {
        setupRetrofit();
    }

    public static NetManager get() {
        if (INSTANCE == null) {
            INSTANCE = new NetManager();
        }
        return INSTANCE;
    }

    private OkHttpClient setupOKHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(getBodyParamInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    @NonNull
    public ParamInterceptor getBodyParamInterceptor() {
        if (mBodyInterceptor == null) {
            mBodyInterceptor = new ParamInterceptor(ParamInterceptor.InterceptType.BODY);
            mBodyInterceptor.setInterceptListener(new AppParamInterceptListener());
        }
        return mBodyInterceptor;
    }


    private void setupRetrofit() {
        mRetrofit = new Retrofit.Builder().client(setupOKHttpClient())
                                          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                          .addConverterFactory(
                                                  MyGsonConverterFactory.create(setupGson()))
                                          .baseUrl(Constants.BASE_URL)
                                          .build();
    }

    private Gson setupGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    public <T> T createService(Class<T> clazzOfService) {
        return mRetrofit.create(clazzOfService);
    }

    private static class AppParamInterceptListener implements ParamInterceptor.OnInterceptListener {

        private static final String TAG = AppParamInterceptListener.class.getSimpleName();

        @Override
        public boolean onIntercept(ParamInterceptor interceptor, Interceptor.Chain chain) {
            Map<String, String> params = interceptor.getParams();
            if (params == null) {
                params = new HashMap<>();
            }
            RequestBody requestBody = chain.request().body();

            Logger.e(TAG, "requestBody===>" + requestBody.getClass().getSimpleName());

            if (!(requestBody instanceof FormBody)) {
                return false;
            }

            String deviceId = Utils.getDeviceId(MyApplication.getInstance());
            String version = BuildConfig.VERSION_NAME;
            params.put(Constants.PARM_KEY_DEVICE_ID, deviceId);
            params.put(Constants.PARM_KEY_VERSION, version);
            params.put(Constants.PARM_KEY_CHANNEL, "2");
            params.put(Constants.PARM_KEY_SIGNATURE,
                       MsgDigests.md5(deviceId.substring(0, 5) + deviceId + 2 + version) + 1);

            interceptor.setParams(params);
            return true;
        }
    }
}
