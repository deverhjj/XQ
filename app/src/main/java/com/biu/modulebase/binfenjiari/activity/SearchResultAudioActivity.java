package com.biu.modulebase.binfenjiari.activity;


import android.content.Intent;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.AvAllFragment;
import com.biu.modulebase.binfenjiari.fragment.AvHomeAllFragment;
import com.biu.modulebase.common.base.BaseActivity;

import java.util.HashMap;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/6/5
 */
public class SearchResultAudioActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        Intent intent = getIntent();
        String title = intent != null ? intent.getStringExtra("title") : null;
        HashMap<String, Object> args = new HashMap<>();
        args.put(Constant.KEY_SEARCH_ARGS_TITLE, title);
        args.put("SearchResultAudioActivity",true);
        args.put("type","0");
//        return AvAllFragment.newInstance(args);
        return AvHomeAllFragment.newInstance(args);
    }

    @Override
    protected String getToolbarTitle() {
        return "搜索结果";
    }
}
