package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CircleDetailActivity;
import com.biu.modulebase.binfenjiari.activity.CircleManageActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ExitCircleDialogFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ShareDialogFragment;
import com.biu.modulebase.binfenjiari.model.CircleVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
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
 * @Title: {所有圈子、我的圈子子Fragment、搜索结果fragment}
 * @Description:{}
 * @date 2016/4/13
 */
public class CircleItemFragment extends BaseFragment {
    private static final String TAG = "CircleItemFragment";
    /**
     * 退出圈子requestCode
     **/
    private static final int TARGET_REQUESST_CODE_EXIT_CIRCLE = 1;


    public static final String KEY_CIRCLE_ITEM = "circleItem";

    private static final String KEY_USER_ID = "user_id";

    /**
     * 所有圈子
     **/
    public static final int TYPE_ALL = 0;
    /**
     * 我的圈子
     **/
    public static final int TYPE_MY = 1;
    /**
     * 搜索的圈子
     **/
    public static final int TYPE_SEARCH = 2;
    public static final int TYPE_OTHERS = 3;
    private int type = TYPE_ALL;

    private static final String KEY_TYPE = "type";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE_ID = "typeId";

    /**
     * 区分搜索我的圈子还是搜索所有去圈子
     **/
    private static final String KEY_SEARCH_TAG = "searchTag";
    private String searchTag;
    private String typeId;
    private int mUserId;
    private String title;

    private long time;
    private int pageNum = 1;
    private int allPageNumber;

    private LSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    public static boolean refresh = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //标记下当前的登录状态，提供后期回到该界面是否需要刷新界面
        MyApplication.sRefreshMap.put(TAG, !OtherUtil.hasLogin(getActivity()));
        Bundle args = getArguments();
        if (args != null) {
            title = args.getString("title");
            type = args.getInt(KEY_TYPE, TYPE_ALL);
            typeId = args.getString(KEY_TYPE_ID);
            mUserId = args.getInt(KEY_USER_ID, -1);
            searchTag = args.getString(KEY_SEARCH_TAG);
        }
    }

    public static CircleItemFragment newInstance(int type, String typeId, int userId) {
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        args.putString(KEY_TYPE_ID, typeId);
        args.putInt(KEY_USER_ID, userId);
        CircleItemFragment circleItemFragment = new CircleItemFragment();
        circleItemFragment.setArguments(args);
        return circleItemFragment;
    }


    public static CircleItemFragment newInstance(int type, String searchTag, String title) {
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        args.putString(KEY_SEARCH_TAG, searchTag);
        args.putString(KEY_TITLE, title);
        CircleItemFragment circleItemFragment = new CircleItemFragment();
        circleItemFragment.setArguments(args);
        return circleItemFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        visibleLoading();
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_recyclerview_swiperefresh, container, false);

        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(final View rootView) {
        mRefreshLayout = (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.LogE(TAG, "onRefresh******************");
                loadData();
            }

            @Override
            public void onLoadMore() {
                LogUtil.LogE(TAG, "onLoadMore******************");
                loadMore();
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
//        mRecyclerView.setBackgroundResource(R.color.white);
//        mRecyclerView.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_left_16dp),0,0,0);
        BaseAdapter adapter = new BaseAdapter(getActivity()) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, final int viewType) {

                BaseViewHolder baseViewHolder = new BaseViewHolder(getActivity().getLayoutInflater()
                        .inflate(type == TYPE_ALL || type == TYPE_OTHERS || type == TYPE_MY ? R.layout.item_circle
                                : R.layout.item_circle_search, parent, false),
                        new BaseViewHolder.Callbacks2() {

                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                if (data == null) {
                                    return;
                                }
                                CircleVO bean = (CircleVO) data;
                                String headUrl = bean.getIntro_img();
                                LogUtil.LogE(TAG, "headUrl----->" + headUrl);
                                holder.setNetImage(Constant.IMG_COMPRESS, R.id.iv_circle_head_portrait, headUrl, ImageDisplayUtil.DISPLAY_ROUND_IMAGE);
                                holder.setText(R.id.tv_circle_name, bean.getName());
                                holder.setText(R.id.tv_circle_subject, bean.getIntro_content());
                                TextView btn_join = holder.getView(R.id.btn_circle_join);
                                int idType = Utils.isInteger(bean.getType());
                                btn_join.setClickable(true);
                                if (idType == 4) {
                                    btn_join.setVisibility(View.GONE);
                                } else {
                                    if (idType == 2 || idType == 3) {
                                        btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_orange));
                                        btn_join.setTextColor(getResources().getColor(R.color.colorAccent));
                                        btn_join.setText("管理");
                                    } else if (idType == 1) {//已加入的普通会员
                                        btn_join.setClickable(false);
                                        btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_gray));
                                        btn_join.setTextColor(getResources().getColor(R.color.colorTextGray));
                                        btn_join.setText("已加入");
                                    } else {
                                        btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_orange));
                                        btn_join.setTextColor(getResources().getColor(R.color.colorAccent));
                                        btn_join.setText("加入");
                                    }
                                    btn_join.setVisibility(View.VISIBLE);
                                }
                                if (type == TYPE_SEARCH) {
                                    holder.setText(R.id.content, bean.getIntro_content());
                                    holder.setText(R.id.join_num, String.format(getString(R.string.circle_join_num), bean.getUser_n()));
                                    holder.setText(R.id.post_num, String.format(getString(R.string.circle_post_num), bean.getPost_n()));
                                }
                            }

                            @Override
                            public void onItemClick(final BaseViewHolder holder, final View view, final int position) {
                                CircleVO bean = (CircleVO) getData(position);
                                final int idType = Utils.isInteger(bean.getType());
                                int i = view.getId();
                                if (i == R.id.btn_circle_join) {//                                        if(type ==TYPE_ALL ||type ==TYPE_SEARCH){
                                    if (idType == 3 || idType == 2) {//管理
                                        Intent intent = new Intent(getActivity(), CircleManageActivity.class);
                                        intent.putExtra(CircleManageFragment.KEY_CIRCLE_ID, bean.getId());
                                        startActivity(intent);
                                    } else if (idType == 1) {//退出
                                        ExitCircleDialogFragment shareDialog = ExitCircleDialogFragment.newInstance(ShareDialogFragment.STYLE_NO_TITLE, position, bean.getId());
                                        shareDialog.setTargetFragment(CircleItemFragment.this, TARGET_REQUESST_CODE_EXIT_CIRCLE);
                                        shareDialog.show(getActivity().getSupportFragmentManager(), "exit_circle");
                                    } else {//加入
                                        OtherUtil.joinCircle(CircleItemFragment.this, bean.getId(), new OtherUtil.JoinCircleCallback() {
                                            @Override
                                            public void onFinish(int key) {
                                                CircleVO bean = (CircleVO) getData(position);
                                                bean.setType("1");
                                                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                                                adapter.changeData(position, bean);
//                                                        changeBtnAppear((TextView) view,idType);
                                            }
                                        });
                                    }
//                                        }else{
//                                            Intent intent = new Intent(getActivity(), CircleManageActivity.class);
//                                            intent.putExtra(CircleManageFragment.KEY_CIRCLE_ID, bean.getId());
//                                            startActivity(intent);
//                                        }

                                } else {
                                    String isEdit = bean.getIsEdit();
                                    if (TYPE_MY == type || (searchTag != null && searchTag.equals(Constant.SEARCH_CIRCLE_MY))) {
                                        if (isEdit.equals("1")) {
                                            showTost("审核失败,不能进入圈子,请重新提交审核...", 1);
                                        } else if (isEdit.equals("3")) {
                                            showTost("圈子审核中,请耐心等待...", 1);
                                        } else {
                                            Intent intent = new Intent(getActivity(), CircleDetailActivity.class);
                                            intent.putExtra(CircleFragment.EXTRA_CIRCLE_ID, bean.getId());
                                            startActivity(intent);
                                        }
                                    } else {
                                        Intent intent = new Intent(getActivity(), CircleDetailActivity.class);
                                        intent.putExtra(CircleFragment.EXTRA_CIRCLE_ID, bean.getId());
                                        startActivity(intent);
                                    }


                                }

                            }
                        });
                baseViewHolder.setItemChildViewClickListener(type == TYPE_ALL || type == TYPE_OTHERS ? R.id
                        .btn_circle_join : R.id.btn_circle_join);
                return baseViewHolder;


            }

        };

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.item_divider_height));
            }
        });
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
        if (type == TYPE_SEARCH) {
            getSearchCircleList(Constant.LIST_REFRESH);
        } else {
            getCircleList(Constant.LIST_REFRESH);
        }

    }

    private void loadMore() {
        pageNum++;
        if (type == TYPE_SEARCH) {
            getSearchCircleList(Constant.LIST_LOAD_MORE);
        } else {
            getCircleList(Constant.LIST_LOAD_MORE);
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

    }

    private void getCircleList(final int tag) {
        JSONObject params = new JSONObject();
        try {
            params.put("token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
            params.put("type_id", typeId);
            params.put("time", time + "");
            params.put("pageNum", pageNum + "");
            if (type == TYPE_ALL) {//所有圈子
                params.put("model", Constant.MODEL_CIRCLE);
                params.put("action", Constant.ACTION_GET_CIRCLE_LIST);
            } else {//我的圈子
                params.put("model", Constant.MODEL_CIRCLE_MANAGE);
                params.put("action", Constant.ACTION_GET_MY_CIRCLE_LIST);
            }

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
                    JSONArray array = JSONUtil.getJSONArray(result, "circleList");
                    ArrayList<CircleVO> list = JSONUtil.fromJson(array.toString(), new TypeToken<List<CircleVO>>() {
                    }.getType());
                    refreshList(tag, list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if (type == CircleItemFragment.TYPE_SEARCH && pageNum == 1) {
                    showTost("未搜索相关结果！", 1);
                    getActivity().finish();
                }
                if (key == 3) {
                    refreshList(tag, new ArrayList<CircleVO>());
                } else {
                    mRefreshLayout.setRefreshing(false);
                    mRefreshLayout.setLoading(false);
                    showTost(message, 1);
                }
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });
    }

    private void getSearchCircleList(final int tag) {
        JSONObject params = new JSONObject();
        try {
            params.put("token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
            params.put("title", title);
            params.put("time", time + "");
            params.put("pageNum", pageNum + "");
            if (searchTag.equals(Constant.SEARCH_CIRCLE_MY)) {//我的圈子搜素
                params.put("model", Constant.MODEL_CIRCLE_MANAGE);
                params.put("action", Constant.ACTION_SEARCH_MY_CIRCLE);
            } else {//搜索所有圈子
                params.put("model", Constant.MODEL_CIRCLE);
                params.put("action", Constant.ACTION_SEARCH_CIRCLE);
            }
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
                    JSONArray array = JSONUtil.getJSONArray(result, "circleList");
                    ArrayList<CircleVO> list = JSONUtil.fromJson(array.toString(), new TypeToken<List<CircleVO>>() {
                    }.getType());
                    refreshList(tag, list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if (key == 3) {
                    refreshList(tag, new ArrayList<CircleVO>());
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

    private void refreshList(int tag, ArrayList<CircleVO> datas) {
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag) {
            case Constant.LIST_REFRESH:
                if (datas.size() == 0) {
                    visibleNoData();
                    if (type == CircleItemFragment.TYPE_SEARCH) {
                        showTost("未搜索相关结果！", 1);
                        getActivity().finish();
                    }
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

    private void changeBtnAppear(TextView btn_join, int status) {
        boolean notJoin = status == 3 || status == 2 || status == 1;
        btn_join.setBackgroundDrawable(getResources().getDrawable(notJoin ? R.drawable.selector_btn_outline_gray : R.drawable.selector_btn_outline_orange));
        btn_join.setTextColor(getResources().getColor(notJoin ? R.color.colorTextGray : R.color.colorAccent));
        btn_join.setText(notJoin ? "已加入" : "加入");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TARGET_REQUESST_CODE_EXIT_CIRCLE:
//                    CircleVO circleVO = (CircleVO) data.getSerializableExtra("bean");
                    int position = data.getIntExtra("position", -1);
                    if (position != -1) {
                        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                        CircleVO circleVO = (CircleVO) adapter.getData(position);
                        circleVO.setType("0");
                        circleVO.setUser_n(Utils.isInteger(circleVO.getUser_n()) - 1 + "");
                        adapter.changeData(position, circleVO);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (refresh == true && OtherUtil.hasLogin(getActivity())) {
            loadData();
            refresh = false;
        }
    }

//    @Override
//    public void onPause() {
//        if (mRefreshLayout!=null) {
//            mRefreshLayout.stopRefresh();
//            mRefreshLayout.setLoading(false);
//        }
//        super.onPause();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Communications.cancelRequest(TAG);
    }

}
