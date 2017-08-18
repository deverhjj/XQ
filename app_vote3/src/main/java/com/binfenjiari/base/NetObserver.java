package com.binfenjiari.base;

import android.support.annotation.NonNull;

import io.reactivex.observers.DisposableObserver;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/5/23
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class NetObserver<T> extends DisposableObserver<AppEcho<T>> {

    private NetCallback<T> mCallback;

    public NetObserver(@NonNull NetCallback<T> callback) {
        mCallback = callback;
    }

    @Override
    protected void onStart() {
        mCallback.onPrepare();
    }

    @Override
    public void onNext(AppEcho<T> echo) {
        mCallback.onEcho(echo);
    }

    @Override
    public void onError(Throwable e) {
        mCallback.onPeace();
        AppExp exp = Expaser.parse(e);
        mCallback.onFailure(exp);
    }

    @Override
    public void onComplete() {
        mCallback.onPeace();
    }

}
