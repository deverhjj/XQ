package com.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.activity.LoginActivity;
import com.binfenjiari.base.AppExp;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.fragment.contract.UserContract;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Msgs;
import com.binfenjiari.utils.Uis;
import com.binfenjiari.utils.Views;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public abstract class UserFragment extends AppFragment<UserContract.UserPresenter>
        implements UserContract.UserView
{
    private static final String TAG = UserFragment.class.getSimpleName();

    @Override
    public void showPostFailureUi(AppExp exp) {
        super.showPostFailureUi(exp);
        Msgs.shortToast(getContext(), exp.msg());
    }
}
