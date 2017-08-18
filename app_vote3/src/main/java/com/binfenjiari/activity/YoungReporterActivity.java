package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.binfenjiari.fragment.YoungReportFragment;
import com.binfenjiari.fragment.YoungReportRecommendFragment;
import com.binfenjiari.fragment.YoungReportWorksFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class YoungReporterActivity extends BaseActivity {

    public final static int TYPE_MAIN = 0;

    /**
     * 优秀作品
     */
    public final static int TYPE_EXCELLENT_WORKS = 1;

    /**
     * 小记者主题  小记者作品
     */
    public final static int TYPE_TOPIC_WORKS = 2;


    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon(null);

        int type = getIntent().getIntExtra("type", TYPE_MAIN);
        if (type == TYPE_EXCELLENT_WORKS) {
            return YoungReportRecommendFragment.newInstance();
        } else if (type == TYPE_TOPIC_WORKS) {
            return YoungReportWorksFragment.newInstance();
        } else {
            return YoungReportFragment.newInstance();
        }

    }

    @Override
    protected String getToolbarTitle() {
        int type = getIntent().getIntExtra("type", TYPE_MAIN);
        if (type == TYPE_EXCELLENT_WORKS) {
            return "优秀作品";
        } else if (type == TYPE_TOPIC_WORKS) {
            return "优秀作品";
        } else {
            return "小记者";
        }
    }

    /**
     * 小记者首页
     *
     * @param context
     */
    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, YoungReporterActivity.class);
//        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);
    }

    /**
     * 小记者  优秀作品 推荐
     *
     * @param context
     */
    public static void beginActivityForRecommend(Context context) {
        Intent intent = new Intent(context, YoungReporterActivity.class);
        intent.putExtra("type", TYPE_EXCELLENT_WORKS);
        context.startActivity(intent);
    }

    /**
     * 小记者  优秀作品 集
     *
     * @param context
     */
    public static void beginActivityForWorks(Context context) {
        Intent intent = new Intent(context, YoungReporterActivity.class);
        intent.putExtra("type", TYPE_TOPIC_WORKS);
        context.startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.young_report, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int i = item.getItemId();
//        if (i == R.id.action_edit) {
////            search();
//        }
//        return super.onOptionsItemSelected(item);
//    }


}
