package com.binfenjiari.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.activity.NaviHomeSearchAudioVideoActivity;
import com.binfenjiari.activity.YoungReporterActivity;
import com.binfenjiari.widget.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.binfenjiari.widget.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;

/**
 * @author tangjin
 * @Title: {视听库}
 * @Description:{描述}
 * @date 2017/6/12
 */
public class FixedRecycleScrollView_AV extends FrameLayout {

    private RecyclerView mRecyclerView;

    private FrameLayout fl_view_more;

    /**
     * 是否初始化数据
     */
    private boolean isInitData = false;

    public FixedRecycleScrollView_AV(Context context) {
        this(context, null);
    }

    public FixedRecycleScrollView_AV(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedRecycleScrollView_AV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.widget_fixrecycle_scroll_view_av, this);
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
     * 设置视听系列
     */
    public void setGridViewData() {
        if (isInitData)
            return;
        isInitData = true;

        fl_view_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NaviHomeSearchAudioVideoActivity.beginActivity(getContext());
            }
        });

        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.item_mine_colloection_av, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {


                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {


                            }
                        });

                holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                return holder;
            }
        };

        mRecyclerView.setPadding(getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.spacing_normal), false));
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        adapter.testLayoutData(4);

    }


    /**
     * 设置RecyclerView GridLayoutManager or StaggeredGridLayoutManager spacing
     * Created by john on 17-1-5.
     */

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
