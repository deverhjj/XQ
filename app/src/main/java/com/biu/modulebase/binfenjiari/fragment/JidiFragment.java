package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.JidiDetailActivity;
import com.biu.modulebase.binfenjiari.activity.MainActivity;
import com.biu.modulebase.binfenjiari.activity.MapActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack2;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.Jidi;
import com.biu.modulebase.binfenjiari.model.JidiItem;
import com.biu.modulebase.binfenjiari.model.Region;
import com.biu.modulebase.binfenjiari.util.DoubleUtil;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.RefreshRecyclerView;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONObject;

import java.util.HashMap;

public class JidiFragment extends BaseFragment {
    private static final String TAG = "JidiFragment";

    private MainActivity context;

    private static final int JIDI_LIKE_REQUEST =112;
//    private LSwipeRefreshLayout mRefreshLayout;

    private Jidi mJidi;
    private Region.RegionItem[] mRegionItems;
//    private boolean mRefreshData=true;

    private RefreshRecyclerView mRefreshRecyclerView;

    private RecyclerView mRecyclerView;

    private int mPage;

    /**
     * 搜索参数
     */
    private HashMap<String,Object> mArgs;
    private String mRegionId=null;

    private double latitude;
    private double longitude;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args=getArguments();
        if (args!=null) {
            mArgs= (HashMap<String, Object>) args.getSerializable(Constant.KEY_SEARCH_ARGS);
        }
    }


    /**
     *  提供搜索时的调用
     * @param args 搜索的参数
     * @return
     */
    public static JidiFragment newInstance(HashMap<String, Object> args) {
        Bundle a = new Bundle();
        a.putSerializable(Constant.KEY_SEARCH_ARGS, args);
        JidiFragment jidiFragment = new JidiFragment();
        jidiFragment.setArguments(a);
        return jidiFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_refresh_recyclerview, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        visibleLoading();
//        if(isAdded())
//            context = (MainActivity) getActivity();
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    protected void initView(View rootView) {
        mRefreshRecyclerView = (RefreshRecyclerView) rootView;
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

//        mRefreshLayout = (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
//        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
//            @Override
//            public void onRefresh() {
//                LogUtil.LogE(TAG, "onRefresh******************");
//                refreshData();
//            }
//
//            @Override
//            public void onLoadMore() {
//                LogUtil.LogE(TAG, "onLoadMore******************");
//                loadMoreData();
//            }
//        });

//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter<JidiItem> adapter = new BaseAdapter<JidiItem>(getActivity()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                        .inflate(R.layout.item_jidi, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                if (data==null||!(data instanceof JidiItem)) return;

                                JidiItem item= (JidiItem) data;
                                holder.setNetImage(Constant.IMG_COMPRESS,R.id.image,item.getBanner_pic(),
                                        ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                                String name =item.getName();
                                holder.setText(R.id.tv_name,name);
                                String address =item.getAddress();
                                holder.setText(R.id.tv_location,getString(R.string.address,address.length()>16?address.substring(0,16)+"...":address, DoubleUtil.round(item.getDistance(),2)+""));
                                holder.setText(R.id.tv_activity_number,getString(R.string
                                        .activity_number,item.getActivity_number()));
                                holder.setText(R.id.like,item.getLike_number());

                                CheckBox likeBox=holder.getView(R.id.like);
                                likeBox.setChecked(item.getLike_status().equals("1"));

                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view,int position) {
                                if (position==RecyclerView.NO_POSITION || mJidi==null) return;
                                final JidiItem item=getData(position);
                                int i = view.getId();
                                if (i == R.id.like) {
                                    JSONObject params = OtherUtil.getJSONObject(getActivity(), Constant.MODEL_PLACE,
                                            Constant.ACTION_PLACE_LIKE, true);
                                    JSONUtil.put(params, "id", item.getId());
                                    OtherUtil.like(JidiFragment.this, (CheckBox) view, params,
                                            new OtherUtil.LikeCallback() {
                                                @Override
                                                public void onFinished(int backKey) {
                                                    if (backKey != -1) {
                                                        item.setLike_status(backKey + "");
                                                        if (backKey == 1) {
                                                            item.setLike_number(Utils.isInteger(item.getLike_number()) + 1 + "");
                                                        } else if (backKey == 2) {
                                                            item.setLike_number(Utils.isInteger(item.getLike_number()) - 1 + "");
                                                        }
                                                    }
                                                }
                                            });

                                } else if (i == R.id.share) {
                                    OtherUtil.showShareFragment(JidiFragment.this, item.getId(), item.getName(), item.getName(), Constant.SHARE_PLACE);

                                } else if (i == R.id.tv_location) {
                                    Intent mapIntent = new Intent(getActivity(), MapActivity
                                            .class);
                                    mapIntent.putExtra(Constant.KEY_LOCATION_LATITUDE, item
                                            .getLatitude());
                                    mapIntent.putExtra(Constant.KEY_LOCATION_LONGITUDE, item.getLongitude());
                                    mapIntent.putExtra(Constant.KEY_MAP_TARGET, item.getAddress());
                                    startActivity(mapIntent);

                                } else {
                                    Intent intent = new Intent(getActivity(),
                                            JidiDetailActivity.class);
                                    intent.putExtra(Constant.KEY_POSITION, position);
                                    intent.putExtra(Constant.KEY_ID, item.getId());
                                    startActivityForResult(intent, JIDI_LIKE_REQUEST);

                                }

                            }
                        });

                holder.setItemChildViewClickListener(R.id.like,R.id.share,R.id.tv_location);
                return holder;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state)
            {
                outRect.set(0,0, 0, getResources().getDimensionPixelSize(R.dimen.item_divider_height));
//                //如果是搜索界面的话不需要
//                if (mArgs != null) {
//                    super.getItemOffsets(outRect, view, parent, state);
//                    return;
//                }
//
//                int childAdapterPosition = parent.getChildAdapterPosition(view);
//                //                if (childAdapterPosition > 0) {
//                int dividerSize = getResources().getDimensionPixelSize(
//                        R.dimen.item_divider_height_8dp);
//                outRect.set(0, childAdapterPosition == 0 ?
//                        getResources().getDimensionPixelSize(R.dimen.toolbar_size) + dividerSize
//                        : dividerSize, 0, 0);
//                // }
            }
        };

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

//    private void reset() {
//        mRefreshData=true;
//        mJidi = null;
//        mPageNum=1;
//    }

    private void refreshData() {
//        reset();
        startLocation(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                LogUtil.LogE(TAG,"onReceiveLocation*************************");
                latitude = bdLocation.getLatitude();
                longitude = bdLocation.getLongitude();
                stopLocation();
                loadJiDiData();
            }
        });
    }


    private void loadMoreData() {
//        if (mRefreshData) {
//            mRefreshData = false;
//        }
//        mPageNum++;
        LogUtil.LogE(TAG,"loadMoreData **********************");
        loadJiDiData();
    }

    @Override
    public void loadData() {
        mRefreshRecyclerView.showSwipeRefresh();

//        refreshData();

        //获取区域数据
        OtherUtil.doGetRegionData(JidiFragment.this, TAG, new OtherUtil.DataRequest() {
            @Override
            public void onFinished(Object data) {
                mRegionItems = data != null ? ((Region) data).getResult() : null;
            }
        });
    }

    private void loadJiDiData() {

        LogUtil.LogE(TAG,"loadJiDiData **********************");

        JSONObject params = OtherUtil.getJSONObject(getContext(), Constant.MODEL_PLACE,
                Constant.ACTION_PLACE_HOME, false);
        long time = mJidi == null || mPage == 1 ? OtherUtil.getTimeSecs() : mJidi.getTime();
//        Double[] location=new Double[2];
//        OtherUtil.getLocation(getActivity(),location);

        JSONUtil.putlong(params,"time",time);
        JSONUtil.putInt(params,"pageNum",mPage);
        JSONUtil.put(params,"longitude",longitude);
        JSONUtil.put(params,"latitude",latitude);

        if (mArgs != null) {
            String title = (String) mArgs.get(Constant.KEY_SEARCH_ARGS_TITLE);
            if (!TextUtils.isEmpty(title)) {
                JSONUtil.put(params, "title", title);
            }
        }

        if (!TextUtils.isEmpty(mRegionId)) {
            JSONUtil.put(params, "districtId", mRegionId);
        }

        dataRequest(false, params, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {
//                visibleLoading();
            }

            @Override
            public void onSuccess(String mainJsonString,JSONObject mainJsonObject,JSONObject rootJsonObject) {
                mRefreshRecyclerView.endPage();

                mJidi = JSONUtil.getGson().fromJson(mainJsonString, Jidi.class);

                BaseAdapter<JidiItem> adapter = (BaseAdapter<JidiItem>) mRecyclerView.getAdapter();

                if(mPage==1){
                    adapter.setData( mJidi.getBaseList());
                    if(mJidi.getBaseList().size()==0){
                        //基地搜索模式结果
                        if(mArgs.get("SearchResultJidiActivity")!=null){
                            showTost("未搜索相关结果！",1);
                            getActivity().finish();
                        }
                    }
                }else {
                    adapter.addData(BaseAdapter.AddType.LASE, mJidi.getBaseList());
                }

                if (mJidi != null) {
                    //判断是否下次还可以上拉加载更多
                    int allPageNumber = mJidi.getAllPageNumber();
                    if (mPage >= allPageNumber) {
                        LogUtil.LogE(TAG, "stop load more");
                        mRefreshRecyclerView.showNoMore();
//                        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
                    } else {
                        mRefreshRecyclerView.showNextMore(mPage);
//                        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
                    }
                }
            }

            @Override
            public void onFail(int key,String message) {
                mRefreshRecyclerView.endPage();
                if (mArgs == null) {
                    ((MainActivity) getActivity()).quickShowTabLayout();
                }

                LogUtil.LogE(TAG,"onFail*");
                //基地搜索模式结果
                if(mPage==1 && mArgs!=null && mArgs.get("SearchResultJidiActivity")!=null){
                    showTost("未搜索相关结果！",1);
                    getActivity().finish();
                }

                //如果上拉时出现错误还原请求时页号
//                if (!mRefreshData) {
//                    mPageNum--;
//                } else {
//                    BaseAdapter<JidiItem> adapter =
//                            (BaseAdapter<JidiItem>) mRecyclerView.getAdapter();
//                    adapter.removeAllData();
//                }
                if (key == RequestCallBack2.KEY_FAIL) {
                    visibleNoNetWork();
                } else if (key == 3 && mPage==1) {
                    visibleNoData();
                }else {
                    OtherUtil.showToast(getActivity(),"没有更多内容了");
                }

            }

            @Override
            public void requestAfter() {
                LogUtil.LogE(TAG,"requestAfter*");
//                mRefreshLayout.setRefreshing(false);
//                mRefreshLayout.setLoading(false);
                inVisibleLoading();
                inVisibleNoData();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case JIDI_LIKE_REQUEST:
                    BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                    int position = data.getIntExtra("position",-1);
                    int backKey = data.getIntExtra("backKey",-1);
                    Object object =adapter.getData(position);
                    JidiItem bean = (JidiItem) object;
                    bean.setLike_status(backKey==1?"1":"2");
                    int likeNum = Utils.isInteger(bean.getLike_number());
                    bean.setLike_number(backKey==1?++likeNum +"":--likeNum+"" );
                    adapter.changeData(position,bean);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //如果是搜索界面的话不需要有这些菜单
        if (mArgs != null) {
            super.onCreateOptionsMenu(menu, inflater);
            return;
        }
        inflater.inflate(R.menu.jidi, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            startSearchIntent(Constant.SEARCH_JIDI);

        } else if (i == R.id.action_filter) {
            OtherUtil.popRegionMenuWindow(getActivity(), ((MainActivity) getActivity()).getToolbar(),
                    TAG, mRegionItems, new OtherUtil.FinishListener() {
                        @Override
                        public void onFinished(String id) {
                            mRegionId = !id.equals("-1") ? id : null;
                            visibleLoading();
                            refreshData();
                        }
                    });

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroyView() {
        LogUtil.LogE(TAG,"onDestroyView");
//        reset();
        cancelRequest(TAG);
        ImageDisplayUtil.stopTask();
        super.onDestroyView();
    }

}
