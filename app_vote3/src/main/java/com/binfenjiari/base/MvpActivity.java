package com.binfenjiari.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


/**
 * 为了自动互相绑定 MVP 架构模式中的 UserView 层 和 UserPresenter 层
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/6/8
 * <br>Email: developer.huajianjiang@gmail.com
 */
public abstract class MvpActivity<V extends MvpFragment<? super P>, P extends BaseContract.BaseIPresenter<? super V>>
        extends BaseActivity
{
    private static final String TAG = MvpActivity.class.getSimpleName();

    protected V mvpView;

    protected P mvpPresenter;

    /**
     * 创建并返回 MVP 层 中的 UserView 层
     *
     * @return
     */
    @NonNull
    public abstract V mvpView();

    /**
     * 创建并返回 MVP 层 中的 UserPresenter 层
     *
     * @return
     */
    @NonNull
    public abstract P mvpPresenter();

    @Override
    protected Fragment onCreateFragment() {
        mvpView = mvpView();
        mvpPresenter = mvpPresenter();
        return mvpView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpView = mvpView();
        mvpPresenter = mvpPresenter();

        if (mvpView != null) {
            mvpView.bindPresenter(mvpPresenter);
        }
        if (mvpPresenter != null) {
            mvpPresenter.bindView(mvpView);
        }
    }

    @Override
    protected void onDestroy() {
        // 自动解绑,避免内存泄漏
        if (mvpPresenter != null) {
            mvpPresenter.unbindView();
        }
        super.onDestroy();
    }

}
