package com.biu.modulebase.binfenjiari.datastructs;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import android.support.multidex.MultiDex;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.biu.modulebase.binfenjiari.BuildConfig;
import com.biu.modulebase.binfenjiari.model.SensitiveWordVO;
import com.biu.modulebase.binfenjiari.model.UpdateVO;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.binfenjiari.util.CrashHandler;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Lee on 2015/12/14.
 */
public class MyApplication extends Application {
    private ArrayList<Activity> activities = new ArrayList<Activity>();
    /** MyApplication实例 **/
    private static MyApplication mInstance;

    public static boolean isInMobileConnectPlayVideo=false;

    private ImageLoader mImgLoader;
    /**volley**/
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    /**Umeng ShareAPI**/
    private static UMShareAPI mShareAPI;
    public static PushAgent mPushAgent;
    /**
     * Umeng 获取的token
     **/
    public static String umengToken = "";

    /** 运行检查更新标志位，如果用户选择稍后更新，则在下次启动应用再检测更新 **/
    public static boolean allow_update = true;

    public static boolean needRefreshUserInfo =false;

    public static UpdateVO updateVO;

    public static UserInfoBean userInfo;

    private static List<SensitiveWordVO> sensitiveWordList;

    public static String deviceToken ;

    /**
     * 界面是否需要刷新 map
     * 如果某个界面需要根据登录状态更改 视图UI ，那么该界面控制器需要注册下供该界面恢复时提供是否刷新判断
     */
    public static Map<String,Boolean> sRefreshMap=new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        mInstance = this;
        if (!BuildConfig.LOG_DEBUG) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            // 注册crashHandler
            crashHandler.init(getApplicationContext());
        }
        initUmengPlatform();
        initUmeng();
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static UserInfoBean getUserInfo(Context context) {
        if (userInfo == null) {
            String userInfoJsonString = PreferencesUtils.getString(context,
                    PreferencesUtils.KEY_USER_INFO);
            userInfo = JSONUtil.getGson().fromJson(userInfoJsonString, UserInfoBean.class);
        }
        return userInfo;
    }

    public static List<SensitiveWordVO> getSensitiveList(Context context) {
        String sensitiveJson = PreferencesUtils.getString(context,
                PreferencesUtils.KEY_SENSITIVE_STRING);
        sensitiveWordList =JSONUtil.fromJson(sensitiveJson,new TypeToken<List<SensitiveWordVO>>(){}.getType());
        return sensitiveWordList;
    }

    private void initUmeng() {
        mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                MyApplication.deviceToken =deviceToken;
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        mPushAgent.onAppStart();
        mPushAgent.setDebugMode(BuildConfig.LOG_DEBUG);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (msg.extra != null) {
                            HashMap<String, String> map = (HashMap<String, String>) msg.extra;
                            String myType = map.get("myType");

                        }
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            /**
             * 参考集成文档的1.6.4
             * http://dev.umeng.com/push/android/integration#1_6_4
             * */
            @Override
            public Notification getNotification(Context context, UMessage msg) {
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(mInstance);
//                builder.setContentTitle(msg.title).setContentText(msg.text).setTicker(msg.ticker).setAutoCancel(true);
                UserInfoBean userInfoVO = MyApplication.getUserInfo(context);
                userInfoVO.setHasMessage("1");
                PreferencesUtils.putString(context, PreferencesUtils.KEY_USER_INFO,JSONUtil.toJson(userInfoVO).toString());
                if (msg.extra != null) {

                }
//                NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(mInstance);
////                notification.setSmallIcon(R.drawable.logo);
//                builder.setContentTitle(msg.title);
//                builder.setContentText(msg.text);
//                builder.setOngoing(true);
//                builder.setAutoCancel(true);	    //点击自动消息
//                builder.setDefaults(Notification.DEFAULT_ALL);	        //铃声,振动,呼吸灯
//                Intent intent = new Intent(context, MainVisitActivity.class);    //点击通知进入的界面
//                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//                builder.setContentIntent(contentIntent);
////                builder.notify(0, builder.build());
//                notifyManager.notify("textid", 123, builder.build());
//                return builder.build();
                return super.getNotification(context, msg);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                if (msg.extra != null) {
                    HashMap<String, String> map = (HashMap<String, String>) msg.extra;
                    String myType = map.get("myType");
                    //...
                }
            }
        };
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

    }

    public static void enablePush(){
        if(PreferencesUtils.putBoolean(mInstance, "allow_push", true)){
            mPushAgent.enable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                    LogUtil.LogD("SUCCESS:"+mPushAgent.getRegistrationId());
                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });

        }else{
            mPushAgent.disable(new IUmengCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });
        }
    }


    protected  void initUmengPlatform(){
        //微信 appid appsecret
        PlatformConfig.setWeixin(Constant.WEIXIN_AppID, Constant.WEIXIN_AppSecret);
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo(Constant.SINA_AppID, Constant.SINA_AppKEY);
        // qq qzone appid appkey
        PlatformConfig.setQQZone(Constant.QQ_AppID, Constant.QQ_AppKEY);

    }

    /**
     * 获取UMShareAPI对象
     * @return
     */
    public static UMShareAPI getUMShareAPI(){
        if(mShareAPI ==null){
            LogUtil.LogI("【初始创建】");
            mShareAPI  = UMShareAPI.get(getInstance());
        }
        return mShareAPI;
    }

    /**
     * 获取网络请求对象
     *
     * @return RequestQueue
     * */

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            LogUtil.LogI("【初始创建】");
            mRequestQueue = Volley.newRequestQueue(mInstance);
        }
        LogUtil.LogI("【获取request】");
        return mRequestQueue;
    }

    /**
     * 取消所有网络请求
     *
     */
    public static void cancleAllRequest() {
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(mInstance);
    }

    public ImageLoader getImgLoader() {
        if (mImgLoader == null) {
            LogUtil.LogI("【ImageLoader创建】");
            mImgLoader = ImageLoader.getInstance();
            mImgLoader.init(initImgloadConf());
        }
        return mImgLoader;
    }

    /**
     * ImageLoader Configuration (ImageLoaderConfiguration) is global for
     * application
     *
     * @return ImageLoaderConfiguration
     */
    private ImageLoaderConfiguration initImgloadConf() {
        LogUtil.LogI("【ImageLoader初始化】");
        File cacheDir = StorageUtils.getOwnCacheDirectory(
                getApplicationContext(), Constant.IMAGE_LOADER_CACHE_PATH);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(1024, 800)
                        // max width, max height，即保存的每个缓存文件的最大长宽
                        // .diskCacheExtraOptions(480, 800,null) // Can slow
                        // ImageLoader, use it carefully (Better don't use
                        // it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)
                        // 线程池内加载的数量,最好是1-5
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()

                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                        // You can pass your own memory cache
                        // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(20)
                        //
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                        // 缓存的文件数量
                .diskCache(new UnlimitedDiscCache(cacheDir))
                        // 自定义缓存路径

                        // 将缓存下来的文件以什么方式命名
                        // 里面可以调用的方法有
                        // 1.new Md5FileNameGenerator() //使用MD5对UIL进行加密命名
                        // 2.new HashCodeFileNameGenerator()//使用HASHCODE对UIL进行加密命名
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)

                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(
                        new BaseImageDownloader(this, 20 * 1000, 30 * 1000)) // connectTimeout
                        // (20
                        // s),
                        // readTimeout
                        // (30
                        // s)超时时间
                .imageDecoder(new BaseImageDecoder(false)) // default
                .writeDebugLogs() // Remove for release app
                .build();// 开始构建

        return config;

    }

    /**
     * 将当前的Activity加入到list中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 移除activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * finish所有List集合中的Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activities) {
            if (activity != null && !activity.isFinishing())
                activity.finish();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        finishAllActivity();
        System.exit(0);
    }

    public static UpdateVO getUpdateVO() {
        return updateVO;
    }

    public static void setUpdateVO(UpdateVO updateVO) {
        mInstance.updateVO = updateVO;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
