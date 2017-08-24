package com.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.binfenjiari.R;
import com.binfenjiari.activity.MineCollectionActivity;
import com.binfenjiari.activity.MineGrowingPathActivity;
import com.binfenjiari.activity.MineIntegralActivity;
import com.binfenjiari.activity.MineMovementActivity;
import com.binfenjiari.activity.MineProjectActivity;
import com.binfenjiari.activity.MinePublishActivity;
import com.binfenjiari.activity.UserActivity;
import com.binfenjiari.activity.ReportWorksPhotoActivity;
import com.binfenjiari.fragment.contract.NaviMineCenterContract;
import com.binfenjiari.fragment.presenter.NaviMineCenterPresenter;
import com.biu.modulebase.binfenjiari.activity.FeedbackActivity;
import com.biu.modulebase.binfenjiari.activity.NotificationActivity;
import com.biu.modulebase.binfenjiari.activity.SettingActivity;
import com.biu.modulebase.common.base.MvpFragment;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class NaviMineCenterFragment extends MvpFragment<NaviMineCenterContract.Presenter> implements NaviMineCenterContract.View {

    private ImageView img_head;

    private ImageView iv_home_msg;

    public static NaviMineCenterFragment newInstance() {
        NaviMineCenterFragment fragment = new NaviMineCenterFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_main_mine_center, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initMvpBinder(View rootView) {
        bindPresenter(new NaviMineCenterPresenter());
    }


    @Override
    protected void initView(View rootView) {
        img_head = (ImageView) rootView.findViewById(R.id.img_head);
        img_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivity.beginLogin(getContext());
            }
        });

        iv_home_msg = (ImageView)rootView.findViewById(R.id.iv_home_msg);
        iv_home_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }
        });

        rootView.findViewById(R.id.tv_collection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineCollectionActivity.beginActivity(getContext());
//                CollectionActivity.startAct(getContext());
            }
        });

        rootView.findViewById(R.id.tv_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinePublishActivity.beginActivity(getContext());

            }
        });

        rootView.findViewById(R.id.tv_event_movement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineMovementActivity.beginActivity(getContext());
            }
        });

        rootView.findViewById(R.id.tv_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineProjectActivity.beginActivity(getContext());
            }
        });

        rootView.findViewById(R.id.tv_group_foot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineGrowingPathActivity.beginActivity(getContext());
            }
        });

        rootView.findViewById(R.id.tv_score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineIntegralActivity.beginActivity(getContext());
            }
        });

        rootView.findViewById(R.id.fl_report_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportWorksPhotoActivity.beginActivity(getContext());
            }
        });

        rootView.findViewById(R.id.fl_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
            }
        });

        rootView.findViewById(R.id.fl_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });


    }

    @Override
    public void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasMessage()) {
            iv_home_msg.setImageResource(R.mipmap.message_hint);
        } else {
            iv_home_msg.setImageResource(R.mipmap.message);
        }
    }
}
