package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CircleCreatedMyActivity;
import com.biu.modulebase.binfenjiari.activity.CollectionActivity;
import com.biu.modulebase.binfenjiari.activity.MyExerciseActivity;
import com.biu.modulebase.binfenjiari.activity.MyQRcodeActivity;
import com.biu.modulebase.binfenjiari.activity.MyShareActivity;
import com.biu.modulebase.binfenjiari.activity.PersonalInfoActivity;
import com.biu.modulebase.binfenjiari.activity.SettingActivity;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/4/11
 */
public class NavigationDrawerFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_drawer_left_layout, container, false);
        return layout;
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        final FrameLayout info = (FrameLayout) rootView.findViewById(R.id.info);
        final FrameLayout huodong = (FrameLayout) rootView.findViewById(R.id.huodong);
        final FrameLayout shangcheng = (FrameLayout) rootView.findViewById(R.id.shangcheng);
        final FrameLayout shoucang = (FrameLayout) rootView.findViewById(R.id.shoucang);
        final FrameLayout circle = (FrameLayout) rootView.findViewById(R.id.circle);
        final FrameLayout erweima = (FrameLayout) rootView.findViewById(R.id.erweima);
        final FrameLayout share = (FrameLayout) rootView.findViewById(R.id.share);
        final FrameLayout shezhi = (FrameLayout) rootView.findViewById(R.id.shezhi);
//        getBaseActivity().getToolbar().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect frame = new Rect();
//                getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//                int statusBarHeight = frame.top;
//                int toolbarHeight =getBaseActivity().getToolbar().getHeight();
//                int height =(Utils.getScreenHeight(getActivity()) - toolbarHeight-statusBarHeight)/8;
//                info.getLayoutParams().height =height;
//                huodong.getLayoutParams().height =height;
//                jidi.getLayoutParams().height =height;
//                shoucang.getLayoutParams().height =height;
//                circle.getLayoutParams().height =height;
//                erweima.getLayoutParams().height =height;
//                share.getLayoutParams().height =height;
//                shezhi.getLayoutParams().height =height;
//            }
//        });
        info.setOnClickListener(this);
        huodong.setOnClickListener(this);
        shangcheng.setOnClickListener(this);
        shoucang.setOnClickListener(this);
        circle.setOnClickListener(this);
        erweima.setOnClickListener(this);
        share.setOnClickListener(this);
        shezhi.setOnClickListener(this);

    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.info) {
            startActivity(new Intent(getActivity(), PersonalInfoActivity.class));

        } else if (i == R.id.huodong) {
            startActivity(new Intent(getActivity(), MyExerciseActivity.class));

        } else if (i == R.id.shangcheng) {
            getBaseActivity().showTost("积分商城,敬请期待...", 1);
//                startActivity(new Intent(getActivity(),ReservationJidiActivity.class));

        } else if (i == R.id.shoucang) {
            startActivity(new Intent(getActivity(), CollectionActivity.class));

        } else if (i == R.id.circle) {
            startActivity(new Intent(getActivity(), CircleCreatedMyActivity.class));

        } else if (i == R.id.shezhi) {
            startActivity(new Intent(getActivity(), SettingActivity.class));

        } else if (i == R.id.erweima) {
            startActivity(new Intent(getActivity(), MyQRcodeActivity.class));

        } else if (i == R.id.share) {
            startActivity(new Intent(getActivity(), MyShareActivity.class));

        }
    }
}
