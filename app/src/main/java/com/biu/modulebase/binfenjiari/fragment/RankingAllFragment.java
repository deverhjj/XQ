package com.biu.modulebase.binfenjiari.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.RankVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
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
 * Created by jhj_Plus on 2016/4/25.
 */
public class RankingAllFragment  extends BaseFragment {
    private static final String TAG = "RankingAllFragment";
    private RecyclerView mRecyclerView;
    private LSwipeRefreshLayout mRefreshLayout;


    private long time;

    private int pageNum =1;
    private int allPageNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh,
                container,false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getRankList( Constant.LIST_LOAD_MORE);
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter=new BaseAdapter(getActivity()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                        .inflate(R.layout.item_ranking, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                final RankVO bean = (RankVO) data;
                                int position =holder.getAdapterPosition();
                                TextView num =holder.getView(R.id.num);
                                TextView rank =holder.getView(R.id.rank);
                                switch (position){
                                    case 0:
                                        num.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.rank1,0,0);
                                        num.setText("");
                                        rank.setBackgroundResource(R.drawable.bg_tv_ranking1);
                                        break;
                                    case 1:
                                        num.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.rank2,0,0);
                                        num.setText("");
                                        rank.setBackgroundResource(R.drawable.bg_tv_ranking2);
                                        break;
                                    case 2:
                                        num.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.rank3,0,0);
                                        num.setText("");
                                        rank.setBackgroundResource(R.drawable.bg_tv_ranking3);
                                        break;
                                    default:
                                        num.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                                        num.setText(position+1+"");
                                        rank.setBackgroundResource(R.drawable.bg_tv_ranking3);
                                        break;
                                }
                                holder.setNetImage(Constant.IMG_SOURCE,R.id.iv_circle_head_portrait,bean.getUser_pic(), ImageDisplayUtil.DISPLAY_HEADER);
                                ImageView header = holder.getView(R.id.iv_circle_head_portrait);
                                header.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        Intent intent =new Intent(getActivity(),PersonalInfoActivity.class);
//                                        intent.putExtra(Constant.KEY_ID,bean.get());
//                                        startActivity(intent);
                                    }
                                });
                                holder.setText(R.id.name,bean.getUsername());
                                holder.setText(R.id.school,bean.getName());
                                rank.setText(bean.getRank()+"分");
                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {

                            }
                        });
                return holder;
            }

        };
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, getResources().getDimensionPixelSize(R.dimen.item_divider_height_4dp), 0,
                        0);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void loadData() {
        time = new Date().getTime()/1000;
        pageNum =1;
        getRankList(Constant.LIST_REFRESH);
    }

    private void getRankList(final int tag){
        JSONObject params = new JSONObject();
        try {
            params.put("model",Constant.MODEL_RANK);
            params.put("action",Constant.ACTION_GET_ALL_RANK);
            params.put("time",time+"");
            params.put("pageNum",pageNum+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result =new JSONObject(jsonString);
                    time = JSONUtil.getLong(result,"time");
                    allPageNumber =JSONUtil.getInt(result,"allPageNumber");
                    JSONArray array =JSONUtil.getJSONArray(result,"list");
                    ArrayList<RankVO> list =JSONUtil.fromJson(array.toString(),new TypeToken<List<RankVO>>(){}.getType());
                    refreshList(tag, list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if(key==3){
                    refreshList(tag, new ArrayList<RankVO>());
                }else{
                    showTost(message,1);
                }
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });
    }

    private void refreshList(int tag,ArrayList<RankVO> datas){
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag){
            case Constant.LIST_REFRESH:
                if(datas.size()==0) {
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
        if(pageNum<allPageNumber){
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
        }else{
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
        }
    }

    @Override
    public void onDestroyView() {
        Communications.cancelRequest(getClass().getSimpleName());
        super.onDestroyView();
    }
}
