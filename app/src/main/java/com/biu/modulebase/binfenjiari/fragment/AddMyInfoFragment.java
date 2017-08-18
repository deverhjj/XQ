package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommVoteDetailNewActivity;
import com.biu.modulebase.binfenjiari.activity.VoteSuccessActivity;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.eventbus.VoteSuccessEvent;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/11/3.
 */

public class AddMyInfoFragment extends BaseFragment {

    private EditText editName;
    private EditText editPhone;
    private EditText editCardNumber;
    private EditText verification;
    private LinearLayout ll_cardnumber;
    private Button sendVerfiBtn;

    private String voteIds;
    /**
     * 是否需要生分证
     */
    private Boolean needCardNumber;
//    private String[] mVote;
//    private String[] mOptions;
    String vote="";
    String options="";
    //    private String[] mOptions;
//    private int[] mVote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        voteIds = getBaseActivity().getIntent().getStringExtra("voteIds");
        if(TextUtils.isEmpty(voteIds)){
            this.getBaseActivity().finish();
        }

        String[]l;

       l= voteIds.split("#");
        List l1=new ArrayList();
        for (int i = 0; i < l.length; i++) {
            l1.add(l[i]);
        }
        List vote1=new ArrayList();
        List options1=new ArrayList();
        String m;
        List l3=new ArrayList();
        for (int i = 0; i <l1.size(); i++) {
//            vote=new String[l1.size()];
           m= (String) l1.get(i);
            String[]l2=m.split("@");
            vote1.add(l2[0]);
            options1.add(l2[1]);

//            vote[i]=;

        }
//        int x=options1.size();
//        int y=vote1.size();
//        mOptions = new String[x];
//        mVote = new String[y];
        for (int i = 0; i <vote1.size(); i++) {
//            mVote[i]=Utils.isString(vote1.get(i).toString());
            if(i==vote1.size()-1){
                vote=vote+vote1.get(i).toString();
            }else {
            vote=vote+vote1.get(i).toString()+"@";}
        }
        for (int i = 0; i < options1.size(); i++) {
//            mOptions[i]= Utils.isString(String.valueOf(options1.get(i))) ;
            if(i==options1.size()-1){
                options=options+options1.get(i).toString();
            }else {
            options=options+options1.get(i).toString()+"@";}
        }

        needCardNumber = getBaseActivity().getIntent().getBooleanExtra("needCardNumber",false);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_add_info,container,false);
        return super.onCreateView(inflater,rootView,savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        editName = (EditText)rootView.findViewById(R.id.actv_name);
        editPhone = (EditText)rootView.findViewById(R.id.actv_phone);
        editCardNumber = (EditText)rootView.findViewById(R.id.actv_cardnumber);
        verification = (EditText)rootView.findViewById(R.id.verification);
        ll_cardnumber = (LinearLayout)rootView.findViewById(R.id.ll_cardnumber);

        sendVerfiBtn = (Button) rootView.findViewById(R.id.send_verification);
        sendVerfiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editPhone.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    showTost("请填写手机号", 1);
                    return;
                }
                if (Utils.isMobileNO(phone)) {
                    sendVerification(phone);
                }else{
                    showTost("手机号格式不正确", 1);
                }
            }
        });

        rootView.findViewById(R.id.vote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isErrorFormat()){
                    return;
                }
                doSubmitVote();
            }
        });

    }

    @Override
    public void loadData() {
        if(needCardNumber){
            ll_cardnumber.setVisibility(View.VISIBLE);
        }else{
            ll_cardnumber.setVisibility(View.GONE);
        }

    }

    /**
     * 进行投票V2.5
     */
    private void doSubmitVote() {
        showProgress(getClass().getSimpleName());
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", "NewVote");
        JSONUtil.put(params, "action", "user_proceedVote");
        JSONUtil.put(params, "name", editName.getText().toString());
        JSONUtil.put(params, "phone", editPhone.getText().toString().trim());
        if(needCardNumber) {
            JSONUtil.put(params, "card_number", editCardNumber.getText().toString());
        }
        JSONUtil.put(params, "vote", vote);
        JSONUtil.put(params, "options", options);
        JSONUtil.put(params, "token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));

        JSONUtil.put(params, "device_id", Utils.getDeviceId(getActivity()));
        JSONUtil.put(params, "mobile_verify", verification.getText().toString());
        JSONUtil.put(params, "signature", Base64.encodeToString(Utils.encodeM((Utils.getDeviceId(getActivity())+Constant.s+ editPhone.getText().toString())).getBytes(),Base64.DEFAULT));
//        params.put("mobile_verify",registerVerification);
//        params.put("device_id",Utils.getDeviceId(getActivity()));
//        params.put("signature", Base64.encodeToString(Utils.encodeM((Constant.s +Utils.getDeviceId(getActivity())+ registerAccount)).getBytes(),Base64.DEFAULT));

        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
                EventBus.getDefault().post(new VoteSuccessEvent());
                Intent intent=new Intent(getActivity(), VoteSuccessActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                if (key == 10) {//投票次数已完，不能进行投票
                    showTost(message, 1);
                } else if (key == 11) {//当天投票次数已完，不能进行投票
                    showTost(message, 1);
                } else if (key ==239) {
                    showTost(message, 1);
                }else{
                    showTost(message, 1);
                }
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
                visibleNoNetWork();
            }
        });
    }

    /**
     * 判断输入数据的有效性
     * @return
     */
    private boolean isErrorFormat(){
        if(TextUtils.isEmpty(voteIds)){
            showTost("投票选项不能为空", 1);
            return true;
        }

        if(TextUtils.isEmpty(editName.getText().toString())){
            showTost("姓名不能为空", 1);
            return true;
        }

        String phone = editPhone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            showTost("电话不能为空", 1);
            return true;
        }

        String verify = verification.getText().toString();
        if(TextUtils.isEmpty(verify)){
            showTost("验证码不能为空", 1);
            return true;
        }

        if (!Utils.isMobileNO(phone)) {
            showTost("请输入正确的手机号!",1);
            return true;
        }

        if(needCardNumber && TextUtils.isEmpty(editCardNumber.getText().toString())){
            showTost("身份证不能为空", 1);
            return true;
        }

        return false;
    }

    private void sendVerification(String phone){
        JSONObject params = new JSONObject();
        try {
            params.put("type","30");
            params.put("model", Constant.MODEL_REG);
            params.put("action",Constant.ACTION_VERIFICATION_CODE);
            params.put("mobile",phone.trim());
            if(CommVoteDetailNewActivity.sNewVoteBeanVOList!=null && CommVoteDetailNewActivity.sNewVoteBeanVOList.size()>0) {
                params.put("voteId", CommVoteDetailNewActivity.sNewVoteBeanVOList.get(0).getId());
            }
            params.put("token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
            params.put("device_id",Utils.getDeviceId(getActivity()));
            params.put("signature", Base64.encodeToString(Utils.encodeM(Utils.getDeviceId(getActivity())+ Constant.s +phone).getBytes(),Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                getBaseActivity().showTost("验证码发送成功，请注意查收~",1);
                TimeCount time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
                time.start();
            }

            @Override
            public void onCodeError(int key, String message) {
                getBaseActivity().showTost(message,1);
            }

            @Override
            public void onConnectError(String message) {

            }
        });
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            sendVerfiBtn.setText("重新发送");
            sendVerfiBtn.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            sendVerfiBtn.setClickable(false);
            sendVerfiBtn.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
