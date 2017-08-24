package com.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binfenjiari.R;
import com.binfenjiari.activity.AAMainVote3Activity;
import com.binfenjiari.activity.NavigationActivity;
import com.binfenjiari.activity.UserActivity;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.fragment.contract.UserContract;
import com.binfenjiari.fragment.presenter.UserPresenter;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Logger;
import com.binfenjiari.utils.Msgs;
import com.binfenjiari.utils.Uis;
import com.binfenjiari.utils.Views;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/22
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class UserRegisterDoneFragment extends UserFragment {
    private static final String TAG = UserRegisterDoneFragment.class.getSimpleName();
    Bundle mArgs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = getArguments();
    }

    @Override
    public View onGenerateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_register_success, container, false);
    }

    @Override
    public void onInitView(View root) {
        Views.find(root, R.id.login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
        }
    }

    private void login() {
        presenter.login(mArgs);
    }

    @Override
    public void onLoginSuccess() {
        Msgs.shortToast(getContext(), "登录成功");
        Intent home = new Intent(getContext(), NavigationActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }
}
