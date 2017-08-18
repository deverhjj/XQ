package com.binfenjiari.base;

import android.support.annotation.NonNull;

import com.binfenjiari.BuildConfig;
import com.binfenjiari.utils.Preconditions;


/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/5/27
 * <br>Email: developer.huajianjiang@gmail.com
 */
public abstract class MvpFragment<P extends BaseContract.BaseIPresenter> extends BaseFragment
        implements BaseContract.BaseIView<P>
{
    private static final String TAG = MvpFragment.class.getSimpleName();
    protected P presenter;

    @Override
    public void bindPresenter(@NonNull P presenter) {
        if (!BuildConfig.DEBUG) {
            Preconditions.isNotNull(presenter, "presenter can not be null");
        }
        this.presenter = presenter;
    }

    protected void start() {
        if (presenter != null) {
            presenter.start();
        }
    }

    protected void stop() {
        if (presenter != null) {
            presenter.stop();
        }
    }
}
