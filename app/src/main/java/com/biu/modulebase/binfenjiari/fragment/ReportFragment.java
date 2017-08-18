package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.ReportVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.DialogFactory;
import com.biu.modulebase.binfenjiari.widget.wheeltime.CityMain;
import com.biu.modulebase.binfenjiari.widget.wheeltime.EmotionMain;
import com.biu.modulebase.binfenjiari.widget.wheeltime.OnOkSelectorListener;
import com.biu.modulebase.binfenjiari.widget.wheeltime.WheelMain;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lee
 * @Title: {举报}
 * @Description:{描述}
 * @date 2016/5/25
 */
public class ReportFragment extends BaseFragment {

    private ArrayList<ReportVO> list =new ArrayList<>();

    private TextView reportName;
    /***举报类型id*/
    private  String type_id;
    /**举报模块项id**/
    private String project_id;
    /**举报模块**/
    private int type;
    /**举报内容**/
    private String content;
    private EditText contentEdit;


    @Override
    protected void getIntentData() {
        Intent intent =getActivity().getIntent();
        project_id =intent.getStringExtra("project_id");
        type =intent.getIntExtra("type",-1);
    }

    @Override
    protected void initView(View rootView) {
        setHasOptionsMenu(true);

        contentEdit =(EditText)rootView.findViewById(R.id.content);
        reportName =(TextView)rootView.findViewById(R.id.reportName);
        rootView.findViewById(R.id.report_layout).setOnClickListener(this);
    }


    @Override
    public void loadData() {
        getReoprtList();
    }

    private void getReoprtList(){
        showProgress(getClass().getSimpleName());
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_COMMON);
        JSONUtil.put(params,"action",Constant.ACTION_GET_REPORT_REASON);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
                list =JSONUtil.fromJson(jsonString,new TypeToken<List<ReportVO>>(){}.getType());


            }

            @Override
            public void onCodeError(int key, String message) {
                switch (key){
                    case 3:
                        showTost("获取举报类型失败，请稍后再试",1);
                        break;
                }
                dismissProgress();
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
            }
        });
    }

    /**
     * 举报
     */
    private void doReport(){
        showProgress(getClass().getSimpleName());
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_COMMON);
        JSONUtil.put(params,"action",Constant.ACTION_REPORT);
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"type_id",type_id);//举报类型id
        JSONUtil.put(params,"type",type);//举报模块
        JSONUtil.put(params,"project_id",project_id);//具体项id
        JSONUtil.put(params,"content",content);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
                showTost("举报成功",0);
                getActivity().finish();

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

    private boolean checkParams(){
        if(content.length()>250){
            showTost("举报内容过长，请重新输入",1);
            contentEdit.requestFocus();
            return false;
        }
        if(Utils.isEmpty(content)){
            showTost("请输入举报内容",1);
            contentEdit.requestFocus();
            return false;
        }
        if(Utils.isEmpty(type_id)){
            showTost("请选择举报类型",1);
            return false;
        }
        return true;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.commit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_commit) {
            content = contentEdit.getText().toString();
            if (checkParams()) {
                doReport();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i1 = v.getId();
        if (i1 == R.id.report_layout) {
            if (list.size() > 0) {
                List<String> datas = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    datas.add(list.get(i).getContent());
                }
                DialogFactory.showEmotionAlert(getActivity(), datas, new OnOkSelectorListener() {
                    @Override
                    public void onOkSelector(WheelMain wheelMain) {

                    }

                    @Override
                    public void onOkSelector(CityMain wheelMain) {

                    }

                    @Override
                    public void onOkSelector(EmotionMain wheelMain) {
                        String data = wheelMain.getSelectedData();
                        reportName.setText(data);
                        type_id = list.get(wheelMain.getCurrentPosition()).getId();
                        LogUtil.LogD(type_id + "");
                    }
                });
            }


        }
    }

    @Override
    public void onDestroyView() {
        Communications.cancelRequest(getClass().getSimpleName());
        super.onDestroyView();
    }
}
