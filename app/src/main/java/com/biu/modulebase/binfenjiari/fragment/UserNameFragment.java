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
 * Created by jhj_Plus on 2016/2/25.
 */
public class UserNameFragment extends BaseFragment {
    private static final String TAG = "UserNameFragment";

    public static final String EXTRA_OLD_USER_NAME="oldUserName";
    public static final String EXTRA_NEW_USER_NAME="newUserName";
    
    private EditText mNewUserName;

    private String mOldUserName;

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {

    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle=getArguments();
        if (bundle!=null) {
           mOldUserName=bundle.getString(EXTRA_OLD_USER_NAME);
        }
    }

    public static UserNameFragment getInstance(String oldUserName) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_OLD_USER_NAME, oldUserName);
        UserNameFragment userNameFragment = new UserNameFragment();
        userNameFragment.setArguments(bundle);
        return userNameFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.fragment_user_name,container,false);

        mNewUserName= (EditText) rootView.findViewById(R.id.et_new_user_name);
        mNewUserName.setText(mOldUserName);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId=item.getItemId();
        if (menuId==R.id.action_save) {
            String newUserName=mNewUserName.getText().toString();
            int length =mNewUserName.length();
            if(length<2 ||length>8){
                showTost("昵称必须在2~8个字符之间",0);
            }else{
                if (Utils.isUserNameCorrect(newUserName)) {
                    if (!newUserName.equals(mOldUserName)) {
                        Intent result=new Intent();
                        result.putExtra(EXTRA_NEW_USER_NAME,mNewUserName.getText().toString());
                        getActivity().setResult(Activity.RESULT_OK,result);
                    }
                    getActivity().finish();
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
