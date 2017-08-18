package com.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.binfenjiari.R;
import com.binfenjiari.activity.LoginActivity;
import com.binfenjiari.activity.MineCollectionActivity;
import com.binfenjiari.activity.MineGrowingPathActivity;
import com.binfenjiari.activity.MineIntegralActivity;
import com.binfenjiari.activity.MineMovementActivity;
import com.binfenjiari.activity.MineProjectActivity;
import com.binfenjiari.activity.MinePublishActivity;
import com.binfenjiari.activity.ReportWorksPhotoActivity;
import com.binfenjiari.fragment.contract.NaviMineCenterContract;
import com.binfenjiari.fragment.presenter.NaviMineCenterPresenter;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.common.base.MvpFragment;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class OtherPersonCenterFragment extends BaseFragment {

    private ImageView img_head;

    public static OtherPersonCenterFragment newInstance() {
        OtherPersonCenterFragment fragment = new OtherPersonCenterFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_other_person_center, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
//        img_head = (ImageView) rootView.findViewById(R.id.img_head);
//        img_head.setOnClickListener(new UserView.OnClickListener() {
//            @Override
//            public void onClick(UserView v) {
//                LoginActivity.beginLogin(getContext());
//            }
//        });

        rootView.findViewById(R.id.fl_his_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinePublishActivity.beginActivity(getContext());
//                CollectionActivity.startAct(getContext());
            }
        });

        rootView.findViewById(R.id.fl_his_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportWorksPhotoActivity.beginActivity(getContext());

            }
        });

    }

    @Override
    public void loadData() {

    }

}
