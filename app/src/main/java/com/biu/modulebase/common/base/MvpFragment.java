package com.biu.modulebase.common.base;

import android.widget.Toast;

import com.biu.modulebase.contract.BaseIContract;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/9
 */
public abstract class MvpFragment<P extends BaseIContract.IPresenter> extends BaseFragment implements BaseIContract.IView<P>{

    protected String Tag = getClass().getSimpleName();

    public P mPresenter;

    @Override
    public void bindPresenter(P presenter) {
        mPresenter = presenter;
        if(presenter!=null)
            presenter.bindView(this);
    }

    @Override
    public void showToast(String msg) {
        showTost(msg, Toast.LENGTH_SHORT);
    }

    public void showViewProgress(){
        showProgress(Tag);
    }

    public void dismissViewProgress(){
        dismissProgress();
    }
}
