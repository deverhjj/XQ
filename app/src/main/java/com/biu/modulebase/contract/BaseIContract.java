package com.biu.modulebase.contract;

import android.app.Activity;
import android.content.Context;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/9
 */
public interface BaseIContract {

    interface IView<P extends IPresenter> {
        //通过该方法，view获得了presenter得实例，从而可以调用presenter代码来处理业务逻辑
        void bindPresenter(P presenter);

        void showToast(String msg);

        void showViewProgress();

        void dismissViewProgress();

    }

    interface IPresenter<V extends IView> {

        void bindView(V view);

        Context getContext();

        Activity getActivity();

    }
}
