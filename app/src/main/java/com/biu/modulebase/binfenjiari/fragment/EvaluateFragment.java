package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.EvaluateVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/5/23
 */
public class EvaluateFragment extends BaseFragment {

    private LinearLayout evaluate_layout;

    private EditText phoneEdit;
    private EditText contentEdit;
    private Button commitBtn;

    private String id;

    private int position = -1;

    /**
     * <project_id,score>
     **/
    private HashMap<Object, Object> scores = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_evaluate, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }


    @Override
    protected void getIntentData() {
        position = getActivity().getIntent().getIntExtra("position", -1);
        id = getActivity().getIntent().getStringExtra(Constant.KEY_ID);
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        evaluate_layout = (LinearLayout) rootView.findViewById(R.id.evaluate_layout);
        phoneEdit = (EditText) rootView.findViewById(R.id.phoneEdit);
        contentEdit = (EditText) rootView.findViewById(R.id.contentEdit);
        rootView.findViewById(R.id.commitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEdit.getText().toString();
                String phone = phoneEdit.getText().toString();
                if (Utils.isEmpty(content)) {
                    showTost("请输入评价内容", 0);
                    return;
                }
                if (scores.size() < evaluate_layout.getChildCount()) {
                    showTost("请先进行评价", 0);
                    return;
                }
                doEvaluate(content, phone);
            }
        });

    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {
        getList();
    }

//    public static String set2json(Set<?> set) {
//        StringBuilder json = new StringBuilder();
//        json.append("[");
//        if (set != null && set.size() > 0) {
//            for (Object obj : set) {
//                json.append(object2json(obj));
//                json.append(",");
//            }
//            json.setCharAt(json.length() - 1, ']');
//        } else {
//            json.append("]");
//        }
//        return json.toString();
//    }

    private void doEvaluate(String content, String phone) {
        showProgress(getClass().getSimpleName());
        JSONUtil.toJson(scores.keySet()).toString();
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", Constant.MODEL_EVALUATE);
        JSONUtil.put(params, "action", Constant.ACTION_EVALUATE);
        JSONUtil.put(params, Constant.KEY_ID, id);
        JSONUtil.put(params, "token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
        if (!Utils.isEmpty(phone))
            JSONUtil.put(params, "phone", phone);
        JSONUtil.put(params, "content", content);
        JSONUtil.put(params, "project_id", JSONUtil.toJson(scores.keySet()).toString());
        JSONUtil.put(params, "score", JSONUtil.toJson(scores.values()).toString());
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
                showTost("评价成功", 1);
                Intent intent = new Intent();
                intent.putExtra("position", position);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                showTost(message, 1);

            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
            }
        });
    }

    private void getList() {
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", Constant.MODEL_ACTIVITY);
        JSONUtil.put(params, "action", Constant.ACTION_GET_EVALUATE_LIST);
        JSONUtil.put(params, Constant.KEY_ID, id);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                LogUtil.LogD(jsonString);
                try {
                    JSONObject result = new JSONObject(jsonString);
                    ArrayList<EvaluateVO> list = JSONUtil.fromJson(JSONUtil.getJSONArray(result, "list").toString(), new TypeToken<List<EvaluateVO>>() {
                    }.getType());
                    addEvaluate(list);
                    inVisibleLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if (key == 3) {
                    showTost("评价项为空...", 1);
                } else {
                    showTost(message, 1);
                }

            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();

            }
        });
    }

    private void addEvaluate(ArrayList<EvaluateVO> list) {
        for (int i = 0; i < list.size(); i++) {
            EvaluateVO bean = list.get(i);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_evaluate, null);
            TextView name = (TextView) view.findViewById(R.id.name);
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
            name.setText(bean.getDescription());
            ratingBar.setOnRatingBarChangeListener(new MyOnRatingBarChangeListener(Utils.isInteger(bean.getId())));
            evaluate_layout.addView(view);
        }

    }

    class MyOnRatingBarChangeListener implements RatingBar.OnRatingBarChangeListener {

        private int id;

        public MyOnRatingBarChangeListener(int id) {
            this.id = id;
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            scores.put(id, rating);
        }
    }
}
