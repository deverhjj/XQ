package com.binfenjiari.base;

import com.binfenjiari.model.LoginResult;
import com.binfenjiari.model.UserInfo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public interface XqService {
    @POST("app_login")
    @FormUrlEncoded
    Observable<AppEcho<LoginResult>> login(@FieldMap Map<String, String> params);

    @POST("user/app_logout")
    @FormUrlEncoded
    Observable<AppEcho> logout(@FieldMap Map<String, String> params);

    @POST("app_register")
    @FormUrlEncoded
    Observable<AppEcho> register(@FieldMap Map<String, String> params);

    @POST("app_sendmobile")
    @FormUrlEncoded
    Observable<AppEcho> verifyCode(@FieldMap Map<String, String> params);

}
