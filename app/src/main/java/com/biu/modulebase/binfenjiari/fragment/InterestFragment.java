package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
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

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/4/14
 */
public class InterestFragment extends BaseFragment {

    public static final String EXTRA_OLD_USER_NAME="oldUserName";
    public static final String EXTRA_NEW_USER_NAME="newUserName";

    private EditText editText;

    public final static String  NEW_HOBBY ="new_hobby";

    private EditText mNewUserName;

    private String mOldUserName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_interest, container, false);
        return layout;
    }

    public static InterestFragment getInstance(String oldUserName) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_OLD_USER_NAME, oldUserName);
        InterestFragment interestFragment = new InterestFragment();
        interestFragment.setArguments(bundle);
        return interestFragment;
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        setHasOptionsMenu(true);
        Bundle bundle=getArguments();
        if (bundle!=null) {
            mOldUserName=bundle.getString(EXTRA_OLD_USER_NAME);
        }
        getBaseActivity().setBackNavigationIcon();
        editText =(EditText)rootView.findViewById(R.id.editText);
        editText.setText(mOldUserName);
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

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sure, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_sure) {
            String signature = editText.getText().toString();
            if (!Utils.isEmpty(signature)) {
                if (!signature.equals(mOldUserName)) {
                    Intent result = new Intent();
                    result.putExtra(NEW_HOBBY, signature);
                    getActivity().setResult(Activity.RESULT_OK, result);
                    getActivity().finish();
                } else {
                    showTost("请输入新的兴趣爱好", 0);
                }

            } else {
                showTost("兴趣爱好在0-30个字以内", 0);
            }

        }
        return super.onOptionsItemSelected(item);
    }

}
