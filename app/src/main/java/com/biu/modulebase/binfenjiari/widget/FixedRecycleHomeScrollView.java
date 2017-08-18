package com.biu.modulebase.binfenjiari.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.EventDetailActivity;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.HomeFragment;
import com.biu.modulebase.binfenjiari.model.EventVO;
import com.biu.modulebase.binfenjiari.model.HomeRecommendAct;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/12
 */
public class FixedRecycleHomeScrollView extends FrameLayout {

    private RecyclerView mRecyclerView;

    private TextView fixview_tv_text_name;

    /**
     * 是否初始化数据
     */
    private boolean isInitData = false;

    public FixedRecycleHomeScrollView(Context context) {
        this(context, null);
    }

    public FixedRecycleHomeScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedRecycleHomeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.widget_fixrecycle_home_scroll_view, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fixview_recyclerview);
        fixview_tv_text_name = (TextView) view.findViewById(R.id.fixview_tv_text_name);
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
     * 设置推荐活动
     */
    public void setRecommendData(final Fragment fragment, HomeRecommendAct homeRecommendVO) {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText("推荐活动");
        BaseAdapter<EventVO> adapter = new BaseAdapter<EventVO>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_fixrecycle_item_home_recommend_activity, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                EventVO eventVO = (EventVO) data;
                                String banner_pic = eventVO.getBanner_pic();
                                String name = eventVO.getName();
                                String limit_num = eventVO.getLimit_number();
                                String apply_num = eventVO.getApply_number();

                                holder.setNetImage(Constant.IMG_COMPRESS, R.id.banner, banner_pic, ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                                holder.setText(R.id.title, name);
                                if (Utils.isInteger(limit_num) > 0) {
                                    holder.setText(R.id.baoming, String.format("报名人数: %1$s/%2$s", apply_num, limit_num));
                                } else {
                                    holder.setText(R.id.baoming, "报名人数: 无限制");
                                }

                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {
                                final Object object = getData(position);
                                String id;
                                String name;
                                String latitude = null;
                                String longitude = null;
                                String address = null;
                                EventVO event = (EventVO) object;
                                id = event.getId();
                                name = event.getName();
                                latitude = event.getLatitude();
                                longitude = event.getLongitude();
                                address = event.getAddress();

                                Intent intent = new Intent(getContext(), EventDetailActivity.class);
                                intent.putExtra(Constant.KEY_ID, id);
                                intent.putExtra("position", position);
                                fragment.startActivityForResult(intent, HomeFragment.HOME_LIKE_REQUEST);

                            }
                        });

                holder.setItemChildViewClickListener(R.id.banner);
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
        adapter.setData(homeRecommendVO.getDatas());
//        adapter.testLayoutData(homeRecommendVO.getDatas().size());

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
