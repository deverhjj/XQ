package com.binfenjiari.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;

import com.binfenjiari.R;
import com.binfenjiari.base.AppActivity;
import com.binfenjiari.base.AppMvpActivity;
import com.binfenjiari.base.BaseContract;
import com.binfenjiari.base.MvpFragment;
import com.binfenjiari.fragment.AvVideoDetailFragment;
import com.binfenjiari.fragment.CommentFragment;
import com.binfenjiari.fragment.CommonSearchFragment;
import com.binfenjiari.utils.Res;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AvVideoDetailActivity extends AppMvpActivity {
    @NonNull
    @Override
    public MvpFragment mvpView() {
        AvVideoDetailFragment v = new AvVideoDetailFragment();
        v.setArguments(getIntent().getExtras());
        return v;
    }

    @NonNull
    @Override
    public BaseContract.BaseIPresenter mvpPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.acti_av;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        //second setup
        setBackNaviAction(R.mipmap.ico_fanhui_back);
        Res.setStatusBarColor(this, Color.TRANSPARENT);
    }

}
