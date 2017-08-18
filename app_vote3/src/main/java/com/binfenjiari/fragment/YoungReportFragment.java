package com.binfenjiari.fragment;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.binfenjiari.R;
import com.binfenjiari.utils.DataTest;
import com.binfenjiari.widget.SliderViewBanner;
import com.binfenjiari.widget.FixedRecycleScrollView;
import com.binfenjiari.widget.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.binfenjiari.widget.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.RefreshRecyclerView;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class YoungReportFragment extends BaseFragment {

    private SliderViewBanner mSliderViewBanner;

    private RefreshRecyclerView mRefreshRecyclerView;
    private RecyclerView mRecyclerView;
    private int mPage;

    public static YoungReportFragment newInstance() {
        YoungReportFragment fragment = new YoungReportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_common_refreshrecyclerview, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        mRefreshRecyclerView = (RefreshRecyclerView) rootView.findViewById(R.id.rrv_refresh_recycleview);
        mRecyclerView = mRefreshRecyclerView.getRecyclerView();

        mRefreshRecyclerView.setRefreshAction(new RefreshRecyclerView.Action() {

            @Override
            public void onAction(int page) {
                mPage = page;
                refreshData();

            }
        });
        mRefreshRecyclerView.setLoadMoreAction(new RefreshRecyclerView.Action() {

            @Override
            public void onAction(int page) {
                mPage = page;
                loadMoreData();

            }
        });

        BaseAdapter<String> adapter = new BaseAdapter<String>(getActivity()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = null;
                if (viewType == 0) {
                    holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_navi_home_bannersliderview, parent, false),
                            new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
                                    mSliderViewBanner = (SliderViewBanner) holder.itemView;

                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view, int position) {


                                }
                            });

                    holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);

                } else if (viewType == 1) {
                    holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_navi_home_fixedrecyclescrollview, parent, false),
                            new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
                                    FixedRecycleScrollView view = (FixedRecycleScrollView) holder.itemView;
                                    view.setTitle("优秀作品推荐");
                                    view.setYoungReporterData();

                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view, int position) {


                                }
                            });

                    holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                } else if (viewType == 2) {
                    holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_navi_home_fixedrecyclescrollview, parent, false),
                            new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
                                    FixedRecycleScrollView view = (FixedRecycleScrollView) holder.itemView;
                                    view.setYoungReporterGracefulData();

                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view, int position) {


                                }
                            });

                    holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                } else if (viewType == 3) {
                    holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_navi_home_fixedrecyclescrollview, parent, false),
                            new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
                                    FixedRecycleScrollView view = (FixedRecycleScrollView) holder.itemView;
                                    view.setYoungReporterTopicData();

                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view, int position) {


                                }
                            });

                    holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                } else if (viewType == 4) {
                    holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_navi_home_fixedrecyclescrollview, parent, false),
                            new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
                                    FixedRecycleScrollView view = (FixedRecycleScrollView) holder.itemView;
                                    view.setOnlyTitle("小记者报道");

                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view, int position) {


                                }
                            });

                    holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                } else {
                    holder = new BaseViewHolder(LayoutInflater.from(getActivity())
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
                }
                return holder;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

        };

        mRecyclerView.setAdapter(adapter);

        DividerDecoration dividerDecoration = new DividerDecoration();
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this.getContext())
                .paintProvider(dividerDecoration)
                .visibilityProvider(dividerDecoration)
                .marginProvider(dividerDecoration)
                .build());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    @Override
    public void loadData() {
        mRefreshRecyclerView.showSwipeRefresh();
    }

    private void refreshData() {
        visibleLoading();
        mRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                inVisibleLoading();
                DataTest.load(mRefreshRecyclerView, mPage);
            }
        }, 1000);
    }

    private void loadMoreData() {
        mRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                DataTest.load(mRefreshRecyclerView, mPage);
            }
        }, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSliderViewBanner != null) {
            mSliderViewBanner.onBannerResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSliderViewBanner != null) {
            mSliderViewBanner.onBannerPause();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_young_report, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_edit) {
//            search();
        }
        return super.onOptionsItemSelected(item);
    }

    private class DividerDecoration implements
            FlexibleDividerDecoration.PaintProvider,
//        FlexibleDividerDecoration.SizeProvider,
//        FlexibleDividerDecoration.ColorProvider,
            FlexibleDividerDecoration.VisibilityProvider,
            HorizontalDividerItemDecoration.MarginProvider {

        @Override
        public int dividerLeftMargin(int position, RecyclerView parent) {
            if (position > 5) {
                return Utils.dp2px(getContext(), 15);
            }
            return 0;
        }

        @Override
        public int dividerRightMargin(int position, RecyclerView parent) {
            if (position > 5) {
                return Utils.dp2px(getContext(), 15);
            }
            return 0;
        }

        @Override
        public boolean shouldHideDivider(int position, RecyclerView parent) {
            if (position == 0 || position == 4)
                return true;
            return false;
        }

        @Override
        public Paint dividerPaint(int position, RecyclerView parent) {
            Paint paint = new Paint();
            if (position >= 1 && position <= 4) {
                Resources resource = (Resources) getContext().getResources();
                ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.colorAppBackground);
                paint.setColor(csl.getDefaultColor());
                paint.setStrokeWidth(Utils.dp2px(getContext(), 8));
                return paint;

            }

            if (position > 4) {
                Resources resource = (Resources) getContext().getResources();
                ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.colorAppBackground);
                paint.setColor(csl.getDefaultColor());
                paint.setStrokeWidth(Utils.dp2px(getContext(), 1));
                return paint;
            }
            paint.setStrokeWidth(0);
            return paint;
        }
    }

}
