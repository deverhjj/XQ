package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.EventDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.CollectionVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @Title: {收藏}
 * @Description:{描述}
 * @date 2016/4/15
 */
public class CollectionActivityFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private LSwipeRefreshLayout mRefreshLayout;
    private long time;

    private int pageNum = 1;
    private int allPageNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        mRefreshLayout = (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getActivityList(Constant.LIST_LOAD_MORE);
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter = new BaseAdapter(getActivity()) {

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_collection_activity, parent, false), new BaseViewHolder.Callbacks() {
                    @Override
                    public void bind(BaseViewHolder holder, Object data) {
                        CollectionVO bean = (CollectionVO) data;
                        ImageView over = holder.getView(R.id.over);
                        String isOpen = bean.getIsopen();
                        if (isOpen.equals("2")) {
                            over.setVisibility(View.GONE);
                        } else {
                            over.setVisibility(View.GONE);
                        }
                        holder.setNetImage(Constant.IMG_THUMBNAIL, R.id.img, bean.getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                        holder.setText(R.id.name, bean.getName());
                        holder.setText(R.id.time, String.format(getString(R.string.time), Utils.sec2Date(bean.getOpen_time(), "yyyy/MM/dd"), Utils.sec2Date(bean.getEnd_time(), "yyyy/MM/dd")));
//                        Palette.generateAsync(bitmap,
//                                new Palette.PaletteAsyncListener() {
//                                    @Override
//                                    public void onGenerated(Palette palette) {
//                                        Palette.Swatch vibrant =
//                                                palette.getVibrantSwatch();
//                                        if (vibrant != null) {
//
//                                            int rgb = vibrant.getRgb();
//                                            int titleTextColor = vibrant.getTitleTextColor();
//                                            int bodyTextColor = vibrant.getBodyTextColor();
//                                        }
//                                    }
//                                });
                    }

                    @Override
                    public void onItemClick(View view, int position) {
                        CollectionVO bean = (CollectionVO) getData(position);
                        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                        intent.putExtra(Constant.KEY_ID, bean.getId());
                        startActivity(intent);
                    }
                });


            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state) {
                outRect.set(0, 0, 0, 0);
            }
        };
        //设置网格布局管理器
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        mRefreshLayout.startLoad();
                    }
                }
            }
        });


    }


    @Override
    public void loadData() {
        time = new Date().getTime() / 1000;
        pageNum = 1;
        getActivityList(Constant.LIST_REFRESH);
    }

    private void getActivityList(final int tag) {
        JSONObject params = new JSONObject();
        try {
            params.put("model", Constant.MODEL_COLLECTION);
            params.put("action", Constant.ACTION_GET_MY_COLLECTION);
            params.put("token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
            params.put("type", "1");// 1活动 2基地  3视听
            params.put("time", time + "");
            params.put("pageNum", pageNum + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result = new JSONObject(jsonString);
                    time = JSONUtil.getLong(result, "time");
                    allPageNumber = JSONUtil.getInt(result, "allPageNumber");
                    JSONArray array = JSONUtil.getJSONArray(result, "list");
                    ArrayList<CollectionVO> list = JSONUtil.fromJson(array.toString(), new TypeToken<List<CollectionVO>>() {
                    }.getType());
                    refreshList(tag, list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if (key == 3) {
                    refreshList(tag, new ArrayList<CollectionVO>());
                } else {
                    showTost(message, 1);
                }
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });
    }

    private void refreshList(int tag, ArrayList<CollectionVO> datas) {
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag) {
            case Constant.LIST_REFRESH:
                if (datas.size() == 0) {
                    visibleNoData();
                    return;
                }
                adapter.setData(datas);
                mRefreshLayout.setRefreshing(false);
                break;
            case Constant.LIST_LOAD_MORE:
                adapter.addItems(datas);
                mRefreshLayout.setLoading(false);
                break;
        }
        if (pageNum < allPageNumber) {
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
        } else {
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
        }
    }
}
