package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.WindowManager;

import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.AvVideoLandFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/6/20.
 */
public class AvVideoLandActivity extends BaseActivity {
    private static final String TAG = "AvVideoLandActivity";
    private Callback mCallback;
    @Override
    protected Fragment getFragment() {
        Intent intent = getIntent();
        int videoPositionInList = intent != null ? intent.getIntExtra(Constant.KEY_POSITION, 0) : 0;
        String url = intent != null ? intent.getStringExtra(Constant.KEY_VIDEO_URL) : null;
        int preSeekToPos = intent != null ? intent.getIntExtra(
                Constant.KEY_VIDEO_PRE_SEEK_TO_POSITION, 0) : 0;
        return AvVideoLandFragment.newInstance(videoPositionInList,url, preSeekToPos);
    }

    @Override
    protected String getToolbarTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null) {
            getSupportActionBar().hide();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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
        if (mCallback != null) {
            mCallback.onBackPressed();
        }
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
        void onBackPressed();
    }
}
