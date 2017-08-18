package com.binfenjiari.base;


import com.binfenjiari.utils.Logger;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/5/11
 * <br>Email: developer.huajianjiang@gmail.com
 */
public abstract class NetPresenter<V extends BaseContract.BaseIView> extends BasePresenter<V> {
    public static final String TAG = NetPresenter.class.getSimpleName();

    private CompositeDisposable mDisposable = new CompositeDisposable();

    protected XqService service;

    public NetPresenter() {
        service = AppManager.get().getService();
    }

    protected void pushTask(Disposable task) {
        mDisposable.add(task);
    }

    @Override
    public void stop() {
        Logger.e(TAG, "stop");
        mDisposable.clear();
        stopAll();
    }

}
