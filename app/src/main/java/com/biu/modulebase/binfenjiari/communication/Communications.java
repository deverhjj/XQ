package com.biu.modulebase.binfenjiari.communication;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biu.modulebase.binfenjiari.communication.uploadservice.MultipartUploadRequest;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.Utils;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.apache.http.Header;

/**
 * 用于和服务器端通信
 *
 * @author Lee
 */
public class Communications {

    private static final String TAG = "Communications";
    private static RequestQueue mVolleyQueue;

    /**
     * 获取网络请求对象
     *
     * @return RequestQueue
     */
    private static void getRequestInstance() {
        mVolleyQueue = MyApplication.getRequestQueue();
    }

    /**
     * 普通http GET请求
     *
     * @param requestToken  token
     *         注：是否为必须参数，用于进行未登录处理;
     *                      1.token为必要参数： requestToken==true；params.put("token",token);
     *                      2.token为非必要参数：requestToken =false；token不为空的情况下传
     *                      3.token无需传：requestToken =false；params不传
     * @param url     请求url
     * @param Tag     用于取消网络请求的标识
     * @param callback 回调 ，返回json格式数据
     */
    public static void stringGetRequest(boolean requestToken,Map<String, Object> params, String url,
                                        String Tag, final RequestCallBack callback)
    {
        getRequestInstance();
        if (mVolleyQueue == null) {
            return;
        }
        if(requestToken && Utils.isEmpty(params.get("token").toString())){
            callback.onUnLogin();
            return;
        }
        String urlString =url;
        if(params.size()>0){
            StringBuilder urlBuilder =new StringBuilder().append(url).append("?");
            for(String key:params.keySet()){
                urlBuilder.append(key).append("=").append(params.get(key)).append("&");
            }
            urlString =urlBuilder.substring(0,urlBuilder.length()-1);
        }
        Request<JSONObject> request = new NormalStringRequest(params,urlString , Method.GET,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String key = JSONUtil.getString(response, "key");
                        if (key.equals("1024")||key.equals("1023")) {
                            callback.onUnLogin();
                        } else {
                            callback.onResponse(response);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(callback, error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(Tag);
        mVolleyQueue.add(request);
    }

    /**
     * 普通http POST请求
     * @param requestToken  token
     *         注：是否为必须参数，用于进行未登录处理;
     *                      1.token为必要参数： requestToken==true；params.put("token",token);
     *                      2.token为非必要参数：requestToken =false；token不为空的情况下传
     *                      3.token无需传：requestToken =false；params不传
     * @param params   put所有参数的集合
     * @param url     请求url
     * @param Tag     用于取消网络请求的标识
     * @param callback 回调 ，返回json格式数据
     */
    public static void stringPostRequest(boolean requestToken,Map<String, String> headers,Map<String, Object> params, String url, String Tag, final RequestCallBack callback)
    {
        getRequestInstance();
        if (mVolleyQueue == null) {
            return;
        }
        if(requestToken && Utils.isEmpty(params.get("token").toString())){
            callback.onUnLogin();
            return;
        }
        Request<JSONObject> request = new NormalStringRequest(headers,params, url, Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String key = JSONUtil.getString(response, "key");
                        if (key.equals("1024")||key.equals("1023")) {
                            callback.onUnLogin();
                        } else {
                            callback.onResponse(response);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(callback, error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(Tag);
        mVolleyQueue.add(request);


    }

    /**
     * 普通http请求
     *
     * @param params   string型参数
     * @param url     请求url
     * @param Tag     用于取消网络请求的标识
     * @param method   get/post
     * @param callback 回调 ，返回json格式数据
     */
    public static void stringRequestData(Map<String, Object> params, String url, int method,
                                         String Tag, final RequestCallBack callback)
    {
        getRequestInstance();
        if (mVolleyQueue == null) {
            return;
        }
        if (params!=null) {
            params.put("channel", Constant.ANDROID_CHANNEL);
            params.put("key", Constant.KEY);
            params.put("version", Constant.VERSION);
        }

        Request<JSONObject> request = new NormalStringRequest(params, url, method,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String key = JSONUtil.getString(response, "key");
                        if (key.equals("1024")||key.equals("1023")) {
                            callback.onUnLogin();
                        } else {
                            callback.onResponse(response);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(callback, error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(Tag);
        mVolleyQueue.add(request);
    }

    /**
     * 普通http请求
     *
     * @param headers  请求头
     * @param params   string型参数
     * @param url
     * @param Tag
     * @param method   get/post
     * @param callback 回调 ，返回json格式数据
     */
    public static void stringRequestData(HashMap<String, String> headers,
                                         HashMap<String, Object> params, String url, int method, String Tag,
                                         final RequestCallBack callback)
    {
        getRequestInstance();
        if (mVolleyQueue == null) {
            return;
        }
        params.put("channel", Constant.ANDROID_CHANNEL);
        params.put("key", Constant.KEY);
        params.put("version", Constant.VERSION);
        Request<JSONObject> request = new NormalStringRequest(headers, params, url, method,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String key = JSONUtil.getString(response, "key");
                        if (key.equals("1024")||key.equals("1023")) {
                            callback.onUnLogin();
                        } else {
                            callback.onResponse(response);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(callback, error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(Tag);
        request.getUrl();
        try {
            request.getHeaders();
        } catch (AuthFailureError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mVolleyQueue.add(request);
    }

    public static void cancelRequest(Object tag) {
        if (mVolleyQueue == null) {
            return;
        }
        LogUtil.LogE("cancelRequest:" + tag);
        mVolleyQueue.cancelAll(tag);
    }

    private static void handleError(RequestCallBack callback,VolleyError error){
        callback.onErrorResponse(getErrorInfo(error));
    }

    /**
     * Volley Json格式参数 POST请求
     * @param requestToken  token
     *         注：是否为必须参数，用于进行未登录处理;
     *                      1.token为必要参数： requestToken==true；params.put("token",token);
     *                      2.token为非必要参数：requestToken =false；token不为空的情况下传
     *                      3.token无需传：requestToken =false；params不传
     * @param params   put所有参数的集合
     * @param url     请求url
     * @param Tag     用于取消网络请求的标识
     * @param callback 回调 ，返回json格式数据
     */
    public static void jsonRequestData(boolean requestToken,JSONObject params, String url, String Tag, final RequestCallBack callback)
    {
        getRequestInstance();
        if (mVolleyQueue == null) {
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String key = JSONUtil.getString(response, "key");
                        if (key.equals("1024")||key.equals("1023")||key.equals("1025")) {
                            callback.onUnLogin();
                        } else {
                            callback.onResponse(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(callback, error);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(Tag);
        mVolleyQueue.add(request);
    }

    /**
     * 获取网络请求的异常信息
     *
     * @param error
     * @return
     */
    private static String getErrorInfo(VolleyError error) {
        String errorInfo = error.toString();
        if (error instanceof NetworkError) {
          errorInfo = "网络错误，请检查网络设置!";
        } else if (error instanceof ClientError) {
            errorInfo = "服务器异常,请稍后再试!";
        } else if (error instanceof ServerError) {
            errorInfo = "服务无响应!";
        } else if (error instanceof AuthFailureError) {

        } else if (error instanceof ParseError) {

        } else if (error instanceof NoConnectionError) {
            errorInfo = "无连接!";
        } else if (error instanceof TimeoutError) {
            errorInfo = "连接超时!";
        }
        return errorInfo;
    }


    /**
     * HttpURLConnection 多文件上传
     * @param requestToken
     * @param actionUrl    请求url
     * @param params       <参数名,参数值>
     * @param uploadFiles  <参数名,文件路径>
     * @param callback
     */
    public static void uploadFiles(boolean requestToken,String actionUrl,HashMap<String,Object> params,HashMap<String,String> uploadFiles,RequestCallBack callback){
        if(requestToken && Utils.isEmpty(params.get("token").toString())){
            callback.onUnLogin();
            return;
        }
//        MultipartUploadUtil multipartUploadUtil = new MultipartUploadUtil(actionUrl,params,
                //uploadFiles);
        try {
           // String response = multipartUploadUtil.sendRequest();
         //   callback.onResponse(new JSONObject(response));
        } catch (Exception e) {
            callback.onErrorResponse("请求失败...请稍后再试");
            e.printStackTrace();
        }

    }

    /**
     * google官方 多文件同时上传
     *
     * @param context
     * @param url      上传路径
     * @param params   其他参数
     * @param  fileParameterName 文件参数名
     * @param files   文件路径集
     */
    public static void uploadMultipart(final Context context, String url,
                                       Map<String, Object> params,String fileParameterName , List<String> files)
    {
        final MultipartUploadRequest request = new MultipartUploadRequest(context, "upload", url);

		/*
         * parameter-name: is the name of the parameter that will contain file's
		 * data. Pass "uploaded_file" if you're using the test PHP script
		 *
		 * custom-file-name.extension: is the file name seen by the server. E.g.
		 * value of $_FILES["uploaded_file"]["name"] of the test PHP script
		 */
        try {
            if (files != null && files.size() > 0) {
                for (String path : files) {
                    request.addFileToUpload(path, fileParameterName, fileParameterName + ".png",
                            null);
                }
            }
        } catch (FileNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
        }
//        try {
//            if (imgPaths.size() > 0) {
//                for (String path : imgPaths) {
//                    request.addFileToUpload(path, "files", "files", null);
//                }
//            }
//        } catch (FileNotFoundException | IllegalArgumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        // You can add your own custom headers
        // request.addHeader("your-custom-header", "your-custom-value");

        // Set<String> set = params.keySet();
        // for (String key : set) {
        // request.addHeader(key, params.get(key));
        // }

        // and parameters
        // request.addParameter("parameter-name", "parameter-value");

        // If you want to add a parameter with multiple values, you can do the
        // following:
        // request.addParameter("array-parameter-name", "value1");
        // request.addParameter("array-parameter-name", "value2");
        if(params!=null){
            Set<String> set = params.keySet();
            for (String key : set) {
                request.addParameter(key, (String) params.get(key));
            }
        }

        // request.addParameter("array-parameter-name", "valueN");

        // or
        // String[] values = new String[] { "value1", "value2", "valueN" };
        // request.addArrayParameter("array-parameter-name", values);

        // or
        // List<String> valuesList = new ArrayList<String>();
        // valuesList.add("value1");
        // valuesList.add("value2");
        // valuesList.add("valueN");
        // request.addArrayParameter("array-parameter-name", valuesList);

        // configure the notification
        // the last two boolean parameters sets:
        // - whether or not to auto clear the notification after a successful
        // upload
        // - whether or not to clear the notification after the user taps on it
        // request.setNotificationConfig(android.R.drawable.ic_menu_upload,
        // "notification title",
        // "upload in progress text",
        // "upload completed successfully text",
        // "upload error text",
        // false, true);
        // request.setNotificationConfig(null);

        // set a custom user agent string for the upload request
        // if you comment the following line, the system default user-agent will
        // be used
        // request.setCustomUserAgent("UploadServiceDemo/1.0");

        // set the intent to perform when the user taps on the upload
        // notification.
        // currently tested only with intents that launches an activity
        // if you comment this line, no action will be performed when the user
        // taps
        // on the notification
        // request.setNotificationClickIntent(new Intent(context,
        // YourActivity.class));

        // set the maximum number of automatic upload retries on error
        request.setMaxRetries(2);

        try {
            // Start upload service and display the notification
            request.startUpload();

        } catch (Exception exc) {
            // You will end up here only if you pass an incomplete upload
            // request
            Log.e("AndroidUploadService", exc.getLocalizedMessage(), exc);
        }
    }

}
