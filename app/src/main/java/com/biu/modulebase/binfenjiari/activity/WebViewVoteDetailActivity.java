package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;

import com.biu.modulebase.binfenjiari.fragment.WebViewVoteDetailFragment;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author tangjin
 * @Title: {投票详情 通用}
 * @Description:{投票详情 资讯详情 投票项详情}
 * @date 2016/6/13
 *
 * Intent intent = new Intent(context, WebViewVoteDetailActivity.class);
 * intent.putExtra("project_id",proid);
 * intent.putExtra("id",bannerVO.getId());
 * intent.putExtra("project_title",bannerVO.getTitle());
 * intent.putExtra("title",bannerVO.getTitle());
 * intent.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDINFOMATIONINFO);
 * startActivity(intent);
 */
public class WebViewVoteDetailActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new WebViewVoteDetailFragment();
    }

    @Override
    protected String getToolbarTitle() {
                String title = Utils.isString(getIntent().getStringExtra("title1"));
        Log.e("title------->",title);
        if (title.length() > 14) {
            return title.substring(0, 14) + "...";
        } else {
            return title;
        }

    }

    @Override
    public void onBackPressed() {
        WebViewVoteDetailFragment fragment = (WebViewVoteDetailFragment) getSupportFragmentManager().getFragments().get(0);
        if(!fragment.onBackPressed()){
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }
}
