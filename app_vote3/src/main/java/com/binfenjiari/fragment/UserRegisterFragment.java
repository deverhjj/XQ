package com.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.activity.UserActivity;
import com.binfenjiari.base.AppExp;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.fragment.contract.UserContract;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Logger;
import com.binfenjiari.utils.Msgs;
import com.binfenjiari.utils.Uis;
import com.binfenjiari.utils.Views;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class UserRegisterFragment extends UserFragment {
    private static final String TAG = UserRegisterFragment.class.getSimpleName();
    private TextView mAccount;
    private TextView mPwd;
    private TextView mVCode;
    private TextView mSendCode;

    private Disposable mUiTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onGenerateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_login_register, container, false);
    }

    @Override
    public void onInitView(View root) {
        mAccount = Views.find(root, R.id.register_phone);
        mPwd = Views.find(root, R.id.register_pwd);
        mVCode = Views.find(root, R.id.verification);
        mSendCode = Views.find(root, R.id.send_verification);

        Views.find(root, R.id.register_btn).setOnClickListener(this);
        mSendCode.setOnClickListener(this);
        Views.find(root, R.id.policy).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.register_btn:
                register();
                break;

            case R.id.send_verification:
                getVCode();
                break;

            case R.id.policy:

                break;
        }
    }

    private void getVCode() {
        if (!mSendCode.isEnabled()) {
            Msgs.shortToast(getContext(), "一分钟内发送一次");
            return;
        }

        final String account = mAccount.getText().toString();
        final boolean validAccount = !TextUtils.isEmpty(account);
        if (!validAccount) {
            Msgs.shortToast(getContext(), "请输入账号");
            return;
        }

        Bundle args = new Bundle();
        args.putString(Constants.KEY_ACCOUNT, account);
        args.putString(Constants.KEY_TYPE, "10");
        presenter.getCode(args);

        //自动隐藏软键盘
        Uis.hideSoftInput(mPwd);
    }

    private void lockCodeUi() {
        mSendCode.setEnabled(false);
        mUiTask = Observable.interval(0, 1, TimeUnit.SECONDS)
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .take(60)
                            .map(new Function<Long, Integer>() {
                                @Override
                                public Integer apply(@NonNull Long aLong) throws Exception
                                {
                                    return 59 - aLong.intValue();
                                }
                            })
                            .subscribeWith(new DisposableObserver<Integer>() {
                                @Override
                                public void onNext(Integer integer) {
                                    String hint = integer + " 秒";
                                    mSendCode.setText(hint);
                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onComplete() {
                                    resetCodeUi();
                                }
                            });
        addUiTask(mUiTask);
    }

    private void register() {
        final String account = mAccount.getText().toString();
        final String pwd = mPwd.getText().toString();
        final String vCode = mVCode.getText().toString();
        final boolean validAccount = !TextUtils.isEmpty(account);
        final boolean validPwd = !TextUtils.isEmpty(pwd);
        final boolean validVCode = !TextUtils.isEmpty(vCode);

        if (!validAccount || !validPwd || !validVCode) {
            if (!validAccount) {
                Msgs.shortToast(getContext(), "请输入账号");
            } else if (!validVCode) {
                Msgs.shortToast(getContext(), "请输入验证码");
            } else {
                Msgs.shortToast(getContext(), "请输入密码");
            }
            return;
        }

        Bundle args = new Bundle();
        args.putString(Constants.KEY_ACCOUNT, account);
        args.putString(Constants.KEY_VCODE, vCode);
        args.putString(Constants.KEY_PWD, pwd);
        presenter.register(args);
        //自动隐藏软键盘
        Uis.hideSoftInput(mPwd);
    }

    @Override
    public void onRegisterSuccess() {
        Msgs.shortToast(getContext(), "注册成功");
        final String account = mAccount.getText().toString();
        final String pwd = mPwd.getText().toString();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_ACCOUNT, account);
        args.putString(Constants.KEY_PWD, pwd);
        UserActivity.beginRegisterSuccess(getContext(), args);
    }


    private void resetCodeUi() {
        stopUiTask(mUiTask);
        mSendCode.setText("发送验证码");
        mSendCode.setEnabled(true);
    }

    @Override
    public void onCodeSuccess() {
        Msgs.shortToast(getContext(), "验证码发送成功");
        lockCodeUi();
    }

    @Override
    public void onCodeFailed(AppExp exp) {
        resetCodeUi();
    }

    @Override
    public void onDialogBackPressed() {
        super.onDialogBackPressed();
        resetCodeUi();
    }
}
