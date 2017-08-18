package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;

import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.AvVideoDetailFragment;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/4/27.
 */
public class AvVideoDetailActivity extends BaseActivity {
    private static final String TAG = "AvVideoDetailActivity";
    private Callback mCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        Intent intent = getIntent();
        int position =intent != null ? intent.getIntExtra(Constant.KEY_POSITION,-1) : -1;
        String id = intent != null ? intent.getStringExtra(Constant.KEY_ID) : "-1";
        String url=intent!=null?intent.getStringExtra(Constant.KEY_VIDEO_URL):null;
        int prePlayPosition=intent!=null?intent.getIntExtra(Constant
                .KEY_VIDEO_PRE_SEEK_TO_POSITION,0):0;
        return AvVideoDetailFragment.newInstance(position,id,url,prePlayPosition);
    }

    @Override
    protected String getToolbarTitle() {
        return "详情";
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        LogUtil.LogE(TAG,"onConfigurationChanged");

        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                LogUtil.LogE(TAG, "ORIENTATION_LANDSCAPE");

                break;
            case Configuration.ORIENTATION_PORTRAIT:
                LogUtil.LogE(TAG, "ORIENTATION_PORTRAIT");

                break;
        }

    }

    public void toggleSystemUi() {

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();

        //level >= 14 隐藏系统导航栏
        uiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        if (Build.VERSION.SDK_INT >= 16) {
            uiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        //level >= 19 沉浸模式
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }


    @Override
    public void onBackPressed() {
        boolean handled=false;
        if (mCallback != null) {
            handled=mCallback.onBackPressed();
        }
        if (handled) return;
        super.onBackPressed();
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof Callback) {
            mCallback = (Callback) fragment;
        }
    }

    public interface Callback {
        /**
         * @return 是否拦截托管 Activity onBackPressed 时的默认销毁行为
         */
        boolean onBackPressed();
    }

}
