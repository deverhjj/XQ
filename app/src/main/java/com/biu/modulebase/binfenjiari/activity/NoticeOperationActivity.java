package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CircleFragment;
import com.biu.modulebase.binfenjiari.fragment.NoticeListFragment;
import com.biu.modulebase.binfenjiari.fragment.NoticeOperationFragment;
import com.biu.modulebase.binfenjiari.model.NoticeVO;
import com.biu.modulebase.common.base.BaseActivity;


/**
 * @author Lee
 * @Title: {编辑或添加公告}
 * @Description:{描述}
 * @date 2016/1/25
 */
public class NoticeOperationActivity extends BaseActivity {

    public static final String EXTRA_ACTION_NOTICE="action_notion";

    public static final int ACTION_ADD=0;

    public  static final int ACTION_EDIT=1;

    @Override
    protected Fragment getFragment() {
        int action = getAction();
        Intent intent = getIntent();
        String  noticeId = intent != null ? intent.getStringExtra(NoticeListFragment.NOTICE_ID) : "";
        String circleId = intent != null ? intent.getStringExtra(CircleFragment.EXTRA_CIRCLE_ID):"";
        NoticeVO notice= intent!=null? (NoticeVO) intent.getSerializableExtra(NoticeOperationFragment.EXTRA_NOTICE) :null;
        return NoticeOperationFragment.newInstance(circleId,action, noticeId,notice);
    }

    @Override
    protected String getToolbarTitle() {
        setBackNavigationIcon();
        int action = getAction();
        return action == ACTION_ADD ? "公告添加" : "公告编辑";
    }

    private int getAction() {
        Intent intent=getIntent();
        return intent!=null?intent.getIntExtra(EXTRA_ACTION_NOTICE,ACTION_ADD):ACTION_ADD;
    }
}
