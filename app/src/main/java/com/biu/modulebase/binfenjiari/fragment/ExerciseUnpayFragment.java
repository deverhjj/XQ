package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.EventDetailActivity;
import com.biu.modulebase.binfenjiari.activity.PayingActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.MyEventVO;
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
 * @Title: {我的活动- 待付款}
 * @Description:{描述}
 * @date 2016/4/15
 */
public class ExerciseUnpayFragment extends BaseFragment {

    private final static int DO_PAY = 100;

    private RecyclerView mRecyclerView;
    private LSwipeRefreshLayout mRefreshLayout;
    private long time;

    private int pageNum = 1;
    private int allPageNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh,
                container, false);
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
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_exercise_unpay, parent, false), new BaseViewHolder.Callbacks() {
                    @Override
                    public void bind(BaseViewHolder holder, Object data) {
                        MyEventVO bean = (MyEventVO) data;
                        holder.setNetImage(Constant.IMG_COMPRESS, R.id.img, bean.getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                        holder.setText(R.id.name, bean.getName());
                        holder.setText(R.id.time, String.format(getString(R.string.time), Utils.sec2Date(bean.getOpen_time(), "yyyy/MM/dd"), Utils.sec2Date(bean.getEnd_time(), "yyyy/MM/dd")));
                        Button btn = holder.getView(R.id.btn);//付款按钮
//                        ImageView over = holder.getView(R.id.over);
//                        if (bean.getIsopen().equals("1")) {
//                            over.setVisibility(View.GONE);
//                        } else {
//                            btn.setVisibility(View.VISIBLE);
//                        }
                    }


                    @Override
                    public void onItemClick(View view, int position) {
                        MyEventVO bean = (MyEventVO) getData(position);
                        int i = view.getId();
                        if (i == R.id.delete) {
                            deleteMyActivity(position, bean.getId());

                        } else if (i == R.id.btn) {
                            if (bean.getAppointment_ok().equals("4")) {//失效
                                showTost("改订单已失效，请重新下单...", 0);
                            } else {
                                Intent intent = new Intent(getActivity(), PayingActivity.class);
                                intent.putExtra("position", position);
                                intent.putExtra("title", bean.getName());
                                intent.putExtra("order_id", bean.getOrder_id());
                                intent.putExtra("money", bean.getPay_money());
                                startActivityForResult(intent, DO_PAY);
                            }

                        } else {
                            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                            intent.putExtra(Constant.KEY_ID, bean.getId());
                            startActivity(intent);

                        }


                    }
                });
                holder.setItemChildViewClickListener(R.id.delete, R.id.btn);
                return holder;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state) {
                outRect.set(0, 0,
                        0, getResources().getDimensionPixelSize(R.dimen.item_divider_height));
            }
        };
        //设置网格布局管理器
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);


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
            params.put("model", Constant.MODEL_ACTIVITY);
            params.put("action", Constant.ACTION_GET_MY_ACTIVITY);
            params.put("token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
            params.put("type", "1");//1未付款 2已付款
//            params.put("time",time+"");
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
                    JSONArray array = JSONUtil.getJSONArray(result, "activityList");
                    ArrayList<MyEventVO> list = JSONUtil.fromJson(array.toString(), new TypeToken<List<MyEventVO>>() {
                    }.getType());
                    refreshList(tag, list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if (key == 3) {
                    refreshList(tag, new ArrayList<MyEventVO>());
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

    private void deleteMyActivity(final int position, String id) {
        showProgress(getClass().getSimpleName());
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", Constant.MODEL_DELETE_MY_ACTIVITY);
        JSONUtil.put(params, "action", Constant.ACTION_DELETE_MY_ACTIVITY);
        JSONUtil.put(params, "token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params, "activityId", id);
        jsonRequest(true, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                adapter.removeData(position);
                if (adapter.getItemCount() == 0)
                    visibleNoData();
            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                showTost(message, 1);
            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
            }
        });
    }

    private void refreshList(int tag, ArrayList<MyEventVO> datas) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case DO_PAY:
                    int position = data.getIntExtra("position", -1);
                    if (position != -1) {
                        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                        adapter.removeData(position);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
