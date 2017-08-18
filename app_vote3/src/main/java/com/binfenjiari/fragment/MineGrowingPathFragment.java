package com.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.activity.LoginActivity;
import com.binfenjiari.fragment.contract.LoginLoginContract;
import com.binfenjiari.fragment.presenter.LoginLoginPresenter;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.common.base.MvpFragment;
import com.biu.modulebase.retrofit.architecture.ApiException;
import com.biu.modulebase.retrofit.architecture.ApiResponseBody;
import com.biu.modulebase.rxjava.MCallBack;

/**
 * @author tangjin
 * @Title: {登录}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class MineGrowingPathFragment extends BaseFragment {


    public static MineGrowingPathFragment newInstance() {
        MineGrowingPathFragment fragment = new MineGrowingPathFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_mine_growing_path, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        getBaseActivity().setBackNavigationIcon();
        TextView mTvRegister = (TextView) rootView.findViewById(R.id.tv_register);



    }

    @Override
    public void loadData() {

    }

}
