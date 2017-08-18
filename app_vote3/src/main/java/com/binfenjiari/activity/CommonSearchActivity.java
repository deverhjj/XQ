package com.binfenjiari.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.binfenjiari.R;
import com.binfenjiari.base.AppMvpActivity;
import com.binfenjiari.base.BaseContract;
import com.binfenjiari.base.MvpFragment;
import com.binfenjiari.fragment.CommonSearchFragment;
import com.binfenjiari.utils.Res;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/16
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class CommonSearchActivity extends AppMvpActivity {
    @NonNull
    @Override
    public MvpFragment mvpView() {
        CommonSearchFragment v = new CommonSearchFragment();
        v.setArguments(getIntent().getExtras());
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
        // must first
        setCustomViewForToolbar(R.layout.toolbar_search);
        //second setup
        setBackNaviAction(R.mipmap.ico_fanhui_back);

    }

}
