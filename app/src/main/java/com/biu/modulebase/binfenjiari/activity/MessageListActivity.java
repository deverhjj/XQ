package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.MessageListFragment;
import com.biu.modulebase.common.base.BaseActivity;


/**
 * @author Lee
 * @Title: {消息列表}
 * @Description:{描述}
 * @date 2016/1/25
 */
public class MessageListActivity extends BaseActivity {

    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        Intent intent = getIntent();
        String messageType = intent != null ? intent.getStringExtra(MessageListFragment.KEY_MESSAGE_TYPE) : "";
        return MessageListFragment.newInstance(messageType);
    }

    @Override
    protected String getToolbarTitle() {
        Intent intent = getIntent();
        String messageType = intent != null ? intent.getStringExtra(MessageListFragment.KEY_MESSAGE_TYPE) : "";
        String title ="";
        if(messageType.equals(MessageListFragment.MESSAGE_TYPE_CIRCLE)){
            title="圈子消息";
        }else if(messageType.equals(MessageListFragment.MESSAGE_TYPE_POST)){
            title="帖子消息";
        }else if(messageType.equals(MessageListFragment.MESSAGE_TYPE_SYSTEM)){
            title="系统消息";
        }
        return title;

    }

    public void doPositiveClick() {
        MessageListFragment fragment = (MessageListFragment) getSupportFragmentManager().getFragments().get(0);
        fragment.deleteAllMessage();
    }

    public void doNegativeClick() {

    }

}
