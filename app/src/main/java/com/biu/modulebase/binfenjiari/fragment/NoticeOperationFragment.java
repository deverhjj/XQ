package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.NoticeOperationActivity;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.NoticeVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONObject;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/1/25
 */
public class NoticeOperationFragment extends BaseFragment {

    private static final String KEY_ACTION_TYPE="actionType";

    private static final String KEY_NOTICE_ID="notice_id";

    private static final String KEY_CIRCLE_ID="circleId";

    private static final String KEY_NOTICE = "notice";

    public static final String EXTRA_NOTICE="notice";

    private static final String TAG = "NoticeOperationFragment";


    private EditText et_name;

    private EditText et_intro;

    private TextView hintNum;

    private String mCircleId;

    private int mActionType;

    private String mNoticeId;

    private NoticeVO mNotice;

    private String mOrgiName;

    private String mOrgiInto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            mActionType = args.getInt(KEY_ACTION_TYPE, -1);
            mNoticeId = args.getString(KEY_NOTICE_ID);
            mCircleId=args.getString(KEY_CIRCLE_ID);
            mNotice= (NoticeVO) args.getSerializable(KEY_NOTICE);
        }
    }

    public static NoticeOperationFragment newInstance(String circleId,int actionType, String noticeId,NoticeVO notice) {
        Bundle args = new Bundle();
        args.putInt(KEY_ACTION_TYPE, actionType);
        args.putString(KEY_NOTICE_ID, noticeId);
        args.putString(KEY_CIRCLE_ID,circleId);
        args.putSerializable(KEY_NOTICE,notice);
        NoticeOperationFragment noticeOperationFragment = new NoticeOperationFragment();
        noticeOperationFragment.setArguments(args);
        return noticeOperationFragment;
    }


    /**
     * 初始化控件
     */
    protected void initView(View rootView) {
        et_name = (EditText) rootView.findViewById(R.id.name);
        et_name.setText(
                (mActionType == NoticeOperationActivity.ACTION_EDIT && mNotice != null) ? mNotice
                        .getTitle() : "");
        mOrgiName = et_name.getText().toString();

        et_intro = (EditText) rootView.findViewById(R.id.intro);
        et_intro.setText(
                (mActionType == NoticeOperationActivity.ACTION_EDIT && mNotice != null) ? mNotice
                        .getContent() : "");
        hintNum =(TextView)rootView.findViewById(R.id.extra_num);
        et_intro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                hintNum.setText((140-length)+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mOrgiInto = et_intro.getText().toString();
    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.fragment_notice_edit, container, false);
        initView(rootView);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.save,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_save) {
            uploadData();

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkEmpty(String name,String into){
        boolean hasName = !TextUtils.isEmpty(name);
        boolean hasInto = !TextUtils.isEmpty(into);
        if (!hasName || !hasInto) {
            OtherUtil.showToast(getActivity(), !hasName ? "名称不能为空" : "内容不能为空");
            return false;
        }
        if(name.length()<2 || name.length()>10){
            showTost("标题在2-10个字符之内",0);
            return false;
        }
        if(into.length()>140){
            showTost("内容在140个字符之内",0);
            return false;
        }
        return true;
    }
    private void uploadData() {
        JSONObject params = new JSONObject();
        final int action = mActionType;
         String name=mOrgiName;
         String into=mOrgiInto;

        if (action == NoticeOperationActivity.ACTION_ADD) {
            name = et_name.getText().toString();
            into = et_intro.getText().toString();
            if(!checkEmpty(name,into)){
                return;
            }
            JSONUtil.put(params,"model", Constant.MODEL_CIRCLE_MANAGE);
            JSONUtil.put(params,"action", Constant.ACTION_ADD_NOTICE);
            JSONUtil.put(params,"circle_id", mCircleId);

        } else if (action == NoticeOperationActivity.ACTION_EDIT) {
            String curName=et_name.getText().toString();
            String curInto=et_intro.getText().toString();
            boolean nameChanged=!curName.equals(name);
            boolean intoChanged=!curInto.equals(into);
            if (!nameChanged && !intoChanged) {
                Activity activity=getActivity();
                if (activity!=null) {
                    activity.finish();
                }
                return;
            }

            name=curName;
            into=curInto;

            final String noticeId=mNoticeId;
            JSONUtil.put(params,"announce_id", noticeId);
        }
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"title", name);
        JSONUtil.put(params,"content", into);
        jsonRequest(true, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                Intent intent=new Intent();
                getActivity().setResult(Activity.RESULT_OK,intent);
                getActivity().finish();
            }

            @Override
            public void onCodeError(int key, String message) {
                showTost(message,0);
            }

            @Override
            public void onConnectError(String message) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Communications.cancelRequest(TAG);
    }
}
