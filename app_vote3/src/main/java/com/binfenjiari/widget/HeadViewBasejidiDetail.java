package com.binfenjiari.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.binfenjiari.R;
import com.binfenjiari.activity.MovementEvaluateActivity;
import com.binfenjiari.activity.MovementListActivity;
import com.binfenjiari.activity.ProjectListActivity;

/**
 * @author tangjin
 * @Title: {活动详情头内容}
 * @Description:{描述}
 * @date 2017/6/12
 */
public class HeadViewBasejidiDetail {

    private Context mContext;

    public HeadViewBasejidiDetail(Context context) {
        this.mContext = context;
    }


    public void setGroupView(ViewGroup viewGroup) {
        View view = View.inflate(mContext, R.layout.part_basejidi_detail_head_view, null);
//        UserView view = LayoutInflater.from(mContext).inflate(R.layout.part_movement_detail_head_view, null, false);
        viewGroup.addView(view);


        view.findViewById(R.id.ratings_movement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //评价
                MovementEvaluateActivity.beginActivity(mContext);
            }
        });


        view.findViewById(R.id.fl_cur_movement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当前活动
                MovementListActivity.beginActivity(mContext);
            }
        });

        view.findViewById(R.id.fl_cur_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //基地
                ProjectListActivity.beginActivity(mContext);
            }
        });



    }


}
