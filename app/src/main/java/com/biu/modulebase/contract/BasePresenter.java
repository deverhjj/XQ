package com.biu.modulebase.contract;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/9
 */
public class BasePresenter<V extends BaseIContract.IView> implements BaseIContract.IPresenter<V> {

    public V mViewFragment;

    private Context context;

    private Activity activity;

    @Override
    public void bindView(V view) {
        mViewFragment = view;

        if (view instanceof Fragment) {
            Fragment fragment = (Fragment) view;
            context = fragment.getContext();
            activity = fragment.getActivity();
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }
}
