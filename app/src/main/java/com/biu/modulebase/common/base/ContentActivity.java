package com.biu.modulebase.common.base;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.biu.modulebase.binfenjiari.R;


/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/15
 */
public abstract class ContentActivity extends BaseActivity {

    private FrameLayout fragmentContainer;

    private LayoutInflater mLayoutInflater;

    protected void init() {
        mLayoutInflater = LayoutInflater.from(this);

        setTitle(null);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(getToolbarTitle());

        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);

        mLayoutInflater.inflate(getContentView(), fragmentContainer, true);

        setViewData();
    }

    protected abstract int getContentView();

    protected abstract void setViewData();


    @Override
    protected Fragment getFragment() {
        return null;
    }


}
