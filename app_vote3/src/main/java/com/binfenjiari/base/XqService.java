package com.binfenjiari.base;

import com.binfenjiari.model.Comment;
import com.binfenjiari.model.LoginResult;
import com.binfenjiari.model.UserInfo;
import com.binfenjiari.model.WorksDetail;

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
    @POST("xiangqu/mobile")
    @FormUrlEncoded
    Observable<AppEcho<LoginResult>> login(@FieldMap Map<String, String> params);

    @POST("xiangqu/mobile")
    @FormUrlEncoded
    Observable<AppEcho<Void>> logout(@FieldMap Map<String, String> params);

    @POST("xiangqu/mobile")
    @FormUrlEncoded
    Observable<AppEcho<Void>> register(@FieldMap Map<String, String> params);

    @POST("xiangqu/mobile")
    @FormUrlEncoded
    Observable<AppEcho<Void>> verifyCode(@FieldMap Map<String, String> params);

    @POST("xiangqu/mobile")
    @FormUrlEncoded
    Observable<AppEcho<UserInfo>> userInfo(@FieldMap Map<String, String> params);

    @POST("xiangqu/mobile")
    @FormUrlEncoded
    Observable<AppEcho<WorksDetail>> reporterWorksDetail(@FieldMap Map<String, String> params);

    @POST("xiangqu/mobile")
    @FormUrlEncoded
    Observable<AppEcho<Comment>> comments(@FieldMap Map<String, String> params);
}
