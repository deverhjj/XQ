package com.binfenjiari.base;

import android.support.annotation.NonNull;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/5/23
 * <br>Email: developer.huajianjiang@gmail.com
 */
public abstract class PreCallback<T> implements NetCallback<T> {
    private PreIView mView;

    public PreCallback(@NonNull PreIView view) {
        mView = view;
    }

    @Override
    public void onPrepare() {
        mView.showPrePrepareUi();
    }

    @Override
    public void onFailure(AppExp e) {
        mView.showPreFailureUi(e);
    }

    @Override
    public void onPeace() {
        mView.clearPreUi();
    }
}
