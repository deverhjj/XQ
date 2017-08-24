package com.biu.modulebase.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.UnLoginActivity;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.Utils;


import org.json.JSONObject;

import java.util.logging.Logger;


public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = BaseActivity.class.getSimpleName();
    protected Toolbar toolbar;
    protected TextView title;
    protected AppBarLayout layout_app_bar;

    protected abstract Fragment getFragment();

    protected abstract String getToolbarTitle();

    protected int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    protected View getFragmentContainer() {
        return findViewById(getFragmentContainerId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActView();
    }

    protected void setActView(){
        setContentView(R.layout.activity_base);
        // setStatusBar();
        init();
    }

    /**
     * 1.Android5.0以上：material design风格，半透明(APP 的内容不被上拉到状态
     * 2.Android4.4(kitkat)以上至5.0：全透明(APP 的内容不被上拉到状态)
     * 3.Android4.4(kitkat)以下:不占据status bar
     *
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void init() {
        setTitle(null);
        layout_app_bar = (AppBarLayout) findViewById(R.id.layout_app_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(getToolbarTitle());
        FragmentManager fm = getSupportFragmentManager();
        Fragment content = fm.findFragmentById(R.id.fragmentContainer);
        if (content == null) {
            content = getFragment();
            if (content != null) {
                fm.beginTransaction().add(getFragmentContainerId(), content).commit();
            }
            Log.e(TAG, "Create new Fragment");
        } else {
            Log.e(TAG, "restore Fragment");
        }
    }

    /**
     * json Post网络请求  后期增加网络连接判断
     * 统一处理loading、获取数据失败、请求异常等错误等逻辑
     */
    public void jsonRequest(boolean requestToken, JSONObject params, String url, final String tag, final
    ContextRequestCallBack callback){
        Communications.jsonRequestData(requestToken, params, url, tag, new RequestCallBack() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.LogE(tag,"onResponse=>"+ JSONUtil.prettyPrintJsonString(2,response));
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
                //统一showUnLoginSnackbar
                showUnLoginSnackbar();
            }
        });
    }

    /**
     *
     * @param layoutId
     */
    public View setToolBarCustomView(int layoutId) {
        setBackNavigationIcon();
        View view=getLayoutInflater().inflate(layoutId, null, false);
        ActionBar toolbar=getSupportActionBar();
        if (toolbar!=null) {
            Toolbar.LayoutParams layoutParams=new Toolbar.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity= Gravity.CENTER;
            toolbar.setCustomView(view,layoutParams);
            toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            toolbar.setDisplayShowCustomEnabled(true);
            toolbar.setDisplayHomeAsUpEnabled(true);
        }
        return view;
    }

    public View setToolBarCustomView(int layoutId, Toolbar.LayoutParams layoutParams) {
        setBackNavigationIcon();
        View view=getLayoutInflater().inflate(layoutId, null, false);
        ActionBar toolbar=getSupportActionBar();
        if (toolbar!=null) {
            layoutParams.gravity= Gravity.CENTER;
            toolbar.setCustomView(view,layoutParams);
            toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            toolbar.setDisplayShowCustomEnabled(true);
            toolbar.setDisplayHomeAsUpEnabled(true);
        }
        return view;
    }

    public Toolbar getToolbar() {
        if(toolbar ==null){
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        return toolbar;
    }

    /**
     * 设置标题
     * @param titleString
     */
    public void setToolBarTitle(String titleString) {
        if (title == null)
            title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(titleString);
    }

    public void setBackNavigationIcon(int... icon) {
        getToolbar();
//        toolbar.setNavigationIcon(icon != null && icon.length > 0 ? icon[0]
//                : R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationIcon(icon != null && icon.length > 0 ? icon[0]
                : R.mipmap.ico_fanhui_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Toast
     *
     * @param msg
     * @param time LENGTH_SHORT = 0;/ LENGTH_LONG = 1;
     */
    public void showTost(String msg, int time) {
        Toast.makeText(BaseActivity.this, msg, time).show();
    }

    /**
     * 软盘控制
     * 显示或隐藏软键盘
     */
    public void showSoftKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeybord(){
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputManager.isActive()&& getCurrentFocus()!=null){
            inputManager.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(),0);
        }
    }


//    /**
//     * 显示progress loading
//     *    进行操作需请求网络时显示  如 登录 注册...
//     *    @param  requestTag  网络请求tag，用于取消请求
//     */
//    public void showProgress(String requestTag){
//        LoadingDialogFragment loading =LoadingDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,R.layout.loading,requestTag);
//        loading.show(getSupportFragmentManager(), "loading");
//
//    }
//
//    /**
//     *隐藏progress loading
//     *
//     */
//    public void dismissProgerss(){
//        LoadingDialogFragment laoding = (LoadingDialogFragment) getSupportFragmentManager().findFragmentByTag("loading");
//        if(laoding!=null)
//            laoding.dismiss();
//    }

    public void showUnLoginSnackbar(){
        hideSoftKeybord();
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), "未登录，请先登录", Snackbar.LENGTH_SHORT).setAction("去登录",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(BaseActivity.this, UnLoginActivity.class));
                    }
                });
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.white));
        snackbar.show();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//
//            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
//            View v = getCurrentFocus();
//
//            if (isShouldHideInput(v, ev)) {
//                hideSoftKeybord();
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
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
