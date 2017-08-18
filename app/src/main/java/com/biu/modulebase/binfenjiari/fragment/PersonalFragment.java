package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CircleCreatedMyActivity;
import com.biu.modulebase.binfenjiari.activity.CollectionActivity;
import com.biu.modulebase.binfenjiari.activity.MainActivity;
import com.biu.modulebase.binfenjiari.activity.MyExerciseActivity;
import com.biu.modulebase.binfenjiari.activity.MyShareActivity;
import com.biu.modulebase.binfenjiari.activity.NotificationActivity;
import com.biu.modulebase.binfenjiari.activity.PersonalInfoActivity;
import com.biu.modulebase.binfenjiari.activity.SettingActivity;
import com.biu.modulebase.binfenjiari.activity.UnLoginActivity;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseActivity2;
import com.biu.modulebase.common.base.BaseFragment;
import com.tencent.connect.UserInfo;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/15
 */
public class PersonalFragment extends BaseFragment {

    private static final String TAG = "PersonalFragment";

    private ImageView img_info;

    private ImageView img_person_edit;

    private RelativeLayout rl_layout_login;

    private LinearLayout ll_layout_unlogin;

    private TextView tv_small_title, tv_schoolname, tv_classname, tv_hobby;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //标记下当前的登录状态，提供后期回到该界面是否需要刷新界面
        MyApplication.sRefreshMap.put(TAG, !OtherUtil.hasLogin(getActivity()));
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_personal_center, container,
                false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }


    @Override
    protected void initView(View rootView) {
        rl_layout_login = (RelativeLayout) rootView.findViewById(R.id.rl_layout_login);
        rl_layout_login.setVisibility(View.GONE);
        ll_layout_unlogin = (LinearLayout) rootView.findViewById(R.id.ll_layout_unlogin);

        img_info = (ImageView) rootView.findViewById(R.id.img_info);
        img_info.setOnClickListener(this);

        img_person_edit = (ImageView) rootView.findViewById(R.id.img_person_edit);
        img_person_edit.setOnClickListener(this);

        tv_small_title = (TextView) rootView.findViewById(R.id.tv_small_title);
        tv_schoolname = (TextView) rootView.findViewById(R.id.tv_schoolname);
        tv_classname = (TextView) rootView.findViewById(R.id.tv_classname);
        tv_hobby = (TextView) rootView.findViewById(R.id.tv_hobby);

        rootView.findViewById(R.id.tv_login).setOnClickListener(this);

        rootView.findViewById(R.id.huodong).setOnClickListener(this);
        rootView.findViewById(R.id.circle).setOnClickListener(this);
        rootView.findViewById(R.id.shoucang).setOnClickListener(this);
        rootView.findViewById(R.id.share).setOnClickListener(this);
        rootView.findViewById(R.id.setting).setOnClickListener(this);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.img_info || v.getId() == R.id.img_person_edit) {
            if (Utils.isEmpty(PreferencesUtils.getString(getContext(), PreferencesUtils.KEY_TOKEN)) || MyApplication.getUserInfo(getContext()) == null) {
                startActivity(new Intent(getContext(), UnLoginActivity.class));
                return;
            }
            UserInfoBean userInfoVO = MyApplication.getUserInfo(getContext());
            Intent intent = new Intent(getContext(), PersonalInfoActivity.class);
            intent.putExtra(Constant.KEY_ID, userInfoVO == null ? "" : userInfoVO.getId());
            startActivity(intent);
        } else if (v.getId() == R.id.huodong) {
            //活动
            if (Utils.isEmpty(PreferencesUtils.getString(getContext(), PreferencesUtils.KEY_TOKEN)) || MyApplication.getUserInfo(getContext()) == null) {
                showUnLoginSnackbar();
                return;
            }
            startActivity(new Intent(this.getContext(), MyExerciseActivity.class));
        } else if (v.getId() == R.id.circle) {
            //圈子
            if (Utils.isEmpty(PreferencesUtils.getString(getContext(), PreferencesUtils.KEY_TOKEN)) || MyApplication.getUserInfo(getContext()) == null) {
                showUnLoginSnackbar();
                return;
            }
            startActivity(new Intent(this.getContext(), CircleCreatedMyActivity.class));

        } else if (v.getId() == R.id.shoucang) {
            //收藏
            if (Utils.isEmpty(PreferencesUtils.getString(getContext(), PreferencesUtils.KEY_TOKEN)) || MyApplication.getUserInfo(getContext()) == null) {
                showUnLoginSnackbar();
                return;
            }
            startActivity(new Intent(this.getContext(), CollectionActivity.class));
        } else if (v.getId() == R.id.share) {
            //邀请好友
            startActivity(new Intent(this.getContext(), MyShareActivity.class));
        } else if (v.getId() == R.id.setting) {
            //设置
            startActivity(new Intent(this.getContext(), SettingActivity.class));
        } else if (v.getId() == R.id.tv_login) {
            startActivity(new Intent(this.getContext(), UnLoginActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
        updateLoginState();
    }

    public void updateLoginState() {
        if (Utils.isEmpty(PreferencesUtils.getString(getContext(), PreferencesUtils.KEY_TOKEN)) || MyApplication.getUserInfo(getContext()) == null) {
            rl_layout_login.setVisibility(View.GONE);
            ll_layout_unlogin.setVisibility(View.VISIBLE);
        } else {
            rl_layout_login.setVisibility(View.VISIBLE);
            ll_layout_unlogin.setVisibility(View.GONE);

            UserInfoBean userInfoVO = MyApplication.getUserInfo(getContext());
            ImageDisplayUtil.displayImage(Constant.IMG_SOURCE, userInfoVO.getUser_pic(), img_info, ImageDisplayUtil.DISPLAY_PERSONAL_HEADER);
            tv_small_title.setText(userInfoVO.getUsername());

            Drawable drawable = null;
            if (userInfoVO.getGender() == 1) {
                drawable = getResources().getDrawable(R.mipmap.icon_sex_man);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            } else if (userInfoVO.getGender() == 2) {
                drawable = getResources().getDrawable(R.mipmap.icon_sex_woman);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            tv_small_title.setCompoundDrawables(null, null, drawable, null);

            tv_schoolname.setText(TextUtils.isEmpty(userInfoVO.getSchoolName()) ? "暂无" : userInfoVO.getSchoolName());
            tv_classname.setText(TextUtils.isEmpty(userInfoVO.getClassName()) ? "暂无" : userInfoVO.getClassName());
            tv_hobby.setText(TextUtils.isEmpty(userInfoVO.getHobby()) ? "暂无" : userInfoVO.getHobby());

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.circle, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (hasMessage()) {
            menu.findItem(R.id.action_notification).setIcon(R.mipmap.message_hint);
        } else {
            menu.findItem(R.id.action_notification).setIcon(R.mipmap.message);
        }
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_notification) {
            if (checkIsLogin()) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }

        }
        return super.onOptionsItemSelected(item);
    }

}
