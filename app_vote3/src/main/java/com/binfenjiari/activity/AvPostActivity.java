package com.binfenjiari.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;

import com.binfenjiari.R;
import com.binfenjiari.base.AppMvpActivity;
import com.binfenjiari.base.BaseContract;
import com.binfenjiari.base.MvpFragment;
import com.binfenjiari.fragment.AvPostFragment;
import com.binfenjiari.utils.Res;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AvPostActivity extends AppMvpActivity {

    @NonNull
    @Override
    public MvpFragment mvpView() {
        AvPostFragment v = new AvPostFragment();
        return v;
    }

    @NonNull
    @Override
    public BaseContract.BaseIPresenter mvpPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //second setup
        setBackNaviAction(R.mipmap.ico_fanhui_back);
        setTitle("发布视听");
    }
}
