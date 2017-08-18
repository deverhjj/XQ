package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.PayingActivity;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.EventHeaderParentItem;
import com.biu.modulebase.binfenjiari.util.DoubleUtil;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Lee
 * @Title: {提交订单}
 * @Description:{描述}
 * @date 2016/6/6
 */
public class SubmitOrderFragment extends BaseFragment {

    private final static int PAY =111;

    private PopupWindow popupWindow;

    private EditText schoolEdit;
    private EditText gradeEdit;
    private EditText nameEdit;
    private EditText phoneEdit;
    private EditText remarkEdit;

    private ImageButton chengrenMinus;
    private EditText chengrenNumEdit;
    private ImageButton chengrenPlus;
    private ImageButton ertongMinus;
    private EditText ertongNumEdit;
    private ImageButton ertongPlus;
    private ImageButton tuantiMinus;
    private EditText tuantiNumEdit;
    private ImageButton tuantiPlus;
    private TextView chengrenMoneyText;
    private TextView ertongMoneyText;
    private TextView tuantiMoneyText;
    private TextView totlaMoneyText;

    private int chengrenNum =0;
    private int ertongNum=0;
    private int tuantiNum =0;

    private String id;
    private double money ;
    private double child_money;
    private double team_money ;

    private double totalMoney;

    private EventHeaderParentItem bean;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_submit_order, container, false);
        return rootView;
    }

    @Override
    protected void getIntentData() {
        Intent intent =getActivity().getIntent();
        bean = (EventHeaderParentItem) intent.getSerializableExtra("event");
        id =bean.getId();
        money = bean.getMoney();
        child_money =bean.getChild_money();
        team_money =bean.getTeam_money();
//        totalMoney = child_money + money;
        totalMoney= DoubleUtil.add(child_money,money);
    }

    @Override
    protected void initView(View rootView) {
        schoolEdit = (EditText) rootView.findViewById(R.id.schoolEdit);
        gradeEdit = (EditText) rootView.findViewById(R.id.gradeEdit);
        nameEdit = (EditText) rootView.findViewById(R.id.nameEdit);
        phoneEdit = (EditText) rootView.findViewById(R.id.phoneEdit);
        remarkEdit = (EditText) rootView.findViewById(R.id.remarkEdit);
        totlaMoneyText =(TextView)rootView.findViewById(R.id.totlaMoney);
        chengrenMinus =(ImageButton)rootView.findViewById(R.id.chengren_minus);
        chengrenMinus.setEnabled(false);
        chengrenNumEdit =(EditText)rootView.findViewById(R.id.chengren_num);
        chengrenPlus =(ImageButton)rootView.findViewById(R.id.chengren_plus);
        chengrenMoneyText =(TextView)rootView.findViewById(R.id.chengren_money);
        ertongMinus =(ImageButton)rootView.findViewById(R.id.ertong_minus);
        ertongMinus.setEnabled(false);
        ertongNumEdit =(EditText)rootView.findViewById(R.id.ertong_num);
        ertongPlus =(ImageButton)rootView.findViewById(R.id.ertong_plus);
        ertongMoneyText =(TextView)rootView.findViewById(R.id.ertong_money);
        tuantiMinus =(ImageButton)rootView.findViewById(R.id.tuanti_minus);
        tuantiMinus.setEnabled(false);
        tuantiNumEdit =(EditText)rootView.findViewById(R.id.tuanti_num);
        tuantiPlus =(ImageButton)rootView.findViewById(R.id.tuanti_plus);
        tuantiMoneyText =(TextView)rootView.findViewById(R.id.tuanti_money);

        LinearLayout money_layout = (LinearLayout) rootView.findViewById(R.id.money_layout);
        if(bean.getMoney()==0){
            chengrenNum =1;
            money_layout.setVisibility(View.GONE);
        }

        chengrenMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(0,null)));
        ertongMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(0,null)));
        tuantiMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(0,null)));

        updateTotalMoney();

        rootView.findViewById(R.id.submit).setOnClickListener(this);
        chengrenMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chengrenNumEdit.setText(--chengrenNum+"");
                setChengrenMinusEnable();
                chengrenMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(DoubleUtil.mul(money,chengrenNum),null)));
                updateTotalMoney();
            }
        });
        chengrenPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chengrenNumEdit.setText(++chengrenNum+"");
                setChengrenMinusEnable();
                chengrenMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(DoubleUtil.mul(money,chengrenNum),null)));
                updateTotalMoney();
            }
        });
        chengrenNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                chengrenNum =Utils.isInteger(s.toString());
                setChengrenMinusEnable();
                chengrenMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(DoubleUtil.mul(money,chengrenNum),null)));
                updateTotalMoney();
            }
        });
        ertongMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ertongNumEdit.setText(--ertongNum+"");
                setErtongMinusEnable();
                ertongMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(DoubleUtil.mul(child_money,ertongNum),null)));
                updateTotalMoney();
            }
        });
        ertongPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ertongNumEdit.setText(++ertongNum+"");
                setErtongMinusEnable();
                ertongMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(DoubleUtil.mul(child_money,ertongNum),null)));
                updateTotalMoney();
            }
        });
        ertongNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ertongNum =Utils.isInteger(s.toString());
                setErtongMinusEnable();
                ertongMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(DoubleUtil.mul(child_money,ertongNum),null)));
                updateTotalMoney();
            }
        });
        tuantiMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tuantiNumEdit.setText(--tuantiNum+"");
                setErtongMinusEnable();
                tuantiMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(DoubleUtil.mul(team_money,tuantiNum),null)));
                updateTotalMoney();
            }
        });
        tuantiPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tuantiNumEdit.setText(++tuantiNum+"");
                setErtongMinusEnable();
                tuantiMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(DoubleUtil.mul(team_money,tuantiNum),null)));
                updateTotalMoney();
            }
        });
        tuantiNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tuantiNum=Utils.isInteger(s.toString());
                setTuantiMinusEnable();
                tuantiMoneyText.setText(String.format(getString(R.string.money2),DoubleUtil.format(DoubleUtil.mul(team_money,tuantiNum),null)));
                updateTotalMoney();

            }
        });

        rootView.findViewById(R.id.hint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHint(v);
            }
        });

    }

    private void setChengrenMinusEnable(){
        if(chengrenNum==0){
            chengrenMinus.setEnabled(false);
        }else{
            chengrenMinus.setEnabled(true);
        }
    }

    private void setErtongMinusEnable(){
        if(ertongNum==0){
            ertongMinus.setEnabled(false);
        }else{
            ertongMinus.setEnabled(true);
        }
    }
    private void setTuantiMinusEnable(){
        if(tuantiNum==0){
            tuantiMinus.setEnabled(false);
        }else{
            tuantiMinus.setEnabled(true);
        }
    }

    private void  updateTotalMoney(){
//       totalMoney =child_money*ertongNum+money*chengrenNum+team_money*tuantiNum;
        totalMoney = DoubleUtil.add(DoubleUtil.add(DoubleUtil.mul(child_money,ertongNum),DoubleUtil.mul(money,chengrenNum)), DoubleUtil.mul(team_money,tuantiNum));
        totlaMoneyText.setText("¥"+DoubleUtil.format(totalMoney,null));
    }


    @Override
    public void loadData() {

    }

    private void submitOrder(String school, String grade, String name, String phone, String remark, int people_number, final int child_number, final int team_number, double money){
        showProgress(getClass().getSimpleName());
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"model", Constant.MODEL_ACTIVITY);
        JSONUtil.put(params,"action", Constant.ACTION_ACTIVITY_JOIN);
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"id", id);
        JSONUtil.put(params,"school", school);
        JSONUtil.put(params,"grade", grade);
        JSONUtil.put(params,"name", name);
        if(!Utils.isEmpty(phone))
            JSONUtil.put(params,"phone", phone);
        if(!Utils.isEmpty(remark))
        JSONUtil.put(params,"remark", remark);
        JSONUtil.put(params,"people_number",people_number);
        JSONUtil.put(params,"child_number",child_number);
        JSONUtil.put(params,"team_number",team_number);
        JSONUtil.put(params,"money",money);
        jsonRequest(true, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
                LogUtil.LogE("预约活动成功...");
                Intent resultIntent = new Intent();
                try {
                    //报名成功即锁定名额，
                    if(bean.getMoney()==0){//免费
                        showTost("预约成功",0);
                        resultIntent.putExtra("num",1);
                    }else{
                        resultIntent.putExtra("num",chengrenNum+child_number+team_number);
                        JSONObject result  =new JSONObject(jsonString);
                        String order_id=JSONUtil.getString(result,"order_id");
//                    String money=JSONUtil.getString(result,"money");
                        Intent payIntent =new Intent(getActivity(), PayingActivity.class);
                        payIntent.putExtra("title",bean.getName());
                        payIntent.putExtra("order_id",order_id);
                        payIntent.putExtra("money",totalMoney);
                        startActivityForResult(payIntent,PAY);
                    }
                    getActivity().setResult(Activity.RESULT_OK,resultIntent);
                    getActivity().finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                showTost(message,1);

            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();

            }
        });

    }

    private boolean checkParams(String school,String grade,String name,String phone){
        if(Utils.isEmpty(school)){
            showTost("请输入孩子所在学校",1);
            schoolEdit.requestFocus();
            return false;
        }
        if(Utils.isEmpty(grade)){
            showTost("请输入孩子所在班级",1);
            gradeEdit.requestFocus();
            return false;
        }
        if(Utils.isEmpty(name)){
            showTost("请输入孩子姓名",1);
            gradeEdit.requestFocus();
            return false;
        }
        if(!Utils.isEmpty(phone)&&!Utils.isMobileNO(phone)){
            showTost("请输入正确的手机号",1);
            phoneEdit.requestFocus();
            return false;
        }
        if(chengrenNum==0&&ertongNum==0&&tuantiNum==0){
            showTost("预约人数不能为0",1);
            return false;
        }
        if(Utils.isInteger(bean.getLimit_number())!=0 && (chengrenNum+ertongNum+tuantiNum)>(Utils.isInteger(bean.getLimit_number())-Utils.isInteger(bean.getApply_number()))){
            showTost("预约人数过多，已超过限制人数...",1);
            return false;
        }
        if(tuantiNum>0 && tuantiNum<bean.getPeople_numer()){
            showTost("团体票最少"+bean.getPeople_numer()+"人",0);
            return false;
        }
        return true;

    }

    private void showHint(View view){
        if(popupWindow==null)
            initPop();
        popupWindow.showAsDropDown(view, -34, 10);
    }

    private void initPop(){
        View view =View.inflate(getActivity(),R.layout.pop_team_hint,null);
        TextView hint = (TextView) view.findViewById(R.id.hint);
        hint.setText(String.format(getString(R.string.team_hint),bean.getPeople_numer()));
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // 在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK){
            switch (requestCode){
                case PAY://支付成功
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                    break;
            }
        }
        if(requestCode ==Activity.RESULT_CANCELED){
            showTost("请到我的活动完成支付",1);
            getActivity().finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.submit) {
            String school = schoolEdit.getText().toString().trim();
            String grade = gradeEdit.getText().toString().trim();
            String name = nameEdit.getText().toString().trim();
            String phone = phoneEdit.getText().toString().trim();
            String remark = remarkEdit.getText().toString().trim();
            if (checkParams(school, grade, name, phone)) {
                submitOrder(school, grade, name, phone, remark, chengrenNum, ertongNum, tuantiNum, totalMoney);
            }


        }
    }

    @Override
    public void onDestroyView() {
        Communications.cancelRequest(getClass().getSimpleName());
        super.onDestroyView();
    }
}
