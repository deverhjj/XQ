package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
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
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * @author Lee
 * @Title: {个性签名}
 * @Description:{描述}
 * @date 2016/4/14
 */
public class SignatureFragment extends BaseFragment {

    public final static String  NEW_SIGNATURE ="new_signature";

    public static final String EXTRA_OLD_USER_NAME="oldUserName";
    public static final String EXTRA_NEW_USER_NAME="newUserName";

    private TextView hintNum;

    private EditText editText;


    private String mOldUserName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_signature, container, false);
        return layout;
    }

    public static SignatureFragment getInstance(String oldUserName) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_OLD_USER_NAME, oldUserName);
        SignatureFragment signatureFragment = new SignatureFragment();
        signatureFragment.setArguments(bundle);
        return signatureFragment;
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

        hintNum = (TextView) rootView.findViewById(R.id.hintNum);
        editText = (EditText) rootView.findViewById(R.id.editText);
        editText.setText(mOldUserName);
        hintNum.setText((30-mOldUserName.length())+"");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                hintNum.setText((30-length)+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
                    result.putExtra(NEW_SIGNATURE, signature);
                    getActivity().setResult(Activity.RESULT_OK, result);
                    getActivity().finish();
                } else {
                    showTost("请输入新的个性签名", 0);
                }

            } else {
                showTost("个性签名在0-30个字以内", 0);
            }


        }
        return super.onOptionsItemSelected(item);
    }

}
