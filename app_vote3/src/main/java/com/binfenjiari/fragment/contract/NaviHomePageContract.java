package com.binfenjiari.fragment.contract;

import com.biu.modulebase.contract.BaseIContract;

/**
 * @Title: {标题}
 * @Description:{指定view 和presenter之间的契约关系}
 * @date 2017/1/18
 */
public interface NaviHomePageContract {

    interface View extends BaseIContract.IView<Presenter> {

    }

    interface Presenter extends BaseIContract.IPresenter<View> {

    }

}
