package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.WebViewVoteDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.InfoMVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{描述}
 * @date ${2016/9/13}
 */
public class AdMoreFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private LSwipeRefreshLayout mRefreshLayout;
    String id;
    long time=new Date().getTime()/1000;
    int pageNum=1;
    int allPageNum;
    long severTime;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh,
                container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        getBaseActivity().setBackNavigationIcon();
        id=getActivity().getIntent().getStringExtra("id");
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRefreshLayout = (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum=1;
                getMainList(Constant.LIST_REFRESH);
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                if(pageNum>allPageNum){
                    showTost("没有更多数据了",1);
                    mRefreshLayout.setLoading(false);
                }else {
                    getMainList(Constant.LIST_LOAD_MORE);
                }
            }
        });
        BaseAdapter adapter=new BaseAdapter(getActivity()) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder viewHolder=new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_ad, parent,false), new BaseViewHolder.Callbacks() {
                    @Override
                    public void bind(BaseViewHolder holder, Object data) {
                        if(!(data instanceof InfoMVO))
                            return;
                        final InfoMVO vo= (InfoMVO) data;
                        holder.setText(R.id.content,vo.getTitle());
                        if(vo.getType()==1){
                            holder.setText(R.id.title,"公告");
                        }else {
                            holder.setText(R.id.title,"活动");

                        }
                        final LinearLayout ll=holder.getView(R.id.ll);
//                        ll.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                final Intent intent2 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
//                                intent2.putExtra("id",vo.getId());
//                                intent2.putExtra("project_id",id);
//                                intent2.putExtra("title1",vo.getTitle());
//                                intent2.putExtra("content",vo.getTitle());
//                                intent2.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDINFOMATIONINFO);
//                                ll.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        startActivity(intent2);
//                                    }
//                                });
//                            }
//                        });

                    }

                    @Override
                    public void onItemClick(View view, int position) {
                        InfoMVO vo= (InfoMVO) getData().get(position);
                        Intent intent2 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                        intent2.putExtra("id",vo.getId());
                        intent2.putExtra("project_id",id);
                        intent2.putExtra("title1",vo.getTitle());
                        intent2.putExtra("content",vo.getTitle());
                        intent2.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDINFOMATIONINFO);
                        startActivity(intent2);

                    }
                });

                return viewHolder;

            }
            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state) {
                outRect.set(0, 0, 0, 2);
            }
        };
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void loadData() {
        getMainList(Constant.LIST_REFRESH);

    }

    private void getMainList(final int tag) {
        Log.e("time",time+"");
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model","NewVote");
        JSONUtil.put(params,"action","app_findInfomationList");
        JSONUtil.put(params,"id",id);
        if(tag==Constant.LIST_REFRESH){
            JSONUtil.put(params,"time",time);

        }else {
            JSONUtil.put(params,"time",severTime);

        }
        JSONUtil.put(params,"pageNum",pageNum);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
//                time
                try {
                    JSONObject result=new JSONObject(jsonString);
                    allPageNum=JSONUtil.getInt(result,"allPageNumber");
                    JSONArray array=JSONUtil.getJSONArray(result,"informationList");
                    severTime=JSONUtil.getLong(result,"time");
                    List<InfoMVO> list=JSONUtil.fromJson(array.toString(),new TypeToken<List<InfoMVO>>(){}.getType());
                    refreshList(tag,list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }



            @Override
            public void onCodeError(int key, String message) {

            }

            @Override
            public void onConnectError(String message) {

            }
        });


    }
    private void refreshList(int tag, List<InfoMVO> list) {
        BaseAdapter adapter= (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag){
            case Constant.LIST_REFRESH:
                adapter.setData(list);
                mRefreshLayout.stopRefresh();

                break;
            case Constant.LIST_LOAD_MORE:
                adapter.addItems(list);
                mRefreshLayout.setLoading(false);
                break;
        }


    }
}
