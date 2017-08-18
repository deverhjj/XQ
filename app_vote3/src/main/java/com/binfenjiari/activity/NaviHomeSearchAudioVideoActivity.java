package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.fragment.MineCollectVideoFragment;
import com.binfenjiari.fragment.MineMovementFragment;
import com.biu.modulebase.binfenjiari.activity.SearchActivity;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author tangjin
 * @Title: {视听搜索}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class NaviHomeSearchAudioVideoActivity extends BaseActivity {

    public final static int TYPE_LOGIN = 0;

    private TextView searchText;

    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon(null);
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        return MineCollectVideoFragment.newInstance();

    }

    @Override
    protected String getToolbarTitle() {

        searchText = (TextView) setToolBarCustomView(R.layout.item_search_title_view);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            search();
        }
        return super.onOptionsItemSelected(item);
    }

    private void search() {
        SearchActivity.beginActivity(this, Constant.SEARCH_TAG);
    }

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, NaviHomeSearchAudioVideoActivity.class);
//        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);

    }


}
