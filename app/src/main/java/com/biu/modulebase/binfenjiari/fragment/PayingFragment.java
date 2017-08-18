package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.other.alipay.AliPayUtil;
import com.biu.modulebase.binfenjiari.other.alipay.PayResult;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.common.base.BaseFragment;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/5/26
 */
public class PayingFragment extends BaseFragment {

    public static boolean WEIXIN_PAY_SUCCESS = false;

    private final static String PAY_ALI ="1";
    private final static String PAY_WEIXIN ="2";

    private TextView nameText;
    private TextView moneyText;
    private CheckBox aliCheckbox;
    private CheckBox wxCheckbox;
    private TextView submitText;

    private String name;
    private double money;
    private String order_id;
    private String payType ;


    private int position;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_paying, container,false);
    }

    @Override
    protected void getIntentData() {
        Intent intent =getActivity().getIntent();
        position =intent.getIntExtra("position",-1);
        order_id =intent.getStringExtra("order_id");
        name =intent.getStringExtra("title");
        money =intent.getDoubleExtra("money",0.00);
    }

    @Override
    protected void initView(View rootView) {
        nameText =(TextView)rootView.findViewById(R.id.name);
        moneyText =(TextView)rootView.findViewById(R.id.money);
        nameText.setText(name);
        moneyText.setText(String.format(getString(R.string.money2),money));
        aliCheckbox =(CheckBox)rootView.findViewById(R.id.aliCheckbox);
        wxCheckbox=(CheckBox)rootView.findViewById(R.id.wxCheckbox);
        submitText =(TextView)rootView.findViewById(R.id.submit);
        submitText.setText(String.format(getString(R.string.pay_money),money));
        aliCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    submitText.setEnabled(true);
                    payType =PAY_ALI;
                    wxCheckbox.setChecked(false);
                }else{
                    if(!wxCheckbox.isChecked())
                        submitText.setEnabled(false);
                }
            }
        });
        wxCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    submitText.setEnabled(true);
                    payType =PAY_WEIXIN;
                    aliCheckbox.setChecked(false);
                }else{
                    if(!aliCheckbox.isChecked())
                        submitText.setEnabled(false);
                }
            }
        });
        aliCheckbox.setChecked(true);

        rootView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayType();
            }
        });
    }


    /**
     * 获取支付类型相关信息接口
     */
    private void getPayType(){
        showProgress(getClass().getSimpleName());
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"type",payType);
        JSONUtil.put(params,"order_id",order_id);
        JSONUtil.put(params,"model", Constant.MODEL_PAY);
        JSONUtil.put(params,"action",Constant.ACTION_GET_PAY_INFO);
        jsonRequest(true, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result = new JSONObject(jsonString);
                    String payInfo = JSONUtil.getString(result, "payInfo");
                    if (payType.equals(PAY_ALI)){
                        aliPay(payInfo);
                    }else{
                         wxPay(new JSONObject(payInfo));
                    }
                    dismissProgress();

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                showTost(message,0);
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
            }
        });

    }

    /**
     * 调用手机支付宝付款
     *
     * @param payInfo
     */
    protected void aliPay(String payInfo) {
        new AliPayUtil(getActivity(), new AliPayUtil.AliPaySDKCallBack() {

            @Override
            public void paySuccess(PayResult payResult) {
                dismissProgress();
                Intent intent = new Intent();
                intent.putExtra("position",position);
                getActivity().setResult(Activity.RESULT_OK,intent);
                showTost("支付成功",0);
                getActivity().finish();


            }

            @Override
            public void payResultConfirming(PayResult payResult) {

            }

            @Override
            public void payError(PayResult payResult) {
                showTost(payResult.getMemo(),1);
                dismissProgress();

            }

            @Override
            public void checkSDK(boolean isExist) {
                if(!isExist)
                showTost("请先下载最新版支付宝客户端",0);

            }
        }).pay(payInfo);

    }

    protected void wxPay(JSONObject wxObject){
        IWXAPI api  = WXAPIFactory.createWXAPI(getActivity(), Constant.WEIXIN_AppID);
        api.registerApp(Constant.WEIXIN_AppID);
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if(isPaySupported){
            if(null != wxObject && !wxObject.has("retcode") ){
                PayReq req = new PayReq();
                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                req.appId = JSONUtil.getString(wxObject,"appid");
                req.partnerId = JSONUtil.getString(wxObject,"partnerid");
                req.prepayId = JSONUtil.getString(wxObject,"prepayid");
                req.nonceStr = JSONUtil.getString(wxObject,"noncestr");
                req.timeStamp = JSONUtil.getString(wxObject,"timestamp");
                req.packageValue = JSONUtil.getString(wxObject,"package");
                req.sign = JSONUtil.getString(wxObject,"sign");
                req.extData = "app data"; // optional
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信  使用Umeng  在Applicaion中已註冊
                api.sendReq(req);
            }else{
                LogUtil.LogE("返回错误"+ JSONUtil.getString(wxObject,"retmsg"));
                showTost("返回错误"+ JSONUtil.getString(wxObject,"retmsg"),1);
            }
        }else{
            showTost("请下载最新微信客户端",0);
        }

    }

    @Override
    public void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(WEIXIN_PAY_SUCCESS){
            WEIXIN_PAY_SUCCESS =false;
            Intent intent = new Intent();
            intent.putExtra("position",position);
            getActivity().setResult(Activity.RESULT_OK,intent);
            getActivity().finish();
        }
    }
}
