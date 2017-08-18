package com.binfenjiari.base;


import android.support.annotation.NonNull;


import com.binfenjiari.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;


/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/2/27
 * <br>Email: developer.huajianjiang@gmail.com
 */
public abstract class BasePresenter<V extends BaseContract.BaseIView>
        implements BaseContract.BaseIPresenter<V>
{
    private static final String TAG = BasePresenter.class.getSimpleName();
    protected V view;
    private List<BaseContract.BaseIPresenter<? super V>> mPresenterModules;

    @Override
    public void bindView(@NonNull V view) {
        Preconditions.isNotNull(view, "view can not be null");
        this.view = view;
        if (!Preconditions.isNullOrEmpty(mPresenterModules)) {
            for (BaseContract.BaseIPresenter<? super V> p : mPresenterModules) {
                p.bindView(view);
            }
        }
    }

    @Override
    public void unbindView() {
        view = null;
        if (!Preconditions.isNullOrEmpty(mPresenterModules)) {
            for (BaseContract.BaseIPresenter p : mPresenterModules) {
                p.unbindView();
            }
        }
    }

    protected boolean isActive() {
        return view != null;
    }

    protected void addPresenterModule(BaseContract.BaseIPresenter<? super V> presenter) {
        getPresenterModuleContainer().add(presenter);
    }

    protected void stopAll() {
        if (!Preconditions.isNullOrEmpty(mPresenterModules)) {
            for (BaseContract.BaseIPresenter<? super V> p : mPresenterModules) {
                p.stop();
            }
        }
    }

    public List<BaseContract.BaseIPresenter<? super V>> presenterModels() {
        return mPresenterModules;
    }

    private List<BaseContract.BaseIPresenter<? super V>> getPresenterModuleContainer() {
        if (mPresenterModules == null) {
            mPresenterModules = new ArrayList<>();
        }
        return mPresenterModules;
    }
}
