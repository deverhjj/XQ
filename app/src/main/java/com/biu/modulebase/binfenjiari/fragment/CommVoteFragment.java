package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommVoteDetail2Activity;
import com.biu.modulebase.binfenjiari.activity.CommVoteDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.VoteProjectVO;
import com.biu.modulebase.binfenjiari.model.VoteVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
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
 * Created by jhj_Plus on 2016/4/18.
 */
public class CommVoteFragment extends BaseFragment {
    private static final String TAG = "CommVoteFragment";

    private static final int TYPE_TEXT=0;

    private static final int TYPE_IMAGE=1;

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
    protected void initView(final View rootView) {
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
                getList( Constant.LIST_LOAD_MORE);
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter=new BaseAdapter(getActivity()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder;
                if(viewType ==TYPE_TEXT){
                    holder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate( R.layout.item_vote ,parent, false), new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
                                    final VoteVO bean = (VoteVO) data;
                                    holder.setText(R.id.tv_nickname,bean.getUsername());
                                    holder.setNetImage(Constant.IMG_SOURCE,R.id.iv_head_portrait,bean.getUser_pic(), ImageDisplayUtil.DISPLAY_HEADER);
                                    ImageView header = holder.getView(R.id.iv_head_portrait);
//                                    header.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Intent intent =new Intent(getActivity(),PersonalInfoActivity.class);
//                                            intent.putExtra(Constant.KEY_ID,bean.getUser_id());
//                                            startActivity(intent);
//                                        }
//                                    });
                                    holder.setText(R.id.tv_date, Utils.getReleaseTime(new Date(Utils.isLong(bean.getCreate_time())*1000)));
                                    holder.setText(R.id.tv_content,bean.getTitle());
                                    LinearLayout vote_layout =holder.getView(R.id.vote_layout);
                                    vote_layout.removeAllViews();
                                    List<VoteProjectVO> projectList = bean.getProject();
                                    int length =projectList.size()>3?3:projectList.size();
                                    for(int i =0;i<length;i++){
                                        View vote_item = LayoutInflater.from(getActivity()).inflate(R.layout.part_vote_select_item,null);
                                        VoteProjectVO voteProjectVO =projectList.get(i);
                                        ImageView img =(ImageView)vote_item.findViewById(R.id.img);
                                        if(i==0){
                                            img.setImageResource(R.mipmap.vote_1);
                                        }else if (i==1){
                                            img.setImageResource(R.mipmap.vote_2);
                                        }else if(i==2){
                                            img.setImageResource(R.mipmap.vote_3);
                                        }
                                        TextView project_name = (TextView) vote_item.findViewById(R.id.project_name);
                                        project_name.setText(voteProjectVO.getTitle());
                                        ProgressBar progressBar =(ProgressBar)vote_item.findViewById(R.id.progress);
                                        progressBar.setMax(Utils.isInteger(bean.getAll_poll()));
                                        progressBar.setProgress(Utils.isInteger(voteProjectVO.getNumber()));
                                        TextView num = (TextView) vote_item.findViewById(R.id.num);
                                        num.setText(voteProjectVO.getNumber()+"");
                                        vote_layout.addView(vote_item);
                                    }
                                    holder.setText(R.id.tv_vote_info,String.format(getString(R.string.all_vote_num),bean.getAll_number()));
                                    holder.setText(R.id.time, String.format(getString(R.string.finish_time),Utils.sec2Date(bean.getEnd_time(),"yyyy/MM/dd HH:mm")));
                                    ImageView over =holder.getView(R.id.over);
                                    if(bean.getIsopen().equals("2")){
                                        over.setVisibility(View.VISIBLE);
                                    }else{
                                        over.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view, int position) {
                                    VoteVO bean  = (VoteVO) getData(position);
                                    Intent intent=new Intent(getActivity(), CommVoteDetailActivity.class);
                                    intent.putExtra(Constant.KEY_ID,bean.getId());
                                    startActivity(intent);
                                }
                            });
                }else{
                    holder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_vote_image, parent, false), new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
                                    VoteVO bean = (VoteVO) data;
                                    holder.setText(R.id.tv_nickname,bean.getUsername());
                                    holder.setNetImage(Constant.IMG_SOURCE,R.id.iv_head_portrait,bean.getUser_pic(), ImageDisplayUtil.DISPLAY_HEADER);
                                    holder.setText(R.id.tv_date,Utils.getReleaseTime(new Date(Utils.isLong(bean.getCreate_time())*1000)));
                                    holder.setText(R.id.tv_content,bean.getTitle());

                                    LinearLayout vote_layout =holder.getView(R.id.vote_layout);
                                    vote_layout.removeAllViews();
                                    List<VoteProjectVO> projectList = bean.getProject();
                                    int width =(Utils.getScreenWidth(getActivity())-getResources().getDimensionPixelSize(R.dimen.view_margin_24dp))/3;
                                    for(int i =0;i<projectList.size();i++){
                                        FrameLayout vote_item = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.part_image_vote,null);
                                        VoteProjectVO voteProjectVO =projectList.get(i);
                                        ImageView img =(ImageView)vote_item.findViewById(R.id.img);
                                        ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL,voteProjectVO.getPic(),img,ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                        ImageView rank_img =(ImageView)vote_item.findViewById(R.id.rank_img);
                                        if(i==0){
                                            rank_img.setImageResource(R.mipmap.vote_11);
                                        }else if (i==1){
                                            rank_img.setImageResource(R.mipmap.vote_22);
                                        }else if(i==2){
                                            rank_img.setImageResource(R.mipmap.vote_33);
                                        }
                                        TextView name = (TextView) vote_item.findViewById(R.id.name);
                                        name.setText(voteProjectVO.getTitle());//voteProjectVO.getCreate_number()+" "+
                                        ProgressBar progressBar =(ProgressBar)vote_item.findViewById(R.id.progress);
                                        progressBar.setMax(Utils.isInteger(bean.getAll_poll()));
                                        progressBar.setProgress(Utils.isInteger(voteProjectVO.getNumber()));
                                        TextView num = (TextView) vote_item.findViewById(R.id.num);
                                        num.setText(voteProjectVO.getNumber()+"");
                                        vote_layout.addView(vote_item);
                                        ViewGroup.LayoutParams layoutParams =vote_item.getLayoutParams();
                                        layoutParams.height = width;
                                        layoutParams.width = width;
                                        vote_item.setLayoutParams(layoutParams);
                                        if(i==1){
                                            LinearLayout.LayoutParams  layoutParam= (LinearLayout.LayoutParams) vote_item.getLayoutParams();
                                            layoutParam.setMargins(4,0,4,0);
                                            vote_item.setLayoutParams(layoutParam);
                                        }
                                    }

                                    holder.setText(R.id.tv_vote_info,String.format(getString(R.string.all_vote_num),bean.getAll_number()));
                                    holder.setText(R.id.time, String.format(getString(R.string.finish_time),Utils.sec2Date(bean.getEnd_time(),"yyyy/MM/dd HH:mm")));
                                    ImageView over =holder.getView(R.id.over);
                                    if(bean.getIsopen().equals("2")){
                                        over.setVisibility(View.VISIBLE);
                                    }else{
                                        over.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view, int position) {
                                    VoteVO bean  = (VoteVO) getData(position);
                                    Intent intent=new Intent(getActivity(), CommVoteDetail2Activity.class);
                                    intent.putExtra(Constant.KEY_ID,bean.getId());
                                    startActivity(intent);
                                }
                            });
                }
                return holder;
            }

            @Override
            public int getItemViewType(int position) {
                VoteVO bean = (VoteVO) getData(position);
                int type = Utils.isInteger(bean.getType());
                return type == 1 ? TYPE_TEXT : TYPE_IMAGE;
            }
        };
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void loadData() {
        time = new Date().getTime()/1000;
        pageNum =1;
        getList(Constant.LIST_REFRESH);
    }

    private void getList(final int tag){
        JSONObject params = new JSONObject();
        try {
            params.put("model",Constant.MODEL_VOTE);
            params.put("action",Constant.ACTION_VOTE_HOME);
            params.put("time",time);
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
                    JSONArray array =JSONUtil.getJSONArray(result,"voteList");
                    ArrayList<VoteVO> list =JSONUtil.fromJson(array.toString(),new TypeToken<List<VoteVO>>(){}.getType());
                    refreshList(tag, list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if(key==3){

                    refreshList(tag, new ArrayList<VoteVO>());
                }else{
                    if(tag ==Constant.LIST_REFRESH){
                        mRefreshLayout.setRefreshing(false);
                    }else{
                        mRefreshLayout.setLoading(false);
                    }

                    showTost(message,1);
                }
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });
    }

    private void refreshList(int tag,ArrayList<VoteVO> datas){
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
