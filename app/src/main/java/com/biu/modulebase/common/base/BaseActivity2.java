package com.biu.modulebase.common.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.UnLoginActivity;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.LoadingDialogFragment;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.NetworkUtil;
import com.biu.modulebase.binfenjiari.util.Utils;

import org.json.JSONObject;

/**
 * 不繼承BaseActivity 只統一流程和公共方法
 *
 */
public abstract class BaseActivity2 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BaseActivity2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStatusBar();
    }

    public void hideNavBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            int uiOptions =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                uiOptions = uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void showNavBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            int uiOptions =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }




    /**
     * 1.Android5.0以上：material design风格，半透明(APP 的内容不被上拉到状态
     * 2.Android4.4(kitkat)以上至5.0：全透明(APP 的内容不被上拉到状态)
     * 3.Android4.4(kitkat)以下:不占据status bar
     *
     */
    protected void setStatusBar(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Toast
     *
     * @param msg
     * @param time LENGTH_SHORT = 0;/ LENGTH_LONG = 1;
     */
    public void showTost(String msg, int time) {
        Toast.makeText(BaseActivity2.this, msg, time).show();
    }

    /**
     * 软盘控制
     * 显示或隐藏软键盘
     */
    public void showSoftKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showSetNetworkSnackbar() {
        Snackbar snackbar = Snackbar.make(this.getWindow().getDecorView(), "当前无网络", Snackbar.LENGTH_SHORT).setAction("去设置",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setNetwork();
                    }
                });
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.white));
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
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

    public void showUnLoginSnackbar() {
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "未登录，请先登录", Snackbar
                .LENGTH_SHORT).setAction
                ("去登录",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(BaseActivity2.this, UnLoginActivity.class));
                    }
                });
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.white));
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * json Post网络请求
     * 统一处理 网络连接判断、未登录等操作
     */
    public void jsonRequest(boolean requestToken, JSONObject params, String url, final String tag, final ContextRequestCallBack callback){
        LogUtil.LogE(tag,"param:"+params.toString());
        if(!NetworkUtil.isNetworkConnected(this)){
            showSetNetworkSnackbar();
            return;
        }
        if (requestToken && TextUtils.isEmpty(JSONUtil.getString(params, "token"))) {
            showUnLoginSnackbar();
            return;
        }
        Communications.jsonRequestData(requestToken, params, url, tag, new RequestCallBack() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.LogE(tag,"onResponse=>"+JSONUtil.prettyPrintJsonString(2,response));
                int key = JSONUtil.getInt(response, "key");

                switch (key) {
                    case 1://有数据或者请求成功
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
                //统一showUnLoginSnackbar
                showUnLoginSnackbar();
            }
        });
    }

    /**
     * 显示progress loading
     *    进行操作需请求网络时显示  如 登录 注册...
     *    @param  requestTag  网络请求tag，用于取消请求
     */
    public void showProgress(String requestTag) {
        FragmentActivity activity = BaseActivity2.this;
        LoadingDialogFragment loading =LoadingDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,R.layout.loading,requestTag);
        loading.show(activity.getSupportFragmentManager(), "loading");
    }

    /**
     *隐藏progress loading
     *
     */
    public void dismissProgress() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    LoadingDialogFragment loading = (LoadingDialogFragment) getSupportFragmentManager().findFragmentByTag("loading");
                    if (loading != null) {
                        loading.dismiss();
                    }
                }
        },500);

    }


    /**
     * 隐藏软键盘
     */
    public void hideSoftKeybord(){
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputManager.isActive()&& getCurrentFocus()!=null){
            inputManager.hideSoftInputFromWindow(BaseActivity2.this.getCurrentFocus().getWindowToken(),0);
        }
    }


//    public void showUnLoginSnackbar() {
//        Snackbar.make(getView(), "未登录，请先登录", Snackbar.LENGTH_SHORT).setAction
//                ("去登录",
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                startActivity(new Intent(this, UnLoginActivity.class));
//                            }
//                        }).show();
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftKeybord();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }


    @Override
    public void onClick(View v) {

    }




}
