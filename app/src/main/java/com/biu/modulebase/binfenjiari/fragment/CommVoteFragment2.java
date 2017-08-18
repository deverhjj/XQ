package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.ProjectVO;
import com.biu.modulebase.binfenjiari.model.VoteListVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by hasee on 2016/11/3.
 */

public class CommVoteFragment2<E> extends BaseFragment {
    private RecyclerView mRecyclerView;
    long phoneTime;

    long serveTime;
    int allPageNumber;
    int pageNumber = 1;
    int tag;
    private LSwipeRefreshLayout mRefreshLayout;
    private Object mainList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh,
                container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRefreshLayout = (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber=1;

                getMainList(Constant.LIST_REFRESH);
            }

            @Override
            public void onLoadMore() {
                pageNumber++;
                if(pageNumber>allPageNumber){
                    showTost("没有更多数据了",1);
                    mRefreshLayout.setLoading(false);
                    return;

                }else {

                    getMainList(Constant.LIST_LOAD_MORE);
                }

            }
        });
        BaseAdapter adapter = new BaseAdapter(getActivity()) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder viewHolder = null;
                if (viewType == 1) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_home_vote, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            if(!(data instanceof VoteListVO))
                                return;

                            LinearLayout vote_layout =holder.getView(R.id.vote_layout);
                            VoteListVO  bean= (VoteListVO) data;

                            ImageView over=holder.getView(R.id.over);

                            if(bean.getIsopen().equals("1")){
                                over.setVisibility(View.GONE);

                            }else {
                                over.setVisibility(View.GONE);

                            }
                            holder.setNetImage(Constant.IMG_COMPRESS,R.id.banner,bean.getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                            holder.setText(R.id.title,bean.getTitle());
                            holder.setText(R.id.time,"截止时间："+Utils.sec2Date(bean.getEnd_time(),"yyyy/MM/dd HH:mm"));
                            holder.setText(R.id.tv_vote_info,"共"+bean.getAll_number()+"个投票项");
                            List<ProjectVO> projectList = bean.getProject();

                            vote_layout.removeAllViews();
                            if(projectList.size()==0||projectList==null){
                                return;
                            }

                            if(projectList.size()>3){
                                holder.getView(R.id.tv_more).setVisibility(View.INVISIBLE);
                            }else{
                                holder.getView(R.id.tv_more).setVisibility(View.INVISIBLE);
                            }

                            for (int i = 0; i < (projectList.size()>3?3:projectList.size()); i++) {
                                View vote_item = LayoutInflater.from(getActivity()).inflate(R.layout.item_organiztion,null);
                                TextView name= (TextView) vote_item.findViewById(R.id.project_name);
                                name.setText(projectList.get(i).getTitle());//projectList.get(i).getCreate_number()+"  "+
                                TextView num= (TextView) vote_item.findViewById(R.id.num);
                                num.setText(projectList.get(i).getNumber());
                                ProgressBar progressBar =(ProgressBar)vote_item.findViewById(R.id.progress);
                                progressBar.setMax(Utils.isInteger(bean.getAll_poll()));
                                Log.e("all_poll",bean.getAll_poll()+"");
                                progressBar.setProgress(Utils.isInteger(projectList.get(i).getNumber()));
                                vote_layout.addView(vote_item);
                                ImageView img =(ImageView)vote_item.findViewById(R.id.img);
                                if(i==0){
                                    img.setImageResource(R.mipmap.vote_1);
                                }else if (i==1){
                                    img.setImageResource(R.mipmap.vote_2);
                                }else if(i==2){
                                    img.setImageResource(R.mipmap.vote_3);
                                }

                            }

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {
                            VoteListVO bean= (VoteListVO) getData().get(position);
                            String id=bean.getId();
                            String pic=bean.getBanner_pic();
                            Intent intent = new Intent(getActivity(), CommDetailActivity.class);
                            intent.putExtra("id",id);
                            intent.putExtra("pic",pic);
                            startActivity(intent);
                        }
                    });
                } else if (viewType == 2) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_home_vote_image, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            LinearLayout vote_layout =holder.getView(R.id.vote_layout);
                            VoteListVO bean= (VoteListVO) data;
                            ImageView over=holder.getView(R.id.over);

                            if(bean.getIsopen().equals("1")){
                                over.setVisibility(View.GONE);

                            }else {
                                over.setVisibility(View.GONE);

                            }
                            holder.setNetImage(Constant.IMG_COMPRESS,R.id.banner,bean.getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                            holder.setText(R.id.title,bean.getTitle());
                            holder.setText(R.id.time,"截止时间："+Utils.sec2Date(bean.getEnd_time(),"yyyy/MM/dd HH:mm"));
                            holder.setText(R.id.tv_vote_info,"共"+bean.getAll_number()+"个投票项");
                            List<ProjectVO> projectList = bean.getProject();
                            vote_layout.removeAllViews();
                            if(projectList.size()==0||projectList==null){
                                return;

                            }
                            int width =(Utils.getScreenWidth(getActivity())-getResources().getDimensionPixelSize(R.dimen.view_margin_12dp)*2)/3;

                            if(projectList.size()>3){
                                holder.getView(R.id.tv_more).setVisibility(View.INVISIBLE);
                            }else{
                                holder.getView(R.id.tv_more).setVisibility(View.INVISIBLE);
                            }

                            for (int i = 0; i < (projectList.size()>3?3:projectList.size()); i++) {
                                View vote_item = LayoutInflater.from(getActivity()).inflate(R.layout.part_image_vote,null);
                                ProjectVO vo=projectList.get(i);
                                ImageView img= (ImageView) vote_item.findViewById(R.id.img);
                                ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL,vo.getPic(),img,ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                ImageView rank_img =(ImageView)vote_item.findViewById(R.id.rank_img);
                                if(i==0){
                                    rank_img.setImageResource(R.mipmap.vote_11);
                                }else if (i==1){
                                    rank_img.setImageResource(R.mipmap.vote_22);
                                }else if(i==2){
                                    rank_img.setImageResource(R.mipmap.vote_33);
                                }
                                TextView name = (TextView) vote_item.findViewById(R.id.name);
                                name.setText(vo.getSmall_title());//vo.getCreate_number()+" "+
                                Utils.setSexIconState(getContext(),name,vo.getSex());
                                ProgressBar progressBar =(ProgressBar)vote_item.findViewById(R.id.progress);
                                progressBar.setMax(Utils.isInteger(bean.getAll_poll()));
                                Log.e("all_poll",bean.getAll_poll()+"");
                                progressBar.setProgress(Utils.isInteger(vo.getNumber()));
                                TextView num = (TextView) vote_item.findViewById(R.id.num);
                                num.setText(vo.getNumber()+"");
                                vote_layout.addView(vote_item);
                                ViewGroup.LayoutParams vlayoutParams =vote_item.getLayoutParams();
//                                layoutParams.height = width;
                                vlayoutParams.width = width;
                                vote_item.setLayoutParams(vlayoutParams);
                                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) img.getLayoutParams();
                                layoutParams.width = width;
                                layoutParams.height = width;
                                img.setLayoutParams(layoutParams);

                                if(i==1){
                                    LinearLayout.LayoutParams  layoutParam= (LinearLayout.LayoutParams) vote_item.getLayoutParams();
                                    layoutParam.setMargins(4,0,4,0);
                                    vote_item.setLayoutParams(layoutParam);
                                }

                            }

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {
                            VoteListVO bean= (VoteListVO) getData().get(position);

                            String id=bean.getId();
                            String pic=bean.getBanner_pic();
                            Intent intent = new Intent(getActivity(), CommDetailActivity.class);
                            intent.putExtra("id",id);
                            intent.putExtra("pic",pic);

                            startActivity(intent);
                        }
                    });
                }
                else if(viewType==0){
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_home_vote_image, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {
                            Intent intent = new Intent(getActivity(), CommDetailActivity.class);
                            startActivity(intent);
                        }
                    });
                }

                return viewHolder;
            }

            @Override
            public int getItemViewType(int position) {
                List<VoteListVO> list=getData();
                Object bean=list.get(position);
                if(bean instanceof VoteListVO){
                    VoteListVO vo= (VoteListVO) bean;
                    if(Utils.isInteger(vo.getType())==1){
                        return 1;
                    }else {
                        return 2;
                    }

                }else {
                    return 0;
                }

            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0,
                        getContext().getResources().getDimensionPixelSize(R.dimen.item_divider_height_8dp));
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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
        getMainList(Constant.LIST_REFRESH);
    }

    private void getMainList(final int tag) {
        phoneTime = new Date().getTime() / 1000;
        Log.e("time",phoneTime+"");
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", "NewVote");
        JSONUtil.put(params, "action", "app_findNewVoteList");
        Log.e("time",phoneTime+"");
        if (tag == Constant.LIST_REFRESH) {
            JSONUtil.put(params, "time", phoneTime);
        } else if ((tag == Constant.LIST_LOAD_MORE)) {
            JSONUtil.put(params, "time", serveTime);

        }
        JSONUtil.put(params, "pageNum", pageNumber);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result=new JSONObject(jsonString);
                    serveTime=JSONUtil.getLong(result,"time");
                    allPageNumber=JSONUtil.getInt(result,"allPageNumber");
                    JSONArray votearray = JSONUtil.getJSONArray(result, "voteList");
                    List<VoteListVO> voteList = JSONUtil.fromJson(votearray.toString(), new TypeToken<List<VoteListVO>>() {}.getType());
                    List<E> datas = new ArrayList<>();
                    datas.addAll((Collection<? extends E>) voteList);
                    refrushList(datas,tag);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }



            @Override
            public void onCodeError(int key, String message) {
                if(key==3){
                    visibleNoData();

                }


            }

            @Override
            public void onConnectError(String message) {
                showTost(message,1);

            }
        });


    }
    private void refrushList(List<E> datas, int tag) {
        BaseAdapter adapter= (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag){
            case  Constant.LIST_REFRESH:
                adapter.setData(datas);
                mRefreshLayout.setRefreshing(false);
                break;
            case Constant.LIST_LOAD_MORE:
                adapter.addItems(datas);
                mRefreshLayout.setLoading(false);
                break;
        }

    }

}
