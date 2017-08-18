package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.CommentDetailFragment2;
import com.biu.modulebase.binfenjiari.model.CommentItem;
import com.biu.modulebase.common.base.BaseActivity;

import java.util.HashMap;

/**
 * Created by jhj_Plus on 2016/5/27.
 */
public class CommentDetailActivity extends BaseActivity {
    private static final String TAG = "CommentDetailActivity";

    private Callback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {

        Intent intent = getIntent();

        HashMap<String, Object> args =
                intent != null ? (HashMap<String, Object>) intent.getSerializableExtra(
                        Constant.KEY_ARGS) : null;
        CommentItem commentItem=
                intent!=null? (CommentItem) intent.getSerializableExtra(Constant.KEY_COMMENT) :null;

        return CommentDetailFragment2.newInstance(args,commentItem);
    }

    @Override
    protected String getToolbarTitle() {
        return "评论详情";
    }

    @Override
    public void onBackPressed() {
        if (mCallback != null) {
            mCallback.onBackPressed();
        }
        super.onBackPressed();
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof Callback) {
            mCallback = (Callback) fragment;
        }
    }

    public interface Callback {
        void onBackPressed();
    }
}
