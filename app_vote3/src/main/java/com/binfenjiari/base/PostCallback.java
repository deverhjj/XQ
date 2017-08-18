package com.binfenjiari.base;

import android.support.annotation.NonNull;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/5/23
 * <br>Email: developer.huajianjiang@gmail.com
 */
public abstract class PostCallback<T> implements NetCallback<T> {
    private PostIView mView;

    public PostCallback(@NonNull PostIView view) {
        mView = view;
    }

    @Override
    public void onPrepare() {
        mView.showPostPrepareUi();
    }

    @Override
    public void onFailure(AppExp e) {
        mView.showPostFailureUi(e);
    }

    @Override
    public void onPeace() {
        mView.clearPostUi();
    }
}
