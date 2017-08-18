package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.WebViewFragment;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseActivity;
/*import android.webkit.WebViewFragment;*/

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/6/13
 */
public class WebViewActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new WebViewFragment();
    }

    @Override
    protected String getToolbarTitle() {
        String title = Utils.isString(getIntent().getStringExtra("title"));
        if(title.length()>14){
            return title.substring(0,14)+"...";
        }else{
            return title;
        }

    }
}
