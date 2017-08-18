package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.AboutActivity;
import com.biu.modulebase.binfenjiari.activity.FeedbackActivity;
import com.biu.modulebase.binfenjiari.activity.UnLoginActivity;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ClearCacheAlertDialogFragment;
import com.biu.modulebase.binfenjiari.util.DataCleanManager;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;
import com.umeng.message.IUmengCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/4/14
 */
public class SettingFragment extends BaseFragment {

    private TextView cache_size;

    private TextView account;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_setting, container, false);
        return layout;
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        setHasOptionsMenu(true);
        getBaseActivity().setBackNavigationIcon();

        account =(TextView)rootView.findViewById(R.id.account);
        if(MyApplication.userInfo!=null){
            account.setText(MyApplication.userInfo.getTelephone());
        }
        Switch notice= (Switch) rootView.findViewById(R.id.notice);
        notice.setChecked(PreferencesUtils.getBoolean(getActivity().getApplicationContext(), "allow_push", true));
        notice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MyApplication.mPushAgent.enable(new IUmengCallback() {
                        @Override
                        public void onSuccess() {
                            PreferencesUtils.putBoolean(getActivity().getApplicationContext(), "allow_push", true);
                            Toast.makeText(getActivity().getApplicationContext(), "消息推送已开启", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String s, String s1) {

                        }
                    });
                    PreferencesUtils.putBoolean(getActivity().getApplicationContext(), "allow_push", true);
                    Toast.makeText(getActivity().getApplicationContext(), "消息推送已开启", Toast.LENGTH_SHORT).show();
                } else {
                    MyApplication.mPushAgent.disable(new IUmengCallback() {
                        @Override
                        public void onSuccess() {
                            PreferencesUtils.putBoolean(getActivity().getApplicationContext(), "allow_push", false);
                            Toast.makeText(getActivity().getApplicationContext(), "消息推送已关闭", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String s, String s1) {

                        }
                    });
                    PreferencesUtils.putBoolean(getActivity().getApplicationContext(), "allow_push", false);
                    Toast.makeText(getActivity().getApplicationContext(), "消息推送已关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cache_size = (TextView) rootView.findViewById(R.id.cache_size);
        try {
            double totalSize = DataCleanManager
                    .getFolderSize(getActivity().getCacheDir())
                    + DataCleanManager.getFolderSize(new File(
                    Constant.SZTW_CACHE_PATH));
            cache_size.setText(DataCleanManager.getFormatSize(totalSize));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Button exitBtn = (Button) rootView.findViewById(R.id.exitBtn);
        rootView.findViewById(R.id.feedback).setOnClickListener(this);
        rootView.findViewById(R.id.about).setOnClickListener(this);
        rootView.findViewById(R.id.clear_cache_layout).setOnClickListener(this);
        if(Utils.isEmpty(PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN))){
            exitBtn.setVisibility(View.GONE);
        }else{
            exitBtn.setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.exitBtn).setOnClickListener(this);
        }
    }

    private void doLoginOut(){
        showProgress(getClass().getSimpleName().toString());
        JSONObject params = new JSONObject();
        try {
            params.put("device_id", Utils.getDeviceId(getActivity()));
            params.put("token",PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
            params.put("model",Constant.MODEL_REG);
            params.put("action",Constant.ACTION_LOGOUT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
//                clearUserInfo();
                clearSharedPreferences();
                dismissProgress();
                getActivity().finish();
                Intent intent=new Intent(getActivity(), UnLoginActivity.class);
                startActivity(intent);


            }

            @Override
            public void onCodeError(int key, String message) {
                showTost(message,1);
                dismissProgress();
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();

            }
        });
    }

    private void clearUserInfo(){
        if(MyApplication.userInfo!=null)
            MyApplication.userInfo=null;
        PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_TOKEN, "");
        PreferencesUtils.putString(getActivity(), PreferencesUtils.KEY_USER_INFO, "");
    }


    private void clearSharedPreferences(){
        if(MyApplication.userInfo!=null)
            MyApplication.userInfo=null;
        PreferencesUtils.clear(getActivity());
    }

    @Override
    public void loadData() {}

    private void showAlertDialog() {
        ClearCacheAlertDialogFragment newFragment = ClearCacheAlertDialogFragment.newInstance("提示", "确定要清除缓存吗?", R.style.AppCompatAlertDialogStyle);
        newFragment.show(getFragmentManager(), "dialog");
    }

    public void clearCache(){
        DataCleanManager.cleanApplicationData(getActivity(), Constant.SZTW_CACHE_PATH);
        Toast.makeText(getActivity(), "清除缓存成功", Toast.LENGTH_SHORT).show();
        cache_size.setText("0KB");
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.feedback) {
            startActivity(new Intent(getActivity(), FeedbackActivity.class));


        } else if (i == R.id.about) {
            startActivity(new Intent(getActivity(), AboutActivity.class));

        } else if (i == R.id.clear_cache_layout) {
            showAlertDialog();

        } else if (i == R.id.exitBtn) {
            doLoginOut();

        }

    }
}
