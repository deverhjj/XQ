package com.binfenjiari.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binfenjiari.R;
import com.binfenjiari.utils.DataTest;
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
public class MovementEvaluateFragment extends BaseFragment {

    private RefreshRecyclerView mRefreshRecyclerView;
    private RecyclerView mRecyclerView;
    private int mPage;

    public static MovementEvaluateFragment newInstance() {
        MovementEvaluateFragment fragment = new MovementEvaluateFragment();
        return fragment;
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
                            .inflate(R.layout.item_movement_evaluate_ratings, parent, false),
                            new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
//                                    mBannerSliderView = (BannerSliderView) holder.itemView;

                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view, int position) {


                                }
                            });

                    holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);
                } else {
                    holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_movement_evaluate_list, parent, false),
                            new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
//                                    mBannerSliderView = (BannerSliderView) holder.itemView;

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
        DividerCommonDecoration dividerDecoration = new DividerCommonDecoration(getContext());
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
        }, 500);
    }

    private void loadMoreData() {
        mRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                DataTest.load(mRefreshRecyclerView, mPage);
            }
        }, 1000);
    }

    public class DividerCommonDecoration implements
            FlexibleDividerDecoration.PaintProvider,
            FlexibleDividerDecoration.VisibilityProvider,
            HorizontalDividerItemDecoration.MarginProvider {

        private Context mContext;

        public DividerCommonDecoration(Context context) {
            this.mContext = context;
        }

        public Context getContext() {
            return mContext;
        }

        @Override
        public int dividerLeftMargin(int position, RecyclerView parent) {
//        if (position > 7) {
//            return Utils.dp2px(getContext(), 15);
//        }
            return 0;
        }

        @Override
        public int dividerRightMargin(int position, RecyclerView parent) {
//        if (position > 7) {
//            return Utils.dp2px(getContext(), 15);
//        }
            return 0;
        }

        @Override
        public boolean shouldHideDivider(int position, RecyclerView parent) {
//        if (position == 7)
//            return true;
            return false;
        }

        @Override
        public Paint dividerPaint(int position, RecyclerView parent) {
            Paint paint = new Paint();
            if (position == 0) {
                Resources resource = (Resources) getContext().getResources();
                ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.app_color_background);
                paint.setColor(csl.getDefaultColor());
                paint.setStrokeWidth(Utils.dp2px(getContext(), 8));
                return paint;
//
            }
//
            if (position > 0) {
                Resources resource = (Resources) getContext().getResources();
                ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.app_color_background);
                paint.setColor(csl.getDefaultColor());
                paint.setStrokeWidth(Utils.dp2px(getContext(), 1));
                return paint;
            }
            paint.setStrokeWidth(0);
            return paint;
        }

    }

}
