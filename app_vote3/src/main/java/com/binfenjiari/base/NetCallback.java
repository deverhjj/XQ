package com.binfenjiari.base;

import android.support.annotation.UiThread;

/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/2/24
 * <br>Email: developer.huajianjiang@gmail.com
 */
public interface NetCallback<T> {
    @UiThread
    void onPrepare();

    @UiThread
    void onEcho(AppEcho<T> echo);

    @UiThread
    void onFailure(AppExp e);

    @UiThread
    void onPeace();

    class SimpleNetCallback<T> implements NetCallback<T> {

        @Override
        public void onPrepare() {

        }

        @Override
        public void onEcho(AppEcho<T> echo) {

        }

        @Override
        public void onFailure(AppExp e) {

        }

        @Override
        public void onPeace() {

        }
    }
}
