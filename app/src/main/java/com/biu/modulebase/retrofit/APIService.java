package com.biu.modulebase.retrofit;

import com.biu.modulebase.binfenjiari.model.LoginBean;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.retrofit.architecture.ApiResponseBody;
import com.tencent.mm.sdk.modelbase.BaseResp;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;


/**
 * @author Lee
 * @Title: {APIService}
 * @Description:{使用Retrofit将HTTP API转换为Java接口}
 * @date 2016/11/25
 */
public interface APIService {
    //demo test
    @GET("users/{user}/repos")
    Call<String> listRepos(@Path("user") String user);

    /**
     * 登录
     *
     * @param route
     * @return //
     */
//    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(" ")
    Call<String> doLogin(@Body RequestBody route);

    /**
     * 注意1：必须使用{@code @POST}注解为post请求<br>
     * 注意：使用{@code @Multipart}注解方法，必须使用{@code @Part}/<br>
     * {@code @PartMap}注解其参数<br>
     * 本接口中将文本数据和文件数据分为了两个参数，是为了方便将封装<br>
     * {@link MultipartBody.Part}的代码抽取到工具类中<br>
     * 也可以合并成一个{@code @Part}参数
     *
     * @param params 用于封装文本数据
     * @param parts  用于封装文件数据
     * @return BaseResp为服务器返回的基本Json数据的Model类
     */
    @Multipart
    @POST(" ")
    Observable<BaseResp> requestMutliUpload(@PartMap Map<String, RequestBody> params,
                                            @Part List<MultipartBody.Part> parts);

    /**
     * 注意1：必须使用{@code @POST}注解为post请求<br>
     * 注意2：使用{@code @Body}注解参数，则不能使用{@code @Multipart}注解方法了<br>
     * 直接将所有的{@link MultipartBody.Part}合并到一个{@link MultipartBody}中
     */
    @POST(" ")
    Observable<BaseResp> requestMutliUpload(@Body MultipartBody body);

    /**
     * 上传错误日志
     * <p>
     * 注意1：必须使用{@code @POST}注解为post请求<br>
     * 注意2：使用{@code @Body}注解参数，则不能使用{@code @Multipart}注解方法了<br>
     * 直接将所有的{@link MultipartBody.Part}合并到一个{@link MultipartBody}中
     */
    @POST("appunifiedfile?modelname=app_errorUpload")
    Call<ResponseBody> uploadErrorLog(@Body MultipartBody body);

    /**
     * 上传错误日志
     * <p>
     * 注意1：必须使用{@code @POST}注解为post请求<br>
     * 注意2：使用{@code @Body}注解参数，则不能使用{@code @Multipart}注解方法了<br>
     * 直接将所有的{@link MultipartBody.Part}合并到一个{@link MultipartBody}中
     */
    @POST("appunifiedfile?modelname=app_errorUpload")
    Call<String> uploadErrorLog2(@Body MultipartBody body);

    @POST("app")
    Call<ResponseBody> version_up(@Body RequestBody params);

    /**
     * 登录
     *
     * @param params
     * @return ApiResult为服务器返回的基本Json数据的Model类
     */
//    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
//    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("app")
//    @FormUrlEncoded
    Call<ApiResponseBody<LoginBean>> app_login(@Body RequestBody params);

    /**
     * 获取用户资料
     *
     * @param params
     * @return ApiResult为服务器返回的基本Json数据的Model类
     */
//    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
//    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("app")
//    @FormUrlEncoded
    Call<ApiResponseBody<UserInfoBean>> user_userInfo(@Body RequestBody params);

    /**
     * 登录
     *
     * @param params
     * @return ApiResult为服务器返回的基本Json数据的Model类
     */
//    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
//    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("app")
    @FormUrlEncoded
    Observable<ApiResponseBody> rx_app_login(@Body RequestBody params);

    /**
     * 获取用户资料
     *
     * @param params
     * @return ApiResult为服务器返回的基本Json数据的Model类
     */
//    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
//    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("app")
    @FormUrlEncoded
    <T> Observable<ApiResponseBody<T>> rx_user_userInfo(@Body RequestBody params);


    /**
     * 发送验证码
     *
     * @param params
     * @return ApiResult为服务器返回的基本Json数据的Model类
     */
//    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
//    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("app")
//    @FormUrlEncoded
    Call<ApiResponseBody> app_sendmobile(@Body RequestBody params);

    /**
     * 注册
     * @param params
     * @return ApiResult为服务器返回的基本Json数据的Model类
     */
    @POST("app")
    Call<ApiResponseBody> app_register(@Body RequestBody params);

    /**
     *  忘记密码
     * @param params
     * @return ApiResult为服务器返回的基本Json数据的Model类
     */
    @POST("app")
    Call<ApiResponseBody> app_findPassword(@Body RequestBody params);



}
