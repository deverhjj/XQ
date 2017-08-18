package com.biu.modulebase.common.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.PhotoViewActivity;
import com.biu.modulebase.binfenjiari.activity.SearchActivity;
import com.biu.modulebase.binfenjiari.activity.UnLoginActivity;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack2;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.LoadingDialogFragment;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.NetworkUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * @author Lee
 * @Title: {Fragment 基类}
 * @Description:{描述}
 * @date 2016/1/25
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private static final java.lang.String TAG = "BaseFragment";
    protected BaseActivity mBaseActivity;

    private ViewGroup mContainert;
    private ViewGroup loading_layout;
    private ViewGroup no_data_layout;
    private FrameLayout no_net_layout;
    private Button noNetWorkBtn;
    private AnimationDrawable frameAnimation;
    public LocationClient mLocationClient = null;

    /**
     * 设置OptionMenu
     */
    protected void setOptionMenu(boolean hasOption,int menuId) {
    }

    /**
     * 初始化控件 绑定mvp
     */
    protected void initMvpBinder(View rootView){}

    /**
     * 初始化控件
     */
    protected abstract void initView(View rootView);
    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    public abstract void loadData();
    /**
     * 获取intent中的extra
     */
    protected  void getIntentData(){}
    /**
     * 设置监听事件
     */
    protected void setListener(){}

    /**
     * 重新加载数据(子 Fragment 加载数据失败可覆盖该方法通过点击 NoDataLayout 任意位置进行重新加载数据)
     */
    protected void reLoadData() {}

    protected ViewGroup getContainer(View rootView){return null;}
//    /**
//     * 返回 RecyclerView 具体的 Adapter，BaseAdapter、ExpandableRecyclerViewAdapter 等等
//     * @return
//     */
//    public RecyclerView.Adapter getAdapter(){return null;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup cont=getContainer(container);
        View rootView = inflater.inflate(R.layout.fragment_base_frame_load, cont!=null?cont:container,
            true);
        loading_layout = (ViewGroup) rootView.findViewById(R.id.loading_layout);
        no_data_layout = (ViewGroup) rootView.findViewById(R.id.no_data_layout);
        no_net_layout =(FrameLayout)rootView.findViewById(R.id.no_net_layout);
        noNetWorkBtn = (Button) rootView.findViewById(R.id.btn);
        noNetWorkBtn.setOnClickListener(this);
        no_data_layout.setOnClickListener(this);
        ImageView loadingImg = (ImageView) rootView.findViewById(R.id.loading);
        if (loadingImg != null) {
            frameAnimation = (AnimationDrawable) loadingImg.getDrawable();
        }
        return cont != null ? container : rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity=getActivity();
        if (activity instanceof BaseActivity) {
            mBaseActivity = (BaseActivity) activity;
        }
        initMvpBinder(getView());
        initLocation();
        getIntentData();
        initView(getView());
        setListener();
        loadData();
    }

    private void initLocation(){
        mLocationClient = new LocationClient(getActivity().getApplicationContext());     //声明LocationClient类
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);

    }

    public void startLocation(BDLocationListener myListener){
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();

    }

    public void stopLocation(){
        mLocationClient.stop();
    }

    /**
     * json Post网络请求
     * 统一处理 网络连接判断、未登录等操作
     */
    public void jsonRequest(boolean requestToken,JSONObject params, String url, final String tag, final
    ContextRequestCallBack callback){
        LogUtil.LogE(tag,"param:"+params.toString());
        if(!NetworkUtil.isNetworkConnected(getActivity())){
            visibleNoNetWork();
            showSetNetworkSnackbar();
            return;
        }
        if (requestToken && TextUtils.isEmpty(JSONUtil.getString(params, "token"))) {
            dismissProgress();
            showUnLoginSnackbar();
            return;
        }
        Communications.jsonRequestData(requestToken, params, url, tag, new RequestCallBack() {
            @Override
            public void onResponse(JSONObject response) {
                inVisibleLoading();
                LogUtil.LogE(tag,"onResponse=>"+JSONUtil.prettyPrintJsonString(2,response));
                int key = JSONUtil.getInt(response, "key");

                switch (key) {
                    case 1://有数据或者请求成功
                        inVisibleNoData();
                        String jsonString = JSONUtil.getString(response, "result");
                        callback.onSuccess(jsonString);
                        break;
                    default:
                        String msg =JSONUtil.getString(response,"message");
                        callback.onCodeError(key, Utils.getString(msg));
                        break;

                }
            }

            @Override
            public void onErrorResponse(String errorInfo) {
                LogUtil.LogE(tag,"onErrorResponse==>"+errorInfo);
                //统一showTost
                showTost(errorInfo,1);
                callback.onConnectError(errorInfo);
            }

            @Override
            public void onUnLogin() {
                LogUtil.LogE(tag,"onUnLogin");
                dismissProgress();
                clearSharedPreferences();
                //统一showUnLoginSnackbar
                showUnLoginSnackbar();
            }
        });
    }

    private void clearSharedPreferences(){
        if(MyApplication.userInfo!=null)
            MyApplication.userInfo=null;
        PreferencesUtils.clear(getActivity());
    }

    public void cancelRequest(Object tag) {
        Communications.cancelRequest(tag);
    }


    public void dataRequest(boolean requestToken,JSONObject params, String url, final String
            tag, final RequestCallBack2 callback) {
        LogUtil.LogE(tag,"param:"+params.toString());
        if(!NetworkUtil.isNetworkConnected(getActivity())){
            visibleNoNetWork();
            showSetNetworkSnackbar();
            return;
        }
        if (requestToken && TextUtils.isEmpty(JSONUtil.getString(params, "token"))) {
            LogUtil.LogE(tag,"dataRequest/// unLogin");
            showUnLoginSnackbar();
            return;
        }
        callback.requestBefore();
        Communications.jsonRequestData(requestToken, params, url, tag, new RequestCallBack() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.LogE(tag,"onResponse=>"+JSONUtil.prettyPrintJsonString(1,response));

                int key = JSONUtil.getInt(response, "key");
                if (key==1) {//有数据或者请求成功
                    String mainJsonString = JSONUtil.getString(response, "result");
                    JSONObject mainJsonObject=JSONUtil.getJSONObject(response,"result");
                    callback.onSuccess(mainJsonString,mainJsonObject,response);
                } else {
                    String msg =JSONUtil.getString(response,"message");
                    callback.onFail(key,msg);
                }
                callback.requestAfter();
            }

            @Override
            public void onErrorResponse(String errorInfo) {
                LogUtil.LogE(tag,"onErrorResponse==>"+errorInfo);

                showTost(errorInfo,1);

                callback.onFail(RequestCallBack2.KEY_FAIL,errorInfo);

                callback.requestAfter();

            }

            @Override
            public void onUnLogin() {
                LogUtil.LogE(tag,"onUnLogin");
                callback.requestAfter();
                showUnLoginSnackbar();
            }
        });
    }



    /**
     * 跳轉搜索界面的Intent
     * @param tag
     *      Constant.SEARCH_EVENT or Constant.SEARCH_JIDI or Constant.SEARCH_AUDIO or Constant.SEARCH_VOTE
     *
     */
    public void startSearchIntent(String tag){
        Intent intent =new Intent(getActivity(), SearchActivity.class);
        intent.putExtra(Constant.SEARCH_TAG,tag);
        startActivity(intent);
        getActivity().overridePendingTransition(0, R.anim.fade_in);
    }

    /**
     * 跳轉图片预览界面的Intent
     * @param position  点击图片在图片集中的位置
     * @param imgList   图片集
     */
    public void startPhotoViewIntent(int position, ArrayList<String> imgList){
        Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
        intent.putExtra("position",position);
        intent.putStringArrayListExtra("imgs",imgList);
        startActivity(intent);
    }

    /**
     * Toast
     *
     * @param msg
     * @param time LENGTH_SHORT = 0;/ LENGTH_LONG = 1;
     */
    public void showTost(String msg, int time) {
        if(getActivity()!=null)
            Toast.makeText(getActivity(), msg, time).show();
    }


    /**
     * 显示loading
     *      初始化界面时首先显示的元素
     */
    public void visibleLoading() {
        inVisibleNoData();
        inVisibleNoNetWork();
        if (!loading_layout.isShown()) {
            LogUtil.LogE(TAG,"visibleLoading");
            loading_layout.setVisibility(View.VISIBLE);
            // Start the animation (looped playback by default).
            if (frameAnimation != null) {
                frameAnimation.start();
            }
        }
    }

    /**
     * 隐藏loading
     */
    public void inVisibleLoading() {
        LogUtil.LogE(TAG, "inVisibleLoading");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            if (frameAnimation != null && frameAnimation.isRunning()) {
                frameAnimation.stop();
            }
                if(loading_layout!=null)
                    loading_layout.setVisibility(View.GONE);
            }
        },1000);

    }

    /**
     * 显示空数据布局
     */
    public void visibleNoData() {
        inVisibleLoading();
        inVisibleNoNetWork();
        if (!no_data_layout.isShown()) {
            no_data_layout.setVisibility(View.VISIBLE);
        }
    }


    public void visibleNoNetWork(){
        inVisibleLoading();
        inVisibleNoData();
        if(no_net_layout!=null)
            no_net_layout.setVisibility(View.VISIBLE);
    }

    public void inVisibleNoNetWork(){
        if(no_net_layout!=null)
            no_net_layout.setVisibility(View.GONE);
    }

    /**
     * 影藏空数据布局
     */
    public void inVisibleNoData() {
        if(no_data_layout!=null)
            no_data_layout.setVisibility(View.GONE);
    }

    /**
     * 显示progress loading
     *    进行操作需请求网络时显示  如 登录 注册...
     *    @param  requestTag  网络请求tag，用于取消请求
     */
    public void showProgress(String requestTag) {
        FragmentActivity activity=getActivity();
        if (activity!=null) {
            LoadingDialogFragment loading =LoadingDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,R.layout.loading,requestTag);
            loading.show(activity.getSupportFragmentManager(), "loading");
        }
    }

    /**
     *隐藏progress loading
     *
     */
    public void dismissProgress() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            LoadingDialogFragment loading =
                    (LoadingDialogFragment) getActivity().getSupportFragmentManager()
                            .findFragmentByTag("loading");
            if (loading != null) {
                loading.dismiss();
            }
        }
    }

    /**
     * 软盘控制
     * 显示或隐藏软键盘
     */
    public void showSoftKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showSoftKeyboard2(View focusedView) {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(focusedView, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if(inputManager.isActive()&& getActivity().getCurrentFocus()!=null){
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
        }
    }
    public boolean checkIsLogin(){
        if(Utils.isEmpty(PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN))){
            showUnLoginSnackbar();
            return false;
        }
        return true;
    }

    public void showUnLoginSnackbar() {
        Snackbar snackbar = Snackbar.make(
                ((ViewGroup) getActivity().getWindow().getDecorView().getRootView().findViewById(
                        android.R.id.content)).getChildAt(0),
                "未登录，请先登录", Snackbar
                .LENGTH_SHORT)
                .setAction("去登录",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isAdded())
                            startActivity(new Intent(getActivity(), UnLoginActivity.class));
                    }
                });
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.white));
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showSetNetworkSnackbar() {
        Snackbar snackbar = Snackbar.make( ((ViewGroup) getActivity().getWindow().getDecorView().getRootView().findViewById(
                android.R.id.content)).getChildAt(0), "当前无网络", Snackbar.LENGTH_SHORT).setAction("去设置",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setNetwork();
                    }
               });
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.white));
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void setNetwork(){
        Intent intent = null;
        /**
         * 判断手机系统的版本！如果API大于10 就是3.0+
         * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
         */
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(Settings.ACTION_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName(
                    "com.android.settings","com.android.settings.Settings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        startActivity(intent);
    }


    public void hideNavBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getActivity().getWindow().getDecorView();
            int uiOptions =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                uiOptions = uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void showNavBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getActivity().getWindow().getDecorView();
            int uiOptions =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 是否有最新消息
     * @return
     */
    public boolean hasMessage(){
        UserInfoBean userInfoVO = MyApplication.getUserInfo(getActivity());
        if(userInfoVO!=null){
            String hasMessage = userInfoVO.getHasMessage();
            if(hasMessage!=null){
                return hasMessage.equals("1")?true:false;
            }
        }
        return false;
    }

    public BaseActivity getBaseActivity() {
        if (mBaseActivity == null) {
            Activity activity = getActivity();
            if (activity instanceof BaseActivity) {
                mBaseActivity = (BaseActivity) activity;
            }
        }
        return mBaseActivity;
    }

    @Override
    public void onClick(View v) {
        LogUtil.LogE(TAG, "************************");
        int id=v.getId();
        int i = v.getId();
        if (i == R.id.no_data_layout) {
            visibleLoading();
            loadData();

        } else if (i == R.id.btn) {
            visibleLoading();
            loadData();

        }
    }

}