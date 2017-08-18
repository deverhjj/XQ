package com.binfenjiari.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.binfenjiari.R;
import com.binfenjiari.activity.CommVoteActivity;
import com.binfenjiari.activity.OtherPersonCenterActivity;
import com.binfenjiari.activity.ReportWorksPhotoActivity;
import com.binfenjiari.activity.YoungReportTopicActivity;
import com.binfenjiari.activity.YoungReporterActivity;
import com.binfenjiari.activity.YoungReporterGracefulActivity;
import com.binfenjiari.activity.YoungReporterMediaActivity;
import com.binfenjiari.fragment.NaviHomePageFragment;
import com.binfenjiari.fragment.ReportWorksPhotoFragment;
import com.binfenjiari.widget.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.binfenjiari.widget.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/12
 */
public class FixedRecycleScrollView extends FrameLayout {

    private RecyclerView mRecyclerView;

    private TextView fixview_tv_text_name;

    private TextView fixview_tv_text_more;

    /**
     * 是否初始化数据
     */
    private boolean isInitData = false;

    public FixedRecycleScrollView(Context context) {
        this(context, null);
    }

    public FixedRecycleScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedRecycleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.widget_fixrecycle_scroll_view, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fixview_recyclerview);
        fixview_tv_text_name = (TextView) view.findViewById(R.id.fixview_tv_text_name);
        fixview_tv_text_more = (TextView) view.findViewById(R.id.fixview_tv_text_more);
        fixview_tv_text_more.setVisibility(View.GONE);
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
     * 设置右边的更多按钮
     */
    public void setRightTextMore(OnClickListener listener) {
        if (listener == null) {
            fixview_tv_text_more.setVisibility(View.GONE);
        } else {
            fixview_tv_text_more.setVisibility(View.VISIBLE);
            fixview_tv_text_more.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoungReporterActivity.beginActivityForRecommend(getContext());
                }
            });
        }
    }

    public void setTitle(String title) {
        fixview_tv_text_name.setText(title);
    }

    public void setOnlyTitle(String title) {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText(title);
        mRecyclerView.setVisibility(View.GONE);
    }

    /**
     * 设置投票展示
     */
    public void setVoteData() {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText("投票");
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_fixrecycle_item_home_vote_image, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {


                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {
                                CommVoteActivity.beginActivity(getContext());

                            }
                        });

                holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                return holder;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state) {
                outRect.set(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.item_divider_height_4dp));
            }
        };

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        adapter.testLayoutData(1);
    }


    /**
     * 设置推荐活动
     */
    public void setRecommendData() {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText("推荐活动");
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_fixrecycle_item_recommend_activity, parent, false),
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

    /**
     * 设置小记者作品秀  优秀作品推荐
     */
    public void setYoungReporterData() {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText("小记者作品秀");
        fixview_tv_text_more.setVisibility(View.VISIBLE);
        fixview_tv_text_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                YoungReporterActivity.beginActivityForRecommend(getContext());
            }
        });

        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_fixrecycle_item_young_report, parent, false),
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
        adapter.testLayoutData(4);

    }


    /**
     * 设置小记者风采
     */
    public void setYoungReporterGracefulData() {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText("小记者风采");
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_fixrecycle_item_young_reporter_graceful, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {


                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {
                                YoungReporterGracefulActivity.beginActivity(getContext());

                            }
                        });

                holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                return holder;
            }
        };

        mRecyclerView.setPadding(getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.spacing_normal), false));
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        adapter.testLayoutData(3);

    }

    /**
     * 设置小记者话题
     */
    public void setYoungReporterTopicData() {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText("小记者话题");
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {

                BaseViewHolder holder = null;
                holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_fixrecycle_item_little_report_topic, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {


                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {
                                YoungReportTopicActivity.beginActivity(getContext());

                            }
                        });

                holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                return holder;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state) {
                outRect.set(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.list_margin));
            }
        };

//        mRecyclerView.setPadding(getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0);
        mRecyclerView.setAdapter(adapter);

        DividerDecoration dividerDecoration = new DividerDecoration();
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this.getContext())
                .paintProvider(dividerDecoration)
                .visibilityProvider(dividerDecoration)
                .marginProvider(dividerDecoration)
                .build());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        adapter.testLayoutData(1);
    }

    /**
     * 小记者 报道
     */
    public void setYoungReporterMedia() {
        if (isInitData)
            return;
        isInitData = true;
        fixview_tv_text_more.setVisibility(View.VISIBLE);
        fixview_tv_text_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                YoungReporterMediaActivity.beginActivity(getContext());
            }
        });
        fixview_tv_text_name.setText("小记者报道");
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {

                BaseViewHolder holder = null;
                holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.item_little_report_news, parent, false),
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

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state) {
                outRect.set(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.list_margin));
            }
        };

//        mRecyclerView.setPadding(getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0);
        mRecyclerView.setAdapter(adapter);

        DividerDecoration dividerDecoration = new DividerDecoration();
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this.getContext())
                .paintProvider(dividerDecoration)
                .visibilityProvider(dividerDecoration)
                .marginProvider(dividerDecoration)
                .build());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        adapter.testLayoutData(3);
    }

    /**
     * 我爱摄影  我爱画画
     */
    public void setYoungReporterPictures() {
        if (isInitData)
            return;
        isInitData = true;
        fixview_tv_text_more.setVisibility(View.VISIBLE);
        fixview_tv_text_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportWorksPhotoActivity.beginActivity(getContext());
            }
        });
        fixview_tv_text_name.setText("我爱摄影");
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_fixrecycle_item_home_vote_image, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                holder.setText(R.id.title, "落日");
                                holder.setText(R.id.time, "作者：刘小宁");
                                ImageView imageview = holder.getView(R.id.banner);
                                imageview.setImageResource(R.drawable.test_young_report_picture);
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
     * 设置最新动态
     */
    public void setLatestNews() {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText("最新动态");
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {

                BaseViewHolder holder = null;
                if (viewType == 1) {
                    holder = new BaseViewHolder(LayoutInflater.from(getContext())
                            .inflate(R.layout.widget_fixrecycle_item_latest_news_type_three, parent, false),
                            new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {


                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view, int position) {


                                }
                            });

                    holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                } else {
                    holder = new BaseViewHolder(LayoutInflater.from(getContext())
                            .inflate(R.layout.widget_fixrecycle_item_latest_news_type_one, parent, false),
                            new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {


                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view, int position) {


                                }
                            });

                    holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                }
                return holder;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state) {
                outRect.set(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.list_margin));
            }
        };

//        mRecyclerView.setPadding(getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0);
        mRecyclerView.setAdapter(adapter);

        DividerDecoration dividerDecoration = new DividerDecoration();
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this.getContext())
                .paintProvider(dividerDecoration)
                .visibilityProvider(dividerDecoration)
                .marginProvider(dividerDecoration)
                .build());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        adapter.testLayoutData(3);
    }

    private class DividerDecoration implements
            FlexibleDividerDecoration.PaintProvider,
//        FlexibleDividerDecoration.SizeProvider,
//        FlexibleDividerDecoration.ColorProvider,
            FlexibleDividerDecoration.VisibilityProvider,
            HorizontalDividerItemDecoration.MarginProvider {

        @Override
        public int dividerLeftMargin(int position, RecyclerView parent) {
            return Utils.dp2px(getContext(), 15);
        }

        @Override
        public int dividerRightMargin(int position, RecyclerView parent) {
            return Utils.dp2px(getContext(), 15);
        }

        @Override
        public boolean shouldHideDivider(int position, RecyclerView parent) {
            return false;
        }

        @Override
        public Paint dividerPaint(int position, RecyclerView parent) {
            Paint paint = new Paint();
//            if (position >= 1 && position <= 6) {
            Resources resource = (Resources) getContext().getResources();
            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.colorDividerLight);
            paint.setColor(csl.getDefaultColor());
            paint.setStrokeWidth(Utils.dp2px(getContext(), 1));
//                return paint;

//            }
//            paint.setStrokeWidth(1);
            return paint;
        }
    }


    /**
     * 设置热门影音
     */
    public void setHotVideoData() {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText("热门影音");
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_fixrecycle_item_hot_video, parent, false),
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

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state) {
                outRect.set(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.item_divider_height_4dp));
            }
        };

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        adapter.testLayoutData(1);
    }


    /**
     * 设置 逛逛社区  秀场
     */
    public void setCommunityShowfieldData() {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText("I秀场");
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_fixrecycle_item_community_show_field, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {


                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {
                                OtherPersonCenterActivity.beginActivity(getContext());

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
     * 设置 逛逛社区 最热
     */
    public void setCommunityHotData() {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText("最热");
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_fixrecycle_item_community_hot, parent, false),
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
        adapter.testLayoutData(10);

    }

    /**
     * 设置 逛逛社区 推荐圈子
     */
    public void setCommunityRecommendCircle() {
        if (isInitData)
            return;
        isInitData = true;

        fixview_tv_text_name.setText("推荐圈子");
        fixview_tv_text_name.setGravity(Gravity.LEFT);
        fixview_tv_text_name.setPadding(getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0);
        fixview_tv_text_name.setCompoundDrawables(null, null, null, null);
        BaseAdapter<String> adapter = new BaseAdapter<String>(getContext()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {

                BaseViewHolder holder = null;
                holder = new BaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_fixrecycle_item_community_recommend_circle, parent, false),
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

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state) {
                outRect.set(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.list_margin));
            }
        };

//        mRecyclerView.setPadding(getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0);
        mRecyclerView.setAdapter(adapter);

        DividerDecoration dividerDecoration = new DividerDecoration();
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this.getContext())
                .paintProvider(dividerDecoration)
                .visibilityProvider(dividerDecoration)
                .marginProvider(dividerDecoration)
                .build());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        adapter.testLayoutData(2);
    }


}
