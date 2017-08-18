package com.binfenjiari.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.binfenjiari.R;
import com.binfenjiari.activity.NaviHomeSearchAudioVideoActivity;
import com.binfenjiari.activity.YoungReporterActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;

/**
 * @author tangjin
 * @Title: {视听库}
 * @Description:{描述}
 * @date 2017/6/12
 */
public class FixedRecycleScrollView_Navi_AV extends FrameLayout {

    private RecyclerView mRecyclerView;

    private FrameLayout fl_view_more;

    /**
     * 是否初始化数据
     */
    private boolean isInitData = false;

    public FixedRecycleScrollView_Navi_AV(Context context) {
        this(context, null);
    }

    public FixedRecycleScrollView_Navi_AV(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedRecycleScrollView_Navi_AV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.widget_fixrecycle_scroll_view_navi_av, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fixview_recyclerview);
        fl_view_more = (FrameLayout) view.findViewById(R.id.fl_view_more);
//        navigationsliderview_ll_dot_group_banner = (LinearLayout) view.findViewById(R.id.navigationsliderview_ll_dot_group_banner);

//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
//        boolean refreshAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_refresh_able, true);
//        loadMoreAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_load_more_able,true);
//        if(!refreshAble){
//            mSwipeRefreshLayout.setEnabled(false);
//        }


//        FixedRecycleScrollView view = (FixedRecycleScrollView) holder.itemView;
//        RecyclerView recyclerview = view.getRecyclerView();
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerview.getLayoutParams();
//        //高度等于＝条目的高度＋ 10dp的间距 ＋ 10dp（为了让条目居中）
//        layoutParams.height = 300;
//        recyclerview.setLayoutParams(layoutParams);


    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    /**
     * 我爱动漫
     */
    public void setLinearLayoutData() {
        if (isInitData)
            return;
        isInitData = true;
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.item_navi_home_love_av, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {


                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {
                                YoungReporterActivity.beginActivityForWorks(getContext());

                            }
                        });

                holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                return holder;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0,
                        getContext().getResources().getDimensionPixelSize(com.biu.modulebase.binfenjiari.R.dimen.item_divider_height_8dp),
                        0);
            }
        };
        mRecyclerView.setPadding(getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0);
        mRecyclerView.setClipToPadding(false);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        adapter.testLayoutData(6);

    }



}
