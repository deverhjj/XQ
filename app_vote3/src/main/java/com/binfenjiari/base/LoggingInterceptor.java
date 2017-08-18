package com.binfenjiari.base;



import com.binfenjiari.utils.Logger;

import java.io.IOException;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Title: 网络请求/响应 日志 debug 拦截器
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/2/23
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class LoggingInterceptor implements Interceptor {
    private static final String TAG = LoggingInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        synchronized (this) {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Logger.e(TAG, String.format(Locale.getDefault(), "Sending request %s on %s%n%s%n",
                                        request.url(), chain.connection(), request.headers()));

            RequestBody requestBody = request.body();
            if (requestBody instanceof FormBody) {
                FormBody body = (FormBody) requestBody;
                for (int i = 0; i < body.size(); i++) {
                    Logger.e(TAG, "key=" + body.name(i) + ",value=" + body.value(i) + "\n");
                }
            }

            Logger.e(TAG, "---------------DIVIDER----------------");

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Logger.e(TAG, String.format(Locale.getDefault(),
                                        "Received response for %s in %.1fms%n%s%n%s",
                                        response.request().url(), (t2 - t1) / 1e6d, response.code(),
                                        response.headers()));
            //        Logger.e(TAG,"\nbody===>\n"+response.body().string());
            return response;
        }
    }
}
