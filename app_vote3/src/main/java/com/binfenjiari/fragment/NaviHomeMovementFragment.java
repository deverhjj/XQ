package com.binfenjiari.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.binfenjiari.R;
import com.binfenjiari.activity.CommonSearchActivity;
import com.binfenjiari.activity.MovementCalendlerActivity;
import com.binfenjiari.activity.MovementDetailActivity;
import com.binfenjiari.model.FilterItem;
import com.binfenjiari.utils.DataTest;
import com.binfenjiari.utils.DividerCommonDecoration;
import com.binfenjiari.utils.Logger;
import com.binfenjiari.utils.Views;
import com.binfenjiari.widget.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.widget.RefreshRecyclerView;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.common.base.BaseFragment;
import com.github.huajianjiang.expandablerecyclerview.widget.PatchedRecyclerView;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class NaviHomeMovementFragment extends BaseFragment {
    private static final String TAG = NaviHomeMovementFragment.class.getSimpleName();
    private RefreshRecyclerView mRefreshRecyclerView;
    private RecyclerView mRecyclerView;
    private int mPage;

    private LinearLayout ll_pop;
    private RelativeLayout rl_sort;
    public int dateCheckedPos = -1;
    public String dateArray[] = {"综合排序", "时间从近到远", "时间从远到近"};

    private View mFilterTab;
    private View mFilterContentView;

    private PatchedRecyclerView mActiFilterGroup;
    private PatchedRecyclerView mAgeFilterGroup;
    private PatchedRecyclerView mAddrFilterGroup;
    private PatchedRecyclerView mPayingFilterGroup;
    private PatchedRecyclerView mDateFilterGroup;

    public static NaviHomeMovementFragment newInstance() {
        NaviHomeMovementFragment fragment = new NaviHomeMovementFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView =
                (ViewGroup) inflater.inflate(R.layout.fragment_movement_hot, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }


    @Override
    protected void initView(View rootView) {

        //        setHasOptionsMenu(true);
        //        searchEditText = (SearchEditText) getBaseActivity().setToolBarCustomView(com.biu.modulebase.binfenjiari.R.layout.search_editview);
        //        searchEditText.requestFocus();
        ll_pop = (LinearLayout) rootView.findViewById(R.id.ll_pop);
        rootView.findViewById(R.id.rl_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortFilter();

            }
        });

        mFilterTab = Views.find(rootView, R.id.rl_date);
        mFilterTab.setOnClickListener(this);
        mFilterContentView = getActivity().getLayoutInflater().inflate(R.layout.popup_filter, null);

        mRefreshRecyclerView =
                (RefreshRecyclerView) rootView.findViewById(R.id.rrv_refresh_recycleview);
        mRecyclerView = mRefreshRecyclerView.getRecyclerView();

        mActiFilterGroup = Views.find(mFilterContentView, R.id.actiFilterGroup);
        ActiFilterGroupAdapter filterAdapter = new ActiFilterGroupAdapter(getContext());
        mActiFilterGroup.setAdapter(filterAdapter);
        mActiFilterGroup.addItemDecoration(filterAdapter.new GridItemDecor());

        mAgeFilterGroup = Views.find(mFilterContentView, R.id.ageFilterGroup);
        mAgeFilterGroup.setAdapter(filterAdapter);
        mAgeFilterGroup.addItemDecoration(filterAdapter.new GridItemDecor());

        mAddrFilterGroup = Views.find(mFilterContentView, R.id.addressFilterGroup);
        mAddrFilterGroup.setAdapter(filterAdapter);
        mAddrFilterGroup.addItemDecoration(filterAdapter.new GridItemDecor());

        mDateFilterGroup = Views.find(mFilterContentView, R.id.dateFilterGroup);
        mDateFilterGroup.setAdapter(filterAdapter);
        mDateFilterGroup.addItemDecoration(filterAdapter.new GridItemDecor());


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

                holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                                                          .inflate(
                                                                  R.layout.item_mine_collection_movement,
                                                                  parent, false),
                                            new BaseViewHolder.Callbacks2() {
                                                @Override
                                                public void bind(BaseViewHolder holder,
                                                        Object data)
                                                {
                                                    //                                    mBannerSliderView = (BannerSliderView) holder.itemView;

                                                }

                                                @Override
                                                public void onItemClick(BaseViewHolder holder,
                                                        View view, int position)
                                                {
                                                    MovementDetailActivity.beginActivity(
                                                            getContext());

                                                }
                                            });

                holder.setItemChildViewClickListener(R.id.like, R.id.share, R.id.tv_location);

                return holder;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

        };

        mRecyclerView.setAdapter(adapter);
        DividerCommonDecoration dividerDecoration = new DividerCommonDecoration(getContext());
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this.getContext()).paintProvider(
                        dividerDecoration)
                                                                              .visibilityProvider(
                                                                                      dividerDecoration)
                                                                              .marginProvider(
                                                                                      dividerDecoration)
                                                                              .build());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_date:
                showFilter();
                break;

            default:
                break;
        }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movement_hot, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            launchSearchScreen();
        } else if (i == R.id.action_calen) {
            MovementCalendlerActivity.beginActivity(getContext());
        }
        return super.onOptionsItemSelected(item);
    }


    private void launchSearchScreen() {
        Intent search = new Intent(getContext(), CommonSearchActivity.class);
        startActivity(search);
    }



    private void showSortFilter() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this.getContext(), R.layout.item_menu2, R.id.tv,
                                         dateArray);
        OtherUtil.showPopupWin(this.getContext(), ll_pop, adapter,
                               ViewGroup.LayoutParams.MATCH_PARENT,
                               ViewGroup.LayoutParams.WRAP_CONTENT, dateCheckedPos,
                               new AdapterView.OnItemClickListener() {
                                   @Override
                                   public void onItemClick(AdapterView<?> parent, View view,
                                           int position, long id)
                                   {
                                       //                        tv_date.setText(dateArray[position]);
                                       dateCheckedPos = position;
                                       //                        if (position == 0) {
                                       //                            timeOrder = "";
                                       //                        } else {
                                       //                            timeOrder = orderArray[position - 1];
                                       //                        }
                                       //                        if (!v.isSelected()) {
                                       //                            v.setSelected(true);
                                       //                        }
                                   }
                               });
    }

    @SuppressLint("InflateParams")
    private void showFilter() {
        //必须先将窗口的 contentView 从 它的 parent 中移出，因为内容视图被添加到弹窗的背景 parent view 里了
        ViewGroup parent = (ViewGroup) mFilterContentView.getParent();
        if (parent != null) {
            parent.removeView(mFilterContentView);
        }
        PopupWindow pw = new PopupWindow(getContext(), null, 0, R.style.PopupWin);
        pw.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.setContentView(mFilterContentView);
        pw.showAsDropDown(mFilterTab);
    }

    private class ActiFilterGroupAdapter extends
            com.github.huajianjiang.baserecyclerview.widget.ArrayAdapter<com.github.huajianjiang.baserecyclerview.widget.BaseViewHolder, FilterItem>
    {
        public ActiFilterGroupAdapter(Context ctxt) {
            super(ctxt);
        }

        @Override
        public com.github.huajianjiang.baserecyclerview.widget.BaseViewHolder onGenerateViewHolder(
                ViewGroup parent, int viewType)
        {
            return new com.github.huajianjiang.baserecyclerview.widget.BaseViewHolder(
                    getLayoutInflater().inflate(viewType, parent, false))
            {

            };
        }

        @Override
        public void onPopulateViewHolder(
                com.github.huajianjiang.baserecyclerview.widget.BaseViewHolder holder, int position)
        {

        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.item_filter;
        }

        @Override
        public int getItemCount() {
            return 9;
        }

        private class GridItemDecor extends RecyclerView.ItemDecoration {
            int itemOffset;

            public GridItemDecor() {
                this.itemOffset =
                        getContext().getResources().getDimensionPixelSize(R.dimen.item_offset);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                    RecyclerView.State state)
            {
                RecyclerView.LayoutManager lm = parent.getLayoutManager();
                if (!(lm instanceof GridLayoutManager)) {
                    return;
                }

                GridLayoutManager glm = (GridLayoutManager) lm;
                final int childAdapterPos = parent.getChildAdapterPosition(view);
                final int columnCount = glm.getSpanCount();

                int top = childAdapterPos >= columnCount ? itemOffset : 0;

                outRect.set(0, top, 0, 0);

//                ViewGroup.MarginLayoutParams lp =
//                        (ViewGroup.MarginLayoutParams) view.getLayoutParams();
//                lp.rightMargin = (childAdapterPos + 1) % columnCount == 0 ? 0 : itemOffset;
//                view.setLayoutParams(lp);
            }
        }
    }

}
